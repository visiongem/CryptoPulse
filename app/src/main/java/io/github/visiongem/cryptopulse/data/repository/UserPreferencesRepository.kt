package io.github.visiongem.cryptopulse.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import io.github.visiongem.cryptopulse.data.local.appPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val context: Context,
) {
    val rippleEnabledFlow: Flow<Boolean> = context.appPreferences.data.map { prefs ->
        prefs[KEY_RIPPLE_ENABLED] ?: true
    }

    suspend fun setRippleEnabled(enabled: Boolean) {
        context.appPreferences.edit { prefs ->
            prefs[KEY_RIPPLE_ENABLED] = enabled
        }
    }

    private companion object {
        val KEY_RIPPLE_ENABLED = booleanPreferencesKey("ripple_enabled")
    }
}
