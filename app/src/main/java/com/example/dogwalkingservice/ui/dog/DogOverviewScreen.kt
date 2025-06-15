package com.example.dogwalkingservice.ui.dog

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogwalkingservice.DogWalkingServiceTopAppBar
import com.example.dogwalkingservice.R
import com.example.dogwalkingservice.data.Gebruiker
import com.example.dogwalkingservice.data.Hond
import com.example.dogwalkingservice.ui.AppViewModelProvider
import com.example.dogwalkingservice.ui.navigation.NavigationDestination
import com.example.dogwalkingservice.ui.theme.DogWalkingServiceTheme

object DogOverviewDestination : NavigationDestination {
    override val route = "dogOverview"
    override val titleRes = R.string.dog_overview_title
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class) // Needs for DogWalkingServiceTopAppBar.
@Composable
fun DogOverviewScreen(
    navigateToAddDog: () -> Unit,
    navigateToEditDog: (String) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    // Open the DogOverviewViewModel in the AppViewModelProvider.Factory.
    viewModel: DogOverviewViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Get the saved Email address from the DataStore.
    val getOwnerEmailAddress by viewModel.getEmailAddress.collectAsState()
    Log.d("Owner email address", getOwnerEmailAddress.emailAddress)

    /* Receive the whole object of the owner from the Database
       through the email address from the DataStore. */
    val getOwnerObject by viewModel.getOwnerName(getOwnerEmailAddress.emailAddress)
        .collectAsState(Gebruiker("","","","","","","","","",""))
    Log.e("Owner user name", getOwnerObject.gebruikersnaam)

    // Get a list of dogs from the username of the signed in owner.
    val getDogList by viewModel.getDogs(getOwnerObject.gebruikersnaam).collectAsState(emptyList())
    Log.e("Hond list", "${getDogList.size}")

    Scaffold(
        topBar = {
            DogWalkingServiceTopAppBar(
                title = stringResource(DogOverviewDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddDog,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.dog_add_item_title)
                )
            }
        }
        ) { paddingValues ->
            DogListView(
                contentPadding = paddingValues,
                dogList = getDogList,
                onDogClick = navigateToEditDog
            )
        }
}

@Composable
fun DogListView(
    contentPadding: PaddingValues,
    dogList: List<Hond>,
    onDogClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        items(items = dogList, key = { it.chipNummer }) { dog ->
            DogItem(
                dog = dog,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .clickable { onDogClick(dog.chipNummer) }
            )
        }
    }
}

@Composable
fun DogItem(
    dog: Hond,
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
                    text = stringResource(R.string.dog_overview_dog_name, dog.hondnaam)
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.dog_overview_owner_dog, dog.eigenaar)
                )
            }
            Text(
                text = stringResource(R.string.dog_overview_dog_breed, dog.hondenras)
            )
        }
    }
}

@Preview
@Composable
fun DogListPreview() {
    DogWalkingServiceTheme {
        val owner1 = Gebruiker("LeonK", "leon@gmail.com", "ab1cd2", "06-12345678", "2000-1-1", "Onbekend", "0000XX", "Nederland", null, "Eigenaar")
        val owner2 = Gebruiker("PietJ", "piet@gmail.com", "ef3gh4", "06-87960543", "1998-7-5", "Onbekend", "1111YY", "Nederland", null, "Eigenaar")
        val dog1 = Hond("NEDFLO570342401", "Spike", "Shiba", owner1.gebruikersnaam)
        val dog2 = Hond("NEDSPY460166719", "Spy", "Golden Retriever", owner1.gebruikersnaam)
        val dog3 = Hond("NEDGUS247019698", "Guus", "Rottweiler", owner2.gebruikersnaam)
        DogListView(
            contentPadding = PaddingValues(0.dp),
            dogList = listOf(
                dog1, dog2, dog3
            ),
            onDogClick = {}
        )
    }
}

@Preview
@Composable
fun DogItemPreview() {
    DogWalkingServiceTheme {
        val owner = Gebruiker("LeonK", "leon@gmail.com", "ab1cd2", "06-12345678", "2000-1-1", "Onbekend", "0000XX", "Nederland", null, "Eigenaar")
        DogItem(
            dog = Hond("NEDFLO570342401", "Spike", "Shiba", owner.gebruikersnaam)
        )
    }
}