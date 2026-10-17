package com.example.impostergame.domain.model

data class WordPair(
    val citizenWord: String,
    val imposterWord: String = "",
    val hint: String = ""
)

data class WordPack(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val iconName: String = "Star",
    val wordPairs: List<WordPair>,
    val isCustom: Boolean = false
)
