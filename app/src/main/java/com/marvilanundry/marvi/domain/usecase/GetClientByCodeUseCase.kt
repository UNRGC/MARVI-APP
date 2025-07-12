package com.marvilanundry.marvi.domain.usecase

import com.marvilanundry.marvi.domain.model.Client
import com.marvilanundry.marvi.domain.repository.ClientRepository

class GetClientByCodeUseCase (
    private val repository: ClientRepository
) {
    suspend operator fun invoke(code: String): Client {
        return repository.getClientByCode(code)
    }
}