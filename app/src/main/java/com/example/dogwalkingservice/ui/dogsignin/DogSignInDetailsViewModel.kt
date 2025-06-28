package com.example.dogwalkingservice.ui.dogsignin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogwalkingservice.data.AanmeldenHond
import com.example.dogwalkingservice.data.AanmeldenHondenRepository
import com.example.dogwalkingservice.data.Afspraak
import com.example.dogwalkingservice.data.AfsprakenRepository
import com.example.dogwalkingservice.data.Hond
import com.example.dogwalkingservice.data.HondenRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DogSignInDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val afsprakenRepository: AfsprakenRepository,
    private val aanmeldenHondenRepository: AanmeldenHondenRepository,
    private val hondenRepository: HondenRepository,
) : ViewModel() {

    /**
     * Get the appointment ID of the selected appointment.
     * [checkNotNull] compares if the value is not null.
     * [SavedStateHandle] is a class that saved data through key-value.
     * [DogSignInDetailsDestination.appointmentId] this is the value of the selected appointment from the LazyColumn.
     */
    private val afspraakId: Int = checkNotNull(savedStateHandle[DogSignInDetailsDestination.appointmentId])

    /**
     * Get the chipnumber of the selected dog from the appointment.
     * [checkNotNull] compares if the value is not null.
     * [SavedStateHandle] is a class that saved data through key-value.
     * [DogSignInDetailsDestination.chipNumber] this is the value of the selected  dog from the LazyColumn.
     */
    private val chipNumber: String = checkNotNull(savedStateHandle[DogSignInDetailsDestination.chipNumber])

    var dogSignInDetailsUiState by mutableStateOf(DogSignInDetailsUiState())
        private set

    init {
        viewModelScope.launch {
            // Get all sign in dogs from the selected appointment.
            dogSignInDetailsUiState = aanmeldenHondenRepository
                .getAllSignInDogsFromAnAppointment(afspraakId)
                .first()
                .toDogSignInDetailsUistate()

            // Get the whole selected appointment object.
            val getAppointmentInfo = afsprakenRepository
                .getAppointmentByPrimaryKey(afspraakId)
                .first()

            dogSignInDetailsUiState = dogSignInDetailsUiState.copy(
                appointment = getAppointmentInfo
            )

            // Get the selected dog object.
            val getDogInfo = hondenRepository
                .getDogByPrimaryKey(chipNumber)
                .first()
            dogSignInDetailsUiState = dogSignInDetailsUiState.copy(
                dog = getDogInfo
            )

            /* Maakt een nieuwe lijst aan.
               De lijst wordt in the foreach gevuld met de aangemelde honden van de afspraak */
            val listOfSignInDogs: MutableList<DogSignInProperties> = mutableListOf()

            // Walk through the list of sign in dogs from the appointment.
            for (signInDogs in dogSignInDetailsUiState.listOfAllSignInDogs) {
                val dog = hondenRepository.getDogByPrimaryKey(signInDogs.chipNummer).first()

                listOfSignInDogs.add(
                    DogSignInProperties(
                        appointment = getAppointmentInfo,
                        dog = dog
                    )
                )
            }

            // Add the complete list to the UI State.
            dogSignInDetailsUiState = dogSignInDetailsUiState.copy(
                signInDogList = listOfSignInDogs,

                geselecteerdeAfspraakID = afspraakId,
                geselecteerdeChipnummer = chipNumber
            )
        }
    }

    /**
     * Delete the selected sign in dog [chipNumber] from the appointment [afspraakId].
     */
    suspend fun deleteSignInDogFromTheAppointment(): String {

        // Make a new object to delete it.
        val signInDog = AanmeldenHond(afspraakId, chipNumber)
        aanmeldenHondenRepository.delete(signInDog)

        return "De aanmelding van de hond is succesvol verwijderd."
    }
}

/**
 * Represent the Ui State of the [DogSignInDetailsViewModel].
 */
data class DogSignInDetailsUiState(
    val listOfAllSignInDogs: List<AanmeldenHond> = listOf(),
    val signInDogList: MutableList<DogSignInProperties> = mutableListOf(DogSignInProperties()),

    val appointment: Afspraak = Afspraak(0, "", "", ""),

    /* Get the dog object from the selected appointment to show the name of the dog
       when you decide to delete the dog from the appointment. */
    val dog: Hond = Hond("","","",""),

    // Is uit de LazyColumn (OverviewScreen) geselecteerd.
    val geselecteerdeAfspraakID: Int = 0,
    val geselecteerdeChipnummer: String = ""
)

/**
 * Represent one record to pair the appointment and chip number.
 */
data class DogSignInProperties(
    val appointment: Afspraak = Afspraak(0, "", "", ""),
    val dog: Hond = Hond("", "", "", "")
)

/**
 * Extension function to convert the list of [AanmeldenHond] to a list of [DogSignInDetailsUiState].
 */
fun List<AanmeldenHond>.toDogSignInDetailsUistate(): DogSignInDetailsUiState = DogSignInDetailsUiState(
    listOfAllSignInDogs = this.toList()
)