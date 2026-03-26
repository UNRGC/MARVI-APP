package com.marvilanundry.marvi.presentation.auth.home.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.presentation.auth.home.HomeUiState
import com.marvilanundry.marvi.presentation.auth.home.HomeViewModel
import com.marvilanundry.marvi.presentation.auth.home.components.ColumnScrollable
import com.marvilanundry.marvi.presentation.auth.home.components.Container
import com.marvilanundry.marvi.presentation.auth.home.components.SectionHeader
import com.marvilanundry.marvi.presentation.core.components.MARVIButton
import com.marvilanundry.marvi.presentation.core.components.MARVIButtonType
import com.marvilanundry.marvi.presentation.core.components.MARVITextField
import MARVIFloatingActionButton

@Composable
fun AccountScreen(homeViewModel: HomeViewModel, homeViewModelState: HomeUiState) {
    var viewCurrentPassword by remember { mutableStateOf(false) }
    var viewNewPassword by remember { mutableStateOf(false) }
    var viewConfirmPassword by remember { mutableStateOf(false) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        ColumnScrollable(
            paddingValues = PaddingValues(24.dp, 24.dp, 24.dp, 16.dp)
        ) {
            Container {
                SectionHeader(
                    iconRes = R.drawable.ic_person_vcard, title = "Información de la cuenta"
                )
            }
            Container {
                MARVITextField(
                    label = stringResource(id = R.string.marvi_register_name),
                    value = homeViewModelState.clientName,
                    enabled = homeViewModelState.isEditEnabled,
                    placeholder = stringResource(id = R.string.marvi_register_name_placeholder),
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                    singleLine = true,
                    maxLength = 50,
                    onValueChange = { homeViewModel.onClientNameChange(it) })
                MARVITextField(
                    label = stringResource(id = R.string.marvi_register_first_surname),
                    value = homeViewModelState.clientFirstSurname,
                    enabled = homeViewModelState.isEditEnabled,
                    placeholder = stringResource(id = R.string.marvi_register_first_surname_placeholder),
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                    singleLine = true,
                    maxLength = 50,
                    onValueChange = { homeViewModel.onClientFirstSurnameChange(it) })
                MARVITextField(
                    label = stringResource(id = R.string.marvi_register_second_surname),
                    value = homeViewModelState.clientSecondSurname,
                    enabled = homeViewModelState.isEditEnabled,
                    placeholder = stringResource(id = R.string.marvi_register_second_surname_placeholder),
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                    singleLine = true,
                    maxLength = 50,
                    onValueChange = { homeViewModel.onClientSecondSurnameChange(it) })
                MARVITextField(
                    label = stringResource(id = R.string.marvi_register_phone),
                    value = homeViewModelState.clientPhone,
                    enabled = homeViewModelState.isEditEnabled,
                    placeholder = stringResource(id = R.string.marvi_register_phone_placeholder),
                    keyboardType = KeyboardType.Phone,
                    capitalization = KeyboardCapitalization.None,
                    singleLine = true,
                    maxLength = 10,
                    onValueChange = { homeViewModel.onClientPhoneChange(it) })
                MARVITextField(
                    label = stringResource(id = R.string.marvi_register_email),
                    value = homeViewModelState.clientEmail,
                    enabled = homeViewModelState.isEditEnabled,
                    placeholder = stringResource(id = R.string.marvi_register_email_placeholder),
                    keyboardType = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.None,
                    singleLine = true,
                    onValueChange = { homeViewModel.onClientEmailChange(it) })
                Row(modifier = Modifier) {
                    MARVIButton(
                        text = "Descartar",
                        type = MARVIButtonType.SECONDARY,
                        modifier = Modifier.weight(1f),
                        enabled = homeViewModelState.isEditEnabled,
                        message = "No se han realizado cambios"
                    ) {
                        homeViewModel.setEditModes(false)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    MARVIButton(
                        text = "Guardar",
                        modifier = Modifier.weight(1f),
                        enabled = homeViewModelState.isEditEnabled && homeViewModelState.isSaveEnabled,
                        message = if (homeViewModelState.isEditEnabled) {
                            "Completa nombre, apellido, teléfono y correo para guardar"
                        } else {
                            "No se han realizado cambios"
                        }
                    ) {
                        homeViewModel.updateClient()
                    }
                }
            }
            Container {
                SectionHeader(
                    iconRes = R.drawable.ic_key, title = "Cambiar contraseña"
                )
            }
            Container {
                MARVITextField(
                    label = "Contraseña actual",
                    value = homeViewModelState.currentPassword,
                    enabled = homeViewModelState.isChangePasswordEnabled,
                    placeholder = "Ingresa tu contraseña actual",
                    keyboardType = KeyboardType.Password,
                    capitalization = KeyboardCapitalization.None,
                    singleLine = true,
                    trailingIcon = {
                        Icon(
                            painter = if (viewCurrentPassword) painterResource(id = R.drawable.ic_eye_slash) else painterResource(
                                id = R.drawable.ic_eye
                            ),
                            contentDescription = "Toggle password visibility",
                            modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    viewCurrentPassword = !viewCurrentPassword
                                },
                            tint = MaterialTheme.colorScheme.outlineVariant
                        )
                    },
                    visualTransformation = if (!viewCurrentPassword) PasswordVisualTransformation() else VisualTransformation.None,
                    onValueChange = { homeViewModel.onCurrentPasswordChange(it) })
                MARVITextField(
                    label = "Nueva contraseña",
                    value = homeViewModelState.newPassword,
                    enabled = homeViewModelState.isChangePasswordEnabled,
                    placeholder = "Ingresa tu nueva contraseña",
                    keyboardType = KeyboardType.Password,
                    capitalization = KeyboardCapitalization.None,
                    singleLine = true,
                    trailingIcon = {
                        Icon(
                            painter = if (viewNewPassword) painterResource(id = R.drawable.ic_eye_slash) else painterResource(
                                id = R.drawable.ic_eye
                            ),
                            contentDescription = "Toggle password visibility",
                            modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    viewNewPassword = !viewNewPassword
                                },
                            tint = MaterialTheme.colorScheme.outlineVariant
                        )
                    },
                    visualTransformation = if (!viewNewPassword) PasswordVisualTransformation() else VisualTransformation.None,
                    onValueChange = { homeViewModel.onNewPasswordChange(it) })
                MARVITextField(
                    label = "Confirmar nueva contraseña",
                    value = homeViewModelState.confirmPassword,
                    enabled = homeViewModelState.isChangePasswordEnabled,
                    placeholder = "Confirma tu nueva contraseña",
                    keyboardType = KeyboardType.Password,
                    capitalization = KeyboardCapitalization.None,
                    singleLine = true,
                    trailingIcon = {
                        Icon(
                            painter = if (viewConfirmPassword) painterResource(id = R.drawable.ic_eye_slash) else painterResource(
                                id = R.drawable.ic_eye
                            ),
                            contentDescription = "Toggle password visibility",
                            modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    viewConfirmPassword = !viewConfirmPassword
                                },
                            tint = MaterialTheme.colorScheme.outlineVariant
                        )
                    },
                    visualTransformation = if (!viewConfirmPassword) PasswordVisualTransformation() else VisualTransformation.None,
                    onValueChange = { homeViewModel.onConfirmPasswordChange(it) })
                Row(modifier = Modifier) {
                    MARVIButton(
                        text = "Cancelar",
                        type = MARVIButtonType.SECONDARY,
                        modifier = Modifier.weight(1f),
                        enabled = homeViewModelState.isChangePasswordEnabled,
                        message = "No se han realizado cambios"
                    ) {
                        homeViewModel.setEditModes(false)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    MARVIButton(
                        text = "Guardar",
                        modifier = Modifier.weight(1f),
                        enabled = if (homeViewModelState.isChangePasswordEnabled) {
                            homeViewModelState.currentPassword.isNotBlank() && homeViewModelState.newPassword.isNotBlank() && homeViewModelState.confirmPassword.isNotBlank()
                        } else {
                            false
                        }
                    ) {
                        homeViewModel.changePassword()
                    }
                }
            }
        }
        
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            MARVIFloatingActionButton(
                icon = if (homeViewModelState.isEditEnabled || homeViewModelState.isChangePasswordEnabled) 
                    R.drawable.ic_x 
                else 
                    R.drawable.ic_pencil_square
            ) {
                homeViewModel.setEditModes(!(homeViewModelState.isEditEnabled || homeViewModelState.isChangePasswordEnabled))
            }
        }
    }
}