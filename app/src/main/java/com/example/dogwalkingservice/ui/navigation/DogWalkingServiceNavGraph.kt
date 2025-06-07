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
import com.example.dogwalkingservice.ui.password.PasswordDestination
import com.example.dogwalkingservice.ui.password.PasswordScreen
import com.example.dogwalkingservice.ui.registration.RegistrationDestination
import com.example.dogwalkingservice.ui.registration.RegistrationScreen
import com.example.dogwalkingservice.ui.settings.EditPersonalDataDestination
import com.example.dogwalkingservice.ui.settings.EditPersonalDataScreen
import com.example.dogwalkingservice.ui.settings.SettingsOverviewDestination
import com.example.dogwalkingservice.ui.settings.SettingsOverviewScreen

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
        // Login screen.
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
                    navController.navigate(PasswordDestination.route)
                },
                navigateToRegisterUser = {
                    navController.navigate(RegistrationDestination.route)
                }
            )
        }

        // Homepage owner.
        composable(route = HomeScreenOwnerDestination.route) {
            HomeScreenOwner(
                navigateToSettingScreen = {
                    navController.navigate(SettingsOverviewDestination.route)
                },
                navigateToPictureDogScreen = {

                },
                navigateToOverviewDogScreen = {

                },
                navigateToAddDogToAnAppointment = {

                }
            )
        }

        // Homepage dog sitter.
        composable(route = HomeScreenDogSitterDestination.route) {
            HomeScreenDogSitter(
                navigateToSettingScreen = {
                    navController.navigate(SettingsOverviewDestination.route)
                },
                navigateToAppointmentScreen = {

                }
            )
        }

        // Password screen.
        composable(route = PasswordDestination.route) {
            PasswordScreen(
                navigateToLoginUser = {}
            )
        }

        // Register screen.
        composable(route = RegistrationDestination.route) {
            RegistrationScreen(
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
                }
            )
        }

        // Settings overview screen.
        composable(route = SettingsOverviewDestination.route) {
            SettingsOverviewScreen(
                navigateToEditPersonalData = {

                },
                navigateToEditPassword = {
                    navController.navigate(PasswordDestination.route)
                },
                logOut = {
                    // Open the LoginScreen where the user can login again.
                    navController.navigate(LoginDestination.route) {
                        // Remove all previous screens, so the user has to sign in to use the application.
                        popUpTo(0)
                    }
                }
            )
        }

        // Edit personal data screen
        composable(route = EditPersonalDataDestination.route) {
            EditPersonalDataScreen()
        }
    }
}