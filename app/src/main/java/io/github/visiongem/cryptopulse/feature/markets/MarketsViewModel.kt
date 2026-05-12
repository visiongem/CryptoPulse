package io.github.visiongem.cryptopulse.feature.markets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.visiongem.cryptopulse.data.repository.MarketsRepository
import io.github.visiongem.cryptopulse.data.repository.TickerRepository
import io.github.visiongem.cryptopulse.domain.LoadResult
import io.github.visiongem.cryptopulse.util.BINANCE_USDT_SYMBOLS
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MarketsViewModel(
    private val repository: MarketsRepository,
    private val tickerRepository: TickerRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MarketsUiState())
    val state: StateFlow<MarketsUiState> = _state.asStateFlow()

    private var tickerJob: Job? = null

    init {
        load(isRefresh = false)
    }

    fun onAction(action: MarketsAction) {
        when (action) {
            MarketsAction.Refresh -> load(isRefresh = true)
            MarketsAction.Retry -> load(isRefresh = false)
        }
    }

    private fun load(isRefresh: Boolean) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = !isRefresh && it.coins.isEmpty(),
                    isRefreshing = isRefresh,
                    error = null,
                )
            }
            when (val result = repository.getTopMarkets()) {
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
        tickerJob = viewModelScope.launch {
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

    companion object {
        fun factory(
            repository: MarketsRepository,
            tickerRepository: TickerRepository,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer { MarketsViewModel(repository, tickerRepository) }
        }
    }
}
