package com.marvilanundry.marvi.domain.usecase

import com.marvilanundry.marvi.domain.model.Services
import com.marvilanundry.marvi.domain.repository.ServiceRepository
import javax.inject.Inject

class GetServicesUseCase @Inject constructor(
    val repository: ServiceRepository
) {
    suspend operator fun invoke(): List<Services> {
        return repository.getServices()
    }
}