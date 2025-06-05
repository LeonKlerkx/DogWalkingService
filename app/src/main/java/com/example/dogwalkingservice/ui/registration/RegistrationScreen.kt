package com.example.dogwalkingservice.ui.registration

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogwalkingservice.R
import com.example.dogwalkingservice.ui.AppViewModelProvider
import com.example.dogwalkingservice.ui.navigation.NavigationDestination
import com.example.dogwalkingservice.ui.theme.DogWalkingServiceTheme
import kotlinx.coroutines.launch

object RegistrationDestination : NavigationDestination {
    override val route = "registration"
    override val titleRes = R.string.registration_title
}

@Composable
fun RegistrationScreen(
    navigateToStartPageOwner: () -> Unit,
    navigateToStartPageDogSitter: () -> Unit,
    modifier: Modifier = Modifier,
    // Open the RegistrationViewModel in the AppViewModelProvider.Factory
    viewModel: RegistrationViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    RegistrationBody(
        emailAddress = viewModel.registrationUiState.emailAddress,
        onChangeEmailAddress = viewModel::updateEmailaddress,
        password = viewModel.registrationUiState.password,
        onChangePassword = viewModel::updatePassword,
        repeatPassword = viewModel.registrationUiState.repeatPassword,
        onChangeRepeatPassword = viewModel::updateRepeatPassword,
        onSelectedChangeRole = viewModel::updateUserRole,
        personName = viewModel.registrationUiState.userName,
        onChangePersonName = viewModel::updateUserName,
        phoneNumber = viewModel.registrationUiState.phoneNumber,
        onChangePhoneNumber = viewModel::updatePhoneNumber,
        dateOfBirth = viewModel.registrationUiState.dateOfBirth,
        onChangeDateOfBirth = viewModel::updateDateOfBirth,
        address = viewModel.registrationUiState.address,
        onChangeAddress = viewModel::updateAddress,
        postalCode = viewModel.registrationUiState.postalCode,
        onChangePostalCode = viewModel::updatePostalCode,
        placeOfResidence = viewModel.registrationUiState.placeOfResidence,
        onChangePlaceOfResidence = viewModel::updatePlaceOfResidence,
        personalDescription = viewModel.registrationUiState.personalDescription,
        onChangePersonalDescription = viewModel::updatePersonalDescription,
        registerUser = {
            coroutineScope.launch {
                try {
                    val giveTheUserRoleInfo = viewModel.addUserIntoTheDatabase()

                    when (giveTheUserRoleInfo) {
                        "Eigenaar" -> navigateToStartPageOwner()
                        "Oppasser" -> navigateToStartPageDogSitter()
                    }
                }
                catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
             //   navigationToAdditionalUserInformation()
            }
        },
        modifier = modifier
    )
}

@Composable
fun RegistrationBody(
    emailAddress: String,
    onChangeEmailAddress: (String) -> Unit,
    password: String,
    onChangePassword: (String) -> Unit,
    repeatPassword: String,
    onChangeRepeatPassword: (String) -> Unit,
    onSelectedChangeRole: (String) -> Unit,// = {},
    personName: String,
    onChangePersonName: (String) -> Unit,
    phoneNumber: String,
    onChangePhoneNumber: (String) -> Unit,
    dateOfBirth: String,
    onChangeDateOfBirth: (String) -> Unit,
    address: String,
    onChangeAddress: (String) -> Unit,
    postalCode: String,
    onChangePostalCode: (String) -> Unit,
    placeOfResidence: String,
    onChangePlaceOfResidence: (String) -> Unit,
    personalDescription: String,
    onChangePersonalDescription: (String) -> Unit,
    registerUser: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedValueRole by rememberSaveable { mutableStateOf("") }

    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        item {
            // TextField Email address.
            OutlinedTextField(
                value = emailAddress,
                onValueChange = onChangeEmailAddress,
                label = {
                    Text(stringResource(R.string.registration_email_address))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = modifier.fillMaxWidth(),
                enabled = true,
                singleLine = true
            )
        }
        item {
            // TextField Password.
            OutlinedTextField(
                value = password,
                onValueChange = onChangePassword,
                label = {
                    Text(stringResource(R.string.registration_password))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                visualTransformation = PasswordVisualTransformation(),
                modifier = modifier.fillMaxWidth(),
                enabled = true,
                singleLine = true
            )

            // TextField Repeat password.
            OutlinedTextField(
                value = repeatPassword,
                onValueChange = onChangeRepeatPassword,
                label = {
                    Text(stringResource(R.string.registration_repeat_password))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                visualTransformation = PasswordVisualTransformation(),
                modifier = modifier.fillMaxWidth(),
                enabled = true,
                singleLine = true
            )
        }

        // Add two RadioButton items that belong together.
        item {
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Radiobuttons role.
                    RadioButton(
                        selected = selectedValueRole == stringResource(R.string.registration_radio_button_owner),
                        onClick = {
                            selectedValueRole = "Eigenaar"
                            onSelectedChangeRole("Eigenaar")
                        }
                    )
                    Text(
                        text = stringResource(R.string.registration_radio_button_owner)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedValueRole == stringResource(R.string.registration_radio_button_dog_sitter),
                        onClick = {
                            selectedValueRole = "Oppasser"
                            onSelectedChangeRole("Oppasser")
                        }
                    )
                    Text(
                        text = stringResource(R.string.registration_radio_button_dog_sitter)
                    )
                }
            }
        }

        item {
            // TextField Name.
            OutlinedTextField(
                value = personName,
                onValueChange = onChangePersonName,
                label = {
                    Text(stringResource(R.string.additional_user_information_name))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = modifier.fillMaxWidth(),
                enabled = true,
                singleLine = true
            )
        }

        item {
            // TextField Phone number.
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onChangePhoneNumber,
                label = {
                    Text(stringResource(R.string.additional_user_information_phone_number))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                modifier = modifier.fillMaxWidth(),
                enabled = true,
                singleLine = true
            )
        }

        item {
            // TextField Date of Birth.
            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = onChangeDateOfBirth,
                label = {
                    Text(stringResource(R.string.additional_user_information_date_of_birth))
                },
                placeholder = {
                    Text("yyyy-MM-dd")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            // TextField Address.
            OutlinedTextField(
                value = address,
                onValueChange = onChangeAddress,
                label = {
                    Text(stringResource(R.string.additional_user_information_address))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            // TextField Postal Code.
            OutlinedTextField(
                value = postalCode,
                onValueChange = onChangePostalCode,
                label = {
                    Text(stringResource(R.string.additional_user_information_postal_code))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            // TextField Place of Residence.
            OutlinedTextField(
                value = placeOfResidence,
                onValueChange = onChangePlaceOfResidence,
                label = {
                    Text(stringResource(R.string.additional_user_information_place_of_residence))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            if (selectedValueRole == "Oppasser") {
                // TextField Personal description.
                OutlinedTextField(
                    value = personalDescription,
                    onValueChange = onChangePersonalDescription,
                    label = {
                        Text(stringResource(R.string.additional_user_information_personal_description))
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    modifier = modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        item {
            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
        }

        item {
            // Button Register.
            Button(
                onClick = registerUser,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.registration_button_register))
            }
        }

        item {
            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_large)))
        }
    }
}

@Preview
@Composable
fun PreviewRegistrationScreen() {
    DogWalkingServiceTheme {
        RegistrationBody(
            emailAddress = "test@gmail.com",
            onChangeEmailAddress = {},
            password = "ab1cd2",
            onChangePassword = {},
            repeatPassword = "ab1cd2",
            onChangeRepeatPassword = {},
            onSelectedChangeRole = {},
            personName = "Leon",
            onChangePersonName = {},
            phoneNumber = "06-12345678",
            onChangePhoneNumber = {},
            dateOfBirth = "2000-01-01",
            onChangeDateOfBirth = {},
            address = "Onbekend 1",
            onChangeAddress = {},
            postalCode = "0000XX",
            onChangePostalCode = {},
            placeOfResidence = "Noord-Brabant",
            onChangePlaceOfResidence = {},
            personalDescription = "",
            onChangePersonalDescription = {},
            registerUser = {}
        )
    }
}