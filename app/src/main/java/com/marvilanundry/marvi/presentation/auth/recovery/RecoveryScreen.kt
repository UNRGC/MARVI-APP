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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
    val messageParts = recoveryViewModelState.message?.split(",", limit = 2)
    val errorParts = recoveryViewModelState.error?.split(",", limit = 2)
    val animatedProgress by animateFloatAsState(
        targetValue = recoveryViewModelState.progressBar, label = "progress"
    )

    var showDialogQuestion by remember { mutableStateOf(false) }
    var showDialogSuccess by remember { mutableStateOf(false) }
    var showDialogError by remember { mutableStateOf(false) }
    var disableScreen by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var viewPassword by remember { mutableStateOf(false) }
    var viewConfirmPassword by remember { mutableStateOf(false) }

    LaunchedEffect(recoveryViewModelState.message, recoveryViewModelState.error) {
        if (recoveryViewModelState.message != null && recoveryViewModelState.isSuccess) {
            dialogMessage = if (messageParts != null && messageParts.size > 1) messageParts[1].trim() else recoveryViewModelState.message ?: ""
            showDialogSuccess = true
        } else if (recoveryViewModelState.error != null) {
            dialogTitle = if (errorParts != null && errorParts.isNotEmpty()) errorParts[0] else "Error"
            dialogMessage = if (errorParts != null && errorParts.size > 1) errorParts[1].trim() else recoveryViewModelState.error ?: ""
            showDialogError = true
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
                    recoveryViewModel.sendOtp()
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
                    recoveryViewModel.clearMessages()
                },
                onDismiss = {
                    showDialogError = false
                    recoveryViewModel.clearMessages()
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
                    .verticalScroll(rememberScrollState())
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
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (recoveryViewModelState.step == RecoveryStep.ENTER_EMAIL)
                        stringResource(id = R.string.marvi_recovery_subtitle)
                    else "Ingresa el código enviado a tu correo y tu nueva contraseña",
                    color = CustomColors.textColor
                )
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outline,
                    strokeCap = StrokeCap.Round)
                Spacer(modifier = Modifier.height(24.dp))
                
                if (recoveryViewModelState.step == RecoveryStep.ENTER_EMAIL) {
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
                            recoveryViewModel.onEmailChange(input)
                        })
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        MARVITextField(
                            icon = R.drawable.ic_unlock2,
                            iconDescription = "Código",
                            label = "Código",
                            value = recoveryViewModelState.code,
                            placeholder = "Ingresa el código",
                            keyboardType = KeyboardType.Number,
                            singleLine = true,
                            onValueChange = { recoveryViewModel.onCodeChange(it) }
                        )
                        MARVITextField(
                            icon = R.drawable.ic_unlock2,
                            iconDescription = "Nueva Contraseña",
                            label = "Nueva Contraseña",
                            value = recoveryViewModelState.password,
                            placeholder = "Mínimo 6 caracteres",
                            trailingIcon = {
                                Icon(
                                    painter = if (viewPassword) painterResource(id = R.drawable.ic_eye_slash) else painterResource(id = R.drawable.ic_eye),
                                    contentDescription = "Ver contraseña",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }) {
                                            viewPassword = !viewPassword
                                        },
                                    tint = MaterialTheme.colorScheme.outlineVariant
                                )
                            },
                            visualTransformation = if (!viewPassword) PasswordVisualTransformation() else VisualTransformation.None,
                            keyboardType = KeyboardType.Password,
                            singleLine = true,
                            onValueChange = { recoveryViewModel.onPasswordChange(it) }
                        )
                        MARVITextField(
                            icon = R.drawable.ic_unlock2,
                            iconDescription = "Confirmar Contraseña",
                            label = "Confirmar Contraseña",
                            value = recoveryViewModelState.confirmPassword,
                            placeholder = "Repite la contraseña",
                            trailingIcon = {
                                Icon(
                                    painter = if (viewConfirmPassword) painterResource(id = R.drawable.ic_eye_slash) else painterResource(id = R.drawable.ic_eye),
                                    contentDescription = "Ver contraseña",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }) {
                                            viewConfirmPassword = !viewConfirmPassword
                                        },
                                    tint = MaterialTheme.colorScheme.outlineVariant
                                )
                            },
                            visualTransformation = if (!viewConfirmPassword) PasswordVisualTransformation() else VisualTransformation.None,
                            keyboardType = KeyboardType.Password,
                            singleLine = true,
                            onValueChange = { recoveryViewModel.onConfirmPasswordChange(it) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(id = R.string.marvi_recovery_tip),
                    color = CustomColors.placeholderColor,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                MARVIButton(
                    text = if (recoveryViewModelState.step == RecoveryStep.ENTER_EMAIL)
                        stringResource(id = R.string.marvi_recovery_button)
                    else "Restablecer Contraseña",
                    modifier = Modifier.fillMaxWidth(),
                    enabled = if (recoveryViewModelState.step == RecoveryStep.ENTER_EMAIL)
                        recoveryViewModelState.isRecoveryEnabled
                    else recoveryViewModelState.isResetEnabled
                ) {
                    if (recoveryViewModelState.step == RecoveryStep.ENTER_EMAIL) {
                        showDialogQuestion = true
                    } else {
                        recoveryViewModel.resetPassword()
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