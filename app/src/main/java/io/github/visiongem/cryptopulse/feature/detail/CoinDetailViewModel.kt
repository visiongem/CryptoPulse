package io.github.visiongem.cryptopulse.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.visiongem.cryptopulse.data.repository.ChartRepository
import io.github.visiongem.cryptopulse.data.repository.MarketsStore
import io.github.visiongem.cryptopulse.data.repository.WatchlistRepository
import io.github.visiongem.cryptopulse.domain.LoadResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CoinDetailViewModel(
    private val coinId: String,
    private val store: MarketsStore,
    private val chartRepository: ChartRepository,
    private val watchlistRepository: WatchlistRepository,
) : ViewModel() {

    private val period = MutableStateFlow(ChartPeriod.DAY)
    private val chart = MutableStateFlow<ChartUi>(ChartUi.Loading)

    val state: StateFlow<CoinDetailUiState> = combine(
        store.state,
        watchlistRepository.symbolsFlow,
        period,
        chart,
    ) { storeState, watchlist, currentPeriod, chartUi ->
        val baseCoin = storeState.coins.find { it.id == coinId }
        CoinDetailUiState(
            coin = baseCoin?.copy(isFavorite = baseCoin.symbol in watchlist),
            period = currentPeriod,
            chart = chartUi,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
        initialValue = CoinDetailUiState(),
    )

    init {
        loadChart(period.value)
    }

    fun onAction(action: CoinDetailAction) {
        when (action) {
            is CoinDetailAction.SelectPeriod -> {
                if (period.value != action.period) {
                    period.value = action.period
                    loadChart(action.period)
                }
            }
            CoinDetailAction.Retry -> loadChart(period.value)
            is CoinDetailAction.ToggleFavorite -> viewModelScope.launch {
                watchlistRepository.toggle(action.symbol)
            }
        }
    }

    private fun loadChart(target: ChartPeriod) {
        viewModelScope.launch {
            chart.value = ChartUi.Loading
            chart.value = when (val result = chartRepository.getChart(coinId, target.days)) {
                is LoadResult.Success -> ChartUi.Success(result.data)
                is LoadResult.Failure -> ChartUi.Failure(result.error)
            }
        }
    }

    companion object {
        private const val STOP_TIMEOUT_MS = 5_000L

        fun factory(
            coinId: String,
            store: MarketsStore,
            chartRepository: ChartRepository,
            watchlistRepository: WatchlistRepository,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                CoinDetailViewModel(coinId, store, chartRepository, watchlistRepository)
            }
        }
    }
}
