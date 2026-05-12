package io.github.visiongem.cryptopulse.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.visiongem.cryptopulse.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val versionName: String,
) : ViewModel() {

    val state: StateFlow<SettingsUiState> = userPreferencesRepository.rippleEnabledFlow
        .map { rippleEnabled ->
            SettingsUiState(
                rippleEnabled = rippleEnabled,
                versionName = versionName,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = SettingsUiState(versionName = versionName),
        )

    fun setRippleEnabled(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setRippleEnabled(enabled)
        }
    }

    companion object {
        private const val STOP_TIMEOUT_MS = 5_000L

        fun factory(
            userPreferencesRepository: UserPreferencesRepository,
            versionName: String,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer { SettingsViewModel(userPreferencesRepository, versionName) }
        }
    }
}
