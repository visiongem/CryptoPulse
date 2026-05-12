package io.github.visiongem.cryptopulse.data.repository

import android.util.LruCache
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
    private val cache = LruCache<String, CacheEntry>(CACHE_MAX_ENTRIES)

    suspend fun getChart(coinId: String, days: Int): LoadResult<ChartData> {
        val key = "$coinId/$days"
        val now = System.currentTimeMillis()
        cache.get(key)?.takeIf { now - it.timestamp < TTL_MS }?.let { entry ->
            return LoadResult.Success(entry.data)
        }
        return try {
            val dto = api.getMarketChart(id = coinId, days = days)
            val data = dto.toDomain(coinId = coinId, days = days)
            cache.put(key, CacheEntry(data = data, timestamp = now))
            LoadResult.Success(data)
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

    private data class CacheEntry(val data: ChartData, val timestamp: Long)

    private companion object {
        const val CACHE_MAX_ENTRIES = 20
        const val TTL_MS = 5L * 60 * 1000
    }
}
