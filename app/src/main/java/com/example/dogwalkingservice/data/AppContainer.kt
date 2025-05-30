package com.example.dogwalkingservice.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    // Hier worden alle Repoositories neergezet die de applicatie moet gebruiken.

    val gebruikersRepository: GebruikersRepository
    val recensieRepository: RecensieRepository
    val afsprakenRepository: AfsprakenRepository
    val hondenRepository: HondenRepository
    val aanmeldenHondenRepository: AanmeldenHondenRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineGebruikersRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {

    /**
     * Override the [GebruikersRepository] with the [OfflineGebruikersRepository],
     * so you can use this repository in the whole application.
     */
    override val gebruikersRepository: GebruikersRepository by lazy {
        OfflineGebruikersRepository(DogWalkingServiceDatabase.getDatabase(context).gebruikerDao())
    }

    /**
     * Override the [RecensieRepository] with the [OfflineRecensieRepository],
     * so you can use this repository in the whole application.
     */
    override val recensieRepository: RecensieRepository by lazy {
        OfflineRecensieRepository(DogWalkingServiceDatabase.getDatabase(context).recensieDao())
    }

    /**
     * Override the [AfsprakenRepository] with the [OfflineAfsprakenRepository],
     * so you can use this repository in the whole application.
     */
    override val afsprakenRepository: AfsprakenRepository by lazy {
        OfflineAfsprakenRepository(DogWalkingServiceDatabase.getDatabase(context).afspraakDao())
    }

    /**
     * Override the [HondenRepository] with the [OfflineHondenRepository],
     * so you can use this repository in the whole application.
     */
    override val hondenRepository: HondenRepository by lazy {
        OfflineHondenRepository(DogWalkingServiceDatabase.getDatabase(context).hondDao())
    }

    /**
     * Override the [AanmeldenHondenRepository] with the [OfflineAanmeldenHondenRepository],
     * so you can use this repository in the whole application.
     */
    override val aanmeldenHondenRepository: AanmeldenHondenRepository by lazy {
        OfflineAanmeldenHondenRepository(DogWalkingServiceDatabase.getDatabase(context).aanmeldenHondDao())
    }
}