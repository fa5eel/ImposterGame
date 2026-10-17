package com.example.impostergame.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.impostergame.ui.screens.DiscussionScreen
import com.example.impostergame.ui.screens.HomeScreen
import com.example.impostergame.ui.screens.HowToPlayScreen
import com.example.impostergame.ui.screens.ResultsScreen
import com.example.impostergame.ui.screens.RoleRevealScreen
import com.example.impostergame.ui.screens.SettingsScreen
import com.example.impostergame.ui.screens.SetupScreen
import com.example.impostergame.ui.screens.VotingScreen
import com.example.impostergame.ui.screens.WordPacksScreen
import com.example.impostergame.ui.viewmodel.GameViewModel
import com.example.impostergame.ui.viewmodel.SettingsViewModel
import com.example.impostergame.ui.viewmodel.WordPackViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    gameViewModel: GameViewModel = hiltViewModel(),
    wordPackViewModel: WordPackViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onStartGame = {
                    gameViewModel.startNewGameSetup()
                    navController.navigate(Screen.Setup.route)
                },
                onOpenWordPacks = { navController.navigate(Screen.WordPacks.route) },
                onOpenHowToPlay = { navController.navigate(Screen.HowToPlay.route) },
                onOpenSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.Setup.route) {
            SetupScreen(
                gameViewModel = gameViewModel,
                settingsViewModel = settingsViewModel,
                wordPackViewModel = wordPackViewModel,
                onBack = { navController.popBackStack() },
                onStartGame = { navController.navigate(Screen.RoleReveal.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.RoleReveal.route) {
            RoleRevealScreen(
                gameViewModel = gameViewModel,
                onAllRolesRevealed = { navController.navigate(Screen.Discussion.route) }
            )
        }

        composable(Screen.Discussion.route) {
            DiscussionScreen(
                gameViewModel = gameViewModel,
                onProceedToVoting = { navController.navigate(Screen.Voting.route) }
            )
        }

        composable(Screen.Voting.route) {
            VotingScreen(
                gameViewModel = gameViewModel,
                onShowResults = { navController.navigate(Screen.Results.route) }
            )
        }

        composable(Screen.Results.route) {
            ResultsScreen(
                gameViewModel = gameViewModel,
                onPlayAgain = { navController.navigate(Screen.RoleReveal.route) },
                onHome = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                }
            )
        }

        composable(Screen.WordPacks.route) {
            WordPacksScreen(
                wordPackViewModel = wordPackViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.HowToPlay.route) {
            HowToPlayScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                settingsViewModel = settingsViewModel,
                wordPackViewModel = wordPackViewModel
            )
        }
    }
}