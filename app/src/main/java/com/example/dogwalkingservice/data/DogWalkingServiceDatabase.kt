package com.example.dogwalkingservice.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Gebruiker::class, Recensie::class, Afspraak::class, Hond::class, AanmeldenHond::class], version = 1, exportSchema = false)
abstract class DogWalkingServiceDatabase : RoomDatabase() {

    abstract fun gebruikerDao(): GebruikerDao

    abstract fun recensieDao(): RecensieDao

    abstract fun afspraakDao(): AfspraakDao

    abstract fun hondDao(): HondDao

    abstract fun aanmeldenHondDao(): AanmeldenHondDao

    companion object {

        /**
         * [Instance] houdt de referentie van de Database bij, wanneer er een is gemaakt.
         *
         * [Volatile]: De waardes die met [Volatile] zijn gemarkeerd, worden nooit gecached en alle lees- en schrijfbewerkingen zijn van en naar de hoofdthread.
         * Deze functie zorgt ervoor dat de waarde van [Instance] altijd up-to-date is en hetzelfde is voor alle uitvoeringsthreads.
         * Dit betekent dat wijzigingen die door één thread zijn aangebracht in [Instance] direct zichtbaar zijn voor alle andere threads.
         */
        @Volatile
        private var Instance: DogWalkingServiceDatabase? = null

        fun getDatabase(context: Context) : DogWalkingServiceDatabase {
            // Geeft de Database terug OF als de Database null is, initialiseer de Database in een synchronized block.
            return Instance ?: synchronized(this) {
                // Haal de Database op met databaseBuilder van Room
                Room.databaseBuilder(
                    context,
                    klass = DogWalkingServiceDatabase::class.java, // Database class
                    name = "dog_walking_service_database") // Naam van de Database
                    .fallbackToDestructiveMigration()
                    .build() // Aanmaken van de Database instance
                    .also { Instance = it } // Behoudt de wijzigingen naar de onlangs aangemaakte Database instance
            }
        }
    }
}