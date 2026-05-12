package io.github.visiongem.cryptopulse.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Wrapper for Binance combined-stream messages.
 *
 * Format: { "stream": "btcusdt@miniTicker", "data": { ... } }
 */
@Serializable
data class BinanceStreamEnvelope(
    val stream: String,
    val data: BinanceTickerDto,
)

/**
 * Binance @miniTicker payload (24h rolling, ~1 msg/sec per stream).
 *
 * See: https://binance-docs.github.io/apidocs/spot/en/#individual-symbol-mini-ticker-stream
 */
@Serializable
data class BinanceTickerDto(
    @SerialName("e") val eventType: String,
    @SerialName("E") val eventTime: Long,
    @SerialName("s") val symbol: String,
    @SerialName("c") val closePrice: String,
    @SerialName("o") val openPrice: String,
    @SerialName("h") val highPrice: String,
    @SerialName("l") val lowPrice: String,
    @SerialName("v") val baseVolume: String,
    @SerialName("q") val quoteVolume: String,
)
