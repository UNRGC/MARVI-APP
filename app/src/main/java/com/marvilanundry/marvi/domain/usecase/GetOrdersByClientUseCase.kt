package com.marvilanundry.marvi.domain.usecase

import com.marvilanundry.marvi.domain.model.Orders
import com.marvilanundry.marvi.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrdersByClientUseCase @Inject constructor(
    val repository: OrderRepository
) {
    suspend operator fun invoke(clientId: Int, search: String? = null): List<Orders> {
        return repository.getOrdersByClient(clientId, search)
    }
}