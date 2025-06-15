package com.example.dogwalkingservice.ui.dog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogwalkingservice.data.Hond
import com.example.dogwalkingservice.data.HondenRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DogEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val hondenRepository: HondenRepository
) : ViewModel() {

    /**
     * Get the chip number of the selected dog.
     * [checkNotNull] compares if the value is not null.
     * [SavedStateHandle] is a class that saved data through key-value.
     * [DogEditScreenDestination.chipnummer] this is the value of the selected dog from the LazyColumn.
     */
    private val chipnummer: String = checkNotNull(savedStateHandle[DogEditScreenDestination.chipnummer])

    /**
     * When the [DogEditViewModel] will be run, the owner has opened the screen where you
     *  can edit a dog.
     *
     * In this screen, the dog details have to display in the screen, so the owner can edit this.
     * This must be done immediately and once, so this have to execute in the init block.
     */
    init {
        viewModelScope.launch {
            // Get the object of the dog from the chip number.
            dogUiState = hondenRepository.getDogByPrimaryKey(chipnummer)
                // Filter nullable values
                .filterNotNull()
                /* Get the first dog of the list (is always the first,
                   because the chip number is the PK) */
                .first()
                /* Convert the [Hond] to a [DogEditUiState],
                   so the user can read and manipulate the data in the DogEditScreen.kt */
                .toDogUiState(true)
        }
    }


    /**
     * Holds the UI state of the edit dog.
     */
    var dogUiState by mutableStateOf(DogEditUiState())
        private set

    /**
     * Update the name of the dog.
     */
    fun updateDogName(dogName: String) {
        dogUiState = dogUiState.copy(
            hondnaam = dogName
        )
    }

    /**
     * Update the breed name of the dog.
     */
    fun updateHondenras(hondenras: String) {
        dogUiState = dogUiState.copy(
            hondenras = hondenras
        )
    }

    /**
     * Check if the owner filled in the name and the breed of the dog.
     */
    private fun validateUserInput(uiState: DogEditUiState = dogUiState): Boolean {
        return with(uiState) {
            hondnaam.isNotBlank() && hondenras.isNotBlank()
        }
    }

    /**
     * Edit an existing dog object into the Database.
     */
    suspend fun saveDog(): String {
        if (validateUserInput()) {
            // Update the dog into the Database.
            hondenRepository.updateDog(dogUiState.toDog())

            return "De hond is succesvol gewijzigd."
        }
        else {
            throw IllegalArgumentException("Niet alle velden zijn ingevuld!")
        }
    }

    /**
     * Delete the dog into the Database.
     */
    suspend fun deleteDog() {
        // Controleren of de hond afbeeldingen heeft.

        // Controleren of de hond is aangemeld voor een afspraak.

        // Hond verwijderen.
    }
}

/**
 * UI State for the [DogEditViewModel].
 */
data class DogEditUiState(
    val chipnummer: String = "",
    val hondnaam: String = "",
    val hondenras: String = "",
    val eigenaar: String = "",
    val hondItem: Hond = Hond("", "", "", "")
)

/**
 * Extension function to convert a [Hond] to a [DogEditUiState].
 */
fun Hond.toDogUiState(isEntryValid: Boolean = true) : DogEditUiState = DogEditUiState(
    chipnummer = chipNummer,
    hondnaam = hondnaam,
    hondenras = hondenras,
    eigenaar = eigenaar
)

/**
 * Extension function to convert a [DogEditUiState] to a [Hond] object
 * to edit the dog into the Database
 */
fun DogEditUiState.toDog(): Hond = Hond(
    chipNummer = chipnummer,
    hondnaam = hondnaam,
    hondenras = hondenras,
    eigenaar = eigenaar

)