package com.marvilanundry.marvi.data.remote

import com.marvilanundry.marvi.domain.model.Client
import kotlinx.serialization.Serializable

@Serializable
class ClientDto(
    val id_cliente: Int,
    val codigo: String,
    val nombre: String,
    val primer_apellido: String,
    val segundo_apellido: String?,
    val telefono: String?,
    val correo: String?,
    val contrasena: String?,
    val fecha_registro: String,
    val foto_src: String?,
    val activo: Boolean
)

fun ClientDto.toClient(): Client = Client(
    id_cliente = id_cliente,
    codigo = codigo,
    nombre = nombre,
    primer_apellido = primer_apellido,
    segundo_apellido = segundo_apellido,
    telefono = telefono,
    correo = correo,
    contrasena = contrasena,
    fecha_registro = fecha_registro,
    foto_src = foto_src,
    activo = activo
)