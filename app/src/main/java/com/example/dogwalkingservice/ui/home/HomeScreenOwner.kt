package com.example.dogwalkingservice.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogwalkingservice.R
import com.example.dogwalkingservice.ui.AppViewModelProvider
import com.example.dogwalkingservice.ui.login.LoginViewModel
import com.example.dogwalkingservice.ui.navigation.NavigationDestination
import com.example.dogwalkingservice.ui.password.PasswordScreen

object HomeScreenOwnerDestination : NavigationDestination {
    override val route = "HomeOwner"
    override val titleRes = R.string.home_screen_owner
}

@Composable
fun HomeScreenOwner(
    testenNavigateToPasswordScreen: () -> Unit,
    navigateToSettingScreen: () -> Unit,
    navigateToPictureDogScreen: () -> Unit,
    navigateToOverviewDogScreen: () -> Unit,
    navigateToAddDogToAnAppointment: () -> Unit,
    uitloggen: () -> Unit,
    // Open the HomeScreenOwnerViewModel in the AppViewModelProvider.Factory.
    viewModel: HomeScreenOwnerViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val uiStateDataStoreEmailAddress by viewModel.uiStateEmailAddress.collectAsState()

    val uiStateDataStoreUserRole by viewModel.uiStateUserRole.collectAsState()

    HomeScreenOwnerBody(
        getEmailaddressFromDataStore = uiStateDataStoreEmailAddress,
        getUserRoleFromDataStore = uiStateDataStoreUserRole,
        testenNavigateToPasswordScreen = testenNavigateToPasswordScreen,
        navigateToSettingScreen = navigateToSettingScreen,
        navigateToPictureDogScreen = navigateToPictureDogScreen,
        navigateToOverviewDogScreen = navigateToOverviewDogScreen,
        navigateToAddDogToAnAppointment = navigateToAddDogToAnAppointment,
        uitloggen = {
            /* Delete the email address from the DataStore in the entry screen of the application,
        so the user can always fill in an email address. */
            viewModel.deleteEmailAddressAndUserRoleInDataStore()
            uitloggen()
        }
    )
}


@Composable
fun HomeScreenOwnerBody(
    getEmailaddressFromDataStore: HomeScreenUiState,
    getUserRoleFromDataStore: HomeScreenUiState,
    testenNavigateToPasswordScreen: () -> Unit,
    navigateToSettingScreen: () -> Unit,
    navigateToPictureDogScreen: () -> Unit,
    navigateToOverviewDogScreen: () -> Unit,
    navigateToAddDogToAnAppointment: () -> Unit,
    uitloggen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(HomeScreenOwnerDestination.titleRes)
        )

        Text(
            text = "E-mailadres: ${getEmailaddressFromDataStore.emailAddress} User Rol: ${getUserRoleFromDataStore.userRole}"
        )

        Button(
            onClick = navigateToSettingScreen,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Instellingen")
        }

        Button(
            onClick = navigateToPictureDogScreen,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Afbeeldingen hond toevoegen")
        }

        Button(
            onClick = navigateToOverviewDogScreen,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Overzicht honden")
        }

        Button(
            onClick = navigateToAddDogToAnAppointment,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Honden aan afspraak toevoegen")
        }

        Button(
            onClick = testenNavigateToPasswordScreen,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Wachtwoord wijzigen")
        }

        Button(
            onClick = uitloggen,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Uitloggen")
        }
    }
}
