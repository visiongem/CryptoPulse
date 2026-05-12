package io.github.visiongem.cryptopulse.feature.markets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.visiongem.cryptopulse.CryptoPulseApp
import io.github.visiongem.cryptopulse.feature.markets.components.CoinRow
import io.github.visiongem.cryptopulse.ui.component.ErrorState
import io.github.visiongem.cryptopulse.ui.component.LoadingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val locator = (context.applicationContext as CryptoPulseApp).serviceLocator

    val viewModel: MarketsViewModel = viewModel(
        factory = MarketsViewModel.factory(
            repository = locator.marketsRepository,
            tickerRepository = locator.tickerRepository,
        ),
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val error = state.error

    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                LoadingState()
            }
            error != null && state.coins.isEmpty() -> {
                ErrorState(
                    error = error,
                    onRetry = { viewModel.onAction(MarketsAction.Retry) },
                )
            }
            else -> {
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = { viewModel.onAction(MarketsAction.Refresh) },
                ) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.coins, key = { it.id }) { coin ->
                            CoinRow(coin = coin)
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}
