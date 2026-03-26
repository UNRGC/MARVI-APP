package com.marvilanundry.marvi.presentation.auth.login

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.presentation.core.components.MARVIButton
import com.marvilanundry.marvi.presentation.core.components.MARVIButtonType
import com.marvilanundry.marvi.presentation.core.components.MARVIDialog
import com.marvilanundry.marvi.presentation.core.components.MARVIDialogType
import com.marvilanundry.marvi.presentation.core.components.MARVITextField
import com.marvilanundry.marvi.presentation.core.navigation.SharedViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun LoginScreen(
    sharedViewModel: SharedViewModel,
    onNavigateToRegister: () -> Unit = {},
    onNavigateToForgot: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    // Variables
    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginViewModelState by loginViewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val showDialogLoading = loginViewModelState.isLoading
    val googleNoCredentialMessage = stringResource(id = R.string.marvi_login_google_no_credential_message)

    var showDialogSuccess: Boolean by remember { mutableStateOf(false) }
    var showDialogError: Boolean by remember { mutableStateOf(false) }
    var viewPassword: Boolean by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(loginViewModelState.error, loginViewModelState.client) {
        if (loginViewModelState.error != null) {
            val error = loginViewModelState.error!!.split(",", limit = 2)
            dialogMessage = if (error.size > 1) error[1].trim() else error[0]
            showDialogError = true
        } else if (loginViewModelState.client != null) {
            sharedViewModel.setSession(
                client = loginViewModelState.client!!,
                token = loginViewModelState.authToken
            )
            showDialogSuccess = true
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
                message = stringResource(id = R.string.marvi_login_dialog_success_message),
                confirmButtonText = stringResource(id = R.string.marvi_login_dialog_success_confirm_button),
                onConfirm = {
                    loginViewModel.resetState()
                    showDialogSuccess = false
                    onNavigateToHome()
                },
                onDismiss = {
                    loginViewModel.resetState()
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
                    loginViewModel.resetState()
                },
                onDismiss = {
                    showDialogError = false
                    loginViewModel.resetState()
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
                            loginViewModel.onEmailChange(input)
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
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                loginViewModel.login()
                            }),
                        capitalization = KeyboardCapitalization.None,
                        singleLine = true,
                        onValueChange = { input ->
                            loginViewModel.onPasswordChange(input)
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
                        focusManager.clearFocus()
                        loginViewModel.login()
                    }
                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                    MARVIButton(
                        text = stringResource(id = R.string.marvi_login_google_button),
                        modifier = Modifier.fillMaxWidth(),
                        type = MARVIButtonType.OUTLINED
                    ) {
                        focusManager.clearFocus()
                        scope.launch {
                            try {
                                val idToken = getGoogleIdToken(context)
                                loginViewModel.loginAsGoogle(idToken)
                            } catch (e: GetCredentialCancellationException) {
                                Log.w("GoogleSignIn", "Inicio con Google cancelado")
                                loginViewModel.onGoogleSignInError("Inicio con Google cancelado.")
                            } catch (e: NoCredentialException) {
                                Log.i("GoogleSignIn", "Sin credenciales de Google disponibles para este dispositivo")
                                loginViewModel.onGoogleSignInError(googleNoCredentialMessage)
                            } catch (e: GetCredentialException) {
                                Log.e("GoogleSignIn", "Error de CredentialManager: ${e.message} (Type: ${e.type})", e)
                                loginViewModel.onGoogleSignInError(
                                    e.message ?: "No se pudo iniciar con Google."
                                )
                            } catch (e: Exception) {
                                Log.e("GoogleSignIn", "Error inesperado al obtener token: ${e.message}", e)
                                loginViewModel.onGoogleSignInError(
                                    e.message ?: "No se pudo obtener el token de Google."
                                )
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        MARVIButton(
                            text = stringResource(id = R.string.marvi_login_forgot_button),
                            modifier = Modifier.weight(1f),
                            type = MARVIButtonType.LINK,
                            fontSize = 12.sp
                        ) {
                            loginViewModel.resetState()
                            onNavigateToForgot()
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        MARVIButton(
                            text = stringResource(id = R.string.marvi_login_register_button),
                            modifier = Modifier.weight(1f),
                            type = MARVIButtonType.LINK,
                            fontSize = 12.sp
                        ) {
                            loginViewModel.resetState()
                            onNavigateToRegister()
                        }
                    }
                }
            }
        }
    }
}

private suspend fun getGoogleIdToken(context: Context): String {
    val credentialManager = CredentialManager.create(context)
    val googleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId(context.getString(R.string.web_client_id))
        .setFilterByAuthorizedAccounts(false)
        .setAutoSelectEnabled(false)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    val result = credentialManager.getCredential(context = context, request = request)
    val credential = result.credential

    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
    ) {
        return GoogleIdTokenCredential.createFrom(credential.data).idToken
    }

    throw IllegalStateException("Credencial de Google invalida.")
}
