package io.github.visiongem.cryptopulse.feature.markets

import io.github.visiongem.cryptopulse.data.model.Coin
import io.github.visiongem.cryptopulse.domain.AppError

data class MarketsUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val coins: List<Coin> = emptyList(),
    val error: AppError? = null,
)

sealed interface MarketsAction {
    data object Refresh : MarketsAction
    data object Retry : MarketsAction
}
