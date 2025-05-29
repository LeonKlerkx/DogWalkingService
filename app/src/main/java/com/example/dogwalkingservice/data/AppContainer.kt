package com.example.dogwalkingservice.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    // Hier worden alle Repoositories neergezet die de applicatie moet gebruiken.

    val gebruikersRepository: GebruikersRepository
    val recensieRepository: RecensieRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineGebruikersRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {

    /**
     * Override the GebruikersRepository with the OfflineGebruikersRepository,
     * so you can you this repository in the whole application.
     */
    override val gebruikersRepository: GebruikersRepository by lazy {
        OfflineGebruikersRepository(DogWalkingServiceDatabase.getDatabase(context).gebruikerDao())
    }

    /**
     * Override the RecensieRepository with the OfflineRecensieRepository,
     * so you can you this repository in the whole application.
     */
    override val recensieRepository: RecensieRepository by lazy {
        OfflineRecensieRepository(DogWalkingServiceDatabase.getDatabase(context).recensieDao())
    }
}