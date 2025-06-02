package com.example.dogwalkingservice.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dogwalkingservice.ui.home.HomeScreenDogSitter
import com.example.dogwalkingservice.ui.home.HomeScreenDogSitterDestination
import com.example.dogwalkingservice.ui.home.HomeScreenOwner
import com.example.dogwalkingservice.ui.home.HomeScreenOwnerDestination
import com.example.dogwalkingservice.ui.login.LoginDestination
import com.example.dogwalkingservice.ui.login.LoginScreen

@Composable
fun DogWalkingServiceNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = LoginDestination.route,
        modifier = modifier
    ) {
        // Inlog screen.
        composable(route = LoginDestination.route) {
            LoginScreen(
                navigateToStartPageOwner = {
                    /* Remove the LoginScreen from the BackStack,
                       so the login screen won't display when you go back. */
                    navController.popBackStack()

                    navController.navigate(HomeScreenOwnerDestination.route)
                },
                navigateToStartPageDogSitter = {
                    /* Remove the LoginScreen from the BackStack,
                       so the login screen won't display when you go back. */
                    navController.popBackStack()

                    navController.navigate(HomeScreenDogSitterDestination.route)
                },
                navigateToForgetPassword = {

                },
                navigateToRegisterUser = {}
            )
        }

        // Homepage owner.
        composable(route = HomeScreenOwnerDestination.route) {
            HomeScreenOwner(

            )
        }

        // Homepage dog sitter.
        composable(route = HomeScreenDogSitterDestination.route) {
            HomeScreenDogSitter(

            )
        }
    }
}