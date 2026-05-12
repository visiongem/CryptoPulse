package io.github.visiongem.cryptopulse.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import io.github.visiongem.cryptopulse.data.local.appPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WatchlistRepository(
    private val context: Context,
) {
    val symbolsFlow: Flow<Set<String>> = context.appPreferences.data.map { prefs ->
        prefs[KEY_WATCHLIST] ?: DEFAULT_WATCHLIST
    }

    suspend fun toggle(symbol: String) {
        context.appPreferences.edit { prefs ->
            val current = prefs[KEY_WATCHLIST] ?: DEFAULT_WATCHLIST
            prefs[KEY_WATCHLIST] = if (symbol in current) current - symbol else current + symbol
        }
    }

    suspend fun add(symbol: String) {
        context.appPreferences.edit { prefs ->
            prefs[KEY_WATCHLIST] = (prefs[KEY_WATCHLIST] ?: DEFAULT_WATCHLIST) + symbol
        }
    }

    suspend fun remove(symbol: String) {
        context.appPreferences.edit { prefs ->
            prefs[KEY_WATCHLIST] = (prefs[KEY_WATCHLIST] ?: DEFAULT_WATCHLIST) - symbol
        }
    }

    private companion object {
        val KEY_WATCHLIST = stringSetPreferencesKey("watchlist")
        val DEFAULT_WATCHLIST = setOf("BTC", "ETH", "SOL", "BNB", "XRP")
    }
}
