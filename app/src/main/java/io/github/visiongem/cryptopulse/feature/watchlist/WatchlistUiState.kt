package io.github.visiongem.cryptopulse.feature.watchlist

import io.github.visiongem.cryptopulse.data.model.Coin
import io.github.visiongem.cryptopulse.domain.AppError

data class WatchlistUiState(
    val isLoading: Boolean = false,
    val coins: List<Coin> = emptyList(),
    val error: AppError? = null,
    val rippleEnabled: Boolean = true,
)

sealed interface WatchlistAction {
    data object Retry : WatchlistAction
    data class ToggleFavorite(val symbol: String) : WatchlistAction
}
