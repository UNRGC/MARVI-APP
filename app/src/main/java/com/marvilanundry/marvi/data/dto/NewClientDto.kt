package com.marvilanundry.marvi.data.dto

import com.marvilanundry.marvi.domain.model.NewClient
import kotlinx.serialization.Serializable

@Serializable
data class NewClientDto(
    val codigo: String,
    val nombre: String,
    val primer_apellido: String,
    val segundo_apellido: String?,
    val telefono: String,
    val correo: String,
    val contrasena: String
)

fun NewClient.toNewClientDto(): NewClientDto = NewClientDto(
    codigo = codigo,
    nombre = nombre,
    primer_apellido = primer_apellido,
    segundo_apellido = segundo_apellido,
    telefono = telefono,
    correo = correo,
    contrasena = contrasena
)