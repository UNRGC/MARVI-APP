package com.marvilanundry.marvi.data.dto

import com.marvilanundry.marvi.domain.model.ChangePassword
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordDto(
    val contrasena_actual: String,
    val contrasena_nueva: String
)

fun ChangePassword.toChangePasswordDto(): ChangePasswordDto = ChangePasswordDto(
    contrasena_actual = contrasena_actual,
    contrasena_nueva = contrasena_nueva
)

