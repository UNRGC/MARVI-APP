package com.marvilanundry.marvi.domain.usecase

import com.marvilanundry.marvi.domain.repository.ApiRepository
import javax.inject.Inject

class GetApiWakeUpUseCase @Inject constructor(
    private val repository: ApiRepository
){
    suspend operator fun invoke(): Boolean {
        return repository.getApiWakeUp()
    }
}