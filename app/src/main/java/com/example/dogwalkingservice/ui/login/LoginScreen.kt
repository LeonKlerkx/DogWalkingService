package com.example.dogwalkingservice.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogwalkingservice.R
import com.example.dogwalkingservice.ui.AppViewModelProvider
import com.example.dogwalkingservice.ui.navigation.NavigationDestination
import com.example.dogwalkingservice.ui.theme.DogWalkingServiceTheme
import kotlinx.coroutines.launch

object LoginDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.login_title
}

@Composable
fun LoginScreen(
    navigateToStartPageOwner: () -> Unit,
    navigateToStartPageDogSitter: () -> Unit,
    navigateToForgetPassword: () -> Unit,
    navigateToRegisterUser: () -> Unit,
    modifier: Modifier = Modifier,
    // Open the LoginViewModel in the AppViewModelProvider.Factory.
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    LoginBody(
        emailaddress = viewModel.loginUiState.emailAddress,
        onChangeEmailAddress = viewModel::updateEmailaddress,
        password = viewModel.loginUiState.password,
        onChangePassword = viewModel::updatePassword,
        navigateToForgetPassword = navigateToForgetPassword,
        navigateToRegisterUser = navigateToRegisterUser,
        loginClick = {
            coroutineScope.launch {
                // Email address or password is not filled in.
                if (viewModel.loginUiState.emailAddress.isEmpty() || viewModel.loginUiState.password.isEmpty()) {
                    Toast.makeText(context, "Vul het e-mailadres en het wachtwoord in.", Toast.LENGTH_LONG)
                        .show()
                }
                // The user filled in the right login credentials
                else {
                    // Get the object of the sign in user.
                    val getUser = viewModel.checkLoginCredentials()

                    // When the user does exists in the Database and has the role Owner,
                    // send the user to the homepage of the Owner.
                    if (getUser?.rolnaam?.equals("Eigenaar") == true) {
                        navigateToStartPageOwner()
                        Toast.makeText(context, "Welkom ${getUser.gebruikersnaam}!", Toast.LENGTH_LONG)
                            .show()
                    }
                    // When the user does exists in the Database and has the role DogSitter,
                    // send the user to the homepage of the DogSitter.
                    else if (getUser?.rolnaam?.equals("Oppasser") == true) {
                        navigateToStartPageDogSitter()
                        Toast.makeText(context, "Welkom ${getUser.gebruikersnaam}!", Toast.LENGTH_LONG)
                            .show()
                    }
                    // The user does not fill in the right login credentials
                    else {
                        Toast.makeText(context, "E-mailadres en/of wachtwoord komen niet overeen.", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun LoginBody(
    emailaddress: String,
    onChangeEmailAddress: (String) -> Unit,
    password: String,
    onChangePassword: (String) -> Unit,
    navigateToForgetPassword: () -> Unit,
    navigateToRegisterUser: () -> Unit,
    loginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {

        // TextField E-mailadres.
        OutlinedTextField(
            value = emailaddress,
            onValueChange = onChangeEmailAddress,
            label = {
                Text(stringResource(R.string.login_screen_email_address))
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )

        // TextField Wachtwoord.
        OutlinedTextField(
            value = password,
            onValueChange = onChangePassword,
            label = {
                Text(stringResource(R.string.login_screen_password))
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

        // Knop Inloggen.
        Button(
            onClick = loginClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.login_screen_log_in))
        }

        // Tekst Wachtwoord vergeten.
        TextButton(
            onClick = navigateToForgetPassword,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.login_screen_forgot_password))
        }

        // Tekst Geen account? Meld je hier aan!
        TextButton(
            onClick = navigateToRegisterUser,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.login_screen_no_account))
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    DogWalkingServiceTheme {
        LoginBody(
            emailaddress = "test@gmail.com",
            onChangeEmailAddress = {},
            password = "ab1cd2",
            onChangePassword = {},
            navigateToForgetPassword = {},
            navigateToRegisterUser = {},
            loginClick = {}
        )
    }
}