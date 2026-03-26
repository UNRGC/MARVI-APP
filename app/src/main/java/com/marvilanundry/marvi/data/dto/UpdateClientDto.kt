package com.marvilanundry.marvi.data.dto

import com.marvilanundry.marvi.domain.model.UpdateClient
import kotlinx.serialization.Serializable

@Serializable
data class UpdateClientDto(
    val nombre: String,
    val primer_apellido: String,
    val segundo_apellido: String? = null,
    val telefono: String? = null,
    val correo: String,
    val estado: String,
    val imagen_perfil: String? = null
)

fun UpdateClient.toUpdateClientDto(): UpdateClientDto = UpdateClientDto(
    nombre = nombre,
    primer_apellido = primer_apellido,
    segundo_apellido = segundo_apellido,
    telefono = telefono,
    correo = correo,
    estado = estado,
    imagen_perfil = imagen_perfil
)