package com.marvilanundry.marvi.data.dto

import com.marvilanundry.marvi.domain.model.Services
import kotlinx.serialization.Serializable

@Serializable
data class ServicesDto(
    val total: Int,
    val codigo: String,
    val nombre: String,
    val precio: Double,
    val nombre_unidad: String
)

fun ServicesDto.toServices(): Services = Services(
    total = total,
    codigo = codigo,
    nombre = nombre,
    precio = precio,
    nombre_unidad = nombre_unidad
)

fun Services.toServicesDto(): ServicesDto = ServicesDto(
    total = total,
    codigo = codigo,
    nombre = nombre,
    precio = precio,
    nombre_unidad = nombre_unidad
)