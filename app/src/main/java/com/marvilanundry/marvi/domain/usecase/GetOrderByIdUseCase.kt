package com.marvilanundry.marvi.domain.usecase

import com.marvilanundry.marvi.domain.model.Order
import com.marvilanundry.marvi.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrderByIdUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(order: Int): Order {
        return repository.getOrderById(order)
    }
}