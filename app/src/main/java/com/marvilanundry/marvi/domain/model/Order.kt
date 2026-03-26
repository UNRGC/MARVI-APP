package com.marvilanundry.marvi.domain.model

data class OrderProduct(
    val producto_id: String? = null,
    val codigo: String? = null,
    val nombre: String? = null,
    val cantidad: Int? = null,
    val precio_unitario: Double? = null,
    val subtotal: Double? = null
)

data class OrderService(
    val servicio_id: String? = null,
    val codigo: String? = null,
    val nombre: String? = null,
    val cantidad: Int? = null,
    val precio_unitario: Double? = null,
    val subtotal: Double? = null
)

data class Order (
    val id_pedido: Int,
    val codigo_pedido: String? = null,
    val id_cliente: Int,
    val cliente: String,
    val fecha_pedido: String,
    val fecha_entrega: String,
    val estado: String,
    val observaciones: String?,
    val id_usuario: Int,
    val total: Double,
    val productos: List<OrderProduct>? = null,
    val servicios: List<OrderService>? = null,
    val activo: Boolean,
    val createdAt: String? = null,
    val updatedAt: String? = null
)