package com.example.dogwalkingservice.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogwalkingservice.data.Gebruiker
import com.example.dogwalkingservice.data.GebruikersRepository
import com.example.dogwalkingservice.data.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditPersonalDataViewModel(
    private val gebruikersRepository: GebruikersRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    /**
     * Holds the current state.
     */
    var editPersonalDataUiState by mutableStateOf(EditPersonalDataUiState())
        private set

    /**
     * Constructor of the [EditPersonalDataViewModel].
     *
     * In this constructor, you must read the email address from the DataStore
     *  to get the whole object of the user.
     *
     * It is mandatory do to this operation in the init block of the ViewModel,
     *  because if you do this in the screen, the data will be lost by re-composition.
     *
     * The init block of a ViewModel will be executed once, so it doesn't matter how this is used in the screen.
     */
    init {
        viewModelScope.launch {
            // First read the email address and the user role from the DataStore.
            readDataInDataStore()

            // Retrieve the user object from the email address (saved in DataStore) of the user.
            getUserObject(editPersonalDataUiState.emailAddress)
        }
    }

    /**
     * Read the [UserPreferencesRepository.getEmailAddress] and the [UserPreferencesRepository.getUserRole] from the DataStore,
     * and save it into the [EditPersonalDataUiState], so you can receive to whole user object,
     * and use it in the screen.
     */
    private suspend fun readDataInDataStore(): EditPersonalDataUiState {
        editPersonalDataUiState = editPersonalDataUiState.copy(
                emailAddress = userPreferencesRepository.getEmailAddress.first(),
                userRole = userPreferencesRepository.getUserRole.first()
            )
        return editPersonalDataUiState
    }

    /**
     * Received the user object from the signed in user.
     */
    private suspend fun getUserObject(emailAddressDatastore: String) : EditPersonalDataUiState {

        // Get the whole user object by filtering the email address of the user.
        val getUser = gebruikersRepository.getUserbyEmailAddress(emailAddressDatastore)
            .first()

        editPersonalDataUiState = editPersonalDataUiState.copy(
            emailAddress = getUser!!.emailadres,
            userRole = getUser.rolnaam,
            userName = getUser.gebruikersnaam,
            phoneNumber = getUser.telefoonnummer,
            dateOfBirth = getUser.geboortedatum,
            address = getUser.adres,
            postalCode = getUser.postcode,
            placeOfResidence = getUser.woonplaats,
            personalDescription = getUser.persoonsomschrijving ?: ""
        )

        return editPersonalDataUiState
    }

    /**
     * Update the [EditPersonalDataUiState] with a new value of [EditPersonalDataUiState.emailAddress].
     */
    fun updateEmailAddress(emailAddress: String) {
        editPersonalDataUiState = editPersonalDataUiState.copy(
            emailAddress = emailAddress
        )
    }

    /**
     * Update the [EditPersonalDataUiState] with a new value of [EditPersonalDataUiState.phoneNumber].
     */
    fun updatePhoneNumber(phoneNumber: String) {
        editPersonalDataUiState = editPersonalDataUiState.copy(
            phoneNumber = phoneNumber
        )
    }

    /**
     * Update the [EditPersonalDataUiState] with a new value of [EditPersonalDataUiState.dateOfBirth].
     */
    fun updateDateOfBirth(dateOfBirth: String) {
        editPersonalDataUiState = editPersonalDataUiState.copy(
            dateOfBirth = dateOfBirth
        )
    }

    /**
     * Update the [EditPersonalDataUiState] with a new value of [EditPersonalDataUiState.address].
     */
    fun updateAddress(address: String) {
        editPersonalDataUiState = editPersonalDataUiState.copy(
            address = address
        )
    }

    /**
     * Update the [EditPersonalDataUiState] with a new value of [EditPersonalDataUiState.postalCode].
     */
    fun updatePostalCode(postalCode: String) {
        editPersonalDataUiState = editPersonalDataUiState.copy(
            postalCode = postalCode
        )
    }

    /**
     * Update the [EditPersonalDataUiState] with a new value of [EditPersonalDataUiState.placeOfResidence].
     */
    fun updatePlaceOfResidence(placeOfResidence: String) {
        editPersonalDataUiState = editPersonalDataUiState.copy(
            placeOfResidence = placeOfResidence
        )
    }

    /**
     * Update the [EditPersonalDataUiState] with a new value of [EditPersonalDataUiState.personalDescription].
     */
    fun updatePersonDescription(personalDescription: String) {
        editPersonalDataUiState = editPersonalDataUiState.copy(
            personalDescription = personalDescription
        )
    }

    /**
     * Validate if the user had filled in all the input fields.
     */
    private fun validateUserInput(uiState: EditPersonalDataUiState = editPersonalDataUiState): Boolean {
        return with(uiState) {
            emailAddress.isNotBlank() && phoneNumber.isNotBlank() && dateOfBirth.isNotBlank() &&
                    address.isNotBlank() && postalCode.isNotBlank() && placeOfResidence.isNotBlank() &&

                    // The Dog sitter is required to fill in the personal description.
                    (userRole.equals("Oppasser") == personalDescription.isNotBlank())
        }
    }

    /**
     * Save the new user information into the Database.
     */
    suspend fun saveNewUserInformationIntoTheDatabase(): String {
        if (validateUserInput()) {

            // Receive the object of the filled in email address.
            val checkEmailAddressExists = gebruikersRepository
                .getUserbyEmailAddress(editPersonalDataUiState.emailAddress)
                .first()

            // Check if the filled in email address exists with another user in the Database.
            if (checkEmailAddressExists?.emailadres.equals(editPersonalDataUiState.emailAddress) &&
                !checkEmailAddressExists?.gebruikersnaam.equals(editPersonalDataUiState.userName)
            ) {
                throw IllegalArgumentException("Het e-mailadres bestaat al! Vul een andere e-mailadres in.")
            }

            // Receive the object of the filled in phone number.
            val checkPhoneNumberExists = gebruikersRepository
                .getUserByPhoneNumber(editPersonalDataUiState.phoneNumber)
                .first()

            // Check if the filled in phone number exists with another user in the Database.
            if (checkPhoneNumberExists?.telefoonnummer.equals(editPersonalDataUiState.phoneNumber) &&
                !checkPhoneNumberExists?.gebruikersnaam.equals(editPersonalDataUiState.userName)
            ){
                throw IllegalArgumentException("Het telefoonnummer bestaat al! Vul een andere telefoonnummer in.")
            }

            /* Receive the object of the sign in user, because you need the password to make an extension function.
               If you don't retrieve the password of the user, the user password will be updated with null.  */
            val getUserObjectByUserName = gebruikersRepository
                .getUserByUsername(editPersonalDataUiState.userName)
                .first()

            // Update the user object into the Database.
            /* You need to pass the password in this extension method,
                because in this screen you don't fill in a new password. */
            gebruikersRepository.updateUser(editPersonalDataUiState.toUser().copy(
                wachtwoord = getUserObjectByUserName!!.wachtwoord
            ))

            return "De gegevens zijn succesvol bijgewerkt."

        } else {
            throw IllegalArgumentException("Niet alle velden zijn ingevuld!")
        }
    }
}

/**
 * UI state for Edit Personal Data screen.
 */
data class EditPersonalDataUiState (
    val emailAddress: String = "",
    val userRole: String = "",
    val userName: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = "",
    val address: String = "",
    val postalCode: String = "",
    val placeOfResidence: String = "",
    val personalDescription: String = ""
)

/**
 * Extension function to convert a [EditPersonalDataUiState] to a [Gebruiker]
 * to save the new user data into the Database.
 */
fun EditPersonalDataUiState.toUser(): Gebruiker = Gebruiker(
    gebruikersnaam = userName,
    emailadres = emailAddress,
    telefoonnummer = phoneNumber,
    geboortedatum = dateOfBirth,
    adres = address,
    postcode = postalCode,
    woonplaats = placeOfResidence,
    persoonsomschrijving = personalDescription,
    rolnaam = userRole,
    /* Password is empty, but this will be overruled in the function
       saveNewUserInformationIntoTheDatabase() when you get the whole user object. */
    wachtwoord = ""
)