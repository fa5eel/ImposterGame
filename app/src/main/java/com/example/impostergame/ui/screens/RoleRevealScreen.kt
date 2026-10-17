package com.example.impostergame.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.impostergame.domain.model.Role
import com.example.impostergame.ui.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleRevealScreen(
    gameViewModel: GameViewModel,
    onAllRolesRevealed: () -> Unit
) {
    val gameState by gameViewModel.gameState.collectAsState()
    val currentPlayerIndex = gameState.currentRevealPlayerIndex
    val currentPlayer = gameState.players.getOrNull(currentPlayerIndex) ?: return

    var isHoldingToReveal by remember(currentPlayerIndex) { mutableStateOf(false) }
    var hasRevealedOnce by remember(currentPlayerIndex) { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pass & Reveal", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Player indicator header
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Pass phone to:",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = currentPlayer.name,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "Player ${currentPlayerIndex + 1} of ${gameState.players.size}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            // Secret Reveal Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHoldingToReveal) {
                        if (currentPlayer.role == Role.IMPOSTER) MaterialTheme.colorScheme.errorContainer
                        else MaterialTheme.colorScheme.primaryContainer
                    } else MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .pointerInput(currentPlayerIndex) {
                        detectTapGestures(
                            onLongPress = {
                                isHoldingToReveal = true
                                hasRevealedOnce = true
                            },
                            onPress = {
                                tryAwaitRelease()
                                isHoldingToReveal = false
                            }
                        )
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isHoldingToReveal) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "Secret Visible",
                                modifier = Modifier.size(48.dp),
                                tint = if (currentPlayer.role == Role.IMPOSTER) MaterialTheme.colorScheme.onErrorContainer
                                else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            if (currentPlayer.role == Role.IMPOSTER) {
                                Text(
                                    text = "YOU ARE THE IMPOSTER!",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    textAlign = TextAlign.Center
                                )
                                if (gameState.imposterHintEnabled) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "💡 Hint: ${gameState.currentHint}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                                if (currentPlayer.secretWord.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Your Word: ${currentPlayer.secretWord}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                } else {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Blend in & pretend you know the secret word!",
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                                    )
                                }
                            } else {
                                Text(
                                    text = "YOUR SECRET WORD:",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = currentPlayer.secretWord,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Tap & Hold",
                                modifier = Modifier.size(56.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "PRESS AND HOLD",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "to reveal your secret role",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            }

            // Bottom action
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AnimatedVisibility(visible = hasRevealedOnce) {
                    Button(
                        onClick = {
                            if (currentPlayerIndex + 1 < gameState.players.size) {
                                gameViewModel.advanceRoleReveal()
                            } else {
                                gameViewModel.advanceRoleReveal()
                                onAllRolesRevealed()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = if (currentPlayerIndex + 1 < gameState.players.size) "GOT IT • NEXT PLAYER" else "START DISCUSSION",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (!hasRevealedOnce) {
                    Text(
                        text = "Make sure no one else is looking at the screen!",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}