package com.collector.presentation.view.base

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.collector.presentation.R
import com.collector.presentation.util.extension.toResString
import com.collector.presentation.util.log.collectorLog

abstract class BaseActivity : ComponentActivity() {

    protected val activityName = this.javaClass.simpleName

    @Composable
    abstract fun InitView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        collectorLog.d("${activityName} onCreate")

        setContent {
            InitView()
        }
    }

    override fun onResume() {
        super.onResume()
        collectorLog.d("${activityName} onResume")
    }

    override fun onStop() {
        super.onStop()
        collectorLog.d("${activityName} onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        collectorLog.d("${activityName} onDestroy")
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @Composable
    fun CustomTitleBar(
        title: String,
        isBackPressVisible: Boolean,
        modifier: Modifier = Modifier,
        backgroundColor: Color = MaterialTheme.colorScheme.primary,
        titleColor: Color = MaterialTheme.colorScheme.onPrimary,
        backIconColor: Color = MaterialTheme.colorScheme.onPrimary // 뒤로가기 아이콘 색상
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            if (isBackPressVisible) {
                IconButton(
                    onClick = {
                        finish()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "back",
                        tint = backIconColor
                    )
                }
            }

            Text(
                text = title,
                color = titleColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    @Composable
    fun CustomTextField(
        value: String,
        label: String,
        modifier: Modifier = Modifier,
        onValueChange: (String) -> Unit,
        isSecret: Boolean = false,
        focusRequester: FocusRequester,
        onImeAction: () -> Unit
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = label,
                modifier = Modifier.padding(bottom = 4.dp),
                style = TextStyle(
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
            )

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .focusRequester(focusRequester),
                maxLines = 1,
                visualTransformation = if (isSecret) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onImeAction()
                    }
                )
            )
        }
    }

    @Composable
    fun CustomBottomFixedButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        isEnabled: Boolean = true,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Button(
                onClick = onClick,
                enabled = isEnabled,
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
            ) {
                Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    @Composable
    fun SetBackPressHandler() {
        val showExitDialog = remember { mutableStateOf(false) }

        BackHandler {
            showExitDialog.value = true
        }

        if (showExitDialog.value) {
            ExitDialog(onDismiss = { showExitDialog.value = false }, onConfirm = {
                finish()
            })
        }
    }

    @Composable
    fun ExitDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(String.format(R.string.base_activity_exit_dialog_title.toResString))
            },
            text = {
                Text(String.format(R.string.base_activity_exit_dialog_content.toResString))
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(String.format(R.string.base_activity_exit_dialog_positive_button.toResString))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(String.format(R.string.base_activity_exit_dialog_negative_button.toResString))
                }
            }
        )
    }
}