package io.github.visiongem.cryptopulse

import android.app.Application
import io.github.visiongem.cryptopulse.di.ServiceLocator
import io.github.visiongem.cryptopulse.feature.widget.WidgetUpdateWorker

class CryptoPulseApp : Application() {

    lateinit var serviceLocator: ServiceLocator
        private set

    override fun onCreate() {
        super.onCreate()
        serviceLocator = ServiceLocator(this)
        WidgetUpdateWorker.schedule(this)
    }
}
