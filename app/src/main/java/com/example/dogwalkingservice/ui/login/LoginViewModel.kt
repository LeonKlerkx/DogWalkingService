package com.example.dogwalkingservice.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dogwalkingservice.data.Gebruiker
import com.example.dogwalkingservice.data.GebruikersRepository
import kotlinx.coroutines.flow.first

/**
 * ViewModel to hold and retrieve the data of the screen.
 */
class LoginViewModel(
    private val gebruikersRepository: GebruikersRepository
) : ViewModel() {

    /**
     * Holds the current login status.
     */
    var loginUiState by mutableStateOf(LoginUiState())
        private set

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
     * Controleert of de gebruiker het juiste emailadres en wachtwoord heeft ingevuld.
     */
    suspend fun checkLoginCredentials(): Gebruiker? {
        if (validateUserInput()) {
            val checkUserCredentials = gebruikersRepository
                .getUserbyEmailAddress(loginUiState.emailAddress)
                .first()

            // Controleert op een veilige manier de waarde van emailadres (= ?. )
            if (checkUserCredentials?.emailadres.equals(loginUiState.emailAddress) &&
                checkUserCredentials?.wachtwoord.equals(loginUiState.password)) {
                // Object is niet NULL. Het object teruggeven (met !! (= niet nullable objecten))
                return checkUserCredentials!!
            }
            else
                return null
        }

        return null
    }
}

/**
 * Ui State for Login Screen
 */
data class LoginUiState(
    val emailAddress: String = "",
    val password: String = ""
)