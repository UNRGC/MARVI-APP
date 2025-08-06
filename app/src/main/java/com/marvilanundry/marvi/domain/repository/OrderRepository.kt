package com.marvilanundry.marvi.domain.repository

import com.marvilanundry.marvi.domain.model.Order

interface OrderRepository {
    suspend fun getOrderById(order: Int): Order
}