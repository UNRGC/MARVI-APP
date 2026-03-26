package com.marvilanundry.marvi.domain.usecase

import com.marvilanundry.marvi.domain.model.AuthResult
import com.marvilanundry.marvi.domain.model.Login
import com.marvilanundry.marvi.domain.repository.ClientRepository
import javax.inject.Inject

class PostLoginClientUseCase @Inject constructor(
    private val repository: ClientRepository
) {
    suspend operator fun invoke(login: Login): AuthResult {
        return repository.postLoginClient(login)
    }
}