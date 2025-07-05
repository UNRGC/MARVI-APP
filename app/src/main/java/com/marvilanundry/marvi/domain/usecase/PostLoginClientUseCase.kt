package com.marvilanundry.marvi.domain.usecase

import com.marvilanundry.marvi.domain.model.Client
import com.marvilanundry.marvi.domain.model.Login
import com.marvilanundry.marvi.domain.repository.ClientRepository
import javax.inject.Inject

class PostLoginClientUseCase @Inject constructor(
    private val repository: ClientRepository
) {
    suspend operator fun invoke(login: Login): Client {
        return repository.postLoginClient(login)
    }
}