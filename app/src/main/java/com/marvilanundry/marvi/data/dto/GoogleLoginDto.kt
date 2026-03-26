package com.marvilanundry.marvi.data.dto

import com.marvilanundry.marvi.domain.model.AuthResult
import com.marvilanundry.marvi.domain.model.Client
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val success: Boolean,
    val message: String,
    val token: String,
    val data: AuthClientDto
)

@Serializable
data class AuthClientDto(
    @SerialName("_id") val mongoId: String? = null,
    @SerialName("id_cliente") val idCliente: Int? = null,
    val codigo: String,
    val nombre: String,
    val primer_apellido: String,
    val segundo_apellido: String? = null,
    val correo: String? = null,
    val telefono: String? = null,
    val contrasena: String? = null,
    val imagen_perfil: String? = null,
    val estado: String? = null,
    val fecha_registro: String
)

fun AuthResponseDto.toAuthResult(): AuthResult {
    val isActive = data.estado?.equals("activo", ignoreCase = true) ?: true
    val client = Client(
        id_cliente = data.idCliente ?: 0,
        mongo_id = data.mongoId,
        codigo = data.codigo,
        nombre = data.nombre,
        primer_apellido = data.primer_apellido,
        segundo_apellido = data.segundo_apellido,
        telefono = data.telefono,
        correo = data.correo,
        contrasena = data.contrasena,
        estado = data.estado,
        imagen_perfil = data.imagen_perfil,
        fecha_registro = data.fecha_registro,
        activo = isActive
    )

    return AuthResult(client = client, token = token)
}

