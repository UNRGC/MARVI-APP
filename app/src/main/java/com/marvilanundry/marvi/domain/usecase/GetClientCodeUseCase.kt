package com.marvilanundry.marvi.domain.usecase

import com.marvilanundry.marvi.domain.repository.ClientRepository
import javax.inject.Inject

class GetClientCodeUseCase @Inject constructor(
    private val repository: ClientRepository
) {
    suspend operator fun invoke(code: String): String {
        return repository.getClientCode(code)
    }
}