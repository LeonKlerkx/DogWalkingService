package com.example.dogwalkingservice

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dogwalkingservice.ui.navigation.DogWalkingServiceNavGraph

@Composable
fun DogWalkingServiceApp(
    navController: NavHostController = rememberNavController()
) {
    DogWalkingServiceNavGraph(navController = navController)
}