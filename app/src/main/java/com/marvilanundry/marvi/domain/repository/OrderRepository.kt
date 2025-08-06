package com.marvilanundry.marvi.domain.repository

import com.marvilanundry.marvi.domain.model.Order
import com.marvilanundry.marvi.domain.model.Orders

interface OrderRepository {
    suspend fun getOrderById(order: Int): Order
    suspend fun getOrdersByClient(clientId: Int, search: String? = null): List<Orders>
}