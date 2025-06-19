package com.example.dogwalkingservice.ui.appointment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogwalkingservice.data.AanmeldenHondenRepository
import com.example.dogwalkingservice.data.Afspraak
import com.example.dogwalkingservice.data.AfsprakenRepository
import com.example.dogwalkingservice.data.HondenRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class AppointmentDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val afsprakenRepository: AfsprakenRepository,
    private val aanmeldenHondenRepository: AanmeldenHondenRepository,
    private val hondenRepository: HondenRepository
) : ViewModel() {

    /**
     * Get the appointment ID of the selected appointment.
     * [checkNotNull] compares if the value is not null.
     * [SavedStateHandle] is a class that saved data through key-value.
     * [AppointmentDetailsDestination.appointmentId] this is the value of the selected appointment from the LazyColumn.
     */
    private val afspraakId: Int = checkNotNull(savedStateHandle[AppointmentDetailsDestination.appointmentId])

    /**
     * Holds the UI State of the appointment.
     */
    var appointmentUiState by mutableStateOf(AppointmentDetailsUiState())
        private set

    init {
        viewModelScope.launch {
            // Get the selected appointment from the overview of all appointments from the LazyColumn.
            appointmentUiState = afsprakenRepository.getAppointmentByPrimaryKey(afspraakId)
                .filterNotNull()
                .first()
                .toAppointmentUiState()

            // Retrieve all sign in dogs from the selected appointment.
            val getSigninDogsFromAppointment = aanmeldenHondenRepository
                .getAllSignInDogsFromAnAppointment(afspraakId)
                .filterNotNull()
                .first()

            val saveSignInDogInfo: StringBuilder = StringBuilder()

            for (signInDog in getSigninDogsFromAppointment) {
                // Retrieve the whole dog object from the sign in dogs.
                val getWholeDogObjectSignInDog = hondenRepository
                    .getDogByPrimaryKey(signInDog.chipNummer)
                    .first()

                saveSignInDogInfo.append("${getWholeDogObjectSignInDog.hondnaam} - ${getWholeDogObjectSignInDog.eigenaar}\n")
            }

            // Add all sign in dogs from a specific appointment.
            appointmentUiState = appointmentUiState.copy(
                signInDogs = saveSignInDogInfo.toString()
            )
        }
    }

    /**
     * Delete the appointment into the Database.
     */
    suspend fun deleteAppointment(): String {
        if (appointmentUiState.signInDogs.isNotBlank()) {
            throw IllegalArgumentException("Mislukt! Er zijn aanmeldingen voor de afspraak.")
        }
        else {
            // Afspraak verwijderen.
            afsprakenRepository.deleteAppointment(appointmentUiState.toAppointment())

            return "De afspraak is succesvol verwijderd."
        }
    }
}

/**
 * Ui State for [AppointmentDetailsViewModel].
 */
data class AppointmentDetailsUiState(
    val afspraakId: Int = 0,

    val beginDag: LocalDate = LocalDate.now(),
    val beginTijd: LocalTime = LocalTime.now(),

    val eindDag: LocalDate = LocalDate.now(),
    val eindTijd: LocalTime = LocalTime.now(),
    val oppasser: String = "",

    // Save all sign in dogs from a specific appointment.
    val signInDogs: String = ""
)

/**
 * Extension function to convert the [Afspraak] into the [AppointmentDetailsUiState] object
 * to retrieve the selected appointment from the Database and give it to the UiState.
 */
fun Afspraak.toAppointmentUiState(): AppointmentDetailsUiState = AppointmentDetailsUiState(
    afspraakId = afspraakId,
    beginDag = LocalDate.parse(beginmoment.substring(0, 10)),
    beginTijd = LocalTime.parse(beginmoment.substring(11, 16)),
    eindDag = LocalDate.parse(eindmoment.substring(0, 10)),
    eindTijd = LocalTime.parse(eindmoment.substring(11, 16)),
    oppasser = oppasser
)

/**
 * Extension function to convert the [AppointmentDetailsUiState] into the [Afspraak] object
 * to remove the selected [Afspraak].
 */
fun AppointmentDetailsUiState.toAppointment(): Afspraak = Afspraak(
    afspraakId = afspraakId,
    beginmoment = "$beginDag $beginTijd",
    eindmoment = "$eindDag $eindTijd",
    oppasser = oppasser
)