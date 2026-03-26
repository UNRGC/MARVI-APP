package com.marvilanundry.marvi.domain.model

data class UpdateClient (
    val nombre: String,
    val primer_apellido: String,
    val segundo_apellido: String? = null,
    val telefono: String? = null,
    val correo: String,
    val estado: String,
    val imagen_perfil: String? = null
)