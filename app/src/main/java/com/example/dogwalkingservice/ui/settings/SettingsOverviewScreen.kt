package com.example.dogwalkingservice.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogwalkingservice.R
import com.example.dogwalkingservice.ui.AppViewModelProvider
import com.example.dogwalkingservice.ui.navigation.NavigationDestination
import com.example.dogwalkingservice.ui.theme.DogWalkingServiceTheme

object SettingsOverviewDestination : NavigationDestination {
    override val route = "settingsOverview"
    override val titleRes = R.string.setting_overview_title
}

@Composable
fun SettingsOverviewScreen(
    navigateToEditPersonalData: () -> Unit,
    navigateToEditPassword: () -> Unit,
    logOut: () -> Unit,
    // Open the SettingsOverviewViewModel in the AppViewModelProvider.Factory.
    viewModel: SettingsOverviewViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    SettingsBody(
        navigateToEditPersonalData = navigateToEditPersonalData,
        navigateToEditPassword = navigateToEditPassword,
        logOut = {
            viewModel.deleteEmailAddressAndUserRoleInDataStore()

            logOut()
        }
    )
}

@Composable
fun SettingsBody(
    navigateToEditPersonalData: () -> Unit,
    navigateToEditPassword: () -> Unit,
    logOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Text(
            text = stringResource(SettingsOverviewDestination.titleRes)
        )

        // Button Personal Data.
        Button(
            onClick = navigateToEditPersonalData,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.setting_overview_personal_data))
        }

        // Button Edit Password.
        Button(
            onClick = navigateToEditPassword,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.setting_overview_edit_password))
        }

        // Button Logout.
        Button(
            onClick = logOut,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.setting_overview_logout))
        }
    }
}

@Preview
@Composable
fun SettingsPreview() {
    DogWalkingServiceTheme {
        SettingsBody(
            navigateToEditPersonalData = {},
            navigateToEditPassword = {},
            logOut = {}
        )
    }
}