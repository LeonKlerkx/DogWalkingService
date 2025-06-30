package com.example.dogwalkingservice.ui.dogsignin

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogwalkingservice.DogWalkingServiceTopAppBar
import com.example.dogwalkingservice.R
import com.example.dogwalkingservice.data.AanmeldenHond
import com.example.dogwalkingservice.data.Afspraak
import com.example.dogwalkingservice.data.Gebruiker
import com.example.dogwalkingservice.data.Hond
import com.example.dogwalkingservice.ui.AppViewModelProvider
import com.example.dogwalkingservice.ui.navigation.NavigationDestination
import com.example.dogwalkingservice.ui.theme.DogWalkingServiceTheme

object DogSignInOverviewDestination : NavigationDestination {
    override val route = "dogSignInOverview"
    override val titleRes = R.string.dog_sign_in_overview_title
}

@OptIn(ExperimentalMaterial3Api::class) // Needs for DogWalkingServiceTopAppBar.
@Composable
fun DogSignInOverviewScreen(
    navigateBack: () -> Unit,
    navigateToAddSignInDog: () -> Unit,
    navigateToDetailsAppointment: (AanmeldenHondEnHondClass) -> Unit,
    // Open the DogSignInOverviewViewModel in the AppViewModelProvider.Factory.
    viewModel: DogSignInOverviewViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Get the email address from the DataStore.
    val getEmailAddressOwner by viewModel.getEmailAddressFromDataStore.collectAsState()

    // Get the whole owner object.
    val ownerObject: Gebruiker by viewModel.getWholeUserObject(getEmailAddressOwner.eigenaar)
        .collectAsState(Gebruiker("","","","","","","","",null,""))

    // Get all dogs from the sign in owner.
    viewModel.getAllDogsFromTheOwner(ownerObject.gebruikersnaam)


    Scaffold(
        topBar = {
            DogWalkingServiceTopAppBar(
                title = stringResource(DogSignInOverviewDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddSignInDog,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.dog_sign_in_add_title)
                )
            }
        }
    ) { paddingValues ->
        DogSignInScreen(
            contentPadding = paddingValues,
            signInDogList = viewModel.dogSignInUiState.aanmeldenHondEnHondObject,
            onAppointmentClick = navigateToDetailsAppointment
        )
    }
}

@Composable
fun DogSignInScreen(
    contentPadding: PaddingValues,
    signInDogList: List<AanmeldenHondEnHondClass>,
    onAppointmentClick: (AanmeldenHondEnHondClass) -> Unit,
    modifier: Modifier = Modifier
) {
    if (signInDogList.isEmpty()) {
        Text(
            text = stringResource(R.string.no_item_description, stringResource(R.string.no_sign_in_dogs)),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(contentPadding) // Show the text on the screen.
        )
    }
    else {
        DogSignInList(
            contentPadding = contentPadding,
            signInDogList = signInDogList,
            onAppointmentClick = { aanmeldenHondEnHondClass ->// hond, afspraak -> // (AanmeldenHondEnHondClass) -> gebruiken
          //      onAppointmentClick(hond, afspraak)
                onAppointmentClick(aanmeldenHondEnHondClass)
            }
        )
    }
}

@Composable
fun DogSignInList(
    contentPadding: PaddingValues,
    signInDogList: List<AanmeldenHondEnHondClass>,
    onAppointmentClick: (AanmeldenHondEnHondClass) -> Unit,  //(Hond, Afspraak) -> Unit, // (AanmeldenHondEnHondClass) -> Unit, gebruiken
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        items (items = signInDogList) {signInDog ->
            DogSignInBody(
                signInDog = signInDog,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .clickable { onAppointmentClick(signInDog) }
            )
        }
    }
}

@Composable
fun DogSignInBody(
    signInDog: AanmeldenHondEnHondClass,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_large))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Naam: ${signInDog.hondnaam}"
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Eigenaar: ${signInDog.eigenaar}"
                )
            }
            Text(
                text = "Ras: ${signInDog.hondenras}"
            )
            Text(
                text = "Afspraak: ${signInDog.afspraakId}\nChipnummer: ${signInDog.chipnummer}"
            )
        }
    }
}

@Preview
@Composable
fun DogSignInOverviewPreview() {
    DogWalkingServiceTheme {
        val signInDogAndDogObject1 = AanmeldenHondEnHondClass(1, "NEDFLO570342401", "Spike", "Shiba", "Leon")
        val signInDogAndDogObject2 = AanmeldenHondEnHondClass(1, "NEDGUS247019698", "Guus", "Rottweiler", "Leon")

        DogSignInScreen(
            contentPadding = PaddingValues(0.dp),
            signInDogList = listOf(
                signInDogAndDogObject1,
                signInDogAndDogObject2
            ),
            onAppointmentClick = {}
        )
    }
}

@Preview
@Composable
fun DogSignInItemPreview() {
    DogWalkingServiceTheme {
        val signInDogAndDogObject1 = AanmeldenHondEnHondClass(2, "NEDSPY460166719", "Spy", "Golden Retriever", "Leon")
        DogSignInBody(
            signInDog = signInDogAndDogObject1
        )
    }
}

@Preview
@Composable
fun DogSignInEmptyListPreview() {
    DogWalkingServiceTheme {
        DogSignInScreen(
            contentPadding = PaddingValues(0.dp),
            signInDogList = listOf(),
            onAppointmentClick = {}
        )
    }
}