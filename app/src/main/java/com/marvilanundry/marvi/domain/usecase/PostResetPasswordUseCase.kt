package com.marvilanundry.marvi.domain.usecase

import com.marvilanundry.marvi.domain.model.ResetPassword
import com.marvilanundry.marvi.domain.repository.ClientRepository
import javax.inject.Inject

class PostResetPasswordUseCase @Inject constructor(
    private val repository: ClientRepository
) {
    suspend operator fun invoke(email: String, resetPassword: ResetPassword): String {
        return repository.postResetPassword(email, resetPassword)
    }
}