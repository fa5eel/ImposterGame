package com.example.impostergame.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.impostergame.data.repository.WordPackRepository
import com.example.impostergame.domain.model.WordPack
import com.example.impostergame.domain.model.WordPair
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import java.util.UUID
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class WordPackViewModel @Inject constructor(
    private val wordPackRepository: WordPackRepository
) : ViewModel() {

    val wordPacks: StateFlow<List<WordPack>> = wordPackRepository.allPacks

    fun createCustomPack(
        name: String,
        description: String,
        pairs: List<WordPair>,
        packId: String? = null
    ) {
        if (name.isBlank() || pairs.isEmpty()) return
        val pack = WordPack(
            id = packId ?: UUID.randomUUID().toString(),
            name = name,
            category = "Custom",
            description = description,
            iconName = "Edit",
            wordPairs = pairs,
            isCustom = true
        )
        if (packId == null) {
            wordPackRepository.addCustomPack(pack)
        } else {
            wordPackRepository.updateCustomPack(pack)
        }
    }

    fun deleteCustomPack(id: String) {
        wordPackRepository.deleteCustomPack(id)
    }
}