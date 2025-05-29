package com.example.dogwalkingservice

import android.app.Application
import com.example.dogwalkingservice.data.AppContainer
import com.example.dogwalkingservice.data.AppDataContainer
import com.example.dogwalkingservice.data.DogWalkingServiceDatabase

class DogWalkingServiceApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

    // Inladen room database
    val roomDatabase: DogWalkingServiceDatabase by lazy {
        DogWalkingServiceDatabase.getDatabase(this)
    }
}