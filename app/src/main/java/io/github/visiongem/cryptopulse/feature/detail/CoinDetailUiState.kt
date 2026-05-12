package io.github.visiongem.cryptopulse.feature.detail

import io.github.visiongem.cryptopulse.data.model.ChartData
import io.github.visiongem.cryptopulse.data.model.Coin
import io.github.visiongem.cryptopulse.domain.AppError

data class CoinDetailUiState(
    val coin: Coin? = null,
    val period: ChartPeriod = ChartPeriod.DAY,
    val chart: ChartUi = ChartUi.Loading,
)

sealed interface ChartUi {
    data object Loading : ChartUi
    data class Success(val data: ChartData) : ChartUi
    data class Failure(val error: AppError) : ChartUi
}

sealed interface CoinDetailAction {
    data class SelectPeriod(val period: ChartPeriod) : CoinDetailAction
    data object Retry : CoinDetailAction
    data class ToggleFavorite(val symbol: String) : CoinDetailAction
}
