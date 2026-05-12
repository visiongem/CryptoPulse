package io.github.visiongem.cryptopulse.feature.watchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.visiongem.cryptopulse.CryptoPulseApp
import io.github.visiongem.cryptopulse.R
import io.github.visiongem.cryptopulse.feature.markets.components.CoinRow
import io.github.visiongem.cryptopulse.ui.component.ErrorState
import io.github.visiongem.cryptopulse.ui.component.LoadingState

@Composable
fun WatchlistScreen(
    onCoinClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val locator = (context.applicationContext as CryptoPulseApp).serviceLocator

    val viewModel: WatchlistViewModel = viewModel(
        factory = WatchlistViewModel.factory(
            store = locator.marketsStore,
            watchlistRepository = locator.watchlistRepository,
            userPreferencesRepository = locator.userPreferencesRepository,
        ),
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val error = state.error

    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> LoadingState()
            error != null -> ErrorState(
                error = error,
                onRetry = { viewModel.onAction(WatchlistAction.Retry) },
            )
            state.coins.isEmpty() -> WatchlistEmptyState()
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.coins, key = { it.id }) { coin ->
                        CoinRow(
                            coin = coin,
                            rippleEnabled = state.rippleEnabled,
                            onFavoriteClick = {
                                viewModel.onAction(WatchlistAction.ToggleFavorite(it))
                            },
                            onClick = { onCoinClick(it.id) },
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun WatchlistEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Outlined.StarOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.watchlist_empty_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp),
        )
        Text(
            text = stringResource(R.string.watchlist_empty_body),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
