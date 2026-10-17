package com.example.impostergame.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.impostergame.domain.model.WordPair
import com.example.impostergame.ui.viewmodel.SettingsViewModel
import com.example.impostergame.ui.viewmodel.WordPackViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordPacksScreen(
    wordPackViewModel: WordPackViewModel,
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val wordPacks by wordPackViewModel.wordPacks.collectAsState()
    val settings by settingsViewModel.settings.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var packToEditId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Word Packs", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { packToEditId = null; showCreateDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Create Pack") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(wordPacks, key = { it.id }) { pack ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (pack.isCustom) MaterialTheme.colorScheme.tertiaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    var expanded by remember { mutableStateOf(false) }
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Selection mechanism for gameplay pack
                            RadioButton(
                                selected = settings.selectedPackId == pack.id,
                                onClick = { settingsViewModel.updateSettings { it.copy(selectedPackId = pack.id) } },
                                enabled = true
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = pack.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (pack.isCustom) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Badge { Text("Custom") }
                                    }
                                }
                                Text(
                                    text = pack.description,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (pack.isCustom) {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    IconButton(
                                        onClick = { packToEditId = pack.id; showCreateDialog = true }
                                    ) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Edit Pack",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    IconButton(
                                        onClick = { wordPackViewModel.deleteCustomPack(pack.id) }
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete Pack",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }

                            // Expand/collapse indicator (only this icon is clickable for expand/collapse)
                            Icon(
                                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (expanded) "Collapse" else "Expand",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.clickable { expanded = !expanded }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        HorizontalDivider()

                        // Expanded content
                        if (expanded) {
                            Column(modifier = Modifier.padding(top = 8.dp)) {
                                Text(
                                    text = "All Words (${pack.wordPairs.size}):",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                // Show words in a readable format
                                pack.wordPairs.forEachIndexed { index, pair ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 16.dp)
                                    ) {
                                        Text(
                                            text = "${index + 1}. ",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Light
                                        )
                                        Column {
                                            Text(
                                                text = "Citizen: ${pair.citizenWord}",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                            Text(
                                                text = "Imposter: ${pair.imposterWord}",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                            if (pair.hint.isNotBlank()) {
                                                Text(
                                                    text = "Hint: ${pair.hint}",
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                                )
                                            }
                                        }
                                    }
                                    if (index < pack.wordPairs.size - 1) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        HorizontalDivider(
                                            color = MaterialTheme.colorScheme.outline,
                                            thickness = 0.5.dp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                    }
                                }

                                // Show hint if no words
                                if (pack.wordPairs.isEmpty()) {
                                    Text(
                                        text = "No words in this pack",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    )
                                }
                            }
                        }

                        // Always show compact summary when collapsed
                        if (!expanded) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${pack.wordPairs.size} Words: " + pack.wordPairs.take(3).joinToString { it.citizenWord } + if (pack.wordPairs.size > 3) ", ..." else "",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            }
        }

        if (showCreateDialog) {
            val packToEdit = wordPacks.firstOrNull { it.id == packToEditId }
            CreateCustomPackDialog(
                onDismiss = { showCreateDialog = false },
                onCreate = { name, desc, pairs ->
                    if (packToEditId == null) {
                        wordPackViewModel.createCustomPack(name, desc, pairs)
                    } else {
                        wordPackViewModel.createCustomPack(name, desc, pairs, packToEditId)
                        packToEditId = null
                    }
                    showCreateDialog = false
                },
                initialName = packToEdit?.name ?: "",
                initialDescription = packToEdit?.description ?: "",
                initialWordPairs = packToEdit?.wordPairs ?: emptyList()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCustomPackDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, List<WordPair>) -> Unit,
    initialName: String = "",
    initialDescription: String = "",
    initialWordPairs: List<WordPair> = emptyList()
) {
    var packName by remember { mutableStateOf(initialName) }
    var packDesc by remember { mutableStateOf(initialDescription) }
    var wordPairs by remember { mutableStateOf(initialWordPairs) }
    var newCitizenWord by remember { mutableStateOf("") }
    var newImposterWord by remember { mutableStateOf("") }
    var newHint by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Custom Pack") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = packName,
                    onValueChange = { packName = it },
                    label = { Text("Pack Name (e.g. Anime)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = packDesc,
                    onValueChange = { packDesc = it },
                    label = { Text("Description") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Word pairs list
                Text(
                    text = "Word Pairs (${wordPairs.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                if (wordPairs.isEmpty()) {
                    Text(
                        text = "No word pairs added yet",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(wordPairs, key = { it.hashCode() }) { pair ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Citizen: ${pair.citizenWord}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Imposter: ${pair.imposterWord}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    if (pair.hint.isNotBlank()) {
                                        Text(
                                            text = "Hint: ${pair.hint}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        wordPairs = wordPairs.filterNot { it == pair }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Remove",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Add new word pair
                Text(
                    text = "Add New Word Pair",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = newCitizenWord,
                    onValueChange = { newCitizenWord = it },
                    label = { Text("Citizen Word") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = newImposterWord,
                    onValueChange = { newImposterWord = it },
                    label = { Text("Imposter Word") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = newHint,
                    onValueChange = { newHint = it },
                    label = { Text("Hint (optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (newCitizenWord.isNotBlank() && newImposterWord.isNotBlank()) {
                            val newPair = WordPair(
                                citizenWord = newCitizenWord,
                                imposterWord = newImposterWord,
                                hint = newHint
                            )
                            wordPairs = wordPairs + newPair
                            newCitizenWord = ""
                            newImposterWord = ""
                            newHint = ""
                        }
                    },
                    enabled = newCitizenWord.isNotBlank() && newImposterWord.isNotBlank()
                ) {
                    Text("Add Word Pair")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val nonEmptyPairs = wordPairs.filter { it.citizenWord.isNotBlank() && it.imposterWord.isNotBlank() }
                    if (packName.isNotBlank() && nonEmptyPairs.isNotEmpty()) {
                        onCreate(packName, packDesc, nonEmptyPairs)
                    }
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
