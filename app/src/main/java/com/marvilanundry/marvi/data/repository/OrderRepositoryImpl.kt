package com.marvilanundry.marvi.data.repository

import com.marvilanundry.marvi.data.dto.toOrder
import com.marvilanundry.marvi.data.remote.ApiService
import com.marvilanundry.marvi.domain.model.Order
import com.marvilanundry.marvi.domain.repository.OrderRepository
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val api: ApiService
) : OrderRepository {
    override suspend fun getOrderById(order: Int): Order {
        return api.getOrderById(order).toOrder()
    }
}