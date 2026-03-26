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
    override suspend fun getOrderById(orderCode: String, token: String): Order {
        return api.getOrderById(orderCode, token).toOrder()
    }
    override suspend fun getOrdersByClient(clientId: String, token: String, search: String?): List<Orders> {
        return api.getOrdersByClient(clientId, token).map { it.toOrders() }
    }
}