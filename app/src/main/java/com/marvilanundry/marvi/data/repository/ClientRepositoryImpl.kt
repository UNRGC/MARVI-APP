package com.marvilanundry.marvi.data.repository

import com.marvilanundry.marvi.data.dto.toAuthResult
import com.marvilanundry.marvi.data.remote.ApiService
import com.marvilanundry.marvi.data.dto.toChangePasswordDto
import com.marvilanundry.marvi.data.dto.toEmailDto
import com.marvilanundry.marvi.data.dto.toLoginDto
import com.marvilanundry.marvi.data.dto.toNewClientDto
import com.marvilanundry.marvi.data.dto.toResetPasswordDto
import com.marvilanundry.marvi.data.dto.toUpdateClientDto
import com.marvilanundry.marvi.domain.model.AuthResult
import com.marvilanundry.marvi.domain.model.ChangePassword
import com.marvilanundry.marvi.domain.model.Email
import com.marvilanundry.marvi.domain.model.Login
import com.marvilanundry.marvi.domain.model.NewClient
import com.marvilanundry.marvi.domain.model.ResetPassword
import com.marvilanundry.marvi.domain.model.UpdateClient
import com.marvilanundry.marvi.domain.repository.ClientRepository
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ClientRepository {
    override suspend fun postResetPasswordClient(email: Email): String {
        // Keep for compatibility if needed, but the new methods are preferred
        return api.postForgotPassword(email.correo)
    }

    override suspend fun postForgotPassword(email: String): String {
        return api.postForgotPassword(email)
    }

    override suspend fun postResetPassword(email: String, resetPassword: ResetPassword): String {
        return api.postResetPassword(email, resetPassword.toResetPasswordDto())
    }

    override suspend fun postClient(newClient: NewClient): String {
        return api.postNewClient(newClient.toNewClientDto())
    }
    override suspend fun postLoginClient(login: Login): AuthResult {
        return api.postLoginClient(login.toLoginDto()).toAuthResult()
    }
    override suspend fun postLoginGoogle(idToken: String): AuthResult {
        return api.postLoginGoogle(idToken).toAuthResult()
    }
    override suspend fun putUpdateClient(clientId: String, updateClient: UpdateClient, token: String): String {
        return api.putUpdateClient(clientId, updateClient.toUpdateClientDto(), token)
    }
    override suspend fun patchChangePasswordClient(clientId: String, changePassword: ChangePassword, token: String): String {
        return api.patchChangePasswordClient(clientId, changePassword.toChangePasswordDto(), token)
    }
}