package com.example.dogwalkingservice.ui.navigation

/**
 * Interface om de navigatie voor de app te beschrijven.
 */
interface NavigationDestination {

    /**
     * Unieke naam die het pad van de composable definieert.
     */
    val route: String

    /**
     * String Resource ID die de titel bevat die op het scherm moet worden weergegeven.
     */
    val titleRes: Int
}