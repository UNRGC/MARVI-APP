package com.marvilanundry.marvi.domain.usecase

import com.marvilanundry.marvi.domain.model.ChangePassword
import com.marvilanundry.marvi.domain.repository.ClientRepository
import javax.inject.Inject

class PatchChangePasswordClientUseCase @Inject constructor(
    private val repository: ClientRepository
) {
    suspend operator fun invoke(clientId: String, changePassword: ChangePassword, token: String): String {
        return repository.patchChangePasswordClient(clientId, changePassword, token)
    }
}

