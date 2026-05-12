package io.github.visiongem.cryptopulse.feature.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.visiongem.cryptopulse.data.repository.MarketsStore
import io.github.visiongem.cryptopulse.data.repository.UserPreferencesRepository
import io.github.visiongem.cryptopulse.data.repository.WatchlistRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WatchlistViewModel(
    private val store: MarketsStore,
    private val watchlistRepository: WatchlistRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    val state: StateFlow<WatchlistUiState> = combine(
        store.state,
        watchlistRepository.symbolsFlow,
        userPreferencesRepository.rippleEnabledFlow,
    ) { storeState, watchlist, rippleEnabled ->
        val watchedCoins = storeState.coins
            .filter { it.symbol in watchlist }
            .map { it.copy(isFavorite = true) }
        WatchlistUiState(
            isLoading = storeState.isLoading && watchedCoins.isEmpty(),
            coins = watchedCoins,
            error = storeState.error.takeIf { watchedCoins.isEmpty() },
            rippleEnabled = rippleEnabled,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
        initialValue = WatchlistUiState(isLoading = true),
    )

    fun onAction(action: WatchlistAction) {
        when (action) {
            WatchlistAction.Retry -> store.refresh()
            is WatchlistAction.ToggleFavorite -> viewModelScope.launch {
                watchlistRepository.toggle(action.symbol)
            }
        }
    }

    companion object {
        private const val STOP_TIMEOUT_MS = 5_000L

        fun factory(
            store: MarketsStore,
            watchlistRepository: WatchlistRepository,
            userPreferencesRepository: UserPreferencesRepository,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                WatchlistViewModel(store, watchlistRepository, userPreferencesRepository)
            }
        }
    }
}
