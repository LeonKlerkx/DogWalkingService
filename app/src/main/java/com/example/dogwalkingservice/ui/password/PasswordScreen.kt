package com.example.dogwalkingservice.ui.password

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogwalkingservice.R
import com.example.dogwalkingservice.ui.AppViewModelProvider
import com.example.dogwalkingservice.ui.navigation.NavigationDestination
import com.example.dogwalkingservice.ui.theme.DogWalkingServiceTheme
import kotlinx.coroutines.launch

object PasswordDestination : NavigationDestination {
    override val route = "password"
    override val titleRes = R.string.password_screen
}

@Composable
fun PasswordScreen(
    navigateToLoginUser: () -> Unit,
    modifier: Modifier = Modifier,
    // Open the PasswordViewModel in the AppViewModelProvider.Factory.
    viewModel: PasswordViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    PasswordBody(
        emailAddress = viewModel.passwordUiState.emailaddress,
        onChangeEmailAddress = viewModel::updateEmailAddress,
        password = viewModel.passwordUiState.password,
        onChangePassword = viewModel::updatePassword,
        repeatPassword = viewModel.passwordUiState.repeatPassword,
        onChangeRepeatPassword = viewModel::updateRepeatPassword,
        changePassword = {
            coroutineScope.launch {
                try {
                    val resultOfUpdatingPassword = viewModel
                        .checkEmailAddressExistsToUpdatePassword()

                    Toast.makeText(context, resultOfUpdatingPassword, Toast.LENGTH_LONG).show()
                }
                catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    )
}

@Composable
fun PasswordBody(
    emailAddress: String,
    onChangeEmailAddress: (String) -> Unit,
    password: String,
    onChangePassword: (String) -> Unit,
    repeatPassword: String,
    onChangeRepeatPassword: (String) -> Unit,
    changePassword: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        // Text Email address:
        OutlinedTextField(
            value = emailAddress,
            onValueChange = onChangeEmailAddress,
            label = {
                Text(stringResource(R.string.password_screen_email_address))
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

        // Text Password:
        OutlinedTextField(
            value = password,
            onValueChange = onChangePassword,
            label = {
                Text(stringResource(R.string.password_screen_new_password))
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )

        // Text Repeat password:
        OutlinedTextField(
            value = repeatPassword,
            onValueChange = onChangeRepeatPassword,
            label = {
                Text(stringResource(R.string.password_screen_repeat_new_password))
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

        // Button Change password.
        Button(
            onClick = changePassword,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.password_screen_edit_password))
        }
    }
}

@Preview
@Composable
fun PasswordScreenPreview() {
    DogWalkingServiceTheme {
        PasswordBody(
            emailAddress = "test@gmail.com",
            onChangeEmailAddress = {},
            password = "newPassword",
            onChangePassword = {},
            repeatPassword = "newPassword",
            onChangeRepeatPassword = {},
            changePassword = {}
        )
    }
}