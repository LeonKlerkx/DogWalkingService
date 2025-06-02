package com.example.dogwalkingservice.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.dogwalkingservice.R
import com.example.dogwalkingservice.ui.navigation.NavigationDestination

object HomeScreenDogSitterDestination : NavigationDestination {
    override val route = "HomeDogSitter"
    override val titleRes = R.string.home_screen_dog_sitter
}

@Composable
fun HomeScreenDogSitter(
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(HomeScreenDogSitterDestination.titleRes)
        )
    }
}