package com.madar.madartask.common.ui.navigation

import com.madar.madartask.ui.input.InputScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.madar.madartask.common.constants.AppConstants
import com.madar.madartask.ui.display.DisplayScreen

sealed class Screen(val route: String) {
    object Input : Screen(AppConstants.INPUT_SCREEN_ROUTE)
    object Display : Screen(AppConstants.DISPLAY_SCREEN_ROUTE)
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Input.route
    ) {
        composable(route = Screen.Input.route) {
            InputScreen(
                onNavigateToDisplay = {
                    navController.navigate(Screen.Display.route)
                }
            )
        }

        composable(route = Screen.Display.route) {
            DisplayScreen(
                onNavigateToInput = {
                    navController.navigate(Screen.Input.route)
                }
            )
        }
    }
}