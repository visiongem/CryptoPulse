package io.github.visiongem.cryptopulse.data.repository

import io.github.visiongem.cryptopulse.data.model.ChartData
import io.github.visiongem.cryptopulse.data.model.toDomain
import io.github.visiongem.cryptopulse.data.network.CoinGeckoApi
import io.github.visiongem.cryptopulse.domain.AppError
import io.github.visiongem.cryptopulse.domain.LoadResult
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class ChartRepository(
    private val api: CoinGeckoApi,
) {
    suspend fun getChart(coinId: String, days: Int): LoadResult<ChartData> {
        return try {
            val dto = api.getMarketChart(id = coinId, days = days)
            LoadResult.Success(dto.toDomain(coinId = coinId, days = days))
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
}
