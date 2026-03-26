package com.marvilanundry.marvi.domain.model

data class Orders(
    val id_pedido: Int,
    val codigo_pedido: String? = null,
    val id_cliente: Int,
    val mongo_id: String? = null,
    val fecha_pedido: String,
    val detalles: String,
    val estado: String = "creado",
    val total: Double,
    val createdAt: String? = null,
    val updatedAt: String? = null
)