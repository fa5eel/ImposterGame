package com.example.impostergame.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HowToPlayScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("How to Play", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RuleSectionCard(
                title = "1. Game Goal",
                content = "Citizens know the secret word. The Imposter knows NOTHING (or receives a fake word). Citizens must identify the Imposter without giving away the secret word, while the Imposter must blend in!"
            )

            RuleSectionCard(
                title = "2. Pass & Reveal",
                content = "Pass the phone to each player one by one. Press and hold the card privately to see your secret role and word. Do not let other players see your screen!"
            )

            RuleSectionCard(
                title = "3. Discussion",
                content = "Take turns describing the word subtly. Example: If the word is 'Pizza', a Citizen might say 'I like eating this on weekends'. If the Imposter hears this, they must guess a clever response!"
            )

            RuleSectionCard(
                title = "4. Voting & Winning",
                content = "When the discussion timer runs out, all players vote for who they suspect is the Imposter. If the Imposter is voted out, Citizens WIN! If a Citizen is voted out or no consensus is reached, the Imposter WINS!"
            )

            RuleSectionCard(
                title = "💡 Strategy Tips",
                content = "• For Citizens: Be creative with clues so the Imposter cannot guess the exact word.\n• For Imposters: Listen carefully to early clues and nod along confidently!"
            )
        }
    }
}

@Composable
fun RuleSectionCard(title: String, content: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = content, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
