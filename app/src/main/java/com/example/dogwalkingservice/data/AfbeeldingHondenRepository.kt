package com.example.dogwalkingservice.data

import kotlinx.coroutines.flow.Flow

/**
 * De Database functies waaraan de [AfbeeldingHondDao] aan moet voldoen.
 */
interface AfbeeldingHondenRepository {

    suspend fun insert(afbeeldingHond: AfbeeldingHond)

    fun getDogPictureByChipnummer(chipnummer: String): Flow<List<AfbeeldingHond?>>

    suspend fun update(afbeeldingHond: AfbeeldingHond)
}