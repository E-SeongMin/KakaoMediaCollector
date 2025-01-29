package com.collector.presentation.view.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.collector.data.log.CustomLog
import com.collector.domain.kakao.model.SearchResultEntity
import com.collector.presentation.R
import com.collector.domain.kakao.model.SavedSearchEntity
import com.collector.presentation.util.DateUtil
import com.collector.presentation.util.extension.toResString
import dagger.hilt.android.AndroidEntryPoint
import com.collector.presentation.view.base.BaseActivity

@AndroidEntryPoint
class SearchActivity : BaseActivity() {

    private val focusRequester = FocusRequester()

    @Composable
    override fun InitView() {
        CustomLog.d("SearchActivity initView")

        Scaffold (
            topBar = {
                TitleView()
            },
            content = { paddingValues ->
                ContentView(paddingValues)
            }
        )
    }

    @Composable
    fun TitleView() {
        CustomTitleBar(
            title = String.format(R.string.search_activity_title.toResString),
            isBackPressVisible = true
        )
    }

    @Composable
    fun ContentView(paddingValues: PaddingValues, viewModel: SearchViewModel = hiltViewModel()) {

        LaunchedEffect(Unit) {
            // Activity 진입 시 자동 포커스
            focusRequester.requestFocus()

            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    is SearchContract.Effect.ShowError -> {
                        showToast(effect.message)
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
                .padding(start = 8.dp, end = 8.dp)
                .padding(top = 4.dp, bottom = 4.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SetEditText(viewModel)
            SetSearchList(viewModel)
        }
    }

    @Composable
    fun SetEditText(viewModel: SearchViewModel) {

        val state by viewModel.uiState.collectAsState()
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusController = LocalFocusManager.current

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(10.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomTextField(
                value = state.currentQuery,
                label = String.format(R.string.search_activity_edit_text_label.toResString, state.previousQuery),
                onValueChange = { viewModel.setEvent(SearchContract.Event.SetSearchQuery(it)) },
                modifier = Modifier.weight(1f),
                focusRequester = focusRequester,
                onImeAction = {
                    viewModel.setEvent(SearchContract.Event.onClickSearchButton)
                    keyboardController?.hide()
                    focusController.clearFocus()
                }
            )

            Button(
                onClick = {
                    viewModel.setEvent(SearchContract.Event.onClickSearchButton)
                    keyboardController?.hide()
                    focusController.clearFocus()
                },
                modifier = Modifier
                    .wrapContentWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

    @Composable
    fun SetSearchList(viewModel: SearchViewModel) {

        val state by viewModel.uiState.collectAsState()

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.searchList) { item ->
                    GridItemCard(viewModel, item)
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun GridItemCard(viewModel: SearchViewModel, item: SearchResultEntity) {

        val showSaveDialog = remember { mutableStateOf(false) }
        val showDeleteDialog = remember { mutableStateOf(false) }
        val showFullImageDialog = remember { mutableStateOf(false) }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .combinedClickable(
                    onClick = {
                        if (item.isSaved) {
                            showDeleteDialog.value = true
                        } else {
                            showSaveDialog.value = true
                        }
                    },
                    onLongClick = {
                        showFullImageDialog.value = true
                    }
                )
                .border(
                    if (item.isSaved) {
                        BorderStroke(2.dp, Color.Red)
                    } else {
                        BorderStroke(2.dp, Color.White)
                    },
                    shape = RoundedCornerShape(8.dp)
                ),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(item.thumbnail),
                    contentDescription = item.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = DateUtil.formatDateTime(item.dateTime),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        if (showSaveDialog.value) {
            AlertDialog(
                onDismissRequest = { showSaveDialog.value = false },
                title = {
                    Text(String.format(R.string.search_activity_save_item_dialog_title.toResString))
                },
                text = {
                    Text(String.format(R.string.search_activity_save_item_dialog_content.toResString))
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val savedItem = SavedSearchEntity(
                                name = item.name,
                                thumbnail = item.thumbnail,
                                dateTime = DateUtil.formatDateTime(item.dateTime),
                                type = item.type
                            )
                            viewModel.saveSearchItem(savedItem)
                            showSaveDialog.value = false
                            showToast(String.format(R.string.search_activity_save_item_dialog_success_toast.toResString))
                        }
                    ) {
                        Text(String.format(R.string.search_activity_save_item_dialog_positive_button.toResString))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showSaveDialog.value = false
                        }
                    ) {
                        Text(String.format(R.string.search_activity_save_item_dialog_negative_button.toResString))
                    }
                }
            )
        }

        if (showDeleteDialog.value) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog.value = false },
                title = {
                    Text(String.format(R.string.search_activity_delete_item_dialog_title.toResString))
                },
                text = {
                    Text(String.format(R.string.search_activity_delete_item_dialog_content.toResString))
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteSavedSearchItem(item.id)
                            showDeleteDialog.value = false
                            showToast(String.format(R.string.search_activity_delete_item_dialog_sucess_toast.toResString))
                        }
                    ) {
                        Text(String.format(R.string.search_activity_delete_item_dialog_positive_button.toResString))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog.value = false
                        }
                    ) {
                        Text(String.format(R.string.search_activity_delete_item_dialog_negative_button.toResString))
                    }
                }
            )
        }

        if (showFullImageDialog.value) {
            AlertDialog(
                onDismissRequest = { showFullImageDialog.value = false },
                title = {
                    Text(
                        text = String.format(R.string.base_activity_full_image_dialog_title.toResString),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.wrapContentHeight()
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(item.thumbnail),
                            contentDescription = item.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .padding(bottom = 20.dp)
                        )
                        Text(
                            text = String.format(R.string.base_activity_full_image_dialog_content_name.toResString, item.name),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Text(
                            text = String.format(R.string.base_activity_full_image_dialog_content_time.toResString, DateUtil.formatDateTime(item.dateTime)),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Text(
                            text = String.format(R.string.base_activity_full_image_dialog_content_type.toResString, item.type.toString()),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showFullImageDialog.value = false
                        }
                    ) {
                        Text(
                            text = String.format(R.string.base_activity_full_image_dialog_button.toResString, item.type),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    usePlatformDefaultWidth = false
                )
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        InitView()
    }
}
