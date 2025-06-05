package com.example.dogwalkingservice

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.dogwalkingservice.data.AppContainer
import com.example.dogwalkingservice.data.AppDataContainer
import com.example.dogwalkingservice.data.DogWalkingServiceDatabase
import com.example.dogwalkingservice.data.UserPreferencesRepository

// Initialize DataStore value.
private const val EMAIL_ADDRESS = "E-mailadres"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = EMAIL_ADDRESS
)

class DogWalkingServiceApplication : Application() {

    lateinit var container: AppContainer

    // DataStore
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)

        userPreferencesRepository = UserPreferencesRepository(dataStore = dataStore)
    }

    // Inladen room database
    val roomDatabase: DogWalkingServiceDatabase by lazy {
        DogWalkingServiceDatabase.getDatabase(this)
    }
}