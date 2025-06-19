package com.example.dogwalkingservice.ui.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogwalkingservice.data.Afspraak
import com.example.dogwalkingservice.data.AfsprakenRepository
import com.example.dogwalkingservice.data.Gebruiker
import com.example.dogwalkingservice.data.GebruikersRepository
import com.example.dogwalkingservice.data.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AppointmentOverviewViewModel(
    private val afsprakenRepository: AfsprakenRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val gebruikersRepository: GebruikersRepository
) : ViewModel() {

    /**
     * Get the saved email address from the DataStore.
     */
    val getEmailaddressFromDatastore =
        userPreferencesRepository.getEmailAddress
            .map { emailaddress ->
                AppointmentUiState(oppasser = emailaddress)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = AppointmentUiState()
            )

    /**
     * Get the whole dog sitter object through the saved email address from the DataStore.
     */
    fun getDogSitterObject(emailAddress: String): Flow<Gebruiker> =
        gebruikersRepository.getUserbyEmailAddress(emailAddress).filterNotNull()
    /**
     * Get all appointment from the signed in dog sitter.
     */
    fun getAllAppointsmentFromADogSitter(oppasser: String): Flow<List<Afspraak>> =
        afsprakenRepository.getAllAppointmentByUser(oppasser)
}