package com.example.impostergame.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.impostergame.data.repository.WordPackRepository
import com.example.impostergame.data.repository.SettingsRepository
import com.example.impostergame.domain.model.GameMode
import com.example.impostergame.domain.model.GamePhase
import com.example.impostergame.domain.model.GameSessionState
import com.example.impostergame.domain.model.Player
import com.example.impostergame.domain.model.Role
import com.example.impostergame.domain.model.WordPack
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

@HiltViewModel
class GameViewModel @Inject constructor(
    val wordPackRepository: WordPackRepository,
    val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _gameState = MutableStateFlow(GameSessionState())
    val gameState: StateFlow<GameSessionState> = _gameState.asStateFlow()

    private var timerJob: Job? = null

    fun startNewGameSetup() {
        val currentSettings = settingsRepository.settings.value
        val defaultPacks = wordPackRepository.allPacks.value

        // Determine game mode from settings, fallback to CLASSIC
        val gameMode = try {
            currentSettings.selectedGameMode?.let { GameMode.valueOf(it) }
                ?: GameMode.CLASSIC
        } catch (e: IllegalArgumentException) {
            GameMode.CLASSIC
        }

        // Determine initial pack from settings, with robust fallback
        val initialPack = currentSettings.selectedPackId?.let { id ->
            wordPackRepository.getPackById(id) ?: defaultPacks.firstOrNull { it.id == "default" }
        } ?: defaultPacks.firstOrNull { it.id == "default" } ?: defaultPacks.firstOrNull()

        _gameState.update {
            GameSessionState(
                phase = GamePhase.SETUP,
                players = listOf(
                    Player(id = UUID.randomUUID().toString(), name = "Player 1"),
                    Player(id = UUID.randomUUID().toString(), name = "Player 2"),
                    Player(id = UUID.randomUUID().toString(), name = "Player 3")
                ),
                currentPack = initialPack,
                gameMode = gameMode,
                imposterCount = currentSettings.imposterCount,
                // Use custom time if set (> 0), otherwise use preset duration
                roundDurationSeconds = when {
                    currentSettings.customDiscussionTimeSeconds > 0 -> currentSettings.customDiscussionTimeSeconds
                    else -> currentSettings.discussionDurationSeconds
                }.coerceAtLeast(1) // Ensure at least 1 second for timer
            )
        }
    }

    fun updatePlayerName(id: String, newName: String) {
        _gameState.update { state ->
            val updated = state.players.map { player ->
                if (player.id == id) player.copy(name = newName) else player
            }
            state.copy(players = updated)
        }
    }

    fun addPlayer() {
        if (_gameState.value.players.size < 10) {
            val nextNumber = _gameState.value.players.size + 1
            _gameState.update { state ->
                val newPlayer = Player(id = UUID.randomUUID().toString(), name = "Player $nextNumber")
                state.copy(players = state.players + newPlayer)
            }
        }
    }

    fun removePlayer(id: String) {
        if (_gameState.value.players.size > 3) {
            _gameState.update { state ->
                state.copy(players = state.players.filter { it.id != id })
            }
        }
    }

    fun startGame() {
        val currentState = _gameState.value
        val currentSettings = settingsRepository.settings.value
        val pack = currentState.currentPack ?: wordPackRepository.allPacks.value.first()
        val wordPair = pack.wordPairs.randomOrNull() ?: pack.wordPairs.first()

        val totalPlayers = currentState.players.size
        var imposterCount = currentSettings.imposterCount.coerceAtMost(totalPlayers - 2)
        var isTrollRound = false
        var imposterHintEnabled = currentSettings.imposterHintEnabled
        var currentHint: String? = null
        var isDiscussionTimeless = false

        // Check for Troll Round: 25% chance if Troll Mode is enabled
        if (currentSettings.trollModeEnabled && (1..4).random() == 1) {
            isTrollRound = true
            imposterHintEnabled = true // Show hint to all players in Troll Round
            // Generate a random hint for Troll Round (we'll use a simple list)
            val trollHints = listOf(
                "Think like an impostor",
                "Blend in with the citizens",
                "You know the secret word",
                "Pretend you know the answer",
                "Act like you belong"
            )
            currentHint = trollHints.random()
            imposterCount = totalPlayers // Everyone is an impostor
        } else {
            isTrollRound = false
            imposterHintEnabled = currentSettings.imposterHintEnabled
            if (imposterHintEnabled) {
                currentHint = wordPair.hint
            }
        }

        // Handle No Time mode
        isDiscussionTimeless = currentSettings.discussionDurationSeconds == 0

        val shuffledIndices = currentState.players.indices.shuffled()
        val imposterIndices = shuffledIndices.take(imposterCount).toSet()

        val assignedPlayers = currentState.players.mapIndexed { index, player ->
            val isImposter = index in imposterIndices
            val role = if (isImposter) Role.IMPOSTER else Role.CITIZEN
            val word = when {
                !isImposter -> wordPair.citizenWord
                currentState.gameMode == GameMode.RELATED_WORDS -> {
                    wordPair.imposterWord.ifEmpty { wordPair.citizenWord }
                }
                else -> "" // Imposter gets no word in classic mode
            }
            player.copy(
                role = role,
                secretWord = word,
                isRevealed = false,
                votesReceived = 0
            )
        }

        val starter = assignedPlayers.random().name

        _gameState.update { state ->
            state.copy(
                phase = GamePhase.ROLE_REVEAL,
                players = assignedPlayers,
                selectedWordPair = wordPair,
                currentRevealPlayerIndex = 0,
                startingPlayerName = starter,
                discussionTimeRemainingSeconds = if (isDiscussionTimeless) Int.MAX_VALUE else currentSettings.discussionDurationSeconds,
                isTimerRunning = false,
                votes = emptyMap(),
                votedOutPlayer = null,
                imposterWon = false,
                isTrollRound = isTrollRound,
                imposterHintEnabled = imposterHintEnabled,
                currentHint = currentHint,
                isDiscussionTimeless = isDiscussionTimeless,
                showStartWarning = true
            )
        }
    }

    fun advanceRoleReveal() {
        val currentState = _gameState.value
        val nextIndex = currentState.currentRevealPlayerIndex + 1
        if (nextIndex < currentState.players.size) {
            _gameState.update { it.copy(currentRevealPlayerIndex = nextIndex) }
        } else {
            // All players revealed, move to discussion
            _gameState.update { it.copy(phase = GamePhase.DISCUSSION, showStartWarning = false) }
            startDiscussionTimer()
        }
    }

    fun startDiscussionTimer() {
        // If discussion is timeless, we don't start a timer
        if (_gameState.value.isDiscussionTimeless) {
            timerJob?.cancel()
            _gameState.update { it.copy(isTimerRunning = false) }
            return
        }
        timerJob?.cancel()
        _gameState.update { it.copy(isTimerRunning = true) }
        timerJob = viewModelScope.launch {
            while (_gameState.value.discussionTimeRemainingSeconds > 0 && _gameState.value.isTimerRunning) {
                delay(1000L)
                _gameState.update { state ->
                    val remaining = state.discussionTimeRemainingSeconds - 1
                    if (remaining <= 0) {
                        state.copy(discussionTimeRemainingSeconds = 0, isTimerRunning = false, phase = GamePhase.VOTING, showVotingWarning = true)
                    } else {
                        state.copy(discussionTimeRemainingSeconds = remaining)
                    }
                }
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _gameState.update { it.copy(isTimerRunning = false) }
    }

    fun resumeTimer() {
        startDiscussionTimer()
    }

    fun skipToVoting() {
        timerJob?.cancel()
        _gameState.update { it.copy(isTimerRunning = false, phase = GamePhase.VOTING, showVotingWarning = true) }
    }

    fun castVote(voterId: String, targetPlayerId: String) {
        _gameState.update { state ->
            val updatedVotes = state.votes + (voterId to targetPlayerId)
            state.copy(votes = updatedVotes)
        }
    }

    fun completeVoting() {
        val currentState = _gameState.value
        val voteCounts = mutableMapOf<String, Int>()
        currentState.votes.values.forEach { targetId ->
            voteCounts[targetId] = (voteCounts[targetId] ?: 0) + 1
        }

        val mostVotedEntry = voteCounts.maxByOrNull { it.value }
        val votedOutPlayer = currentState.players.find { it.id == mostVotedEntry?.key }

        // Imposter wins if no one is voted out, or if a Citizen is voted out
        val imposterWon = votedOutPlayer == null || votedOutPlayer.role == Role.CITIZEN

        _gameState.update { state ->
            state.copy(
                phase = GamePhase.RESULTS,
                votedOutPlayer = votedOutPlayer,
                imposterWon = imposterWon,
                showVotingWarning = false
            )
        }
    }

    fun resetToHome() {
        timerJob?.cancel()
        _gameState.update { GameSessionState(phase = GamePhase.HOME) }
    }
}