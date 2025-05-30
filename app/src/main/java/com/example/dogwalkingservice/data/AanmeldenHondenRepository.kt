package com.example.dogwalkingservice.data

import kotlinx.coroutines.flow.Flow

/**
 * De Database functies waaraan de [AanmeldenHondDao] aan moet voldoen.
 */
interface AanmeldenHondenRepository {

    suspend fun insert(aanmeldenHond: AanmeldenHond)

    fun getAllSignInDogsFromAnAppointment(afspraakId: Int): Flow<List<AanmeldenHond?>>
}