package com.marvilanundry.marvi.data.dto

import com.marvilanundry.marvi.domain.model.Orders
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrdersResponseDto(
    val success: Boolean,
    val data: List<OrdersDto>,
    val message: String? = null,
    val pagination: OrdersPaginationDto? = null
)

@Serializable
data class OrdersPaginationDto(
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int
)

@Serializable
data class OrderItemDto(
    val producto_id: String? = null,
    val servicio_id: String? = null,
    val codigo: String? = null,
    val nombre: String? = null,
    val cantidad: Int? = null,
    val precio_unitario: Double? = null,
    val subtotal: Double? = null
)

@Serializable
data class ClientSnapshotOrderDto(
    val codigo: String? = null,
    val nombre: String? = null
)

@Serializable
data class OrdersDto(
    val id_pedido: Int = 0,
    val id_cliente: Int = 0,
    @SerialName("_id") val mongoId: String? = null,
    val codigo: String? = null,
    @SerialName("cliente_id") val clienteIdMongo: String? = null,
    @SerialName("cliente_snapshot") val clienteSnapshot: ClientSnapshotOrderDto? = null,
    val fecha_pedido: String = "",
    @SerialName("fecha_registro") val fechaRegistro: String? = null,
    val detalles: String = "",
    val estado: String = "creado",
    val productos: List<OrderItemDto> = emptyList(),
    val servicios: List<OrderItemDto> = emptyList(),
    val total: Double = 0.0,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null,
    @SerialName("__v") val version: Int? = null
)

private fun formatOrderDetails(products: List<OrderItemDto>, services: List<OrderItemDto>): String {
    val items = (products + services).mapNotNull { item ->
        val name = item.nombre?.trim().orEmpty()
        if (name.isBlank()) return@mapNotNull null
        val quantity = item.cantidad ?: 0
        if (quantity > 0) "$name x$quantity" else name
    }
    return items.joinToString(separator = ", ")
}

fun OrdersDto.toOrders(): Orders = Orders(
    id_pedido = if (id_pedido > 0) id_pedido else codigo?.filter(Char::isDigit)?.takeLast(9)?.toIntOrNull() ?: 0,
    codigo_pedido = codigo,
    id_cliente = id_cliente,
    mongo_id = mongoId,
    fecha_pedido = fecha_pedido.ifBlank { fechaRegistro ?: "" },
    detalles = detalles.ifBlank { formatOrderDetails(productos, servicios) },
    estado = estado,
    total = total,
    createdAt = createdAt,
    updatedAt = updatedAt
)