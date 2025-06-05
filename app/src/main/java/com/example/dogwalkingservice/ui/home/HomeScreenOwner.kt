package com.example.dogwalkingservice.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.dogwalkingservice.R
import com.example.dogwalkingservice.ui.navigation.NavigationDestination
import com.example.dogwalkingservice.ui.password.PasswordScreen

object HomeScreenOwnerDestination : NavigationDestination {
    override val route = "HomeOwner"
    override val titleRes = R.string.home_screen_owner
}

@Composable
fun HomeScreenOwner(
    testenNavigateToPasswordScreen: () -> Unit,
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