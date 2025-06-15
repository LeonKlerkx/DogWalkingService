package com.example.dogwalkingservice.ui.dog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogwalkingservice.data.Gebruiker
import com.example.dogwalkingservice.data.GebruikersRepository
import com.example.dogwalkingservice.data.Hond
import com.example.dogwalkingservice.data.HondenRepository
import com.example.dogwalkingservice.data.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DogAddViewModel(
    private val hondenRepository: HondenRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val gebruikersRepository: GebruikersRepository
) : ViewModel() {

    var dogUiState by mutableStateOf(DogAddUiState())
        private set

    /**
     * Update the chip number of the dog.
     */
    fun updateChipNumber(chipNumber: String) {
        dogUiState = dogUiState.copy(
            chipnummer = chipNumber
        )
    }

    /**
     * Update the name of the dog.
     */
    fun updateDogName(dogName: String) {
        dogUiState = dogUiState.copy(
            hondnaam = dogName
        )
    }

    /**
     * Update the dog breed.
     */
    fun updateDogBreed(dogBreed: String) {
        dogUiState = dogUiState.copy(
            hondenras = dogBreed
        )
    }
    
    /**
     * Validate the user input.
     */
    private fun validateUserInput(uiState: DogAddUiState = dogUiState): Boolean {
        return with(uiState) {
            chipnummer.isNotBlank() && hondnaam.isNotBlank() && hondenras.isNotBlank()

                    /* This value will be filled in automatically though the DataStore,
                       so the user does not have rights on. */
                    && eigenaar.isNotBlank()
        }
    }

    /**
     * Get the saved email address from the DataStore.
     */
    val getEmailaddressFromDatastore =
        userPreferencesRepository.getEmailAddress
            .map { emailaddress ->
                DogAddUiState(ownerEmailaddressDataStore = emailaddress)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DogAddUiState()
            )

    /**
     * Get the whole owner object through the saved email address from the DataStore.
     */
    fun getOwnerObject(emailAddress: String): Flow<Gebruiker> =
        gebruikersRepository.getUserbyEmailAddress(emailAddress).filterNotNull()

    /**
     * Save the dog into the Database.
     */
    suspend fun saveDog(usernameOwner: String): String {

        // Save the username of the owner in the UI State.
        dogUiState = dogUiState.copy(eigenaar = usernameOwner)

        if (validateUserInput()) {

            // Check if the filled in chip number is already exists in the Database (= PK).
            val checkChipNumberIsExists = hondenRepository
                .getDogByPrimaryKey(dogUiState.chipnummer)
                .firstOrNull()

            if (checkChipNumberIsExists == null) {

                // Add dog into the Database.
                hondenRepository.insertDog(dogUiState.insertDog())

                return "De hond is succesvol toegevoegd."
            }
            else {
                throw IllegalArgumentException("Het chipnummer bestaat al! Vul een ander chipnummer in.")
            }
        }
        else {
            throw IllegalArgumentException("Niet alle velden zijn ingevuld!")
        }
    }
}

/**
 * Ui State for [DogAddViewModel]
 */
data class DogAddUiState(
    val chipnummer: String = "",
    val hondnaam: String = "",
    val hondenras: String = "",
    val ownerEmailaddressDataStore: String = "",
    val eigenaar: String = "",
)

/**
 * Extension function to convert the [DogAddUiState] object to a [Hond] object.
 */
fun DogAddUiState.insertDog(): Hond = Hond(
    chipNummer = chipnummer,
    hondnaam = hondnaam,
    hondenras = hondenras,
    eigenaar = eigenaar
)