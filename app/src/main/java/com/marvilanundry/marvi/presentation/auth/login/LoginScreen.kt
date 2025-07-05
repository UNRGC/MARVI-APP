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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.presentation.core.components.MARVIIconTextField
import com.marvilanundry.marvi.presentation.core.components.MARVILinkButton
import com.marvilanundry.marvi.presentation.core.components.MARVIPrimaryButton
import com.marvilanundry.marvi.presentation.core.components.MARVIPrimaryOutlinedButton

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = LoginViewModel(),
    onNavigateToRegister: () -> Unit = {},
    onNavigateToForgot: () -> Unit = {}
) {
    Scaffold { padding ->
        // Variables
        val loginViewModelState by loginViewModel.state.collectAsStateWithLifecycle()
        val focusManager = LocalFocusManager.current
        var viewPassword: Boolean by remember { mutableStateOf(false) }

        // Contenedor
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    focusManager.clearFocus()
                }, contentAlignment = Alignment.Center
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
                    MARVIIconTextField(
                        icon = R.drawable.ic_envelope,
                        iconDescription = "Icono de correo electrónico",
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
                    MARVIIconTextField(
                        icon = R.drawable.ic_unlock2,
                        iconDescription = "Icono de contraseña",
                        label = stringResource(id = R.string.marvi_login_password),
                        value = loginViewModelState.password,
                        placeholder = stringResource(id = R.string.marvi_login_password_placeholder),
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
                    MARVIPrimaryButton(
                        text = stringResource(id = R.string.marvi_login_button),
                        modifier = Modifier.fillMaxWidth(),
                        message = stringResource(id = R.string.marvi_login_button_message),
                        enabled = loginViewModelState.isLoginEnabled
                    ) { }
                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                    MARVIPrimaryOutlinedButton(
                        text = stringResource(id = R.string.marvi_login_forgot_button),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        onNavigateToForgot()
                    }
                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                    MARVILinkButton(
                        text = stringResource(id = R.string.marvi_login_register_button),
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 12.sp
                    ) {
                        onNavigateToRegister()
                    }
                }
            }
        }
    }
}