package com.example.impostergame.domain.model

data class GameSettings(
    val discussionDurationSeconds: Int = 60,
    val customDiscussionTimeSeconds: Int = 30,
    val imposterCount: Int = 1,
    val defaultPlayerNames: List<String> = listOf("Alice", "Bob", "Charlie"),
    val enableSoundEffects: Boolean = true,
    val trollModeEnabled: Boolean = false,
    val imposterHintEnabled: Boolean = false,
    val selectedGameMode: String? = null,
    val selectedPackId: String? = null,
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}