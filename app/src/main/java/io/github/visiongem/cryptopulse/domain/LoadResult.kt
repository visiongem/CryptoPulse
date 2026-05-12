package io.github.visiongem.cryptopulse.domain

sealed class LoadResult<out T> {
    data class Success<T>(val data: T) : LoadResult<T>()
    data class Failure(val error: AppError) : LoadResult<Nothing>()
}
