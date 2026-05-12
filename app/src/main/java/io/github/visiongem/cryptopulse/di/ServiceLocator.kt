package io.github.visiongem.cryptopulse.di

import android.content.Context
import io.github.visiongem.cryptopulse.data.network.NetworkModule
import io.github.visiongem.cryptopulse.data.repository.MarketsRepository
import io.github.visiongem.cryptopulse.data.repository.MarketsStore
import io.github.visiongem.cryptopulse.data.repository.TickerRepository
import io.github.visiongem.cryptopulse.data.repository.UserPreferencesRepository
import io.github.visiongem.cryptopulse.data.repository.WatchlistRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class ServiceLocator(context: Context) {

    private val appContext = context.applicationContext
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val watchlistRepository = WatchlistRepository(appContext)
    val userPreferencesRepository = UserPreferencesRepository(appContext)

    private val marketsRepository = MarketsRepository(NetworkModule.coinGeckoApi)
    private val tickerRepository = TickerRepository(NetworkModule.binanceWebSocketClient)

    val marketsStore = MarketsStore(
        marketsRepository = marketsRepository,
        tickerRepository = tickerRepository,
        scope = applicationScope,
    )
}
