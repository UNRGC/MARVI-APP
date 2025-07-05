package com.marvilanundry.marvi.data.repository

import com.marvilanundry.marvi.data.remote.ApiService
import com.marvilanundry.marvi.data.remote.toClient
import com.marvilanundry.marvi.domain.model.Client
import com.marvilanundry.marvi.domain.repository.ClientRepository

class ClientRepositoryImpl(
    private val api : ApiService
): ClientRepository {
    override suspend fun getClientByCode(code: String): Client {
        return api.getClientByCode(code).toClient()
    }
}