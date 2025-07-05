package com.marvilanundry.marvi.data.repository

import com.marvilanundry.marvi.data.remote.ApiService
import com.marvilanundry.marvi.domain.repository.ApiRepository
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ApiRepository {
    override suspend fun getApiWakeUp(): Boolean {
        return api.getApiWakeUp()
    }
}