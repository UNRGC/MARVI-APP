package com.marvilanundry.marvi.domain.model

data class Order (
    val id_pedido: Int,
    val id_cliente: Int,
    val cliente: String,
    val fecha_pedido: String,
    val fecha_entrega: String,
    val estado: String,
    val observaciones: String?,
    val id_usuario: Int,
    val total: Double,
    val activo: Boolean
)