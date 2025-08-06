package com.marvilanundry.marvi.data.dto

import com.marvilanundry.marvi.domain.model.Order
import kotlinx.serialization.Serializable

@Serializable
data class OrderDto (
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

fun OrderDto.toOrder(): Order = Order(
    id_pedido = id_pedido,
    id_cliente = id_cliente,
    cliente = cliente,
    fecha_pedido = fecha_pedido,
    fecha_entrega = fecha_entrega,
    estado = estado,
    observaciones = observaciones,
    id_usuario = id_usuario,
    total = total,
    activo = activo
)

fun Order.toOrderDto(): OrderDto = OrderDto(
    id_pedido = id_pedido,
    id_cliente = id_cliente,
    cliente = cliente,
    fecha_pedido = fecha_pedido,
    fecha_entrega = fecha_entrega,
    estado = estado,
    observaciones = observaciones,
    id_usuario = id_usuario,
    total = total,
    activo = activo
)