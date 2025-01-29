package com.collector.presentation.view.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.collector.data.log.CustomLog
import com.collector.domain.kakao.model.SavedSearchEntity
import com.collector.presentation.R
import com.collector.presentation.util.DateUtil
import com.collector.presentation.util.extension.toResString
import com.collector.presentation.util.ui.ActivityUtil
import com.collector.presentation.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onResume() {
        super.onResume()
        CustomLog.d("MainActivity onResume")

        if (::mainViewModel.isInitialized) {
            mainViewModel.setInitData(mainViewModel)
        }
    }

    @Composable
    override fun InitView() {
        CustomLog.d("MainActivity initView")

        SetBackPressHandler()

        Scaffold (
            topBar = {
                TitleView()
            },
            content = { paddingValues ->
                ContentView(paddingValues)
            },
            bottomBar = {
                BottomView()
            }
        )
    }

    @Composable
    fun TitleView() {
        CustomTitleBar(
            title = String.format(R.string.main_activity_title.toResString),
            isBackPressVisible = false
        )
    }

    @Composable
    fun ContentView(paddingValues: PaddingValues, viewModel: MainViewModel = hiltViewModel()) {

        mainViewModel = viewModel

        LaunchedEffect(Unit) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    is MainContract.Effect.ShowError -> {
                        showToast(effect.message)
                    }
                }
            }
        }

        SetSavedList(paddingValues, viewModel)
    }

    @Composable
    fun SetSavedList(paddingValues: PaddingValues, viewModel: MainViewModel) {

        val state by viewModel.uiState.collectAsState()

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 12.dp),
        ) {
            items(state.saveList) { item ->
                GridItemCard(viewModel, item)
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun GridItemCard(viewModel: MainViewModel, item: SavedSearchEntity) {

        val showDeleteDialog = remember { mutableStateOf(false) }
        val showFullImageDialog = remember { mutableStateOf(false) }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .combinedClickable(
                    onClick = {
                        showDeleteDialog.value = true // 클릭 시 삭제 다이얼로그
                    },
                    onLongClick = {
                        showFullImageDialog.value = true // 롱 클릭 시 이미지 확대 다이얼로그
                    }
                ),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(4.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(item.thumbnail),
                    contentDescription = item.name,
                    modifier = Modifier
                        .fillMaxSize()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (showDeleteDialog.value) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog.value = false },
                title = {
                    Text(String.format(R.string.main_activity_delete_item_dialog_title.toResString))
                },
                text = {
                    Text(String.format(R.string.main_activity_delete_item_dialog_content.toResString))
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteSavedSearchItem(item.id)
                            showDeleteDialog.value = false
                            showToast(String.format(R.string.main_activity_delete_item_dialog_negative_sucess_toast.toResString))
                        }
                    ) {
                        Text(String.format(R.string.main_activity_delete_item_dialog_positive_button.toResString))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog.value = false
                        }
                    ) {
                        Text(String.format(R.string.main_activity_delete_item_dialog_negative_button.toResString))
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

    @Composable
    fun BottomView() {
        CustomBottomFixedButton(
            text = String.format(R.string.main_activity_button_start_search.toResString),
            onClick = {
                ActivityUtil.startSearchActivity(this)
            }
        )
    }
}
