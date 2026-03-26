package com.marvilanundry.marvi.data.dto

import com.marvilanundry.marvi.domain.model.Order
import com.marvilanundry.marvi.domain.model.OrderProduct
import com.marvilanundry.marvi.domain.model.OrderService
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderByCodeResponseDto(
    val success: Boolean,
    val data: OrderDto,
    val message: String? = null
)

@Serializable
data class ClientSnapshotDto(
    val codigo: String? = null,
    val nombre: String? = null
)

@Serializable
data class OrderProductDto(
    val producto_id: String? = null,
    val codigo: String? = null,
    val nombre: String? = null,
    val cantidad: Int? = null,
    val precio_unitario: Double? = null,
    val subtotal: Double? = null
)

@Serializable
data class OrderServiceDto(
    val servicio_id: String? = null,
    val codigo: String? = null,
    val nombre: String? = null,
    val cantidad: Int? = null,
    val precio_unitario: Double? = null,
    val subtotal: Double? = null
)

@Serializable
data class OrderDto(
    val id_pedido: Int = 0,
    val codigo: String? = null,
    val id_cliente: Int = 0,
    @SerialName("_id") val mongoId: String? = null,
    @SerialName("cliente_id") val clienteIdMongo: String? = null,
    val cliente: String = "",
    @SerialName("cliente_snapshot") val clienteSnapshot: ClientSnapshotDto? = null,
    val fecha_pedido: String = "",
    @SerialName("fecha_registro") val fechaRegistro: String? = null,
    val fecha_entrega: String = "",
    val estado: String = "",
    val observaciones: String? = null,
    val id_usuario: Int = 0,
    val total: Double = 0.0,
    val productos: List<OrderProductDto>? = emptyList(),
    val servicios: List<OrderServiceDto>? = emptyList(),
    val activo: Boolean = true,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null,
    @SerialName("__v") val version: Int? = null
)

private fun normalizeOrderStatus(status: String): String {
    return when (status.lowercase()) {
        "creado", "pendiente", "recibido" -> "Pendiente"
        "listo", "completado", "terminado" -> "Listo"
        "entregado", "finalizado" -> "Entregado"
        "cancelado", "cancelled" -> "Cancelado"
        else -> status.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

fun OrderDto.toOrder(): Order = Order(
    id_pedido = if (id_pedido > 0) id_pedido else codigo?.filter(Char::isDigit)?.takeLast(9)?.toIntOrNull() ?: 0,
    codigo_pedido = codigo,
    id_cliente = id_cliente,
    cliente = cliente.ifBlank { clienteSnapshot?.nombre ?: "" },
    fecha_pedido = fecha_pedido.ifBlank { fechaRegistro ?: "" },
    fecha_entrega = fecha_entrega,
    estado = normalizeOrderStatus(estado),
    observaciones = observaciones,
    id_usuario = id_usuario,
    total = total,
    productos = productos?.map {
        OrderProduct(
            producto_id = it.producto_id,
            codigo = it.codigo,
            nombre = it.nombre,
            cantidad = it.cantidad,
            precio_unitario = it.precio_unitario,
            subtotal = it.subtotal
        )
    },
    servicios = servicios?.map {
        OrderService(
            servicio_id = it.servicio_id,
            codigo = it.codigo,
            nombre = it.nombre,
            cantidad = it.cantidad,
            precio_unitario = it.precio_unitario,
            subtotal = it.subtotal
        )
    },
    activo = activo,
    createdAt = createdAt,
    updatedAt = updatedAt
)