package com.novarixis.nebular.feature.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val darkMode: Boolean = true,
    val language: String = "English",
    val notificationsEnabled: Boolean = true,
    val planLabel: String = "Pro Plan"
)

object SettingsKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode")
    val LANGUAGE = stringPreferencesKey("language")
    val NOTIFICATIONS = booleanPreferencesKey("notifications_enabled")
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        viewModelScope.launch {
            dataStore.data
                .map { prefs ->
                    SettingsUiState(
                        darkMode = prefs[SettingsKeys.DARK_MODE] ?: true,
                        language = prefs[SettingsKeys.LANGUAGE] ?: "English",
                        notificationsEnabled = prefs[SettingsKeys.NOTIFICATIONS] ?: true
                    )
                }
                .collect { state -> _uiState.value = state }
        }
    }

    fun setDarkMode(enabled: Boolean) {
        _uiState.update { it.copy(darkMode = enabled) }
        viewModelScope.launch {
            dataStore.edit { prefs -> prefs[SettingsKeys.DARK_MODE] = enabled }
        }
    }

    fun setNotifications(enabled: Boolean) {
        _uiState.update { it.copy(notificationsEnabled = enabled) }
        viewModelScope.launch {
            dataStore.edit { prefs -> prefs[SettingsKeys.NOTIFICATIONS] = enabled }
        }
    }

    fun setLanguage(language: String) {
        _uiState.update { it.copy(language = language) }
        viewModelScope.launch {
            dataStore.edit { prefs -> prefs[SettingsKeys.LANGUAGE] = language }
        }
    }
}
