package com.marvilanundry.marvi.domain.usecase

import com.marvilanundry.marvi.domain.model.AuthResult
import com.marvilanundry.marvi.domain.repository.ClientRepository
import javax.inject.Inject

class PostLoginGoogleUseCase @Inject constructor(
	private val repository: ClientRepository
) {
	suspend operator fun invoke(idToken: String): AuthResult {
		return repository.postLoginGoogle(idToken)
	}
}