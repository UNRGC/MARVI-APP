package com.marvilanundry.marvi.domain.repository

interface ApiRepository {
    suspend fun getApiWakeUp(): Boolean
}