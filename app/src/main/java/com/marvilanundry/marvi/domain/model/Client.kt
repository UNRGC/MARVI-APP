package com.marvilanundry.marvi.domain.model

data class Client(
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