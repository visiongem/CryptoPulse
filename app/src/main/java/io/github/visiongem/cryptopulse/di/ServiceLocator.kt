package io.github.visiongem.cryptopulse.di

import io.github.visiongem.cryptopulse.data.network.NetworkModule
import io.github.visiongem.cryptopulse.data.repository.MarketsRepository
import io.github.visiongem.cryptopulse.data.repository.TickerRepository

class ServiceLocator {

    val marketsRepository: MarketsRepository by lazy {
        MarketsRepository(NetworkModule.coinGeckoApi)
    }

    val tickerRepository: TickerRepository by lazy {
        TickerRepository(NetworkModule.binanceWebSocketClient)
    }
}
