package com.example.dogwalkingservice.ui.password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dogwalkingservice.data.Gebruiker
import com.example.dogwalkingservice.data.GebruikersRepository
import kotlinx.coroutines.flow.first

class PasswordViewModel(
    private val gebruikersRepository: GebruikersRepository
) : ViewModel() {

    /**
     * Holds the current password change state.
     */
    var passwordUiState by mutableStateOf(PasswordUiState())
        private set

    /**
     * Update the [PasswordUiState] with the new value of [PasswordUiState.emailaddress].
     */
    fun updateEmailAddress(emailAddress: String) {
        passwordUiState = PasswordUiState(
            emailaddress = emailAddress,
            password = passwordUiState.password,
            repeatPassword = passwordUiState.repeatPassword
        )
    }

    /**
     * Update the [PasswordUiState] with the new value of [PasswordUiState.password].
     */
    fun updatePassword(password: String) {
        passwordUiState = PasswordUiState(
            emailaddress = passwordUiState.emailaddress,
            password = password,
            repeatPassword = passwordUiState.repeatPassword
        )
    }

    /**
     * Update the [PasswordUiState] with the new value of [PasswordUiState.repeatPassword].
     */
    fun updateRepeatPassword(repeatPassword: String) {
        passwordUiState = PasswordUiState(
            emailaddress = passwordUiState.emailaddress,
            password = passwordUiState.password,
            repeatPassword = repeatPassword
        )
    }

    /**
     * Verify that the user input on the screen has been completed.
     */
    fun validateUserInput(uiState: PasswordUiState = passwordUiState): Boolean {
        return with(uiState) {
            emailaddress.isNotBlank() && password.isNotBlank() && repeatPassword.isNotBlank() &&
                    password == repeatPassword
        }
    }

    /**
     * Check if the email address is exists in the Database,
     * so the password can updated succesfully.
     */
    suspend fun checkEmailAddressExistsToUpdatePassword(): String {
        if (validateUserInput()) {
            // Ask the user object from the Database.
            var getUserObjectFromTheDatabase = gebruikersRepository
                .getUserbyEmailAddress(passwordUiState.emailaddress)
                .first()

            // Check if the user is exists in the Database.
            if (getUserObjectFromTheDatabase?.emailadres?.isNotBlank() == true) {

                // Update the password through copy the object and change the value of password.
                getUserObjectFromTheDatabase = getUserObjectFromTheDatabase.copy(
                    wachtwoord = passwordUiState.password
                )

                // Update the object with the new password in the Database.
                gebruikersRepository.updateUser(getUserObjectFromTheDatabase)
                return "Het wachtwoord is succesvol gewijzigd."
            }

            throw NullPointerException("E-mailadres bestaat niet.")
        }

        throw IllegalArgumentException("Niet alle velden zijn juist ingevuld.")
    }
}

/**
 * UI State for PasswordScreen.
 */
data class PasswordUiState(
    val emailaddress: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)