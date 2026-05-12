package io.github.visiongem.cryptopulse.feature.widget

import kotlinx.serialization.Serializable

@Serializable
data class WidgetDataSnapshot(
    val timestamp: Long,
    val coins: List<WidgetCoinData>,
)

@Serializable
data class WidgetCoinData(
    val symbol: String,
    val name: String,
    val price: Double,
    val priceChangePercentage24h: Double,
)
