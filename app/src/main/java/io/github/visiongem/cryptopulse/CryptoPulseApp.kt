package io.github.visiongem.cryptopulse

import android.app.Application
import io.github.visiongem.cryptopulse.di.ServiceLocator

class CryptoPulseApp : Application() {

    lateinit var serviceLocator: ServiceLocator
        private set

    override fun onCreate() {
        super.onCreate()
        serviceLocator = ServiceLocator()
    }
}
