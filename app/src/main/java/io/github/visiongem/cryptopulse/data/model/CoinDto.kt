package io.github.visiongem.cryptopulse.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinDto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    @SerialName("current_price")
    val currentPrice: Double? = null,
    @SerialName("price_change_percentage_24h")
    val priceChangePercentage24h: Double? = null,
    @SerialName("market_cap")
    val marketCap: Long? = null,
    @SerialName("market_cap_rank")
    val marketCapRank: Int? = null,
    @SerialName("total_volume")
    val totalVolume: Double? = null,
    @SerialName("sparkline_in_7d")
    val sparklineIn7d: SparklineDto? = null,
)

@Serializable
data class SparklineDto(
    val price: List<Double> = emptyList(),
)
