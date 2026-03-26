package com.marvilanundry.marvi.domain.model

data class Services (
    val id: String? = null,
    val codigo: String,
    val nombre: String,
    val descripcion: String? = null,
    val precio: Double,
    val nombre_unidad: String,
    val estado: String? = null,
    val fechaRegistro: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val total: Int = 0
)