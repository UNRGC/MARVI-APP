package com.marvilanundry.marvi.domain.model

data class Orders(
    val id_pedido: Int,
    val id_cliente: Int,
    val fecha_pedido: String,
    val detalles: String,
    val total: Double
)