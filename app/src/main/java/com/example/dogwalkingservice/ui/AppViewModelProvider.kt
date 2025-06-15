package com.example.dogwalkingservice.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dogwalkingservice.DogWalkingServiceApplication
import com.example.dogwalkingservice.ui.dog.DogAddViewModel
import com.example.dogwalkingservice.ui.dog.DogEditViewModel
import com.example.dogwalkingservice.ui.dog.DogOverviewViewModel
import com.example.dogwalkingservice.ui.home.HomeScreenOwnerViewModel
import com.example.dogwalkingservice.ui.login.LoginViewModel
import com.example.dogwalkingservice.ui.password.PasswordViewModel
import com.example.dogwalkingservice.ui.registration.RegistrationViewModel
import com.example.dogwalkingservice.ui.settings.EditPersonalDataViewModel
import com.example.dogwalkingservice.ui.settings.SettingsOverviewViewModel

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
        // Initializer for SettingsOverviewViewModel.
        initializer {
            SettingsOverviewViewModel(
                userPreferencesRepository = dogWalkingServiceApplication().userPreferencesRepository
            )
        }
        // Initializer for EditPersonalDataViewModel.
        initializer {
            EditPersonalDataViewModel(
                gebruikersRepository = dogWalkingServiceApplication().container.gebruikersRepository,
                userPreferencesRepository = dogWalkingServiceApplication().userPreferencesRepository
            )
        }
        // Intitializer for DogOverviewViewModel.
        initializer {
            DogOverviewViewModel(
                hondenRepository = dogWalkingServiceApplication().container.hondenRepository,
                gebruikersRepository = dogWalkingServiceApplication().container.gebruikersRepository,
                userPreferencesRepository = dogWalkingServiceApplication().userPreferencesRepository
            )
        }
        // Initializer for DogEditViewModel.
        initializer {
            DogEditViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                hondenRepository = dogWalkingServiceApplication().container.hondenRepository,
                afbeeldingHondenRepository = dogWalkingServiceApplication().container.afbeeldingHondenRepository,
                aanmeldenHondenRepository = dogWalkingServiceApplication().container.aanmeldenHondenRepository
            )
        }
        // Initializer for DogAddViewModel.
        initializer {
            DogAddViewModel(
                hondenRepository = dogWalkingServiceApplication().container.hondenRepository,
                userPreferencesRepository = dogWalkingServiceApplication().userPreferencesRepository,
                gebruikersRepository = dogWalkingServiceApplication().container.gebruikersRepository
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