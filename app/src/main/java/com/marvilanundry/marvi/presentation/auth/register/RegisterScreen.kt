package com.marvilanundry.marvi.presentation.auth.register

import ClientViewModel
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marvilanundry.marvi.ui.theme.CustomColors
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.presentation.core.components.MARVIBackIconButton
import com.marvilanundry.marvi.presentation.core.components.MARVIDialogError
import com.marvilanundry.marvi.presentation.core.components.MARVIDialogQuestion
import com.marvilanundry.marvi.presentation.core.components.MARVIDialogLoading
import com.marvilanundry.marvi.presentation.core.components.MARVIDialogSuccess
import com.marvilanundry.marvi.presentation.core.components.MARVIPrimaryButton
import com.marvilanundry.marvi.presentation.core.components.MARVISecondaryButton
import com.marvilanundry.marvi.presentation.core.components.MARVITextField

@Composable
private fun RegisterPersonalSection(
    modifier: Modifier,
    state: RegisterUiState,
    scrollState: ScrollState,
    onCodeChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onFirstSurnameChange: (String) -> Unit,
    onSecondSurnameChange: (String) -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MARVITextField(
            label = stringResource(id = R.string.marvi_register_code),
            value = state.code,
            placeholder = stringResource(id = R.string.marvi_register_code_placeholder),
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.None,
            singleLine = true,
            onValueChange = { input ->
                onCodeChange(input.trim())
            })
        Spacer(modifier = Modifier.height(24.dp))
        MARVITextField(
            label = stringResource(id = R.string.marvi_register_name),
            value = state.name,
            placeholder = stringResource(id = R.string.marvi_register_name_placeholder),
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Words,
            singleLine = true,
            onValueChange = onNameChange
        )
        Spacer(modifier = Modifier.height(24.dp))
        MARVITextField(
            label = stringResource(id = R.string.marvi_register_first_surname),
            value = state.firstSurname,
            placeholder = stringResource(id = R.string.marvi_register_first_surname_placeholder),
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Words,
            singleLine = true,
            onValueChange = onFirstSurnameChange
        )
        Spacer(modifier = Modifier.height(24.dp))
        MARVITextField(
            label = stringResource(id = R.string.marvi_register_second_surname),
            value = state.secondSurname,
            placeholder = stringResource(id = R.string.marvi_register_second_surname_placeholder),
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Words,
            singleLine = true,
            onValueChange = onSecondSurnameChange
        )
    }
}

@Composable
private fun RegisterCountSection(
    modifier: Modifier,
    state: RegisterUiState,
    scrollState: ScrollState,
    onPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    var viewPassword: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MARVITextField(
            label = stringResource(id = R.string.marvi_register_phone),
            value = state.phone,
            placeholder = stringResource(id = R.string.marvi_register_phone_placeholder),
            keyboardType = KeyboardType.Phone,
            capitalization = KeyboardCapitalization.None,
            singleLine = true,
            maxLength = 10,
            onValueChange = { input ->
                onPhoneChange(input.filter(Char::isDigit))
            })
        Spacer(modifier = Modifier.height(24.dp))
        MARVITextField(
            label = stringResource(id = R.string.marvi_register_email),
            value = state.email,
            placeholder = stringResource(id = R.string.marvi_register_email_placeholder),
            keyboardType = KeyboardType.Email,
            capitalization = KeyboardCapitalization.None,
            singleLine = true,
            onValueChange = { input ->
                onEmailChange(input.trim())
            })
        Spacer(modifier = Modifier.height(24.dp))
        MARVITextField(
            label = stringResource(id = R.string.marvi_register_password),
            value = state.password,
            placeholder = stringResource(id = R.string.marvi_register_password_placeholder),
            trailingIcon = {
                Icon(
                    painter = if (viewPassword) painterResource(id = R.drawable.ic_eye_slash) else painterResource(
                        id = R.drawable.ic_eye
                    ),
                    contentDescription = "Mostrar/Ocultar contraseña",
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
            capitalization = KeyboardCapitalization.None,
            singleLine = true,
            maxLength = 20,
            onValueChange = { input ->
                onPasswordChange(input.trim())
            })
    }
}

@Composable
fun RegisterScreen(
    clientViewModel: ClientViewModel,
    registerViewModel: RegisterViewModel = viewModel(),
    onNavigateToLogin: () -> Unit = {}
) {
    val clientViewModelState by clientViewModel.state.collectAsStateWithLifecycle()
    val registerViewModelState by registerViewModel.state.collectAsStateWithLifecycle()
    val animatedProgress by animateFloatAsState(
        targetValue = registerViewModelState.progressBar
    )
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var showDialogLoading by remember { mutableStateOf(false) }
    var showDialogQuestion by remember { mutableStateOf(false) }
    var showDialogSuccess by remember { mutableStateOf(false) }
    var showDialogError by remember { mutableStateOf(false) }
    var disableScreen by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        if (registerViewModelState.section == 0) {
            disableScreen = true
            onNavigateToLogin()
        } else registerViewModel.onSectionChange(0)
    }

    MARVIDialogLoading(
        visible = showDialogLoading,
        title = stringResource(id = R.string.marvi_register_dialog_loading_title),
        message = stringResource(id = R.string.marvi_register_dialog_loading_message)
    )

    MARVIDialogQuestion(
        visible = showDialogQuestion,
        title = stringResource(id = R.string.marvi_register_dialog_question_title),
        message = stringResource(id = R.string.marvi_register_dialog_question_message),
        confirmButtonText = "Sí",
        dismissButtonText = "No",
        onDismissRequest = {
            showDialogQuestion = false
        }) {
        showDialogQuestion = false
        showDialogSuccess = true
    }

    MARVIDialogError(
        visible = showDialogError,
        title = "Error al registrar",
        message = "El nombre de usario ya existe.",
        confirmButtonText = "Aceptar",
    ) {
        showDialogError = false
    }

    MARVIDialogSuccess(
        visible = showDialogSuccess,
        title = stringResource(id = R.string.marvi_register_dialog_success_title),
        message = stringResource(id = R.string.marvi_register_dialog_success_message),
        confirmButtonText = stringResource(id = R.string.marvi_login_button)
    ) {
        showDialogSuccess = false
        disableScreen = true
        onNavigateToLogin()
    }

    LaunchedEffect(clientViewModelState.isLoading) {
        val client = clientViewModelState.client
        val message = clientViewModelState.error

        when (clientViewModelState.isLoading) {
            true -> {
                showDialogLoading = true
            }

            false -> {
                showDialogLoading = false
                if (message != null) {
                    registerViewModel.onSectionChange(1)
                } else if (client != null) {
                    Toast.makeText(context, "El nombre de usuario ya existe.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { focusManager.clearFocus() })
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MARVIBackIconButton {
                        if (registerViewModelState.section == 0) {
                            disableScreen = true
                            onNavigateToLogin()
                        } else registerViewModel.onSectionChange(0)
                    }
                    Text(
                        text = stringResource(id = R.string.marvi_register_title),
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
                    text = stringResource(id = R.string.marvi_register_subtitle),
                    color = CustomColors.textColor,
                    textAlign = TextAlign.Justify
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
                AnimatedContent(
                    targetState = registerViewModelState.section,
                    modifier = Modifier.weight(1f),
                    label = "RegisterSection",
                    transitionSpec = { fadeIn().togetherWith(fadeOut()) }) { section ->
                    if (section == 0) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            RegisterPersonalSection(
                                modifier = Modifier.weight(1f),
                                state = registerViewModelState,
                                scrollState = scrollState,
                                onCodeChange = registerViewModel::onCodeChange,
                                onNameChange = registerViewModel::onNameChange,
                                onFirstSurnameChange = registerViewModel::onFirstSurnameChange,
                                onSecondSurnameChange = registerViewModel::onSecondSurnameChange
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            MARVIPrimaryButton(
                                text = stringResource(id = R.string.marvi_register_next_button),
                                modifier = Modifier.fillMaxWidth(),
                                message = stringResource(id = R.string.marvi_register_next_button_message),
                                enabled = registerViewModelState.isNextEnabled
                            ) {
                                clientViewModel.loadClient(registerViewModelState.code)
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            RegisterCountSection(
                                modifier = Modifier.weight(1f),
                                state = registerViewModelState,
                                scrollState = scrollState,
                                onPhoneChange = registerViewModel::onPhoneChange,
                                onEmailChange = registerViewModel::onEmailChange,
                                onPasswordChange = registerViewModel::onPasswordChange
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(modifier = Modifier.fillMaxWidth()) {
                                MARVISecondaryButton(
                                    text = stringResource(id = R.string.marvi_register_back_button),
                                    modifier = Modifier.weight(1f),
                                ) {
                                    registerViewModel.onSectionChange(0)
                                }
                                Spacer(modifier = Modifier.size(16.dp))
                                MARVIPrimaryButton(
                                    text = stringResource(id = R.string.marvi_register_button),
                                    modifier = Modifier.weight(1f),
                                    message = stringResource(id = R.string.marvi_register_button_message),
                                    enabled = registerViewModelState.isRegisterEnabled
                                ) {
                                    showDialogQuestion = true
                                }
                            }
                        }
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