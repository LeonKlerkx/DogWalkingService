package com.example.dogwalkingservice.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AfspraakDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(afspraak: Afspraak)

    /**
     * Haalt alle afspraken van de geselecteerde oppasser op.
     */
    @Query("SELECT * FROM Afspraak WHERE oppasser = :oppasser")
    fun getAllAppointsmentsByOppasser(oppasser: String): Flow<List<Afspraak>>

    /**
     * Haalt de afspraak op basis van [Afspraak.beginmoment], [Afspraak.eindmoment] en [Afspraak.oppasser] op.
     *
     * Er ligt een unique key op de kolommen [Afspraak.beginmoment], [Afspraak.eindmoment] en [Afspraak.oppasser]
     */
    @Query("SELECT * FROM Afspraak WHERE beginmoment = :beginmoment AND eindmoment = :eindmoment AND oppasser = :oppasser")
    fun checkIfTheAppointmentIsExists(beginmoment: String, eindmoment: String, oppasser: String): Flow<Afspraak>

    @Update
    suspend fun update(afspraak: Afspraak)

    @Delete
    suspend fun delete(afspraak: Afspraak)
}