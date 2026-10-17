package com.example.impostergame.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.impostergame.domain.model.Role
import com.example.impostergame.ui.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    gameViewModel: GameViewModel,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit
) {
    val gameState by gameViewModel.gameState.collectAsState()
    val imposterWon = gameState.imposterWon
    val votedOut = gameState.votedOutPlayer
    val secretWord = gameState.selectedWordPair?.citizenWord ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Results", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Winner Banner
                item {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (imposterWon) MaterialTheme.colorScheme.errorContainer
                            else MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (imposterWon) "🎭" else "🏆",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (imposterWon) "IMPOSTER WINS!" else "CITIZENS WIN!",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = if (imposterWon) MaterialTheme.colorScheme.onErrorContainer
                                else MaterialTheme.colorScheme.onPrimaryContainer,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (imposterWon) {
                                    "The Imposter successfully evaded detection!"
                                } else {
                                    "The Citizens found the Imposter!"
                                },
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = if (imposterWon) MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                                else MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                // Voted out & Secret Word Summary Card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Voted Out:", color = MaterialTheme.colorScheme.outline)
                                Text(
                                    text = votedOut?.let { "${it.name} (${if (it.role == Role.IMPOSTER) "Imposter" else "Citizen"})" } ?: "No one",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            HorizontalDivider()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Secret Word:", color = MaterialTheme.colorScheme.outline)
                                Text(
                                    text = secretWord,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                // Players Roles Header
                item {
                    Text(
                        text = "Player Roles",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Players Roles List
                items(gameState.players, key = { it.id }) { player ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (player.role == Role.IMPOSTER) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                            else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (player.role == Role.IMPOSTER) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = if (player.role == Role.IMPOSTER) "IM" else "CZ",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(player.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }

                            Badge(
                                containerColor = if (player.role == Role.IMPOSTER) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            ) {
                                Text(
                                    text = if (player.role == Role.IMPOSTER) "IMPOSTER" else "CITIZEN",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        gameViewModel.startGame()
                        onPlayAgain()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("PLAY AGAIN", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        gameViewModel.resetToHome()
                        onHome()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("MAIN MENU", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
