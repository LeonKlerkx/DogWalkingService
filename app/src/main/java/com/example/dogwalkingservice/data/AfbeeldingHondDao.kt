package com.example.dogwalkingservice.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AfbeeldingHondDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(afbeeldingHond: AfbeeldingHond)

    /**
     * Retrieve all pictures of the dog from the chip number.
     */
    @Query("SELECT * FROM AfbeeldingHond WHERE chipnummer = :chipnummer")
    fun getDogPictureByChipnummer(chipnummer: String): Flow<List<AfbeeldingHond>>

    @Delete
    suspend fun delete(afbeeldingHond: AfbeeldingHond)
}