package com.marvilanundry.marvi.data.repository

import com.marvilanundry.marvi.data.remote.ApiService
import com.marvilanundry.marvi.data.dto.toClient
import com.marvilanundry.marvi.data.dto.toEmailDto
import com.marvilanundry.marvi.data.dto.toLoginDto
import com.marvilanundry.marvi.data.dto.toNewClientDto
import com.marvilanundry.marvi.domain.model.Client
import com.marvilanundry.marvi.domain.model.Email
import com.marvilanundry.marvi.domain.model.Login
import com.marvilanundry.marvi.domain.model.NewClient
import com.marvilanundry.marvi.domain.repository.ClientRepository
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ClientRepository {
    override suspend fun getClientCode(code: String): String {
        return api.getClientCode(code)
    }
    override suspend fun postResetPasswordClient(email: Email): String {
        return api.postResetPasswordClient(email.toEmailDto())
    }
    override suspend fun postClient(newClient: NewClient): String {
        return api.postNewClient(newClient.toNewClientDto())
    }
    override suspend fun postLoginClient(login: Login): Client {
        return api.postLoginClient(login.toLoginDto()).toClient()
    }
}