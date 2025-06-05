package com.example.dogwalkingservice.ui.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dogwalkingservice.data.Gebruiker
import com.example.dogwalkingservice.data.GebruikersRepository
import kotlinx.coroutines.flow.first

class RegistrationViewModel(
    private val gebruikersRepository: GebruikersRepository
) : ViewModel() {

    /**
     * Holds the current registration status.
     */
    var registrationUiState by mutableStateOf(RegistrationUiState())
        private set

    /**
     * Update the [RegistrationUiState] with the new value of [RegistrationUiState.emailAddress].
     */
    fun updateEmailaddress(emailAddress: String) {
        registrationUiState = RegistrationUiState(
            emailAddress = emailAddress,
            password = registrationUiState.password,
            repeatPassword = registrationUiState.repeatPassword,
            userRole =  registrationUiState.userRole,
            userName = registrationUiState.userName,
            phoneNumber = registrationUiState.phoneNumber,
            dateOfBirth = registrationUiState.dateOfBirth,
            address = registrationUiState.address,
            postalCode = registrationUiState.postalCode,
            placeOfResidence = registrationUiState.placeOfResidence,
            personalDescription = registrationUiState.personalDescription
        )
    }

    /**
     * Update the [RegistrationUiState] with the new value of [RegistrationUiState.password].
     */
    fun updatePassword(password: String) {
        registrationUiState = RegistrationUiState(
            emailAddress = registrationUiState.emailAddress,
            password = password,
            repeatPassword = registrationUiState.repeatPassword,
            userRole =  registrationUiState.userRole,
            userName = registrationUiState.userName,
            phoneNumber = registrationUiState.phoneNumber,
            dateOfBirth = registrationUiState.dateOfBirth,
            address = registrationUiState.address,
            postalCode = registrationUiState.postalCode,
            placeOfResidence = registrationUiState.placeOfResidence,
            personalDescription = registrationUiState.personalDescription
        )
    }

    /**
     * Update the [RegistrationUiState] with the new value of [RegistrationUiState.repeatPassword].
     */
    fun updateRepeatPassword(repeatPassword: String) {
        registrationUiState = RegistrationUiState(
            emailAddress = registrationUiState.emailAddress,
            password = registrationUiState.password,
            repeatPassword = repeatPassword,
            userRole =  registrationUiState.userRole,
            userName = registrationUiState.userName,
            phoneNumber = registrationUiState.phoneNumber,
            dateOfBirth = registrationUiState.dateOfBirth,
            address = registrationUiState.address,
            postalCode = registrationUiState.postalCode,
            placeOfResidence = registrationUiState.placeOfResidence,
            personalDescription = registrationUiState.personalDescription
        )
    }

    /**
     * Update the [RegistrationUiState] with the new value of [RegistrationUiState.userRole].
     */
    fun updateUserRole(userRole: String) {
        registrationUiState = RegistrationUiState(
            emailAddress = registrationUiState.emailAddress,
            password = registrationUiState.password,
            repeatPassword = registrationUiState.repeatPassword,
            userRole = userRole,
            userName = registrationUiState.userName,
            phoneNumber = registrationUiState.phoneNumber,
            dateOfBirth = registrationUiState.dateOfBirth,
            address = registrationUiState.address,
            postalCode = registrationUiState.postalCode,
            placeOfResidence = registrationUiState.placeOfResidence,
            personalDescription = registrationUiState.personalDescription
        )
    }

    /**
     * Update the [RegistrationUiState] with the new value of [RegistrationUiState.userName].
     */
    fun updateUserName(userName: String) {
        registrationUiState = RegistrationUiState(
            emailAddress = registrationUiState.emailAddress,
            password = registrationUiState.password,
            repeatPassword = registrationUiState.repeatPassword,
            userRole =  registrationUiState.userRole,
            userName = userName,
            phoneNumber = registrationUiState.phoneNumber,
            dateOfBirth = registrationUiState.dateOfBirth,
            address = registrationUiState.address,
            postalCode = registrationUiState.postalCode,
            placeOfResidence = registrationUiState.placeOfResidence,
            personalDescription = registrationUiState.personalDescription
        )
    }

    /**
     * Update the [RegistrationUiState] with the new value of [RegistrationUiState.phoneNumber].
     */
    fun updatePhoneNumber(phoneNumber: String) {
        registrationUiState = RegistrationUiState(
            emailAddress = registrationUiState.emailAddress,
            password = registrationUiState.password,
            repeatPassword = registrationUiState.repeatPassword,
            userRole =  registrationUiState.userRole,
            userName = registrationUiState.userName,
            phoneNumber = phoneNumber,
            dateOfBirth = registrationUiState.dateOfBirth,
            address = registrationUiState.address,
            postalCode = registrationUiState.postalCode,
            placeOfResidence = registrationUiState.placeOfResidence,
            personalDescription = registrationUiState.personalDescription
        )
    }

    /**
     * Update the [RegistrationUiState] with the new value of [RegistrationUiState.dateOfBirth].
     */
    fun updateDateOfBirth(dateOfBirth: String) {
        registrationUiState = RegistrationUiState(
            emailAddress = registrationUiState.emailAddress,
            password = registrationUiState.password,
            repeatPassword = registrationUiState.repeatPassword,
            userRole =  registrationUiState.userRole,
            userName = registrationUiState.userName,
            phoneNumber = registrationUiState.phoneNumber,
            dateOfBirth = dateOfBirth,
            address = registrationUiState.address,
            postalCode = registrationUiState.postalCode,
            placeOfResidence = registrationUiState.placeOfResidence,
            personalDescription = registrationUiState.personalDescription
        )
    }

    /**
     * Update the [RegistrationUiState] with the new value of [RegistrationUiState.address].
     */
    fun updateAddress(address: String) {
        registrationUiState = RegistrationUiState(
            emailAddress = registrationUiState.emailAddress,
            password = registrationUiState.password,
            repeatPassword = registrationUiState.repeatPassword,
            userRole =  registrationUiState.userRole,
            userName = registrationUiState.userName,
            phoneNumber = registrationUiState.phoneNumber,
            dateOfBirth = registrationUiState.dateOfBirth,
            address = address,
            postalCode = registrationUiState.postalCode,
            placeOfResidence = registrationUiState.placeOfResidence,
            personalDescription = registrationUiState.personalDescription
        )
    }

    /**
     * Update the [RegistrationUiState] with the new value of [RegistrationUiState.postalCode].
     */
    fun updatePostalCode(postalCode: String) {
        registrationUiState = RegistrationUiState(
            emailAddress = registrationUiState.emailAddress,
            password = registrationUiState.password,
            repeatPassword = registrationUiState.repeatPassword,
            userRole =  registrationUiState.userRole,
            userName = registrationUiState.userName,
            phoneNumber = registrationUiState.phoneNumber,
            dateOfBirth = registrationUiState.dateOfBirth,
            address = registrationUiState.address,
            postalCode = postalCode,
            placeOfResidence = registrationUiState.placeOfResidence,
            personalDescription = registrationUiState.personalDescription
        )
    }

    /**
     * Update the [RegistrationUiState] with the new value of [RegistrationUiState.placeOfResidence].
     */
    fun updatePlaceOfResidence(placeOfResidence: String) {
        registrationUiState = RegistrationUiState(
            emailAddress = registrationUiState.emailAddress,
            password = registrationUiState.password,
            repeatPassword = registrationUiState.repeatPassword,
            userRole =  registrationUiState.userRole,
            userName = registrationUiState.userName,
            phoneNumber = registrationUiState.phoneNumber,
            dateOfBirth = registrationUiState.dateOfBirth,
            address = registrationUiState.address,
            postalCode = registrationUiState.postalCode,
            placeOfResidence = placeOfResidence,
            personalDescription = registrationUiState.personalDescription
        )
    }

    /**
     * Update the [RegistrationUiState] with the new value of [RegistrationUiState.personalDescription].
     */
    fun updatePersonalDescription(personalDescription: String) {
        registrationUiState = RegistrationUiState(
            emailAddress = registrationUiState.emailAddress,
            password = registrationUiState.password,
            repeatPassword = registrationUiState.repeatPassword,
            userRole =  registrationUiState.userRole,
            userName = registrationUiState.userName,
            phoneNumber = registrationUiState.phoneNumber,
            dateOfBirth = registrationUiState.dateOfBirth,
            address = registrationUiState.address,
            postalCode = registrationUiState.postalCode,
            placeOfResidence = registrationUiState.placeOfResidence,
            personalDescription = personalDescription
        )
    }

    /**
     * Validate if the user had filled in all the input fields.
     */
    private fun validateUserInput(uiState: RegistrationUiState = registrationUiState): Boolean {
        return with(uiState) {
            emailAddress.isNotBlank() && password.isNotBlank() && repeatPassword.isNotBlank() &&
                    userRole.isNotBlank() && userName.isNotBlank() && phoneNumber.isNotBlank() &&
                    dateOfBirth.isNotBlank() && address.isNotBlank() && postalCode.isNotBlank() &&
                    placeOfResidence.isNotBlank() &&

                    // The Dog sitter is required to fill in the personal description.
                    (userRole.equals("Oppasser") == personalDescription.isNotBlank())
        }
    }

    /**
     * Validate if the user has filled in the same password.
     */
    private fun validateFilledInPassword(uiState: RegistrationUiState = registrationUiState): Boolean {
        return with(uiState) {
            password == repeatPassword
        }
    }

    /**
     * Check if the user filled in all the fields,
     * and check if the username, email address and the phone number does not exists in the Database (= Unique constraint).
     *
     * If all fields are correct filled in, the user will be added in the Database.
     */
    suspend fun addUserIntoTheDatabase(): String {
        if (validateUserInput()) {
            if (!validateFilledInPassword()){
                throw IllegalArgumentException("Het wachtwoord komt niet overeen.")
            }

            // Check if the username does exists in the Database.
            val checkUserNameExists = gebruikersRepository
                .getUserByUsername(registrationUiState.userName)
                .first()

            if (checkUserNameExists?.gebruikersnaam.equals(registrationUiState.emailAddress)) {
                throw IllegalArgumentException("De naam bestaat al! Vul een andere naam in.")
            }

            // Check if the email address does exists in the Database.
            val checkEmailAddressExists = gebruikersRepository
                .getUserbyEmailAddress(registrationUiState.emailAddress)
                .first()

            // The email address exists in the Database. Give an error.
            if (checkEmailAddressExists?.emailadres.equals(registrationUiState.emailAddress)) {
                throw IllegalArgumentException("Het e-mailadres bestaat al! Vul een andere e-mailadres in.")
            }

            val checkPhoneNumberExists = gebruikersRepository
                .getUserByPhoneNumber(registrationUiState.phoneNumber)
                .first()

            if (checkPhoneNumberExists?.telefoonnummer.equals(registrationUiState.phoneNumber)) {
                throw IllegalArgumentException("Het telefoonnummer bestaat al! Vul een andere telefoonnummer in.")
            }

            // Add the user into the Database.
            gebruikersRepository.insertUser(registrationUiState.toUser())

            // Return the role of the user to start the home screen.
            return registrationUiState.userRole
        }
        else {
            throw IllegalArgumentException("Niet alle velden zijn ingevuld!")
        }
    }
}

/**
 * UI state for Registration Screen and User info Screen.
 */
data class RegistrationUiState (
    val emailAddress: String = "",
    val password: String = "",
    val repeatPassword: String = "",
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
 * Extension function to convert [RegistrationUiState] to [Gebruiker].
 */
fun RegistrationUiState.toUser(): Gebruiker = Gebruiker(
    gebruikersnaam = userName,
    emailadres = emailAddress,
    wachtwoord = password,
    telefoonnummer = phoneNumber,
    geboortedatum = dateOfBirth,
    adres = address,
    postcode = postalCode,
    woonplaats = placeOfResidence,
    persoonsomschrijving = personalDescription,
    rolnaam = userRole
)