package com.example.dogwalkingservice.ui.dogsignin

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogwalkingservice.DogWalkingServiceTopAppBar
import com.example.dogwalkingservice.R
import com.example.dogwalkingservice.data.AanmeldenHond
import com.example.dogwalkingservice.ui.AppViewModelProvider
import com.example.dogwalkingservice.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object DogSignInDetailsDestination : NavigationDestination {
    override val route = "dogSignInDetails"
    override val titleRes = R.string.dog_sign_in_details_title
    const val appointmentId = "appointmentId"
    const val chipNumber = "chipNumber"
    val test = "$route/{$chipNumber}"
    val routeWithAppointmentIdAndChipNumber = "$route/{$appointmentId}/{$chipNumber}"
}

@OptIn(ExperimentalMaterial3Api::class) // Needs for DogWalkingServiceTopAppBar
@Composable
fun DogSignInDetailsScreen(
    navigateBack: () -> Unit,
    // Open the DogSignInDetailsViewModel in the AppViewModelProvider.Factory.
    viewModel: DogSignInDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val getList = viewModel.dogSignInDetailsUiState

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            DogWalkingServiceTopAppBar(
                title = stringResource(DogSignInDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { paddingValues ->
        DogSignInDetailsBody(
            contentPadding = paddingValues,
            signInDogList = getList.listOfAllSignInDogs,
            selectedAppointment = viewModel.dogSignInDetailsUiState.geselecteerdeAfspraakID,
            selectedDog = viewModel.dogSignInDetailsUiState.geselecteerdeChipnummer,
            deleteSignInDogFromTheAppointment = {
                coroutineScope.launch {
                    val getResult = viewModel.deleteSignInDogFromTheAppointment()
                    Toast.makeText(context, getResult, Toast.LENGTH_LONG).show()

                    // Navigate to the overview of all sign in dogs from all appointments.
                    navigateBack()
                }
            }
        )
    }
}

@Composable
fun DogSignInDetailsBody(
    contentPadding: PaddingValues,
    signInDogList: List<AanmeldenHond>,
    selectedAppointment: Int,
    selectedDog: String,
    deleteSignInDogFromTheAppointment: () -> Unit,
    modifier: Modifier = Modifier
) {
    var deleteDogFromAppointmentConfirmation by rememberSaveable { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
    ) {
        for (aanmeldenHond in signInDogList) {
            SignInDogItem(
                signInDogItem = aanmeldenHond,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
            )
        }

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

        Button(
            onClick = { deleteDogFromAppointmentConfirmation = true },
            modifier = Modifier.fillMaxWidth()
        ) {
        //    Text(text = "Afspraak $selectedAppointment - hond $selectedDog")
            Text(text = "Verwijder aanmelding hond uit afspraak")
        }

        if (deleteDogFromAppointmentConfirmation) {
            DeleteConfirmationDialog(
                dogName = selectedDog,
                onDeleteConfirm = {
                    deleteDogFromAppointmentConfirmation = false
                    deleteSignInDogFromTheAppointment()
                },
                onDeleteCancel =  {
                    deleteDogFromAppointmentConfirmation = false
                },
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun SignInDogItem(
    signInDogItem: AanmeldenHond,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))
        ) {
            Text(
                text = "AfspraakID: ${signInDogItem.afspraakId}"
            )
            Text(
                text = "Chipnummer: ${signInDogItem.chipNummer}"
            )
        }
    }
}

/**
 * Toont een alert om aan de eigenaar te vragen
 * of de aanmelding van de hond definitief uit de afspraak  verwijderd mag worden.
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