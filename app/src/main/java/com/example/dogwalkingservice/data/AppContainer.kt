package com.example.dogwalkingservice.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val gebruikersRepository: GebruikersRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineGebruikersRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {

    override val gebruikersRepository: GebruikersRepository by lazy {
        OfflineGebruikersRepository(DogWalkingServiceDatabase.getDatabase(context).gebruikerDao())
    }
}