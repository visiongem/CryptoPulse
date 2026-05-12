package io.github.visiongem.cryptopulse.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/**
 * Single DataStore<Preferences> backing all key/value user data.
 *
 * Top-level extension property — repositories take a [Context] and access
 * `context.appPreferences`. This is the only place a real DataStore lives.
 */
internal val Context.appPreferences: DataStore<Preferences> by preferencesDataStore(
    name = "cryptopulse",
)
