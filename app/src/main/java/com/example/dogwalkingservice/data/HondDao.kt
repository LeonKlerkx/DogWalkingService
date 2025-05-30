package com.example.dogwalkingservice.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HondDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(hond: Hond)

    /**
     * Retrieved the dog with the Primary Key,
     * so you can show the dog when this is sign up [AanmeldenHond] in an appointment [Afspraak].
     */
    @Query("SELECT * FROM Hond WHERE chipnummer = :chipnummer")
    fun getDogByPrimaryKey(chipnummer: String): Flow<Hond>

    /**
     * Retrieved all dogs from the selected owner.
     */
    @Query("SELECT * FROM Hond WHERE eigenaar = :eigenaar")
    fun getAllDogsFromOwner(eigenaar: String): Flow<List<Hond>>

    @Update
    suspend fun update(hond: Hond)

    @Delete
    suspend fun delete(hond: Hond)
}