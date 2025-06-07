package com.example.dogwalkingservice.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dogwalkingservice.DogWalkingServiceApplication
import com.example.dogwalkingservice.ui.home.HomeScreenOwnerViewModel
import com.example.dogwalkingservice.ui.login.LoginViewModel
import com.example.dogwalkingservice.ui.password.PasswordViewModel
import com.example.dogwalkingservice.ui.registration.RegistrationViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for LoginViewModel.
        initializer {
            LoginViewModel(
                gebruikersRepository = dogWalkingServiceApplication().container.gebruikersRepository,
                userPreferencesRepository = dogWalkingServiceApplication().userPreferencesRepository
            )
        }
        // Initializer for PasswordViewModel.
        initializer {
            PasswordViewModel(
                gebruikersRepository = dogWalkingServiceApplication().container.gebruikersRepository,
                userPreferencesRepository = dogWalkingServiceApplication().userPreferencesRepository
            )
        }
        // Initializer for RegistrationViewModel.
        initializer {
            RegistrationViewModel(
                gebruikersRepository = dogWalkingServiceApplication().container.gebruikersRepository,
                userPreferencesRepository = dogWalkingServiceApplication().userPreferencesRepository
            )
        }
        // Initializer for HomeScreenOwnwerViewModel.
        initializer {
            HomeScreenOwnerViewModel(
                userPreferencesRepository = dogWalkingServiceApplication().userPreferencesRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [DogWalkingServiceApplication].
 */
fun CreationExtras.dogWalkingServiceApplication(): DogWalkingServiceApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as DogWalkingServiceApplication)