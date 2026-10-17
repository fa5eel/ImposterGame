package com.example.impostergame.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.impostergame.data.repository.SettingsRepository
import com.example.impostergame.domain.model.GameSettings
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val settings: StateFlow<GameSettings> = settingsRepository.settings

    fun updateSettings(updateBlock: (GameSettings) -> GameSettings) {
        settingsRepository.updateSettings { updateBlock(it) }
    }
}
