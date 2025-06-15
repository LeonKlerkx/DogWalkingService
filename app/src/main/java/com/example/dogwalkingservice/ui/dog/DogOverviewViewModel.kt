package com.example.dogwalkingservice.ui.dog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogwalkingservice.data.Gebruiker
import com.example.dogwalkingservice.data.GebruikersRepository
import com.example.dogwalkingservice.data.Hond
import com.example.dogwalkingservice.data.HondenRepository
import com.example.dogwalkingservice.data.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DogOverviewViewModel(
    private val hondenRepository: HondenRepository,
    private val gebruikersRepository: GebruikersRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    /**
     * Get the email address of the signed in owner.
     */
    val getEmailAddress: StateFlow<DogUiState> =
        userPreferencesRepository.getEmailAddress
            .map {  emailAddress ->
            DogUiState(listOf(), emailAddress = emailAddress)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DogUiState(listOf(), "")
            )

    /**
     * Receive the whole object from the user through the saved email address.
     */
    fun getOwnerName(emailAddress: String): Flow<Gebruiker> =
        gebruikersRepository.getUserbyEmailAddress(emailAddress).filterNotNull()

    /**
     * Get the dogs from the owner.
     */
    fun getDogs(eigenaar: String): Flow<List<Hond>> =
        hondenRepository.getAllDogsFromOwner(eigenaar)
}

/**
 * UI State for Dog overview screen.
 */
data class DogUiState(
    val dogList: List<Hond> = listOf(),
    val emailAddress: String = "",
    val gebruikersnaam: String = "",
    val eigenaar: Gebruiker = Gebruiker("", "", "", "", "", "", "", "", "", "")
)