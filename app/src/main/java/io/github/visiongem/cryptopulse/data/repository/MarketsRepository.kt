package io.github.visiongem.cryptopulse.data.repository

import io.github.visiongem.cryptopulse.data.model.Coin
import io.github.visiongem.cryptopulse.data.model.toDomain
import io.github.visiongem.cryptopulse.data.network.CoinGeckoApi
import io.github.visiongem.cryptopulse.domain.AppError
import io.github.visiongem.cryptopulse.domain.LoadResult
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class MarketsRepository(
    private val api: CoinGeckoApi,
) {
    suspend fun getTopMarkets(perPage: Int = DEFAULT_PER_PAGE): LoadResult<List<Coin>> {
        return try {
            val coins = api.getMarkets(perPage = perPage).map { it.toDomain() }
            LoadResult.Success(coins)
        } catch (e: SocketTimeoutException) {
            LoadResult.Failure(AppError.Timeout)
        } catch (e: IOException) {
            LoadResult.Failure(AppError.NoNetwork)
        } catch (e: HttpException) {
            LoadResult.Failure(AppError.Api(e.code(), e.message()))
        } catch (e: Throwable) {
            LoadResult.Failure(AppError.Unknown(e))
        }
    }

    companion object {
        const val DEFAULT_PER_PAGE = 50
    }
}
