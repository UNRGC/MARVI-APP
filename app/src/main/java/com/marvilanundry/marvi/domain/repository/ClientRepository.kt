package com.marvilanundry.marvi.domain.repository

import com.marvilanundry.marvi.domain.model.Client
import com.marvilanundry.marvi.domain.model.Email
import com.marvilanundry.marvi.domain.model.Login
import com.marvilanundry.marvi.domain.model.NewClient
import com.marvilanundry.marvi.domain.model.UpdateClient

interface ClientRepository {
    suspend fun getClientCode(code: String): String
    suspend fun postClient(newClient: NewClient): String
    suspend fun postResetPasswordClient(email: Email): String
    suspend fun postLoginClient(login: Login): Client
    suspend fun putUpdateClient(updateClient: UpdateClient): String
}