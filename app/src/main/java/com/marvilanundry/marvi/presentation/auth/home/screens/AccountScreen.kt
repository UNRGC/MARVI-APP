package com.marvilanundry.marvi.presentation.auth.home.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
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

    ColumnScrollable(
        paddingValues = PaddingValues(24.dp, 24.dp, 24.dp, 16.dp)
    ) {
        Container {
            SectionHeader(
                iconRes = R.drawable.ic_person_vcard, title = "Información de la cuenta"
            )
            MARVIButton(
                text = "Editar información",
                modifier = Modifier.fillMaxWidth(),
                type = MARVIButtonType.SECONDARY,
                enabled = true,
                message = "Ya hay una edición en curso"
            ) { }
        }
        Container {
            MARVITextField(
                label = stringResource(id = R.string.marvi_register_code),
                value = homeViewModelState.client?.codigo ?: "",
                readOnly = true,
                placeholder = stringResource(id = R.string.marvi_register_code_placeholder),
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.None,
                singleLine = true,
                onValueChange = {})
            MARVITextField(
                label = stringResource(id = R.string.marvi_register_name),
                value = homeViewModelState.client?.nombre ?: "",
                readOnly = true,
                placeholder = stringResource(id = R.string.marvi_register_name_placeholder),
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                singleLine = true,
                onValueChange = { })
            MARVITextField(
                label = stringResource(id = R.string.marvi_register_first_surname),
                value = homeViewModelState.client?.primer_apellido ?: "",
                readOnly = true,
                placeholder = stringResource(id = R.string.marvi_register_first_surname_placeholder),
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                singleLine = true,
                onValueChange = {})
            MARVITextField(
                label = stringResource(id = R.string.marvi_register_second_surname),
                value = homeViewModelState.client?.segundo_apellido ?: "",
                readOnly = true,
                placeholder = stringResource(id = R.string.marvi_register_second_surname_placeholder),
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                singleLine = true,
                onValueChange = {})
            MARVITextField(
                label = stringResource(id = R.string.marvi_register_phone),
                value = homeViewModelState.client?.telefono ?: "",
                readOnly = true,
                placeholder = stringResource(id = R.string.marvi_register_phone_placeholder),
                keyboardType = KeyboardType.Phone,
                capitalization = KeyboardCapitalization.None,
                singleLine = true,
                maxLength = 10,
                onValueChange = {})
            MARVITextField(
                label = stringResource(id = R.string.marvi_register_email),
                value = homeViewModelState.client?.correo ?: "",
                readOnly = true,
                placeholder = stringResource(id = R.string.marvi_register_email_placeholder),
                keyboardType = KeyboardType.Email,
                capitalization = KeyboardCapitalization.None,
                singleLine = true,
                onValueChange = {})
            MARVITextField(
                label = stringResource(id = R.string.marvi_register_password),
                value = homeViewModelState.client?.contrasena ?: "",
                readOnly = true,
                placeholder = stringResource(id = R.string.marvi_register_password_placeholder),
                trailingIcon = {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_eye
                        ),
                        contentDescription = stringResource(id = R.string.marvi_register_password),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {},
                        tint = MaterialTheme.colorScheme.outlineVariant
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardType = KeyboardType.Password,
                capitalization = KeyboardCapitalization.None,
                singleLine = true,
                maxLength = 20,
                onValueChange = {})
            MARVIButton(
                text = "Guardar cambios",
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                message = "No se han realizado cambios"
            ) { }
        }
    }
}