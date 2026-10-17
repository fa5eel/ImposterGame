package com.example.impostergame.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.impostergame.domain.model.GameMode
import com.example.impostergame.domain.model.ThemeMode
import com.example.impostergame.domain.model.WordPack
import com.example.impostergame.ui.viewmodel.SettingsViewModel
import com.example.impostergame.ui.viewmodel.WordPackViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    settingsViewModel: SettingsViewModel,
    wordPackViewModel: WordPackViewModel
) {
    val settings by settingsViewModel.settings.collectAsState()
    val wordPacks by wordPackViewModel.wordPacks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
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
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Game Mode",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = Icons.Default.Gamepad,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Choose how impostors are assigned and what information they receive.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    GameMode.entries.forEach { mode ->
                        FilterChip(
                            selected = settings.selectedGameMode == mode.name,
                            onClick = {
                                settingsViewModel.updateSettings { it.copy(selectedGameMode = mode.name) }
                            },
                            label = {
                                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Text(mode.displayName, fontWeight = FontWeight.Bold)
                                    Text(mode.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Word Pack",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = Icons.Default.LibraryBooks,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Select the word pack to use for the game.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    wordPacks.forEach { pack ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = settings.selectedPackId == pack.id,
                                onClick = { settingsViewModel.updateSettings { it.copy(selectedPackId = pack.id) } },
                                enabled = true
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(pack.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(
                                    "${pack.wordPairs.size} word pairs • ${pack.description}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Imposter Count",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Set the number of impostors in the game (adjusts based on player count).",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Imposters:", fontWeight = FontWeight.Medium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = settings.imposterCount == 1,
                            onClick = { settingsViewModel.updateSettings { it.copy(imposterCount = 1) } },
                            label = { Text("1 Imposter") }
                        )
                        FilterChip(
                            selected = settings.imposterCount == 2,
                            onClick = { settingsViewModel.updateSettings { it.copy(imposterCount = 2) } },
                            label = { Text("2 Imposters") },
                            enabled = true // We don't have player count here, but we can enable always and validate in GameViewModel
                        )
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Discussion Time",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Set the time limit for discussions (0 for no limit).",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Duration:", fontWeight = FontWeight.Medium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(30, 60, 120).forEach { seconds ->
                            FilterChip(
                                selected = settings.discussionDurationSeconds == seconds,
                                onClick = { settingsViewModel.updateSettings { it.copy(discussionDurationSeconds = seconds) } },
                                label = { Text("${seconds}s") }
                            )
                        }
                        FilterChip(
                            selected = settings.discussionDurationSeconds == 0,
                            onClick = { settingsViewModel.updateSettings { it.copy(discussionDurationSeconds = 0) } },
                            label = { Text("No Time") }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Custom Time (seconds):", fontWeight = FontWeight.Medium)
                    OutlinedTextField(
                        value = settings.customDiscussionTimeSeconds.toString(),
                        onValueChange = {
                            try {
                                val input = it.toIntOrNull() ?: 0
                                settingsViewModel.updateSettings { it.copy(customDiscussionTimeSeconds = input) }
                            } catch (e: NumberFormatException) {
                                // Ignore invalid input
                            }
                        },
                        label = { Text("Enter custom time") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.5f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Text(
                        text = "${settings.customDiscussionTimeSeconds}s",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "Special Features",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Enable optional game mechanics for varied gameplay.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SentimentVerySatisfied,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Troll Mode: ", fontWeight = FontWeight.Medium)
                    Switch(
                        checked = settings.trollModeEnabled,
                        onCheckedChange = { checked -> settingsViewModel.updateSettings { it.copy(trollModeEnabled = checked) } }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Imposter Hint: ", fontWeight = FontWeight.Medium)
                    Switch(
                        checked = settings.imposterHintEnabled,
                        onCheckedChange = { checked -> settingsViewModel.updateSettings { it.copy(imposterHintEnabled = checked) } }
                    )
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Theme",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = Icons.Default.Brightness6,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Choose the app theme appearance.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Theme: ", fontWeight = FontWeight.Medium)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        RadioButton(
                            selected = settings.themeMode == ThemeMode.LIGHT,
                            onClick = { settingsViewModel.updateSettings { it.copy(themeMode = ThemeMode.LIGHT) } },
                            enabled = true
                        )
                        Text("Light", fontSize = 16.sp)
                        RadioButton(
                            selected = settings.themeMode == ThemeMode.DARK,
                            onClick = { settingsViewModel.updateSettings { it.copy(themeMode = ThemeMode.DARK) } },
                            enabled = true
                        )
                        Text("Dark", fontSize = 16.sp)
                        RadioButton(
                            selected = settings.themeMode == ThemeMode.SYSTEM,
                            onClick = { settingsViewModel.updateSettings { it.copy(themeMode = ThemeMode.SYSTEM) } },
                            enabled = true
                        )
                        Text("System", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}