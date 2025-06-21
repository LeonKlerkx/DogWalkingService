package com.example.dogwalkingservice.ui.appointment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogwalkingservice.DogWalkingServiceTopAppBar
import com.example.dogwalkingservice.R
import com.example.dogwalkingservice.data.Afspraak
import com.example.dogwalkingservice.data.Gebruiker
import com.example.dogwalkingservice.ui.AppViewModelProvider
import com.example.dogwalkingservice.ui.navigation.NavigationDestination

object AppointmentOverviewDestination : NavigationDestination {
    override val route = "appointmentOverview"
    override val titleRes = R.string.appointment_overview_title
}

@OptIn(ExperimentalMaterial3Api::class) // Needs for DogWalkingServiceTopAppBar.
@Composable
fun AppointmentOverviewScreen(
    navigateBack: () -> Unit,
    navigateToAddAppointment: () -> Unit,
    navigateToDetailsAppointment: (Int) -> Unit,
    // Open the AppointmentOverviewViewModel in the AppViewModelProvider.Factory.
    viewModel: AppointmentOverviewViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Get the saved Email address from the DataStore.
    val getDogsitterEmailAddress by viewModel.getEmailaddressFromDatastore.collectAsState()
    /* Receive the whole object of the owner from the Database
       through the email address from the DataStore. */
    val getOwnerObject by viewModel.getDogSitterObject(getDogsitterEmailAddress.oppasser)
        .collectAsState(Gebruiker("","","","","","","","","",""))

    // Get the list of all appointment from the singed in dog sitter.
    val getAppointmentList by viewModel.getAllAppointsmentFromADogSitter(getOwnerObject.gebruikersnaam).collectAsState(emptyList())

    Scaffold(
        topBar = {
            DogWalkingServiceTopAppBar(
                title = stringResource(AppointmentOverviewDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddAppointment,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.appointment_add_item_title)
                )
            }
        }
    ) { paddingValues ->
        AppointmentScreen(
            contentPadding = paddingValues,
            appointmentList = getAppointmentList,
            onAppointmentClick = navigateToDetailsAppointment
        )
    }
}

@Composable
fun AppointmentScreen(
    contentPadding: PaddingValues,
    appointmentList: List<Afspraak>,
    onAppointmentClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (appointmentList.isEmpty()) {
        Text(
            text = stringResource(R.string.no_item_description, stringResource(R.string.no_appointments)),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(contentPadding) // Show the text on the screen.
        )
    }
    else {
        AppointmentList(
            contentPadding = contentPadding,
            appointmentList = appointmentList,
            onAppointmentClick = { onAppointmentClick(it.afspraakId) }
        )
    }

}

@Composable
fun AppointmentList(
    contentPadding: PaddingValues,
    appointmentList: List<Afspraak>,
    onAppointmentClick: (Afspraak) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        items(items = appointmentList, key = { it.afspraakId}) { appointment ->
            AppointmentItem(
                appointment = appointment,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .clickable { onAppointmentClick(appointment) }
            )
        }
    }
}

@Composable
fun AppointmentItem(
    appointment: Afspraak,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.appointment_text)
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.appointment_dog_sitter_name, appointment.oppasser)
                )
            }
            Text(
                text = appointment.beginmoment
            )
            Text(
                text = appointment.eindmoment
            )
        }
    }
}