package com.example.benative.data

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

suspend fun HttpResponse.stringOrException(): String {
    if (!this.status.isSuccess()) {
        throw KtorHttpException(this.status.value, this.bodyAsText())
    }
    return this.bodyAsText()
}