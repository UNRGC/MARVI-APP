package com.marvilanundry.marvi.domain.usecase

import com.marvilanundry.marvi.domain.model.UpdateClient
import com.marvilanundry.marvi.domain.repository.ClientRepository
import javax.inject.Inject

class PutUpdateClientUseCase @Inject constructor(
    private val repository: ClientRepository
) {
    suspend operator fun invoke(updateClient: UpdateClient): String {
        return repository.putUpdateClient(updateClient)
    }
}