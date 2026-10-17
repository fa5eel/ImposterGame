package com.example.impostergame.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.impostergame.domain.model.GameSettings
import com.example.impostergame.domain.model.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "game_settings")

    companion object {
        private val DISCUSSSION_DURATION_SECONDS_KEY = intPreferencesKey("discussion_duration_seconds")
        private val IMPOSTER_COUNT_KEY = intPreferencesKey("imposter_count")
        private val DEFAULT_PLAYER_NAMES_KEY = stringSetPreferencesKey("default_player_names")
        private val ENABLE_SOUND_EFFECTS_KEY = booleanPreferencesKey("enable_sound_effects")
        private val TROLL_MODE_ENABLED_KEY = booleanPreferencesKey("troll_mode_enabled")
        private val IMPOSTER_HINT_ENABLED_KEY = booleanPreferencesKey("imposter_hint_enabled")
        private val SELECTED_GAME_MODE_KEY = stringPreferencesKey("selected_game_mode")
        private val SELECTED_PACK_ID_KEY = stringPreferencesKey("selected_pack_id")
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
    }

    private val _settings = MutableStateFlow(GameSettings())
    val settings: StateFlow<GameSettings> = _settings.asStateFlow()
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        loadSettings()
    }

    private fun loadSettings() {
        repositoryScope.launch {
            context.dataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { prefs ->
                    val defaultPlayerNames = prefs[DEFAULT_PLAYER_NAMES_KEY]?.toList()
                        ?: listOf("Alice", "Bob", "Charlie")
                    val themeModeStr = prefs[THEME_MODE_KEY]
                    val themeMode = themeModeStr?.let { ThemeMode.valueOf(it) } ?: ThemeMode.SYSTEM
                    GameSettings(
                        discussionDurationSeconds = prefs[DISCUSSSION_DURATION_SECONDS_KEY] ?: 60,
                        imposterCount = prefs[IMPOSTER_COUNT_KEY] ?: 1,
                        defaultPlayerNames = defaultPlayerNames,
                        enableSoundEffects = prefs[ENABLE_SOUND_EFFECTS_KEY] ?: true,
                        trollModeEnabled = prefs[TROLL_MODE_ENABLED_KEY] ?: false,
                        imposterHintEnabled = prefs[IMPOSTER_HINT_ENABLED_KEY] ?: false,
                        selectedGameMode = prefs[SELECTED_GAME_MODE_KEY],
                        selectedPackId = prefs[SELECTED_PACK_ID_KEY],
                        themeMode = themeMode
                    )
                }
                .collect { settings ->
                    _settings.value = settings
                }
        }
    }

    fun updateSettings(newSettings: GameSettings) {
        _settings.value = newSettings
        repositoryScope.launch {
            context.dataStore.edit { prefs ->
                prefs[DISCUSSSION_DURATION_SECONDS_KEY] = newSettings.discussionDurationSeconds
                prefs[IMPOSTER_COUNT_KEY] = newSettings.imposterCount
                prefs[DEFAULT_PLAYER_NAMES_KEY] = newSettings.defaultPlayerNames.toSet()
                prefs[ENABLE_SOUND_EFFECTS_KEY] = newSettings.enableSoundEffects
                prefs[TROLL_MODE_ENABLED_KEY] = newSettings.trollModeEnabled
                prefs[IMPOSTER_HINT_ENABLED_KEY] = newSettings.imposterHintEnabled
                newSettings.selectedGameMode?.let { prefs[SELECTED_GAME_MODE_KEY] = it }
                    ?: prefs.remove(SELECTED_GAME_MODE_KEY)
                newSettings.selectedPackId?.let { prefs[SELECTED_PACK_ID_KEY] = it }
                    ?: prefs.remove(SELECTED_PACK_ID_KEY)
                prefs[THEME_MODE_KEY] = newSettings.themeMode.name
            }
        }
    }

    fun updateSettings(updateBlock: (GameSettings) -> GameSettings) {
        val currentSettings = _settings.value
        val newSettings = updateBlock(currentSettings)
        updateSettings(newSettings)
    }
}