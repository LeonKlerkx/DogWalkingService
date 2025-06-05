package com.example.dogwalkingservice.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogwalkingservice.data.GebruikersRepository
import com.example.dogwalkingservice.data.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to hold and retrieve the data of the screen.
 */
class LoginViewModel(
    private val gebruikersRepository: GebruikersRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    /**
     * Holds the current login status.
     */
    var loginUiState by mutableStateOf(LoginUiState())
        private set

    /**
     * Save the email address into the DataStore.
     */
    fun saveEmailAddressInDataStore(emailAddress: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveEmailAddress(emailAddress)
        }
    }

    /**
     * Delete the email address from the DataStore.
     */
    fun deleteEmailAddressInDataStore() {
        viewModelScope.launch {
            userPreferencesRepository.deleteEmailaddress()
        }
    }

    /**
     * Update the [LoginUiState] with the new value of [LoginUiState.emailAddress].
     */
    fun updateEmailaddress(emailAddress: String) {
        loginUiState = LoginUiState(
            emailAddress = emailAddress,
            password = loginUiState.password // Holds the current password.
        )
    }

    /**
     * Update the [LoginUiState] with the new value of [LoginUiState.password].
     */
    fun updatePassword(password: String) {
        loginUiState = LoginUiState(
            emailAddress = loginUiState.emailAddress, // Holds the current email address.
            password = password
        )
    }

    /**
     * Controleert of zowel het emailadres als het wachtwoord is ingevuld.
     */
    private fun validateUserInput(uiState: LoginUiState = loginUiState): Boolean {
        return with(uiState) {
            emailAddress.isNotBlank() && password.isNotBlank()
        }
    }

    /**
     * Check if the user filled in the right email address and password,
     * and return the name of the role of the user.
     */
    suspend fun checkLoginCredentials(): String {
        if (validateUserInput()) {
            val checkUserCredentials = gebruikersRepository
                .getUserbyEmailAddress(loginUiState.emailAddress)
                .first()

            /* Controleert op een veilige manier (= ?. ) of de combinatie van
               het e-mailadres en het wachtwoord in de Database bestaat. */
            if (checkUserCredentials?.emailadres.equals(loginUiState.emailAddress) &&
                checkUserCredentials?.wachtwoord.equals(loginUiState.password)) {
                // Object is niet NULL. Het object teruggeven (met !! (= niet nullable objecten))
                return checkUserCredentials!!.rolnaam
            }
            else
                throw IllegalArgumentException("E-mailadres en/of wachtwoord komen niet overeen.")
        }

        throw IllegalArgumentException("Vul het e-mailadres en het wachtwoord in.")
    }
}

/**
 * Ui State for Login Screen
 */
data class LoginUiState(
    val emailAddress: String = "",
    val password: String = ""
)