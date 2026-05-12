package io.github.visiongem.cryptopulse.data.repository

import io.github.visiongem.cryptopulse.data.model.TickerUpdate
import io.github.visiongem.cryptopulse.data.network.BinanceWebSocketClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen

/**
 * Wraps the raw WebSocket stream with exponential-backoff reconnect.
 *
 * Backoff schedule: 2s, 4s, 8s, 16s, 30s (capped). Retries forever — UI is
 * responsible for showing a "stale data" hint if appropriate.
 */
class TickerRepository(
    private val client: BinanceWebSocketClient,
) {
    fun tickerStream(symbols: List<String>): Flow<TickerUpdate> =
        client.streamMiniTickers(symbols)
            .retryWhen { _, attempt ->
                val seconds = (1L shl attempt.toInt().coerceAtMost(MAX_BACKOFF_POWER))
                    .coerceAtMost(MAX_BACKOFF_SECONDS)
                delay(seconds * 1000)
                true
            }

    private companion object {
        const val MAX_BACKOFF_POWER = 5
        const val MAX_BACKOFF_SECONDS = 30L
    }
}
