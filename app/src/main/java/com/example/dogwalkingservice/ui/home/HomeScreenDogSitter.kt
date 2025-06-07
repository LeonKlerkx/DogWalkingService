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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogwalkingservice.R
import com.example.dogwalkingservice.ui.AppViewModelProvider
import com.example.dogwalkingservice.ui.navigation.NavigationDestination
import com.example.dogwalkingservice.ui.theme.DogWalkingServiceTheme

object HomeScreenDogSitterDestination : NavigationDestination {
    override val route = "HomeDogSitter"
    override val titleRes = R.string.home_screen_dog_sitter
}

@Composable
fun HomeScreenDogSitter(
    navigateToSettingScreen: () -> Unit,
    navigateToAppointmentScreen: () -> Unit,
    // Open the HomeScreenOwnerViewModel in the AppViewModelProvider.Factory.
    viewModel: HomeScreenOwnerViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val uiStateDataStoreEmailAddress by viewModel.uiStateEmailAddress.collectAsState()

    val uiStateDataStoreUserRole by viewModel.uiStateUserRole.collectAsState()

    HomeScreenDogSitterBody(
        getEmailaddressFromDataStore = uiStateDataStoreEmailAddress,
        getUserRoleFromDataStore = uiStateDataStoreUserRole,
        navigateToSettingScreen = navigateToSettingScreen,
        navigateToAppointmentScreen = navigateToAppointmentScreen
    )
}

@Composable
fun HomeScreenDogSitterBody(
    getEmailaddressFromDataStore: HomeScreenUiState,
    getUserRoleFromDataStore: HomeScreenUiState,
    navigateToSettingScreen: () -> Unit,
    navigateToAppointmentScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(HomeScreenDogSitterDestination.titleRes)
        )

        Text(
            text = "E-mailadres: ${getEmailaddressFromDataStore.emailAddress}\nUser Rol: ${getUserRoleFromDataStore.userRole}"
        )

        Button(
            onClick = navigateToSettingScreen,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Instellingen")
        }

        Button(
            onClick = navigateToAppointmentScreen,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Afspraken")
        }
    }
}

@Preview
@Composable
fun HomeScreenDogSitterPreview() {
    DogWalkingServiceTheme {
        HomeScreenDogSitterBody(
            getEmailaddressFromDataStore = HomeScreenUiState("oppasser@gmail.com", ""),
            getUserRoleFromDataStore = HomeScreenUiState("", "Oppasser"),
            navigateToSettingScreen = {},
            navigateToAppointmentScreen = {}
        )
    }
}