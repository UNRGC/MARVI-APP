package com.marvilanundry.marvi.presentation.auth.login

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit = {},
    onNavigateToForgot: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    // Variables
    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginViewModelState by loginViewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val showDialogLoading = loginViewModelState.isLoading
    val error = loginViewModelState.error?.split(",", limit = 2)
    val client = loginViewModelState.client

    var showDialogSuccess: Boolean by remember { mutableStateOf(false) }
    var showDialogError: Boolean by remember { mutableStateOf(false) }
    var viewPassword: Boolean by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(loginViewModelState.isLoading) {
        when {
            error != null -> {
                dialogMessage = if (error.size > 1) error[1].trim() else error[0]
                showDialogError = true
            }

            client != null -> {
                showDialogSuccess = true
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

        showDialogSuccess -> {
            MARVIDialog(
                type = MARVIDialogType.SUCCESS,
                title = stringResource(id = R.string.marvi_login_dialog_success_title),
                onConfirm = {
                    showDialogSuccess = false
                    onNavigateToHome()
                })
        }

        showDialogError -> {
            MARVIDialog(
                type = MARVIDialogType.ERROR,
                title = stringResource(id = R.string.marvi_login_dialog_error_title),
                message = dialogMessage,
                onConfirm = {
                    showDialogError = false
                })
        }
    }

    Scaffold { padding ->
        // Contenedor
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { focusManager.clearFocus() }), contentAlignment = Alignment.Center
        ) {
            // Determinar el alto máximo del formulario
            val maxWeight = if (maxWidth <= 360.dp) 1.5f else 1.2f

            // Contenido
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                // Logo
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.height(82.dp)
                    )
                }

                // Formulario de inicio de sesión
                Column(
                    modifier = Modifier.weight(maxWeight), verticalArrangement = Arrangement.Top
                ) {
                    MARVITextField(
                        icon = R.drawable.ic_envelope,
                        iconDescription = stringResource(id = R.string.marvi_login_email),
                        label = stringResource(id = R.string.marvi_login_email),
                        value = loginViewModelState.email,
                        placeholder = stringResource(id = R.string.marvi_login_email_placeholder),
                        keyboardType = KeyboardType.Email,
                        capitalization = KeyboardCapitalization.None,
                        singleLine = true,
                        onValueChange = { input ->
                            loginViewModel.onEmailChange(input.trim())
                        })
                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )
                    MARVITextField(
                        icon = R.drawable.ic_unlock2,
                        iconDescription = stringResource(id = R.string.marvi_login_password),
                        label = stringResource(id = R.string.marvi_login_password),
                        value = loginViewModelState.password,
                        placeholder = stringResource(id = R.string.marvi_login_password_placeholder),
                        trailingIcon = {
                            Icon(
                                painter = if (viewPassword) painterResource(id = R.drawable.ic_eye_slash) else painterResource(
                                    id = R.drawable.ic_eye
                                ),
                                contentDescription = stringResource(id = R.string.marvi_login_password),
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
                        onValueChange = { input ->
                            loginViewModel.onPasswordChange(input.trim())
                        })
                }

                // Botones de acción
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    MARVIButton(
                        text = stringResource(id = R.string.marvi_login_button),
                        modifier = Modifier.fillMaxWidth(),
                        message = stringResource(id = R.string.marvi_login_button_message),
                        enabled = loginViewModelState.isLoginEnabled
                    ) {
                        loginViewModel.login()
                    }
                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                    MARVIButton(
                        text = stringResource(id = R.string.marvi_login_forgot_button),
                        modifier = Modifier.fillMaxWidth(),
                        type = MARVIButtonType.OUTLINED
                    ) {
                        onNavigateToForgot()
                    }
                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                    MARVIButton(
                        text = stringResource(id = R.string.marvi_login_register_button),
                        modifier = Modifier.fillMaxWidth(),
                        type = MARVIButtonType.LINK,
                        fontSize = 12.sp
                    ) {
                        onNavigateToRegister()
                    }
                }
            }
        }
    }
}