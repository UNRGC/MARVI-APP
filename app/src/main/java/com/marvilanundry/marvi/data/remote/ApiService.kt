package com.marvilanundry.marvi.data.remote

import com.marvilanundry.marvi.data.dto.ClientDto
import com.marvilanundry.marvi.data.dto.CodeDto
import com.marvilanundry.marvi.data.dto.EmailDto
import com.marvilanundry.marvi.data.dto.LoginDto
import com.marvilanundry.marvi.data.dto.MessageDto
import com.marvilanundry.marvi.data.dto.NewClientDto
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ApiService @Inject constructor() {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json { ignoreUnknownKeys = true })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 60000
            connectTimeoutMillis = 60000
            socketTimeoutMillis = 60000
        }
    }

    private val json = Json { ignoreUnknownKeys = true }

    private suspend inline fun <reified T> handleResponse(response: HttpResponse): T {
        val bodyText = response.bodyAsText()
        return if (response.status.value in 200..299) {
            json.decodeFromString(bodyText)
        } else {
            val error = json.decodeFromString<MessageDto>(bodyText)
            throw Exception(error.message)
        }
    }

    // Verificación de la API
    suspend fun getApiWakeUp(): Boolean {
        return try {
            val response = client.get("https://marvi-api.onrender.com")
            response.status.value in 200..299
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Login, registro y recuperación de contraseña
    suspend fun getClientCode(code: String): String {
        val response = client.get("https://marvi-api.onrender.com/login/clients/$code")
        val codeDto = handleResponse<CodeDto>(response)
        return codeDto.codigo
    }

    suspend fun postNewClient(newClientDto: NewClientDto): String {
        val response = client.post("https://marvi-api.onrender.com/login/clients/register") {
            setBody(newClientDto)
            headers {
                append("Content-Type", "application/json")
            }
        }
        val messageDto = handleResponse<MessageDto>(response)
        return messageDto.message
    }

    suspend fun postResetPasswordClient(emailDto: EmailDto): String {
        val response = client.post("https://marvi-api.onrender.com/login/clients/reset/password") {
            setBody(emailDto)
            headers {
                append("Content-Type", "application/json")
            }
        }
        val messageDto = handleResponse<MessageDto>(response)
        return messageDto.message
    }

    suspend fun postLoginClient(loginDto: LoginDto): ClientDto {
        val response = client.post("https://marvi-api.onrender.com/login/clients") {
            setBody(loginDto)
            headers {
                append("Content-Type", "application/json")
            }
        }
        return handleResponse(response)
    }
}