package com.marvilanundry.marvi.presentation.auth.home.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.presentation.auth.home.HomeUiState
import com.marvilanundry.marvi.presentation.auth.home.HomeViewModel
import com.marvilanundry.marvi.presentation.auth.home.components.ColumnScrollable
import com.marvilanundry.marvi.presentation.auth.home.components.Container
import com.marvilanundry.marvi.presentation.auth.home.components.SectionHeader
import com.marvilanundry.marvi.presentation.core.components.MARVIButton
import com.marvilanundry.marvi.presentation.core.components.MARVIButtonType
import com.marvilanundry.marvi.presentation.core.components.MARVITextField

@Composable
fun AccountScreen(homeViewModel: HomeViewModel, homeViewModelState: HomeUiState) {
    var viewPassword by rememberSaveable { mutableStateOf(false) }

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
                label = stringResource(id = R.string.marvi_register_code),
                value = homeViewModelState.clientCode,
                enabled = homeViewModelState.isEditEnabled,
                placeholder = stringResource(id = R.string.marvi_register_code_placeholder),
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.None,
                singleLine = true,
                maxLength = 30,
                onValueChange = { homeViewModel.onClientCodeChange(it) })
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
            MARVITextField(
                label = stringResource(id = R.string.marvi_register_password),
                value = homeViewModelState.clientPassword,
                enabled = homeViewModelState.isEditEnabled,
                placeholder = stringResource(id = R.string.marvi_register_password_placeholder),
                trailingIcon = {
                    if (homeViewModelState.isEditEnabled) {
                        Icon(
                            painter = if (viewPassword) painterResource(id = R.drawable.ic_eye_slash) else painterResource(
                                id = R.drawable.ic_eye
                            ),
                            contentDescription = stringResource(id = R.string.marvi_register_password),
                            modifier = Modifier
                                .size(20.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    viewPassword = !viewPassword
                                },
                            tint = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                },
                visualTransformation = if (!viewPassword) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardType = KeyboardType.Password,
                capitalization = KeyboardCapitalization.None,
                singleLine = true,
                onValueChange = { homeViewModel.onClientPasswordChange(it) })
            Row {
                MARVIButton(
                    text = "Descartar",
                    type = MARVIButtonType.SECONDARY,
                    modifier = Modifier.weight(1f),
                    enabled = homeViewModelState.isEditEnabled,
                    message = "No se han realizado cambios"
                ) {
                    homeViewModel.editEnabled(false)
                    homeViewModel.resetClient()
                }
                Spacer(modifier = Modifier.width(16.dp))
                MARVIButton(
                    text = "Guardar",
                    modifier = Modifier.weight(1f),
                    enabled = homeViewModelState.isEditEnabled && homeViewModelState.isSaveEnabled,
                    message = if (homeViewModelState.isEditEnabled) {
                        "Completa todos los campos* antes de guardar"
                    } else {
                        "No se han realizado cambios"
                    }
                ) {
                    homeViewModel.updateClient()
                }
            }
        }
    }
}