package com.example.benative.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiModule {
    private val client = HttpClient(CIO) {

        expectSuccess = false

        defaultRequest {
            url {
                protocol = URLProtocol.HTTP
                host = "31.207.75.8"
                port = 8080

            }
            contentType(ContentType.Application.Json)
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = false
                encodeDefaults = true
                explicitNulls = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 150000
            connectTimeoutMillis = 100000
            socketTimeoutMillis = 150000
        }

    }
    operator fun invoke(): HttpClient = client
}

