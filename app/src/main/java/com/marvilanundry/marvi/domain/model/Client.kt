package com.marvilanundry.marvi.domain.model

data class Client(
    val id_cliente: Int,
    val mongo_id: String? = null,
    val codigo: String,
    val nombre: String,
    val primer_apellido: String,
    val segundo_apellido: String?,
    val telefono: String?,
    val correo: String?,
    val contrasena: String?,
    val estado: String? = null,
    val imagen_perfil: String? = null,
    val fecha_registro: String,
    val activo: Boolean
)