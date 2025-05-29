package com.example.dogwalkingservice.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecensieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recensie: Recensie)

    /**
     * Haalt alle recensies van de oppasser op.
     * Eigenaar hoeft niet, omdat je hier geen recensies voor kunt schrijven.
     */
    @Query("SELECT * FROM Recensie WHERE oppasser = :oppasser")
    fun getRecensiesByOppasser(oppasser: String): Flow<List<Recensie>>
}