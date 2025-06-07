package com.example.dogwalkingservice.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogwalkingservice.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeScreenOwnerViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    /**
     * Get the email address from the DataStore.
     */

    val uiStateEmailAddress: StateFlow<HomeScreenUiState> =
        userPreferencesRepository.getEmailAddress.map { emailAddress ->
            HomeScreenUiState(emailAddress = emailAddress, "")
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeScreenUiState("", "")
            )

    /**
     * Get the user role from the DataStore.
     */
    val uiStateUserRole: StateFlow<HomeScreenUiState> =
        userPreferencesRepository.getUserRole.map { userRole ->
            HomeScreenUiState("", userRole = userRole)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeScreenUiState("", "")
            )

}

/**
 * UI state for [HomeScreenOwner] screen.
 */
data class HomeScreenUiState(
    val emailAddress: String,
    val userRole: String
)