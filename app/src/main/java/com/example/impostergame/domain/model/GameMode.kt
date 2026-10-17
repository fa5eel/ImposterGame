package com.example.impostergame.domain.model

enum class GameMode(val displayName: String, val description: String) {
    CLASSIC("Classic Imposter", "Imposters know nothing. Citizens share the exact secret word."),
    RELATED_WORDS("Related Words", "Imposters get a similar or related word instead of no word.")
}