package io.github.visiongem.cryptopulse.domain

sealed class AppError {
    data object NoNetwork : AppError()
    data object Timeout : AppError()
    data class Api(val code: Int, val message: String) : AppError()
    data class Unknown(val cause: Throwable) : AppError()
}
