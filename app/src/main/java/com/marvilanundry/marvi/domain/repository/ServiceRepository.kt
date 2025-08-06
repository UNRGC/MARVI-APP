package com.marvilanundry.marvi.domain.repository

import com.marvilanundry.marvi.domain.model.Services

interface ServiceRepository {
    suspend fun getServices(): List<Services>
}