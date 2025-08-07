package com.marvilanundry.marvi.data.dto

import com.marvilanundry.marvi.domain.model.UpdateClient
import kotlinx.serialization.Serializable

@Serializable
data class UpdateClientDto(
    val id_cliente: Int,
    val codigo: String,
    val nombre: String,
    val primer_apellido: String,
    val segundo_apellido: String?,
    val telefono: String,
    val correo: String,
    val contrasena: String
)

fun UpdateClient.toUpdateClientDto(): UpdateClientDto = UpdateClientDto(
    id_cliente = id_cliente,
    codigo = codigo,
    nombre = nombre,
    primer_apellido = primer_apellido,
    segundo_apellido = segundo_apellido,
    telefono = telefono,
    correo = correo,
    contrasena = contrasena
)