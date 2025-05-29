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

    @Update
    suspend fun update(afspraak: Afspraak)

    @Delete
    suspend fun delete(afspraak: Afspraak)
}