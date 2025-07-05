package com.marvilanundry.marvi.domain.usecase

import com.marvilanundry.marvi.domain.model.NewClient
import com.marvilanundry.marvi.domain.repository.ClientRepository
import javax.inject.Inject

class PostNewClientUseCase @Inject constructor(
    private val repository: ClientRepository
) {
    suspend operator fun invoke(newClient: NewClient): String {
        return repository.postClient(newClient)
    }
}