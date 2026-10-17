package com.example.impostergame.domain.model

enum class Role {
    CITIZEN,
    IMPOSTER
}

data class Player(
    val id: String,
    val name: String,
    val role: Role = Role.CITIZEN,
    val secretWord: String = "",
    val isRevealed: Boolean = false,
    val votesReceived: Int = 0
)
