package com.example.dogwalkingservice.ui.appointment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AppointmentAddViewModel(
    private val afsprakenRepository: AfsprakenRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val gebruikersRepository: GebruikersRepository
) : ViewModel() {

    var appointmentUiState by mutableStateOf(AppointmentUiState())
        private set

    /**
     * Update the start day of the appointment.
     */
    fun updateStartDay(startDay: LocalDate) {
        appointmentUiState = appointmentUiState.copy(
            beginDag = startDay
        )
    }

    /**
     * Update the start time of the appointment.
     */
    fun updateStartTime(startTime: LocalTime) {
        appointmentUiState = appointmentUiState.copy(
            beginTijd = startTime
        )
    }

    /**
     * Update the end day of the appointment.
     */
    fun updateEndDay(endDay: LocalDate) {
        appointmentUiState = appointmentUiState.copy(
            eindDag = endDay
        )
    }

    /**
     * Update the end time of the appointment.
     */
    fun updateEndTime(endTime: LocalTime) {
        appointmentUiState = appointmentUiState.copy(
            eindTijd = endTime
        )
    }

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
     * Validate if the choosed datetime of the start moment is early than the end moment.
     */
    private fun validateUserInput(): Boolean {
        // Combine the start day + start time and end day + end time in two LocalDateTime variables.
        appointmentUiState = appointmentUiState.copy(
            beginmoment = this.appointmentUiState.beginDag.atTime(this.appointmentUiState.beginTijd.hour, this.appointmentUiState.beginTijd.minute),
            eindMoment = this.appointmentUiState.eindDag.atTime(this.appointmentUiState.eindTijd.hour, this.appointmentUiState.eindTijd.minute)
        )

        return appointmentUiState.beginmoment.isBefore(appointmentUiState.eindMoment)
    }

    /**
     * Save the appointment into the Database.
     */
    suspend fun saveAppointment(usernameDogSitter: String): String {
        // Add the appointment if the user has choose a valid date time.
        if (validateUserInput()) {
            /* Add the name of the dog sitter to the
                appointmentUiState so you can insert an appointment  */
            appointmentUiState = appointmentUiState.copy(
                oppasser = usernameDogSitter
            )

            // Check if the appointment already exists in the Database.
            val appointmentExists = afsprakenRepository
                .checkIfTheAppointmentIsExists(
                    beginmoment = "${appointmentUiState.beginDag} ${appointmentUiState.beginTijd.hour}:${appointmentUiState.beginTijd.minute}",
                    eindmoment = "${appointmentUiState.eindDag} ${appointmentUiState.eindTijd.hour}:${appointmentUiState.eindTijd.minute}",
                    oppasser = appointmentUiState.oppasser
                )
                .firstOrNull()

            if (appointmentExists == null) {
                // Finally, you can add the appointment into the Database.
                afsprakenRepository.insertAppointment(appointmentUiState.insertAppointment())

                return "De afspraak is succesvol toegevoegd."
            }
            else {
                throw IllegalArgumentException("De afspraak bestaat al! Maak een andere afspraak aan.")
            }

        }
        else {
            throw IllegalArgumentException("Het eindmoment is eerder dan het beginmoment.")
        }
    }
}

/**
 * Ui State for [AppointmentAddViewModel].
 */
data class AppointmentUiState(
    val afspraakId: Int = 0,

    // You need this to combine the selected day and time.
    val beginmoment: LocalDateTime = LocalDateTime.now(),

    val beginDag: LocalDate = LocalDate.now(),
    val beginTijd: LocalTime = LocalTime.now(),

    // You need this to combine the selected day and time.
    val eindMoment: LocalDateTime = LocalDateTime.now(),

    val eindDag: LocalDate = LocalDate.now(),
    val eindTijd: LocalTime = LocalTime.now(),
    val oppasser: String = ""
)

/**
 * Extension function to convert the [AppointmentUiState] object to an [Afspraak] object.
 */
fun AppointmentUiState.insertAppointment(): Afspraak = Afspraak(
    afspraakId = afspraakId,
    beginmoment = "${beginDag} ${beginTijd.format(DateTimeFormatter.ofPattern("HH:mm"))}",
    eindmoment = "${eindDag} ${eindTijd.format(DateTimeFormatter.ofPattern("HH:mm"))}",
    oppasser = oppasser
)