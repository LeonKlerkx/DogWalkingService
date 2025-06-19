package com.example.dogwalkingservice.ui.appointment

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogwalkingservice.DogWalkingServiceTopAppBar
import com.example.dogwalkingservice.R
import com.example.dogwalkingservice.data.Gebruiker
import com.example.dogwalkingservice.ui.AppViewModelProvider
import com.example.dogwalkingservice.ui.navigation.NavigationDestination
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object AppointmentAddDestination : NavigationDestination {
    override val route = "addAppointment"
    override val titleRes = R.string.appointment_add_title
}

@OptIn(ExperimentalMaterial3Api::class) // Needs for DogWalkingServiceTopAppBar.
@Composable
fun AppointmentAddScreen(
    navigateBack: () -> Unit,
    // Open the AppointmentAddViewModel in the AppViewModelProvider.Factory.
    viewModel: AppointmentAddViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    // Get only the email address of the signed in user from the DataStore.
    val getEmailAddressFromDataStore by viewModel.getEmailaddressFromDatastore.collectAsState()

    /* Receive the whole object of the dog sitter from the Database
       through the email address from the DataStore. */
    val getDogSitterObject by viewModel.getDogSitterObject(getEmailAddressFromDataStore.oppasser)
        .collectAsState(Gebruiker("", "", "", "", "", "", "", "", "", ""))


    Scaffold(
        topBar = {
            DogWalkingServiceTopAppBar(
                title = stringResource(AppointmentAddDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { paddingValues ->
        AppointmentAddBody(
            contentPadding = paddingValues,
            onChangeStartDay = viewModel::updateStartDay,
            onchangeStartTime = viewModel::updateStartTime,
            onChangeEndDay = viewModel::updateEndDay,
            onChangeEndTime = viewModel::updateEndTime,
            saveAppointment = {
                coroutineScope.launch {
                    try {
                        val result = viewModel.saveAppointment(getDogSitterObject.gebruikersnaam)

                        Toast.makeText(context, result, Toast.LENGTH_LONG).show()

                        // Navigate to the overview of appointments.
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
fun AppointmentAddBody(
    contentPadding: PaddingValues,
    onChangeStartDay: (LocalDate) -> Unit,
    onchangeStartTime: (LocalTime) -> Unit,
    onChangeEndDay: (LocalDate) -> Unit,
    onChangeEndTime: (LocalTime) -> Unit,
    saveAppointment: () -> Unit
) {
    val context = LocalContext.current

    var pickedStartDate by remember { mutableStateOf(LocalDate.now()) }
    var pickedStartTime by remember { mutableStateOf(LocalTime.now()) }

    var pickedEndDate by remember { mutableStateOf(LocalDate.now()) }
    var pickedEndTime by remember { mutableStateOf(LocalTime.now()) }

    val formattedStartDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd MMMM yyyy")
                .format(pickedStartDate)
        }
    }

    val formattedStartTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("HH:mm")
                .format(pickedStartTime)
        }
    }

    val formattedEndDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("dd MMMM yyyy").format(pickedEndDate)
        }
    }

    val formattedEndTime by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("HH:mm").format(pickedEndTime)
        }
    }

    // Create a start date dialog state variable.
    val startDateDialogState = rememberMaterialDialogState()
    // Create a start time dialog state variable.
    val startTimeDialogState = rememberMaterialDialogState()

    val endDateDialogState = rememberMaterialDialogState()
    val endTimeDialogState = rememberMaterialDialogState()

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize()
    ) {

        item {
            // Button to select the start day of the appointment.
            Button(
                onClick = {
                    // Show the start date dialog.
                    /* The date picker will be only show if this variable is used in the MaterialDialog,
                       otherwise the button has no effect. */
                    startDateDialogState.show()
                },
            ) {
                Text(text = stringResource(R.string.appointment_select_start_day))
            }
            Text(
                text = stringResource(R.string.appointment_choosed_start_day, formattedStartDate)
            )
        }

        item {
            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
        }

        item {
            // Button to select the start time of the appointment.
            Button(
                onClick = {
                    // Show the start time dialog.
                    /* The time picker will be only show if this variable is used in the MaterialDialog,
                   otherwise the button has no effect. */
                    startTimeDialogState.show()
                },
            ) {
                Text(text = stringResource(R.string.appointment_select_start_time))
            }
            Text(
                text = stringResource(R.string.appointment_choosed_start_time, formattedStartTime)
            )
        }

        item {
            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
        }

        item {
            // Button to select the end day of the appointment.
            Button(
                onClick = {
                    // Show the end date dialog.
                    /* The date picker will be only show if this variable is used in the MaterialDialog,
                   otherwise the button has no effect. */
                    endDateDialogState.show()
                }
            ) {
                Text(text = stringResource(R.string.appointment_select_end_day))
            }
            Text(
                text = stringResource(R.string.appointment_choosed_end_day, formattedEndDate)
            )
        }

        item {
            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
        }

        item {
            // Button to select the end time of the appointment.
            Button(
                onClick = {
                    // Show the end time dialog.
                    /* The time picker will be only show if this variable is used in the MaterialDialog,
                   otherwise the button has no effect. */
                    endTimeDialogState.show()
                }
            ) {
                Text(text = stringResource(R.string.appointment_select_end_time))
            }
            Text(
                text = stringResource(R.string.appointment_choosed_end_time, formattedEndTime)
            )
        }

        item {
            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        }

        item {
            // Button Save appointment.
            Button(
                onClick = saveAppointment
            ) {
                Text(
                    text = stringResource(R.string.appointment_add_save_button)
                )
            }
        }
    }



    // Make the Start Date Picker Dialog works.
    MaterialDialog(
        dialogState = startDateDialogState,
         properties = DialogProperties(
             // When the user clicks outside the dialog, this will be disappear.
             dismissOnClickOutside = true
         ),
        buttons = {
            positiveButton(text = stringResource(R.string.appointment_material_dialog_positive_button)) {
              //  Toast.makeText(context, "Begin dag opgeslagen", Toast.LENGTH_LONG).show()
            }
            negativeButton(text = stringResource(R.string.appointment_material_dialog_negative_button))
        }
    ) {
        datepicker(
            initialDate = pickedStartDate,
            title = stringResource(R.string.appointment_material_dialog_date_picker_title),
        ) {
            // Save the selected date of the user.
            pickedStartDate = it
            onChangeStartDay(it)
        }
    }

    // Make the Start Time Picker Dialog works.
    MaterialDialog(
        dialogState = startTimeDialogState,
        properties = DialogProperties(
            dismissOnClickOutside = true
        ),
        onCloseRequest = {
            it.hide()
        },
        buttons = {
            positiveButton(text = stringResource(R.string.appointment_material_dialog_positive_button)) {
              //  Toast.makeText(context, "Begin tijd opgeslagen", Toast.LENGTH_LONG).show()
            }
            negativeButton(text = stringResource(R.string.appointment_material_dialog_negative_button))
        }
    ) {
        timepicker(
            initialTime = pickedStartTime,
            title = stringResource(R.string.appointment_material_dialog_time_picker_title),
            timeRange = LocalTime.of(2, 0) ..LocalTime.of(23, 0),
            is24HourClock = true
        ) {
            // Save the selected time of the user.
            pickedStartTime = it
            onchangeStartTime(it)
        }
    }

    // Make the End Date Picker Dialog works.
    MaterialDialog(
        dialogState = endDateDialogState,
        properties = DialogProperties(
            dismissOnClickOutside = true
        ),
        buttons = {
            positiveButton(text = stringResource(R.string.appointment_material_dialog_positive_button)) {
              //  Toast.makeText(context, "Eind dag opgeslagen", Toast.LENGTH_LONG).show()
            }
            negativeButton(text = stringResource(R.string.appointment_material_dialog_negative_button))
        }
    ) {
        datepicker(
            initialDate = pickedEndDate,
            title = stringResource(R.string.appointment_material_dialog_date_picker_title)
        ) {
            // Save the selected date of the user.
            pickedEndDate = it
            onChangeEndDay(it)
        }
    }

    // Make the End Time Picker Dialog works.
    MaterialDialog(
        dialogState = endTimeDialogState,
        properties = DialogProperties(
            dismissOnClickOutside = true
        ),
        buttons = {
            positiveButton(text = stringResource(R.string.appointment_material_dialog_positive_button)) {
              //  Toast.makeText(context, "Eind tijd opgeslagen", Toast.LENGTH_LONG).show()
            }
            negativeButton(text = stringResource(R.string.appointment_material_dialog_negative_button))
        }
    ) {
        timepicker(
            initialTime = pickedEndTime,
            title = stringResource(R.string.appointment_material_dialog_time_picker_title),
            is24HourClock = true
        ) {
            // Save the selected time of the user.
            pickedEndTime = it
            onChangeEndTime(it)
        }
    }
}