package com.example.benative.data

class KtorHttpException(
    val code: Int,
    rawBody: String
) : kotlinx.io.IOException("HTTP $code\n$rawBody")