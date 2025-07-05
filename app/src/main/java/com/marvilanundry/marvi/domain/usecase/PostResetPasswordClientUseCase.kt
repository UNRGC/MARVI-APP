package com.marvilanundry.marvi.domain.usecase

import com.marvilanundry.marvi.domain.model.Email
import com.marvilanundry.marvi.domain.repository.ClientRepository
import javax.inject.Inject

class PostResetPasswordClientUseCase @Inject constructor(
    private val repository: ClientRepository
){
    suspend operator fun invoke(email: Email): String {
        return repository.postResetPasswordClient(email)
    }
}