package com.marvilanundry.marvi.data.remote

import com.marvilanundry.marvi.domain.model.Login
import kotlinx.serialization.Serializable

@Serializable
data class LoginDto (
    val correo: String,
    val contrasena: String
)

fun Login.toLoginDto() = LoginDto(
    correo = correo,
    contrasena = contrasena
)