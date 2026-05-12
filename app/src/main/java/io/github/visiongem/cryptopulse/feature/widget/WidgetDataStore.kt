package io.github.visiongem.cryptopulse.feature.widget

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.github.visiongem.cryptopulse.data.local.appPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WidgetDataStore(
    private val context: Context,
) {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val snapshotFlow: Flow<WidgetDataSnapshot?> = context.appPreferences.data.map { prefs ->
        prefs[KEY_SNAPSHOT]?.let { raw ->
            runCatching { json.decodeFromString<WidgetDataSnapshot>(raw) }.getOrNull()
        }
    }

    suspend fun save(snapshot: WidgetDataSnapshot) {
        context.appPreferences.edit { prefs ->
            prefs[KEY_SNAPSHOT] = json.encodeToString(snapshot)
        }
    }

    private companion object {
        val KEY_SNAPSHOT = stringPreferencesKey("widget_snapshot_v1")
    }
}
