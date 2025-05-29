package com.example.dogwalkingservice.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GebruikerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(gebruiker: Gebruiker)

    /**
     * Zoekt of de ingevulde gebruikersnaam al in de Database bestaat.
     */
    @Query("SELECT * FROM Gebruiker WHERE gebruikersnaam = :username")
    fun getUsername(username: String): Flow<Gebruiker>

    /**
     * Zoekt of de ingevulde emailadres al in de Database bestaat.
     */
    @Query("SELECT * FROM Gebruiker WHERE emailadres = :emailAddress")
    fun getEmailAddress(emailAddress: String): Flow<Gebruiker>

    /**
     * Zoekt of het ingevulde telefoonnummer al in de Database bestaat.
     */
    @Query("SELECT * FROM Gebruiker WHERE telefoonnummer = :phoneNumber")
    fun getPhoneNumber(phoneNumber: String): Flow<Gebruiker>

    @Update
    suspend fun update(gebruiker: Gebruiker)

    @Delete
    suspend fun delete(gebruiker: Gebruiker)
}