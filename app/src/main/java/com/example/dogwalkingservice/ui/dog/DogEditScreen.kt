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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogwalkingservice.DogWalkingServiceTopAppBar
import com.example.dogwalkingservice.R
import com.example.dogwalkingservice.ui.AppViewModelProvider
import com.example.dogwalkingservice.ui.navigation.NavigationDestination
import com.example.dogwalkingservice.ui.theme.DogWalkingServiceTheme
import kotlinx.coroutines.launch

object DogEditScreenDestination : NavigationDestination {
    override val route = "editDog"
    override val titleRes = R.string.dog_edit_item_title
    const val chipnummer = "chipnummer"
    val routeWithChipnummer = "$route/{$chipnummer}"
}

@OptIn(ExperimentalMaterial3Api::class) // Needs for DogWalkingServiceTopAppBar.
@Composable
fun DogEditScreen(
    navigateBack: () -> Unit,
    // Open the DogEditViewModel in the AppViewModelProvider.Factory.
    viewModel: DogEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    // Get the selected dog item from the LazyColumn.
    val item = viewModel.dogUiState

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    Scaffold(
        topBar = {
            DogWalkingServiceTopAppBar(
                title = stringResource(DogEditScreenDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { paddingValues ->
        DogEditBody(
            contentPadding = paddingValues,
            dogItem = item,
            onChangeDogName = viewModel::updateDogName,
            onChangeDogBreed = viewModel::updateHondenras,
            updateDogIntoTheDatabase = {
                coroutineScope.launch {
                    try{
                        val result = viewModel.saveDog()
                        Toast.makeText(context, result, Toast.LENGTH_LONG).show()

                        // Navigate to the overview of dogs.
                        navigateBack()
                    }
                    catch (e: Exception){
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            },
            deleteDogIntoTheDatabase = {

            }
        )
    }
}

@Composable
fun DogEditBody(
    contentPadding: PaddingValues,
    dogItem: DogEditUiState,
    onChangeDogName: (String) -> Unit,
    onChangeDogBreed: (String) -> Unit,
    updateDogIntoTheDatabase: () -> Unit,
    deleteDogIntoTheDatabase: () -> Unit
) {
    var deleteDogConfirmation by rememberSaveable { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize() // Make sure to fill the dog info the whole screen.
    ) {
        // TextField chip number.
        OutlinedTextField(
            value = dogItem.chipnummer,
            onValueChange = {},
            label = {
                Text(stringResource(R.string.dog_chip_number))
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )

        // TextField dog name.
        OutlinedTextField(
            value = dogItem.hondnaam,
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
            enabled = true,
            singleLine = true
        )

        // TextField dog breed.
        OutlinedTextField(
            value = dogItem.hondenras,
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
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

        // Button Save Dog.
        Button(
            onClick = updateDogIntoTheDatabase,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.dog_add_save_button))
        }

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

        // Button Delete Dog.
        OutlinedButton(
            onClick = { deleteDogConfirmation = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.dog_delete_button))
        }

        if (deleteDogConfirmation) {
            DeleteConfirmationDialog(
                dogName = dogItem.hondnaam,
                onDeleteConfirm = {
                    deleteDogConfirmation = false
                    deleteDogIntoTheDatabase()
                },
                onDeleteCancel = {
                    deleteDogConfirmation = false
                },
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}

/**
 * Toont een alert om aan de eigenaar te vragen of de hond definitief verwijderd mag worden.
 */
@Composable
fun DeleteConfirmationDialog(
    dogName: String,
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
){
    AlertDialog(
        onDismissRequest = {},
        title = { Text(stringResource(R.string.alert_box_title)) },
        text = { Text(stringResource(R.string.alert_box_message, "hond ${dogName}")) },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = onDeleteCancel
            ) {
                Text(stringResource(R.string.alert_box_no))
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDeleteConfirm
            ) {
                Text(stringResource(R.string.alert_box_yes))
            }
        }
    )
}

@Preview
@Composable
fun DogEditScreenPreview() {
    DogWalkingServiceTheme {
        DogEditBody(
            contentPadding = PaddingValues(0.dp),
            dogItem = DogEditUiState(
                chipnummer = "NEDFLO570342401",
                hondnaam = "Spike",
                hondenras = "Shiba",
                eigenaar = "Leon"
            ),
            onChangeDogName = {},
            onChangeDogBreed = {},
            updateDogIntoTheDatabase = {},
            deleteDogIntoTheDatabase = {}
        )
    }
}