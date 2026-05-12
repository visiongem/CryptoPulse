package io.github.visiongem.cryptopulse.data.network

import io.github.visiongem.cryptopulse.data.model.CoinDto
import io.github.visiongem.cryptopulse.data.model.MarketChartDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoApi {

    @GET("coins/markets")
    suspend fun getMarkets(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 50,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = true,
        @Query("price_change_percentage") priceChangePercentage: String = "24h",
    ): List<CoinDto>

    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") id: String,
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("days") days: Int,
    ): MarketChartDto
}
