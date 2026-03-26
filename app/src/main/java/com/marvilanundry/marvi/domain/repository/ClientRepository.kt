package com.marvilanundry.marvi.domain.repository

import com.marvilanundry.marvi.domain.model.AuthResult
import com.marvilanundry.marvi.domain.model.ChangePassword
import com.marvilanundry.marvi.domain.model.Email
import com.marvilanundry.marvi.domain.model.Login
import com.marvilanundry.marvi.domain.model.NewClient
import com.marvilanundry.marvi.domain.model.ResetPassword
import com.marvilanundry.marvi.domain.model.UpdateClient

interface ClientRepository {
    suspend fun postClient(newClient: NewClient): String
    suspend fun postResetPasswordClient(email: Email): String
    suspend fun postForgotPassword(email: String): String
    suspend fun postResetPassword(email: String, resetPassword: ResetPassword): String
    suspend fun postLoginClient(login: Login): AuthResult
    suspend fun postLoginGoogle(idToken: String): AuthResult
    suspend fun putUpdateClient(clientId: String, updateClient: UpdateClient, token: String): String
    suspend fun patchChangePasswordClient(clientId: String, changePassword: ChangePassword, token: String): String
}