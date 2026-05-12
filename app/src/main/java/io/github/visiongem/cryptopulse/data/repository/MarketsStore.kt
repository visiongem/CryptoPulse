package io.github.visiongem.cryptopulse.data.repository

import io.github.visiongem.cryptopulse.data.model.Coin
import io.github.visiongem.cryptopulse.domain.AppError
import io.github.visiongem.cryptopulse.domain.LoadResult
import io.github.visiongem.cryptopulse.util.BINANCE_USDT_SYMBOLS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Application-scoped singleton that owns the live markets dataset.
 *
 * Both Markets and Watchlist screens consume the same [state] — only one REST
 * call and one WebSocket connection regardless of which screen is foreground.
 */
class MarketsStore(
    private val marketsRepository: MarketsRepository,
    private val tickerRepository: TickerRepository,
    private val scope: CoroutineScope,
) {
    private val _state = MutableStateFlow(MarketsStoreState(isLoading = true))
    val state: StateFlow<MarketsStoreState> = _state.asStateFlow()

    private var tickerJob: Job? = null

    init {
        refresh()
    }

    fun refresh() {
        scope.launch {
            _state.update { it.copy(isLoading = it.coins.isEmpty(), isRefreshing = true) }
            when (val result = marketsRepository.getTopMarkets()) {
                is LoadResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            coins = result.data,
                            error = null,
                        )
                    }
                    startTickerStream(result.data.map { coin -> coin.symbol })
                }
                is LoadResult.Failure -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.error,
                        )
                    }
                }
            }
        }
    }

    private fun startTickerStream(symbols: List<String>) {
        tickerJob?.cancel()
        val streamable = symbols.filter { it in BINANCE_USDT_SYMBOLS }
        if (streamable.isEmpty()) return
        tickerJob = scope.launch {
            tickerRepository.tickerStream(streamable).collect { update ->
                _state.update { current ->
                    current.copy(
                        coins = current.coins.map { coin ->
                            if (coin.symbol == update.symbol) {
                                coin.copy(price = update.price)
                            } else {
                                coin
                            }
                        },
                    )
                }
            }
        }
    }
}

data class MarketsStoreState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val coins: List<Coin> = emptyList(),
    val error: AppError? = null,
)
