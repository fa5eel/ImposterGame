package com.example.impostergame.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.impostergame.domain.model.GameMode
import com.example.impostergame.domain.model.WordPack
import com.example.impostergame.ui.viewmodel.GameViewModel
import com.example.impostergame.ui.viewmodel.SettingsViewModel
import com.example.impostergame.ui.viewmodel.WordPackViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    gameViewModel: GameViewModel,
    settingsViewModel: SettingsViewModel,
    wordPackViewModel: WordPackViewModel,
    onBack: () -> Unit,
    onStartGame: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val gameState by gameViewModel.gameState.collectAsState()
    val settings by settingsViewModel.settings.collectAsState()
    val wordPacks by wordPackViewModel.wordPacks.collectAsState()
    val currentPack = wordPacks.firstOrNull { it.id == settings.selectedPackId } ?: wordPacks.firstOrNull()
    val currentGameMode = GameMode.entries.firstOrNull { it.name == settings.selectedGameMode } ?: GameMode.CLASSIC

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Setup", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Section 1: Players
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Players (${gameState.players.size}/10)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (gameState.players.size < 10) {
                        TextButton(onClick = { gameViewModel.addPlayer() }) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Player")
                        }
                    }
                }
            }

            itemsIndexed(gameState.players, key = { _, player -> player.id }) { index, player ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${index + 1}",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        OutlinedTextField(
                            value = player.name,
                            onValueChange = { gameViewModel.updatePlayerName(player.id, it) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        )

                        if (gameState.players.size > 3) {
                            IconButton(onClick = { gameViewModel.removePlayer(player.id) }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Remove Player",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            // Section 2: Settings Summary
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Current Settings",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Column {
                                Text("Game Mode", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                Text(currentGameMode.displayName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Word Pack", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                Text(currentPack?.name ?: "Unknown", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Imposters", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                Text("${settings.imposterCount}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Column {
                                Text("Discussion Time", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                val timeText = if (settings.discussionDurationSeconds == 0) "No Time" else "${settings.discussionDurationSeconds}s"
                                Text(timeText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Troll Mode", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                Text(if (settings.trollModeEnabled) "ON" else "OFF", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Imposter Hint", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                Text(if (settings.imposterHintEnabled) "ON" else "OFF", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Section 3: Customize Settings Shortcut
            item {
                Button(
                    onClick = { onSettingsClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Customize Settings", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Start Game Button
            item {
                Button(
                    onClick = {
                        gameViewModel.startGame()
                        onStartGame()
                    },
                    enabled = gameState.players.size >= 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("START GAME", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}