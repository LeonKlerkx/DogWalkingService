package com.example.dogwalkingservice.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogwalkingservice.data.UserPreferencesRepository
import kotlinx.coroutines.launch

class SettingsOverviewViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    /**
     * Delete the email address and the user role from the DataStore.
     */
    fun deleteEmailAddressAndUserRoleInDataStore() {
        viewModelScope.launch {
            userPreferencesRepository.deleteEmailaddressAndUserRole()
        }
    }
}