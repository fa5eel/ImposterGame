package com.example.impostergame.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Setup : Screen("setup")
    object RoleReveal : Screen("role_reveal")
    object Discussion : Screen("discussion")
    object Voting : Screen("voting")
    object Results : Screen("results")
    object WordPacks : Screen("word_packs")
    object HowToPlay : Screen("how_to_play")
    object Settings : Screen("settings")
}
