package com.marvilanundry.marvi.data.dto

import com.marvilanundry.marvi.domain.model.Services
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServicesResponseDto(
    val success: Boolean,
    val data: List<ServicesDto>,
    val message: String? = null,
    val pagination: ServicesPaginationDto? = null
)

@Serializable
data class ServicesPaginationDto(
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int
)

@Serializable
data class ServicesDto(
    @SerialName("_id") val id: String? = null,
    val codigo: String,
    val nombre: String,
    val descripcion: String? = null,
    val precio: Double,
    @SerialName("nombre_unidad") val nombreUnidad: String? = null,
    @SerialName("unidad_medida") val unidadMedida: String? = null,
    val estado: String? = null,
    @SerialName("fecha_registro") val fechaRegistro: String? = null,
    val imagen: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null,
    @SerialName("__v") val version: Int? = null,
    val total: Int = 0
)

fun ServicesDto.toServices(): Services = Services(
    id = id,
    codigo = codigo,
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    nombre_unidad = nombreUnidad ?: unidadMedida ?: "",
    estado = estado,
    fechaRegistro = fechaRegistro,
    createdAt = createdAt,
    updatedAt = updatedAt,
    total = total
)