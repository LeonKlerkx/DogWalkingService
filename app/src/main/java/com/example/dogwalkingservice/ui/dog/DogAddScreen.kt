package com.example.dogwalkingservice.ui.dog

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogwalkingservice.DogWalkingServiceTopAppBar
import com.example.dogwalkingservice.R
import com.example.dogwalkingservice.data.Gebruiker
import com.example.dogwalkingservice.ui.AppViewModelProvider
import com.example.dogwalkingservice.ui.navigation.NavigationDestination
import com.example.dogwalkingservice.ui.theme.DogWalkingServiceTheme
import kotlinx.coroutines.launch

object DogAddScreenDestination : NavigationDestination {
    override val route = "addDog"
    override val titleRes = R.string.dog_add_item_title
}

@OptIn(ExperimentalMaterial3Api::class) // Needs for DogWalkingServiceTopAppBar.
@Composable
fun DogAddScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    // Open the DogAddViewModel in the AppViewModelProvider.Factory.
    viewModel: DogAddViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Get only the email address of the signed in user from the DataStore.
    val getEmailAddressFromDataStore by viewModel.getEmailaddressFromDatastore.collectAsState()

    /* Receive the whole object of the owner from the Database
       through the email address from the DataStore. */
    val getOwnerObject by viewModel.getOwnerObject(getEmailAddressFromDataStore.ownerEmailaddressDataStore)
        .collectAsState(Gebruiker("", "", "", "", "", "", "", "", "", ""))


    Scaffold(
        topBar = {
            DogWalkingServiceTopAppBar(
                title = stringResource(DogAddScreenDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { paddingValues ->

        DogAddBody(
            contentPadding = paddingValues,
            chipNumber = viewModel.dogUiState.chipnummer,
            onChangeChipNumber = viewModel::updateChipNumber,
            dogName = viewModel.dogUiState.hondnaam,
            onChangeDogName = viewModel::updateDogName,
            dogBreed = viewModel.dogUiState.hondenras,
            onChangeDogBreed = viewModel::updateDogBreed,
            saveDog = {
                coroutineScope.launch {
                    try{
                        val result = viewModel.saveDog(getOwnerObject.gebruikersnaam)
                        Toast.makeText(context, result, Toast.LENGTH_LONG).show()

                        // Navigate to the overview of dogs.
                        navigateBack()
                    }
                    catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        )


    }
}

@Composable
fun DogAddBody(
    contentPadding: PaddingValues,
    chipNumber: String,
    onChangeChipNumber: (String) -> Unit,
    dogName: String,
    onChangeDogName: (String) -> Unit,
    dogBreed: String,
    onChangeDogBreed: (String) -> Unit,
    saveDog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize()
    ) {
        // TextField Chip number.
        OutlinedTextField(
            value = chipNumber,
            onValueChange = onChangeChipNumber,
            label = {
                Text(stringResource(R.string.dog_chip_number))
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

        // TextField dog name.
        OutlinedTextField(
            value = dogName,
            onValueChange = onChangeDogName,
            label = {
                Text(stringResource(R.string.dog_name))
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

        // TextField dog breed.
        OutlinedTextField(
            value = dogBreed,
            onValueChange = onChangeDogBreed,
            label = {
                Text(stringResource(R.string.dog_breed))
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            modifier = modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

        // Button Save dog.
        Button(
            onClick = saveDog,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.dog_add_save_button))
        }
    }
}

@Preview
@Composable
fun DogAddScreenPreview() {
    DogWalkingServiceTheme {
        DogAddBody(
            contentPadding = PaddingValues(0.dp),
            chipNumber = "NEDSPY460166719",
            onChangeChipNumber = {},
            dogName = "Spy",
            onChangeDogName = {},
            dogBreed = "Golden Retriever",
            onChangeDogBreed = {},
            saveDog = {}
        )
    }
}