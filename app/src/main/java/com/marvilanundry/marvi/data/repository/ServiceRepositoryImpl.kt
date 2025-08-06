package com.marvilanundry.marvi.data.repository

import com.marvilanundry.marvi.data.dto.toServices
import com.marvilanundry.marvi.data.remote.ApiService
import com.marvilanundry.marvi.domain.model.Services
import com.marvilanundry.marvi.domain.repository.ServiceRepository
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ServiceRepository {
    override suspend fun getServices(): List<Services> {
        return api.getServices().map { it.toServices() }
    }
}