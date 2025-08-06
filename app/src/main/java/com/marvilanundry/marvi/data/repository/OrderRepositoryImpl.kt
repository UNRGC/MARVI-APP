package com.marvilanundry.marvi.data.repository

import com.marvilanundry.marvi.data.dto.toOrder
import com.marvilanundry.marvi.data.dto.toOrders
import com.marvilanundry.marvi.data.remote.ApiService
import com.marvilanundry.marvi.domain.model.Order
import com.marvilanundry.marvi.domain.model.Orders
import com.marvilanundry.marvi.domain.repository.OrderRepository
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val api: ApiService
) : OrderRepository {
    override suspend fun getOrderById(order: Int): Order {
        return api.getOrderById(order).toOrder()
    }
    override suspend fun getOrdersByClient(clientId: Int, search: String?): List<Orders> {
        return api.getOrdersByClient(clientId, search).map { it.toOrders() }
    }
}