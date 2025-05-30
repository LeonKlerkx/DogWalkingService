package com.example.dogwalkingservice.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AanmeldenHondDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(aanmeldenHond: AanmeldenHond)

    /**
     * Haalt alle aangemelde honden van de geselecteerde afspraak op.
     */
    @Query("SELECT * FROM AanmeldenHond WHERE afspraakId = :afspraakId")
    fun getAllSignInDogsFromAnAppointment(afspraakId: Int): Flow<List<AanmeldenHond>>
}