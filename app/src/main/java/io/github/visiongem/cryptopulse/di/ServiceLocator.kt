package io.github.visiongem.cryptopulse.di

import io.github.visiongem.cryptopulse.data.network.NetworkModule
import io.github.visiongem.cryptopulse.data.repository.MarketsRepository

class ServiceLocator {

    val marketsRepository: MarketsRepository by lazy {
        MarketsRepository(NetworkModule.coinGeckoApi)
    }
}
