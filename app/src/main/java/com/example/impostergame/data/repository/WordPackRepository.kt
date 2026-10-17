package com.example.impostergame.data.repository

import com.example.impostergame.domain.model.WordPack
import com.example.impostergame.domain.model.WordPair
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class WordPackRepository @Inject constructor() {

    private val defaultPacks = listOf(
        WordPack(
            id = "default",
            name = "Default Pack",
            category = "Default",
            description = "A default word pack.",
            iconName = "Edit",
            wordPairs = emptyList(),
            isCustom = false
        ),
        WordPack(
            id = "everyday_objects",
            name = "Everyday Objects",
            category = "Preset",
            description = "Common household and office items",
            iconName = "Home",
            wordPairs = listOf(
                WordPair("Chair", "Table", "Has four legs"),
                WordPair("Lamp", "Light", "Provides illumination"),
                WordPair("Book", "Paper", "Used for reading"),
                WordPair("Cup", "Mug", "Holds beverages"),
                WordPair("Pen", "Pencil", "Writing instrument"),
                WordPair("Clock", "Watch", "Tells time"),
                WordPair("Door", "Window", "Part of a building"),
                WordPair("Keyboard", "Mouse", "Computer peripheral"),
                WordPair("Phone", "Tablet", "Electronic device"),
                WordPair("Blanket", "Pillow", "Bedroom item"),
                WordPair("Fork", "Knife", "Used for eating"),
                WordPair("Plate", "Bowl", "Holds food"),
                WordPair("Napkin", "Tissue", "For cleaning up"),
                WordPair("Lamp", "Switch", "Controls lighting"),
                WordPair("Rug", "Carpet", "Floor covering")
            ),
            isCustom = false
        ),
        WordPack(
            id = "sports",
            name = "Sports",
            category = "Preset",
            description = "Various sports and athletic activities",
            iconName = "Sports_Baseball",
            wordPairs = listOf(
                WordPair("Soccer", "Football", "Played with a round ball"),
                WordPair("Basketball", "Hoop", "Played on a court"),
                WordPair("Tennis", "Racket", "Played with a net"),
                WordPair("Swimming", "Pool", "Done in water"),
                WordPair("Running", "Marathon", "Track and field event"),
                WordPair("Cycling", "Bicycle", "Involves pedaling"),
                WordPair("Baseball", "Bat", "Played on a diamond"),
                WordPair("Golf", "Club", "Played on a course"),
                WordPair("Volleyball", "Net", "Played with hands"),
                WordPair("Boxing", "Gloves", "Combat sport"),
                WordPair("Hockey", "Stick", "Played on ice"),
                WordPair("Cricket", "Bat", "Played with a ball"),
                WordPair("Skiing", "Snow", "Winter sport"),
                WordPair("Surfing", "Board", "Ocean sport"),
                WordPair("Yoga", "Mat", "Flexibility exercise")
            ),
            isCustom = false
        ),
        WordPack(
            id = "foods",
            name = "Foods",
            category = "Preset",
            description = "Various foods and beverages",
            iconName = "Restaurant",
            wordPairs = listOf(
                WordPair("Pizza", "Burger", "Italian dish"),
                WordPair("Sushi", "Rice", "Japanese cuisine"),
                WordPair("Apple", "Banana", "Fruit"),
                WordPair("Chicken", "Beef", "Meat"),
                WordPair("Bread", "Toast", "Bakery item"),
                WordPair("Milk", "Cheese", "Dairy product"),
                WordPair("Chocolate", "Candy", "Sweet treat"),
                WordPair("Soup", "Salad", "Liquid dish"),
                WordPair("Eggs", "Omelette", "Breakfast food"),
                WordPair("Pasta", "Noodles", "Italian staple"),
                WordPair("Steak", "Grill", "Cooked meat"),
                WordPair("Salad", "Lettuce", "Healthy dish"),
                WordPair("Cake", "Frosting", "Sweet dessert"),
                WordPair("Coffee", "Mug", "Hot beverage"),
                WordPair("Rice", "Chopsticks", "Asian staple")
            ),
            isCustom = false
        ),
        WordPack(
            id = "animals",
            name = "Animals",
            category = "Preset",
            description = "Various animals and creatures",
            iconName = "Pets",
            wordPairs = listOf(
                WordPair("Dog", "Cat", "Common pet"),
                WordPair("Lion", "Tiger", "Big cat"),
                WordPair("Elephant", "Giraffe", "Large mammal"),
                WordPair("Eagle", "Hawk", "Bird of prey"),
                WordPair("Shark", "Dolphin", "Ocean creature"),
                WordPair("Snake", "Lizard", "Reptile"),
                WordPair("Bear", "Wolf", "Forest animal"),
                WordPair("Frog", "Toad", "Amphibian"),
                WordPair("Butterfly", "Bee", "Insect"),
                WordPair("Horse", "Cow", "Farm animal"),
                WordPair("Monkey", "Banana", "Primate animal"),
                WordPair("Penguin", "Fish", "Bird that swims"),
                WordPair("Tiger", "Stripes", "Big cat with pattern"),
                WordPair("Owl", "Mouse", "Nocturnal bird"),
                WordPair("Dolphin", "Water", "Intelligent marine mammal")
            ),
            isCustom = false
        ),
        WordPack(
            id = "colors",
            name = "Colors",
            category = "Preset",
            description = "Various colors and shades",
            iconName = "Palette",
            wordPairs = listOf(
                WordPair("Red", "Blue", "Primary color"),
                WordPair("Green", "Yellow", "Secondary color"),
                WordPair("Purple", "Orange", "Mixed color"),
                WordPair("Black", "White", "Neutral color"),
                WordPair("Pink", "Brown", "Light/dark shade"),
                WordPair("Gray", "Silver", "Metallic tone"),
                WordPair("Gold", "Bronze", "Precious metal"),
                WordPair("Navy", "Marine", "Dark blue shade"),
                WordPair("Violet", "Indigo", "Purple shade"),
                WordPair("Turquoise", "Teal", "Blue-green color"),
                WordPair("Silver", "Gray", "Metallic color"),
                WordPair("Lavender", "Purple", "Light purple color"),
                WordPair("Olive", "Green", "Dark yellow-green color"),
                WordPair("Coral", "Orange", "Pink-orange color"),
                WordPair("Indigo", "Blue", "Deep blue color")
            ),
            isCustom = false
        )
    )

    private val customPacks = mutableListOf<WordPack>()
    private val _allPacksFlow = MutableStateFlow(defaultPacks)
    val allPacks: StateFlow<List<WordPack>> = _allPacksFlow.asStateFlow()

    init {
        _allPacksFlow.value = defaultPacks + customPacks
    }

    fun getPackById(id: String): WordPack? {
        if (id.isNullOrBlank()) {
            return null
        }

        // First try to find in all packs (custom + default)
        val pack = _allPacksFlow.value.firstOrNull { it.id == id }
        if (pack != null) {
            return pack
        }

        // If not found, try to find in default packs only
        return defaultPacks.firstOrNull { it.id == id }
    }

    fun addCustomPack(pack: WordPack) {
        customPacks.add(pack)
        _allPacksFlow.value = defaultPacks + customPacks
    }

    fun deleteCustomPack(packId: String) {
        customPacks.removeAll { it.id == packId }
        _allPacksFlow.value = defaultPacks + customPacks
    }

    fun updateCustomPack(pack: WordPack) {
        val index = customPacks.indexOfFirst { it.id == pack.id }
        if (index != -1) {
            customPacks[index] = pack
            _allPacksFlow.value = defaultPacks + customPacks
        }
    }
}