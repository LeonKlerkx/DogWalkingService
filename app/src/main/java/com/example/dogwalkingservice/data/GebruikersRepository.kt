package com.example.dogwalkingservice.data

import androidx.collection.FloatList
import kotlinx.coroutines.flow.Flow

/**
 * De Database functies waaraan de [GebruikerDao] aan moet voldoen.
 */
interface GebruikersRepository {

    /**
     * Voegt de [Gebruiker] aan de Database toe.
     */
    suspend fun insertUser(gebruiker: Gebruiker)

    /**
     * Vraagt de gebruiker op basis van de gebruikersnaam uit de Database op.
     */
    fun getUserByUsername(username: String): Flow<Gebruiker?>

    /**
     * Vraagt de gebruiker op basis van de emailadres uit de Database op.
     */
    fun getUserbyEmailAddress(emailAddress: String): Flow<Gebruiker?>

    /**
     * Vraagt de gebruiker op basis van het telefoonnummer uit de Database op.
     */
    fun getUserByPhoneNumber(phoneNumber: String): Flow<Gebruiker?>

    /**
     * Wijzigt de [Gebruiker] in de Database.
     */
    suspend fun updateUser(gebruiker: Gebruiker)

    /**
     * Verwijdert de [Gebruiker] in de Database.
     */
    suspend fun deleteUser(gebruiker: Gebruiker)
}