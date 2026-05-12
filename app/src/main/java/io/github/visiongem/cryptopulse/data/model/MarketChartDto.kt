package io.github.visiongem.cryptopulse.data.model

import kotlinx.serialization.Serializable

/**
 * CoinGecko `/coins/{id}/market_chart` response.
 *
 * Each entry in [prices] is a `[timestampMs, price]` tuple; market_caps and
 * total_volumes are not used here so we drop them via ignoreUnknownKeys.
 */
@Serializable
data class MarketChartDto(
    val prices: List<List<Double>> = emptyList(),
)
