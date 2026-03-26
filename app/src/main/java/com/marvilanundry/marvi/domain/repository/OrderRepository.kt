package com.marvilanundry.marvi.domain.repository

import com.marvilanundry.marvi.domain.model.Order
import com.marvilanundry.marvi.domain.model.Orders

interface OrderRepository {
    suspend fun getOrderById(orderCode: String, token: String): Order
    suspend fun getOrdersByClient(clientId: String, token: String, search: String? = null): List<Orders>
}