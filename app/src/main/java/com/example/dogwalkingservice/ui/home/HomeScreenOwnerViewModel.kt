package com.example.dogwalkingservice.ui.home

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dogwalkingservice.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeScreenOwnerViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

  /*  var homeScreenUiState by mutableStateOf(HomeScreenUiState())
        private set*/

/*
    */
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


    /**
     * Delete the email address and the user role from the DataStore.
     */
    fun deleteEmailAddressAndUserRoleInDataStore() {
        viewModelScope.launch {
            userPreferencesRepository.deleteEmailaddressAndUserRole()
        }
    }
}

/**
 * UI state for [HomeScreenOwner] screen.
 */
data class HomeScreenUiState(
    val emailAddress: String,
    val userRole: String
)