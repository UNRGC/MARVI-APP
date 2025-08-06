package com.marvilanundry.marvi.data.dto

import com.marvilanundry.marvi.domain.model.Orders
import kotlinx.serialization.Serializable

@Serializable
data class OrdersDto(
    val id_pedido: Int,
    val id_cliente: Int,
    val fecha_pedido: String,
    val detalles: String,
    val total: Double
)

fun OrdersDto.toOrders(): Orders = Orders(
    id_pedido = id_pedido,
    id_cliente = id_cliente,
    fecha_pedido = fecha_pedido,
    detalles = detalles,
    total = total
)

fun Orders.toOrdersDto(): OrdersDto = OrdersDto(
    id_pedido = id_pedido,
    id_cliente = id_cliente,
    fecha_pedido = fecha_pedido,
    detalles = detalles,
    total = total
)