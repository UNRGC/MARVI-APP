package com.marvilanundry.marvi.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun getClientByCode(code: String): ClientDto {
        val response = client.get("https://marvi-api.onrender.com/clients/$code")
        val bodyText = response.bodyAsText()
        val json = Json { ignoreUnknownKeys = true }

        return if ("message" in bodyText) {
            val error = json.decodeFromString<ErrorMessageDto>(bodyText)
            throw Exception(error.message)
        } else {
            json.decodeFromString<ClientDto>(bodyText)
        }
    }
}