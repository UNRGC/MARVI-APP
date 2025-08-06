package com.marvilanundry.marvi.presentation.auth.recovery

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.presentation.core.components.MARVIButton
import com.marvilanundry.marvi.presentation.core.components.MARVIButtonType
import com.marvilanundry.marvi.presentation.core.components.MARVIDialog
import com.marvilanundry.marvi.presentation.core.components.MARVIDialogType
import com.marvilanundry.marvi.presentation.core.components.MARVITextField
import com.marvilanundry.marvi.ui.theme.CustomColors

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun RecoveryScreen(
    onNavigateToLogin: () -> Unit = {}
) {
    val recoveryViewModel: RecoveryViewModel = hiltViewModel()
    val recoveryViewModelState by recoveryViewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val showDialogLoading = recoveryViewModelState.isLoading
    val message = recoveryViewModelState.message?.split(",", limit = 2)
    val error = recoveryViewModelState.error?.split(",", limit = 2)
    val animatedProgress by animateFloatAsState(
        targetValue = recoveryViewModelState.progressBar
    )

    var showDialogQuestion by remember { mutableStateOf(false) }
    var showDialogSuccess by remember { mutableStateOf(false) }
    var showDialogError by remember { mutableStateOf(false) }
    var disableScreen by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(recoveryViewModelState.isLoading) {
        when {
            message != null -> {
                dialogMessage = if (message.size > 1) message[1].trim() else message[0]
                showDialogSuccess = true
            }

            error != null -> {
                dialogTitle = error[0]
                dialogMessage = if (error.size > 1) error[1].trim() else error[0]
                showDialogError = true
            }
        }
    }

    when {
        showDialogLoading -> {
            MARVIDialog(
                type = MARVIDialogType.LOADING,
                title = stringResource(id = R.string.marvi_core_dialog_loading_title),
                message = stringResource(id = R.string.marvi_core_dialog_loading_message)
            )
        }

        showDialogQuestion -> {
            MARVIDialog(
                type = MARVIDialogType.QUESTION,
                title = stringResource(id = R.string.marvi_recovery_dialog_question_title),
                message = stringResource(id = R.string.marvi_recovery_dialog_question_message),
                confirmButtonText = stringResource(id = R.string.marvi_recovery_dialog_question_confirm_button),
                dismissButtonText = stringResource(id = R.string.marvi_recovery_dialog_question_dismiss_button),
                onConfirm = {
                    showDialogQuestion = false
                    recoveryViewModel.recovery()
                },
                onDismiss = {
                    showDialogQuestion = false
                })
        }

        showDialogSuccess -> {
            MARVIDialog(
                type = MARVIDialogType.SUCCESS,
                title = stringResource(id = R.string.marvi_recovery_dialog_success_title),
                message = dialogMessage,
                confirmButtonText = stringResource(id = R.string.marvi_recovery_dialog_success_confirm_button),
                onConfirm = {
                    showDialogSuccess = false
                    onNavigateToLogin()
                })
        }

        showDialogError -> {
            MARVIDialog(
                type = MARVIDialogType.ERROR,
                title = dialogTitle,
                message = dialogMessage,
                confirmButtonText = stringResource(id = R.string.marvi_recovery_dialog_error_confirm_button),
                onConfirm = {
                    showDialogError = false
                },
                onDismiss = {
                    showDialogError = false
                })
        }
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    focusManager.clearFocus()
                }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MARVIButton(
                        type = MARVIButtonType.ICON,
                        iconRes = R.drawable.ic_arrow_left,
                    ) {
                        disableScreen = true
                        onNavigateToLogin()
                    }
                    Text(
                        text = stringResource(id = R.string.marvi_recovery_title),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 40.dp),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                Text(
                    text = stringResource(id = R.string.marvi_recovery_subtitle),
                    color = CustomColors.textColor,
                    textAlign = TextAlign.Center
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outline,
                    strokeCap = StrokeCap.Round,
                    drawStopIndicator = {})
                Spacer(
                    modifier = Modifier.height(24.dp)
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    MARVITextField(
                        icon = R.drawable.ic_envelope,
                        iconDescription = stringResource(id = R.string.marvi_recovery_email),
                        label = stringResource(id = R.string.marvi_recovery_email),
                        value = recoveryViewModelState.email,
                        placeholder = stringResource(id = R.string.marvi_recovery_email_placeholder),
                        keyboardType = KeyboardType.Email,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                showDialogQuestion = true
                            }
                        ),
                        capitalization = KeyboardCapitalization.None,
                        singleLine = true,
                        onValueChange = { input ->
                            recoveryViewModel.onEmailChange(input.trim())
                        })
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = stringResource(id = R.string.marvi_recovery_tip),
                        color = CustomColors.placeholderColor,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )
                    MARVIButton(
                        text = stringResource(id = R.string.marvi_recovery_button),
                        modifier = Modifier.fillMaxWidth(),
                        message = stringResource(id = R.string.marvi_recovery_button_message),
                        enabled = recoveryViewModelState.isRecoveryEnabled
                    ) {
                        showDialogQuestion = true
                    }
                }
            }
            if (disableScreen) Box(
                modifier = Modifier
                    .matchParentSize()
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                awaitPointerEvent()
                            }
                        }
                    })
        }
    }
}