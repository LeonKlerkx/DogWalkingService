package com.example.dogwalkingservice.ui.dogsignin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogwalkingservice.data.AanmeldenHond
import com.example.dogwalkingservice.data.AanmeldenHondenRepository
import com.example.dogwalkingservice.data.Gebruiker
import com.example.dogwalkingservice.data.GebruikersRepository
import com.example.dogwalkingservice.data.Hond
import com.example.dogwalkingservice.data.HondenRepository
import com.example.dogwalkingservice.data.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class DogSignInOverviewViewModel(
    private val hondenRepository: HondenRepository,
    private val aanmeldenHondenRepository: AanmeldenHondenRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val gebruikersRepository: GebruikersRepository
) : ViewModel() {

    var dogSignInUiState by mutableStateOf(DogSignInUiState())
        private set

    /**
     * Get the email adress from the sign in owner
     */
    val getEmailAddressFromDataStore = userPreferencesRepository
        .getEmailAddress
        .map { emailAddress ->
            DogSignInUiState(eigenaar = emailAddress)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DogSignInUiState()
        )

    /**
     * Get the whole owner object through the email address from the DataStore.
     */
    fun getWholeUserObject(emailAddress: String): Flow<Gebruiker> =
        gebruikersRepository.getUserbyEmailAddress(emailAddress).filterNotNull()

    /**
     * Get all dogs from the sign in owner.
     */
    fun getAllDogsFromTheOwner(owner: String) {
        viewModelScope.launch {
            // Get all dogs from the signed in owner.
            val listOfDogs: List<Hond> = hondenRepository.getAllDogsFromOwner(owner).first()

            // Make a new list for adding all appointments of a specific dog.
            val signInDogs = mutableListOf(listOf(AanmeldenHond(1, "")))
            signInDogs.removeAt(0)

            val test = mutableListOf(AanmeldenHondEnHondClass(3459, "ghwgqtg", "hwhwh", "thwq5tr","0yyh650jnt"))
            test.removeAt(0)

            for (dog in listOfDogs) {
                // Get all sign in appointments from a specific dog and add this in the list.
                signInDogs.add(getAllSignInDogsFromASpecificDog(dog.chipNummer))

                for (afspraak in signInDogs) {
                    afspraak.forEach { a1 ->
                        if (a1.chipNummer == dog.chipNummer) {
                            test.add(
                                AanmeldenHondEnHondClass(
                                    chipnummer = dog.chipNummer,
                                    hondnaam = dog.hondnaam,
                                    hondenras = dog.hondenras,
                                    eigenaar = dog.eigenaar,
                                    afspraakId = a1.afspraakId
                                )
                            )
                        }
                    }
                }
            }



            dogSignInUiState = dogSignInUiState.copy(
                aanmeldenHondEnHondObject = test.toList()
            )
        }
    }

    /**
     * Get all sign in dogs of an appointment from a specific dog.
     */
    private suspend fun getAllSignInDogsFromASpecificDog(chipnummer: String): List<AanmeldenHond> =
        aanmeldenHondenRepository.getAllAppointmentFromOneDog(chipnummer).first()
}

/**
 * UI State for [DogSignInOverviewViewModel].
 */
data class DogSignInUiState(
    val afspraakId: Int = 0,
    val chipnummer: Int = 0,
    val beginMoment: LocalDateTime = LocalDateTime.now(),
    val eindMoment: LocalDateTime = LocalDateTime.now(),
    val eigenaar: String = "",

    val aanmeldenHondEnHondObject: List<AanmeldenHondEnHondClass> = listOf()
)

data class AanmeldenHondEnHondClass(
    val afspraakId: Int,
    val chipnummer: String,
    val hondnaam: String,
    val hondenras: String,
    val eigenaar: String,
)