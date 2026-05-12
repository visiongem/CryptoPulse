package io.github.visiongem.cryptopulse.feature.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.visiongem.cryptopulse.CryptoPulseApp
import io.github.visiongem.cryptopulse.data.model.ChartPoint
import io.github.visiongem.cryptopulse.feature.detail.components.ChartPeriodSelector
import io.github.visiongem.cryptopulse.feature.detail.components.CoinDetailHeader
import io.github.visiongem.cryptopulse.feature.detail.components.PriceChart
import io.github.visiongem.cryptopulse.ui.component.ErrorState
import io.github.visiongem.cryptopulse.ui.component.LoadingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailScreen(
    coinId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val locator = (context.applicationContext as CryptoPulseApp).serviceLocator

    val viewModel: CoinDetailViewModel = viewModel(
        key = coinId,
        factory = CoinDetailViewModel.factory(
            coinId = coinId,
            store = locator.marketsStore,
            chartRepository = locator.chartRepository,
            watchlistRepository = locator.watchlistRepository,
        ),
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    var hoveredPoint by remember(state.period) { mutableStateOf<ChartPoint?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = state.coin?.symbol ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        val coin = state.coin
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (coin == null) {
                LoadingState()
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    CoinDetailHeader(
                        coin = coin,
                        hoveredPoint = hoveredPoint,
                        chartDays = state.period.days,
                        onFavoriteClick = {
                            viewModel.onAction(CoinDetailAction.ToggleFavorite(coin.symbol))
                        },
                    )
                    ChartArea(
                        chart = state.chart,
                        onCrosshairChange = { hoveredPoint = it },
                        onRetry = { viewModel.onAction(CoinDetailAction.Retry) },
                    )
                    ChartPeriodSelector(
                        selected = state.period,
                        onSelect = { viewModel.onAction(CoinDetailAction.SelectPeriod(it)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ChartArea(
    chart: ChartUi,
    onCrosshairChange: (ChartPoint?) -> Unit,
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        when (chart) {
            ChartUi.Loading -> LoadingState()
            is ChartUi.Failure -> ErrorState(error = chart.error, onRetry = onRetry)
            is ChartUi.Success -> {
                if (chart.data.points.size < 2) {
                    Text(
                        text = stringResource(io.github.visiongem.cryptopulse.R.string.chart_empty),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    PriceChart(
                        data = chart.data,
                        onCrosshairChange = onCrosshairChange,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}
