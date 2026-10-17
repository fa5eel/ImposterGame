package com.example.impostergame.domain.model

enum class GamePhase {
    HOME,
    SETUP,
    ROLE_REVEAL,
    DISCUSSION,
    VOTING,
    RESULTS
}

data class GameSessionState(
    val phase: GamePhase = GamePhase.HOME,
    val players: List<Player> = emptyList(),
    val currentPack: WordPack? = null,
    val selectedWordPair: WordPair? = null,
    val gameMode: GameMode = GameMode.CLASSIC,
    val imposterCount: Int = 1,
    val roundDurationSeconds: Int = 60,
    val isTrollRound: Boolean = false,
    val imposterHintEnabled: Boolean = false,
    val currentHint: String? = null,
    val isDiscussionTimeless: Boolean = false,
    val currentRevealPlayerIndex: Int = 0,
    val discussionTimeRemainingSeconds: Int = 60,
    val isTimerRunning: Boolean = false,
    val startingPlayerName: String = "",
    val votedOutPlayer: Player? = null,
    val imposterWon: Boolean = false,
    val votes: Map<String, String> = emptyMap(),
    val showStartWarning: Boolean = false,
    val showVotingWarning: Boolean = false
)
