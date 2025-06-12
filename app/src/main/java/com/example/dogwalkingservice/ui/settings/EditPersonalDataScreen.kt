package com.example.dogwalkingservice.ui.settings

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogwalkingservice.DogWalkingServiceTopAppBar
import com.example.dogwalkingservice.R
import com.example.dogwalkingservice.ui.AppViewModelProvider
import com.example.dogwalkingservice.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object EditPersonalDataDestination : NavigationDestination {
    override val route = "editPersonalData"
    override val titleRes = R.string.edit_personal_data_title
}

@OptIn(ExperimentalMaterial3Api::class) // Needs for DogWalkingServiceTopAppBar.
@Composable
fun EditPersonalDataScreen(
    navigateBack: () -> Unit,
    // Open the EditPersonalDataViewModel in the AppViewModelProvider.Factory.
    viewModel: EditPersonalDataViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            DogWalkingServiceTopAppBar(
                title = stringResource(EditPersonalDataDestination.titleRes), // Show the title of the topBar banner.
                canNavigateBack = true, // Show the icon in the top left corner and make it works.
                navigateUp = navigateBack // Navigate to previous screen.
            )
        }
    ) { paddingValues ->
        val coroutineScope = rememberCoroutineScope()

        val context = LocalContext.current

        EditPersonalDataBody(
            paddingValues = paddingValues, // Needs for correct place the content body of the screen.
            personName = viewModel.editPersonalDataUiState.userName,
            emailAddress = viewModel.editPersonalDataUiState.emailAddress,
            onChangeEmailAddress = viewModel::updateEmailAddress,
            phoneNumber = viewModel.editPersonalDataUiState.phoneNumber,
            onChangePhoneNumber = viewModel::updatePhoneNumber,
            dateOfBirth = viewModel.editPersonalDataUiState.dateOfBirth,
            onChangeDateOfBirth = viewModel::updateDateOfBirth,
            address = viewModel.editPersonalDataUiState.address,
            onChangeAddress = viewModel::updateAddress,
            postalCode = viewModel.editPersonalDataUiState.postalCode,
            onChangePostalCode = viewModel::updatePostalCode,
            placeOfResidence = viewModel.editPersonalDataUiState.placeOfResidence,
            onChangePlaceOfResidence = viewModel::updatePlaceOfResidence,
            userRole = viewModel.editPersonalDataUiState.userRole,
            personalDescription = viewModel.editPersonalDataUiState.personalDescription,
            onChangePersonalDescription = viewModel::updatePersonDescription,
            savePersonalData = {
                coroutineScope.launch {
                    try {
                        val resultUpdateUser = viewModel.saveNewUserInformationIntoTheDatabase()

                        Toast.makeText(context, resultUpdateUser, Toast.LENGTH_LONG).show()

                    } catch (e: Exception) {
                        Toast.makeText(context, "catch ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }
}

@Composable
fun EditPersonalDataBody(
    paddingValues: PaddingValues,
    personName: String,
    emailAddress: String,
    onChangeEmailAddress: (String) -> Unit,
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
    userRole: String,
    personalDescription: String,
    onChangePersonalDescription: (String) -> Unit,
    savePersonalData: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize() // Make sure to fill the Edit Personal Data screen the whole screen.
    ) {
        // TextField Name.
        /* @Composable invocation can only call in a @Composable function,
             so you have to surround this with a item of a LazyColumn. */
        item {
            OutlinedTextField(
                value = personName,
                onValueChange = {},
                label = {
                    Text(stringResource(R.string.edit_personal_data_person_name))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = modifier.fillMaxWidth(),
                enabled = false,
                singleLine = true
            )
        }

        // TextField Email address.
        item {
            OutlinedTextField(
                value = emailAddress,
                onValueChange = onChangeEmailAddress,
                label = {
                    Text(stringResource(R.string.edit_personal_data_email_address))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledBorderColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        // TextField Phone number.
        item {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onChangePhoneNumber,
                label = {
                    Text(stringResource(R.string.edit_personal_data_phone_number))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledBorderColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        // TextField Date of Birth.
        item {
            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = onChangeDateOfBirth,
                label = {
                    Text(stringResource(R.string.edit_personal_data_date_of_birth))
                },
                placeholder = {
                    Text(stringResource(R.string.edit_personal_data_placeholder_date_of_birth))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        // TextField Address.
        item {
            OutlinedTextField(
                value = address,
                onValueChange = onChangeAddress,
                label = {
                    Text(stringResource(R.string.edit_personal_data_address))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        // TextField Postal code.
        item {
            OutlinedTextField(
                value = postalCode,
                onValueChange = onChangePostalCode,
                label = {
                    Text(stringResource(R.string.edit_personal_data_postal_code))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledBorderColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        // TextField Place of Residence.
        item {
            OutlinedTextField(
                value = placeOfResidence,
                onValueChange = onChangePlaceOfResidence,
                label = {
                    Text(stringResource(R.string.edit_personal_data_place_of_residence))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledBorderColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        // If the user is a dog sitter, show the description of the user, otherwise don't show this.
        if (userRole == "Oppasser") {
            // TextField Personal Description.
            item {
                OutlinedTextField(
                    value = personalDescription,
                    onValueChange = onChangePersonalDescription,
                    label = {
                        Text(stringResource(R.string.edit_personal_data_personal_description))
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledBorderColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        item {
            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
        }

        // Button Save.
        item {
            Button(
                onClick = savePersonalData,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.edit_personal_data_button_save))
            }
        }

        item {
            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_large)))
        }
    }
}