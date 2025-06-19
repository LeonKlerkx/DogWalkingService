package com.example.dogwalkingservice.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.dogwalkingservice.ui.appointment.AppointmentAddDestination
import com.example.dogwalkingservice.ui.appointment.AppointmentAddScreen
import com.example.dogwalkingservice.ui.appointment.AppointmentDetailsDestination
import com.example.dogwalkingservice.ui.appointment.AppointmentDetailsScreen
import com.example.dogwalkingservice.ui.appointment.AppointmentOverviewDestination
import com.example.dogwalkingservice.ui.appointment.AppointmentOverviewScreen
import com.example.dogwalkingservice.ui.dog.DogAddScreen
import com.example.dogwalkingservice.ui.dog.DogAddScreenDestination
import com.example.dogwalkingservice.ui.dog.DogEditScreen
import com.example.dogwalkingservice.ui.dog.DogEditScreenDestination
import com.example.dogwalkingservice.ui.dog.DogOverviewDestination
import com.example.dogwalkingservice.ui.dog.DogOverviewScreen
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
                    navController.navigate(DogOverviewDestination.route)
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
                    navController.navigate(AppointmentOverviewDestination.route)
                }
            )
        }

        // Password screen.
        composable(route = PasswordDestination.route) {
            PasswordScreen(
                navigateBack = {
                    // Navigate to the previous screen of the BackStack.
                    navController.navigateUp()
                }
            )
        }

        // Register screen.
        composable(route = RegistrationDestination.route) {
            RegistrationScreen(
                navigateBack = {
                    // Navigate to the LoginScreen in the BackStack.
                    navController.navigateUp()
                },
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
                navigateBack = {
                    // Navigate to the HomeScreen<...> screen of the BackStack.
                    navController.navigateUp()
                },
                navigateToEditPersonalData = {
                    navController.navigate(EditPersonalDataDestination.route)
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

        // Edit Personal data screen.
        composable(route = EditPersonalDataDestination.route) {
            EditPersonalDataScreen(
                navigateBack = {
                    // Navigate to the SettingOverviewScreen in the BackStack.
                    navController.navigateUp()
                }
            )
        }

        // Overview dog screen.
        composable(route = DogOverviewDestination.route) {
            DogOverviewScreen(
                navigateToAddDog = {
                    navController.navigate(DogAddScreenDestination.route)
                },
                navigateToEditDog = {
                    navController.navigate("${DogEditScreenDestination.route}/${it}")
                },
                navigateBack = {
                    // Navigate to the SettingOverviewScreen in the BackStack.
                    navController.navigateUp()
                }
            )
        }

        // Add dog screen.
        composable(route = DogAddScreenDestination.route) {
            DogAddScreen(
                navigateBack = {
                    // Navigate to the DogOverviewScreen in the BackStack.
                    navController.navigateUp()
                }
            )
        }

        // Edit dog screen.
        composable(
            route = DogEditScreenDestination.routeWithChipnummer,
            arguments = listOf(navArgument(DogEditScreenDestination.chipnummer) {
                type = NavType.StringType
            })
            ) {
            DogEditScreen(
                navigateBack = {
                    // Navigate to the DogOverviewScreen in the BackStack.
                    navController.navigateUp()
                }
            )
        }

        // Overview appointment screen.
        composable(route = AppointmentOverviewDestination.route) {
            AppointmentOverviewScreen(
                navigateBack = {
                    // Navigate to the HomeScreenDogSitter in the BackStack.
                    navController.navigateUp()
                },
                navigateToAddAppointment = {
                    navController.navigate(AppointmentAddDestination.route)
                },
                navigateToDetailsAppointment = {
                    navController.navigate("${AppointmentDetailsDestination.route}/${it}")
                }
            )
        }

        // Add appointment screen.
        composable(route = AppointmentAddDestination.route) {
            AppointmentAddScreen(
                navigateBack = {
                    // Navigate to the AppointmentOverviewScreen in the BackStack.
                    navController.navigateUp()
                }
            )
        }

        // Details appointment screen.
        composable(
            route = AppointmentDetailsDestination.routeWithAppointmentId,
            arguments = listOf(navArgument(AppointmentDetailsDestination.appointmentId) {
                type = NavType.IntType
            })
        ) {
            AppointmentDetailsScreen(
                navigateBack = {
                    // Navigate to the AppointmentOverviewScreen in the BackStack.
                    navController.navigateUp()
                }
            )
        }
    }
}