package io.github.visiongem.cryptopulse.feature.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import io.github.visiongem.cryptopulse.data.model.Coin
import io.github.visiongem.cryptopulse.data.repository.MarketsRepository
import io.github.visiongem.cryptopulse.data.repository.WatchlistRepository
import io.github.visiongem.cryptopulse.domain.LoadResult
import kotlinx.coroutines.flow.first

/**
 * One-shot fetch → persist → trigger Glance update.
 *
 * Called from [WidgetUpdateWorker] on a 15-min cadence and on widget add.
 */
class WidgetUpdater(
    private val context: Context,
    private val marketsRepository: MarketsRepository,
    private val watchlistRepository: WatchlistRepository,
    private val widgetDataStore: WidgetDataStore,
) {
    suspend fun refresh(): Result<Unit> = runCatching {
        val watchlist = watchlistRepository.symbolsFlow.first()
        val result = marketsRepository.getTopMarkets()
        when (result) {
            is LoadResult.Success -> {
                val widgetCoins = result.data.toWidgetCoins(watchlist)
                widgetDataStore.save(
                    WidgetDataSnapshot(
                        timestamp = System.currentTimeMillis(),
                        coins = widgetCoins,
                    ),
                )
                CryptoPulseWidget().updateAll(context)
            }
            is LoadResult.Failure -> error("Refresh failed: ${result.error}")
        }
    }

    private fun List<Coin>.toWidgetCoins(watchlist: Set<String>): List<WidgetCoinData> {
        val pool = if (watchlist.isNotEmpty()) filter { it.symbol in watchlist } else this
        return pool.take(MAX_WIDGET_COINS).map { coin ->
            WidgetCoinData(
                id = coin.id,
                symbol = coin.symbol,
                name = coin.name,
                price = coin.price,
                priceChangePercentage24h = coin.priceChangePercentage24h,
                sparkline = coin.sparkline7d,
            )
        }
    }

    private companion object {
        const val MAX_WIDGET_COINS = 6
    }
}
