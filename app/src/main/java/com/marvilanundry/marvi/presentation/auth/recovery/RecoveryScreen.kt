package com.marvilanundry.marvi.presentation.auth.recovery

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.presentation.core.components.MARVIDialogQuestion
import com.marvilanundry.marvi.presentation.core.components.MARVIDialogLoading
import com.marvilanundry.marvi.presentation.core.components.MARVIDialogSuccess
import com.marvilanundry.marvi.presentation.core.components.MARVIIconTextField
import com.marvilanundry.marvi.presentation.core.components.MARVIPrimaryButton
import com.marvilanundry.marvi.presentation.core.components.MARVISecondaryButton

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun RecoveryScreen(
    recoveryViewModel: RecoveryViewModel = RecoveryViewModel(), onNavigateToLogin: () -> Unit = {}
) {
    val recoveryViewModelState by recoveryViewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    var showDialogLoading by remember { mutableStateOf(false) }
    var showDialogQuestion by remember { mutableStateOf(false) }
    var showDialogSuccess by remember { mutableStateOf(false) }

    MARVIDialogLoading(
        visible = showDialogLoading,
        title = "Cargando...",
        message = "Por favor, espere mientras se procesa su solicitud."
    )

    MARVIDialogQuestion(
        visible = showDialogQuestion,
        title = "Confirmación",
        message = "¿Está seguro de que desea restablecer su contraseña?",
        dismissButtonText = "Cancelar",
        confirmButtonText = "Confirmar",
        onDismissRequest = { }) { }

    MARVIDialogSuccess(
        visible = showDialogSuccess,
        title = "Éxito",
        message = "Se ha enviado un correo electrónico para restablecer su contraseña.",
        confirmButtonText = "Aceptar"
    ) { }

    Scaffold { padding ->
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

                // Formulario de recuperación
                Column(
                    modifier = Modifier.weight(maxWeight), verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Ingresa tu correo electrónico registrado para restablecer tu contraseña.",
                    )
                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )
                    MARVIIconTextField(
                        icon = R.drawable.ic_envelope,
                        iconDescription = "Icono de correo electrónico",
                        label = stringResource(id = R.string.marvi_login_email),
                        value = recoveryViewModelState.email,
                        placeholder = stringResource(id = R.string.marvi_login_email_placeholder),
                        keyboardType = KeyboardType.Email,
                        capitalization = KeyboardCapitalization.None,
                        singleLine = true,
                        onValueChange = { input ->
                            recoveryViewModel.onEmailChange(input.trim())
                        })
                }

                // Botones de acción
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row (modifier = Modifier.fillMaxWidth()) {
                        MARVISecondaryButton(
                            text = "Cancelar",
                            modifier = Modifier.weight(1f)
                        ) {
                            onNavigateToLogin()
                        }
                        Spacer(
                            modifier = Modifier.width(16.dp)
                        )
                        MARVIPrimaryButton(
                            text = "Restablecer",
                            modifier = Modifier.weight(1f),
                            message = "El correo electrónico debe ser valido",
                            enabled = recoveryViewModelState.isRecoveryEnabled
                        ) {
                            showDialogLoading = true
                        }
                    }
                }
            }
        }
    }
}