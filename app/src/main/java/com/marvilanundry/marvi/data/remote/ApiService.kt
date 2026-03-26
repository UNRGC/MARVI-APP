package com.marvilanundry.marvi.data.remote

import com.marvilanundry.marvi.data.dto.EmailDto
import com.marvilanundry.marvi.data.dto.AuthResponseDto
import com.marvilanundry.marvi.data.dto.ChangePasswordDto
import com.marvilanundry.marvi.data.dto.LoginDto
import com.marvilanundry.marvi.data.dto.MessageDto
import com.marvilanundry.marvi.data.dto.NewClientDto
import com.marvilanundry.marvi.data.dto.OrderDto
import com.marvilanundry.marvi.data.dto.OrderByCodeResponseDto
import com.marvilanundry.marvi.data.dto.OrdersDto
import com.marvilanundry.marvi.data.dto.OrdersResponseDto
import com.marvilanundry.marvi.data.dto.ResetPasswordDto
import com.marvilanundry.marvi.data.dto.ServicesDto
import com.marvilanundry.marvi.data.dto.ServicesResponseDto
import com.marvilanundry.marvi.data.dto.UpdateClientDto
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
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

    private fun buildBearerToken(token: String): String =
        if (token.startsWith("Bearer ", ignoreCase = true)) token else "Bearer $token"

    // Verificación de la API
    suspend fun getApiWakeUp(): Boolean {
        return try {
            val response = client.get("https://marvi-api.onrender.com/api/health")
            response.status.value in 200..299
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Login, registro y recuperación de contraseña
    suspend fun postNewClient(newClientDto: NewClientDto): String {
        val response = client.post("https://marvi-api.onrender.com/api/clients") {
            setBody(newClientDto)
            headers {
                append("Content-Type", "application/json")
            }
        }
        val messageDto = handleResponse<MessageDto>(response)
        return messageDto.message
    }

    suspend fun postForgotPassword(email: String): String {
        val response = client.post("https://marvi-api.onrender.com/api/clients/forgot-password/$email") {
            headers {
                append("Content-Type", "application/json")
            }
        }
        val messageDto = handleResponse<MessageDto>(response)
        return messageDto.message
    }

    suspend fun postResetPassword(email: String, resetPasswordDto: ResetPasswordDto): String {
        val response = client.post("https://marvi-api.onrender.com/api/clients/reset-password/$email") {
            setBody(resetPasswordDto)
            headers {
                append("Content-Type", "application/json")
            }
        }
        val messageDto = handleResponse<MessageDto>(response)
        return messageDto.message
    }

    suspend fun postLoginClient(loginDto: LoginDto): AuthResponseDto {
        val response = client.post("https://marvi-api.onrender.com/api/clients/login") {
            setBody(loginDto)
            headers {
                append("Content-Type", "application/json")
            }
        }
        val payload = handleResponse<AuthResponseDto>(response)
        if (!payload.success) throw Exception(payload.message)
        return payload
    }

    suspend fun postLoginGoogle(token: String): AuthResponseDto {
        val response = client.post("https://marvi-api.onrender.com/api/clients/login/google") {
            setBody(mapOf("id_token" to token))
            headers { append("Content-Type", "application/json") }
        }
        val payload = handleResponse<AuthResponseDto>(response)
        if (!payload.success) throw Exception(payload.message)
        return payload
    }


    // Seguimiento de pedidos
    suspend fun getOrderById(orderCode: String, token: String): OrderDto {
        val normalizedOrderCode = if (orderCode.startsWith("PED", ignoreCase = true)) {
            orderCode.uppercase()
        } else {
            "PED$orderCode"
        }
        val response = client.get("https://marvi-api.onrender.com/api/orders/code/$normalizedOrderCode") {
            headers {
                append(HttpHeaders.Authorization, buildBearerToken(token))
            }
        }
        val bodyText = response.bodyAsText()
        return if (response.status.value in 200..299) {
            try {
                val wrapped = json.decodeFromString<OrderByCodeResponseDto>(bodyText)
                wrapped.data
            } catch (_: Exception) {
                json.decodeFromString<OrderDto>(bodyText)
            }
        } else {
            val errorMessage = try {
                json.decodeFromString<MessageDto>(bodyText).message
            } catch (_: Exception) {
                "Error al consultar el pedido"
            }
            throw Exception(errorMessage)
        }
    }

    // Historial de pedidos
    suspend fun getOrdersByClient(clientId: String, token: String): List<OrdersDto> {
        val response = client.get("https://marvi-api.onrender.com/api/orders/client/$clientId?limit=9999") {
            headers {
                append(HttpHeaders.Authorization, buildBearerToken(token))
            }
        }
        val bodyText = response.bodyAsText()
        return if (response.status.value in 200..299) {
            val wrapped = runCatching {
                json.decodeFromString<OrdersResponseDto>(bodyText)
            }.getOrNull()

            if (wrapped != null) {
                if (!wrapped.success) throw Exception(wrapped.message ?: "Error al obtener historial de pedidos")
                wrapped.data
            } else {
                json.decodeFromString<List<OrdersDto>>(bodyText)
            }
        } else {
            val errorMessage = try {
                json.decodeFromString<MessageDto>(bodyText).message
            } catch (_: Exception) {
                "Error al obtener historial de pedidos"
            }
            throw Exception(errorMessage)
        }
    }

    // Cotización de servicios
    suspend fun getServices(token: String): List<ServicesDto> {
        val response = client.get("https://marvi-api.onrender.com/api/services?limit=9999") {
            headers {
                append(HttpHeaders.Authorization, buildBearerToken(token))
            }
        }
        val bodyText = response.bodyAsText()
        return if (response.status.value in 200..299) {
            val wrapped = try {
                json.decodeFromString<ServicesResponseDto>(bodyText)
            } catch (e: Exception) {
                null
            }

            if (wrapped != null) {
                if (!wrapped.success) throw Exception(wrapped.message ?: "Error al obtener servicios")
                wrapped.data
            } else {
                json.decodeFromString<List<ServicesDto>>(bodyText)
            }
        } else {
            val errorMessage = try {
                json.decodeFromString<MessageDto>(bodyText).message
            } catch (_: Exception) {
                "Error al obtener servicios"
            }
            throw Exception(errorMessage)
        }
    }

    // Actualización de cliente
    suspend fun putUpdateClient(clientId: String, updateClientDto: UpdateClientDto, token: String): String {
        val response = client.put("https://marvi-api.onrender.com/api/clients/$clientId") {
            setBody(updateClientDto)
            headers {
                append("Content-Type", "application/json")
                append(HttpHeaders.Authorization, buildBearerToken(token))
            }
        }
        val messageDto = handleResponse<MessageDto>(response)
        return messageDto.message
    }

    // Cambio de contraseña
    suspend fun patchChangePasswordClient(clientId: String, changePasswordDto: ChangePasswordDto, token: String): String {
        val response = client.patch("https://marvi-api.onrender.com/api/clients/$clientId/password") {
            setBody(changePasswordDto)
            headers {
                append("Content-Type", "application/json")
                append(HttpHeaders.Authorization, buildBearerToken(token))
            }
        }
        val messageDto = handleResponse<MessageDto>(response)
        return messageDto.message
    }
}