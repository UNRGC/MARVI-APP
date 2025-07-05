package com.marvilanundry.marvi.domain.repository

import com.marvilanundry.marvi.domain.model.Client

interface ClientRepository {
    suspend fun getClientByCode(code: String): Client
}