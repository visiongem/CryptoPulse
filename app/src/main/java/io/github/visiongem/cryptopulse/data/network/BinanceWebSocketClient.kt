package io.github.visiongem.cryptopulse.data.network

import io.github.visiongem.cryptopulse.data.model.TickerUpdate
import io.github.visiongem.cryptopulse.data.model.toDomain
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

/**
 * Binance public WebSocket client.
 *
 * The returned [Flow] owns the underlying WebSocket — cancelling collection
 * closes the socket. Server-side close / network failure terminates the Flow
 * with an exception so an outer `retryWhen` can re-establish.
 */
class BinanceWebSocketClient(
    private val client: OkHttpClient,
) {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    fun streamMiniTickers(symbols: List<String>): Flow<TickerUpdate> = callbackFlow {
        if (symbols.isEmpty()) {
            close()
            return@callbackFlow
        }

        val streams = symbols.joinToString(STREAM_SEPARATOR) {
            "${it.lowercase()}usdt@miniTicker"
        }
        val request = Request.Builder()
            .url("$BASE_URL?streams=$streams")
            .build()

        val listener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                runCatching {
                    json.decodeFromString<BinanceStreamEnvelope>(text).data.toDomain()
                }.getOrNull()?.let { trySend(it) }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                close(t)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                if (code != NORMAL_CLOSURE) {
                    close(IllegalStateException("WebSocket closed by server: $code $reason"))
                } else {
                    close()
                }
            }
        }

        val ws = client.newWebSocket(request, listener)

        awaitClose {
            ws.close(NORMAL_CLOSURE, "client cancelled")
        }
    }

    private companion object {
        const val BASE_URL = "wss://stream.binance.com:9443/stream"
        const val STREAM_SEPARATOR = "/"
        const val NORMAL_CLOSURE = 1000
    }
}
