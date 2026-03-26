package com.marvilanundry.marvi.domain.usecase

import com.marvilanundry.marvi.domain.repository.ClientRepository
import javax.inject.Inject

class PostForgotPasswordUseCase @Inject constructor(
    private val repository: ClientRepository
) {
    suspend operator fun invoke(email: String): String {
        return repository.postForgotPassword(email)
    }
}