package com.example.benative.domain

import com.example.benative.data.KtorHttpException

inline fun <T> Result<T>.onHttpError(action: (KtorHttpException) -> Unit): Result<T> {
    exceptionOrNull()?.let { if (it is KtorHttpException) action(it) }
    return this
}
