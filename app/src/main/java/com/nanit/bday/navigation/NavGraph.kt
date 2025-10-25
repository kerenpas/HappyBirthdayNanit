package com.nanit.bday.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nanit.bday.presentation.bdaycard.BirthdayScreen
import com.nanit.bday.presentation.connection.ConnectionScreen

sealed class Screen(val route: String) {
    data object ConnectionScreen : Screen("connection")

    data object BirthdayScreen : Screen("birthday")
}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier) {
  NavHost(
        navController = navController,
        startDestination = Screen.ConnectionScreen.route
    ) {

      composable(Screen.ConnectionScreen.route) {
         ConnectionScreen (
             onNavigateToBirthday = {
                 navController.navigate(Screen.BirthdayScreen.route)
             }
         )
      }


      composable(Screen.BirthdayScreen.route) {
          BirthdayScreen(
              onNavigateBack = {
                  navController.popBackStack()
              }
          )

      }

    }
}