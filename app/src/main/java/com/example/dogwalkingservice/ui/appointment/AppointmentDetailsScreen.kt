package com.example.dogwalkingservice.ui.appointment

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogwalkingservice.DogWalkingServiceTopAppBar
import com.example.dogwalkingservice.R
import com.example.dogwalkingservice.ui.AppViewModelProvider
import com.example.dogwalkingservice.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

object AppointmentDetailsDestination : NavigationDestination {
    override val route = "appointmentDetails"
    override val titleRes = R.string.appointment_details_title
    const val appointmentId = "afspraakId"
    val routeWithAppointmentId = "$route/{$appointmentId}"
}

@OptIn(ExperimentalMaterial3Api::class) // Needs for DogWalkingServiceTopAppBar.
@Composable
fun AppointmentDetailsScreen(
    navigateBack: () -> Unit,
    // Open the AppointmentDetailsViewModel in the AppViewModelProvider.Factory.
    viewModel: AppointmentDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Get the selected appointment from the LazyColumn.
    val detailsAppointment = viewModel.appointmentUiState

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            DogWalkingServiceTopAppBar(
                title = stringResource(AppointmentDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { paddingValues ->
        AppointmentDetailsBody(
            contentPadding = paddingValues,
            detailsAppointment = detailsAppointment,
            deleteAppointment = {
                coroutineScope.launch {
                    try {
                        val result = viewModel.deleteAppointment()

                        Toast.makeText(context, result, Toast.LENGTH_LONG).show()

                        // Navigate to the overview of appointments.
                        navigateBack()
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }
        )
    }
}

@Composable
fun AppointmentDetailsBody(
    contentPadding: PaddingValues,
    detailsAppointment: AppointmentDetailsUiState,
    deleteAppointment: () -> Unit
) {
    var deleteAppointmentConfirmation by rememberSaveable { mutableStateOf(false) }

    val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val formatStartingMoment = detailsAppointment.beginDag.format(dateFormat)
    val formatEndMoment = detailsAppointment.eindDag.format(dateFormat)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.appointment_starting_moment, "$formatStartingMoment ${detailsAppointment.beginTijd}")
        )
        Text(
            text = stringResource(R.string.appointment_end_moment, "$formatEndMoment ${detailsAppointment.eindTijd}")
        )
        Text(
            text = if(detailsAppointment.signInDogs.isEmpty()) stringResource(R.string.appointment_no_sign_in_dogs)
                    else stringResource(R.string.appointment_sign_in_dogs, detailsAppointment.signInDogs),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

        Button (
            onClick = { deleteAppointmentConfirmation = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.appointment_delete_button))
        }
        if (deleteAppointmentConfirmation) {
            DeleteAppointmentConfirmationDialog(
                onDeleteConfirm = {
                    deleteAppointmentConfirmation = false
                    deleteAppointment()
                },
                onDeleteCancel = {
                    deleteAppointmentConfirmation = false
                }
            )
        }
    }
}

@Composable
fun DeleteAppointmentConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(stringResource(R.string.alert_box_title)) },
        text = { Text(stringResource(R.string.alert_box_message, "de afspraak")) },
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