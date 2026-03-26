package com.marvilanundry.marvi.data.dto

import com.marvilanundry.marvi.domain.model.ResetPassword
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordDto(
    val codigo: String,
    val contrasena: String
)

fun ResetPassword.toResetPasswordDto(): ResetPasswordDto = ResetPasswordDto(
    codigo = codigo,
    contrasena = contrasena
)