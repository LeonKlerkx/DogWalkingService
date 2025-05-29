package com.example.dogwalkingservice.data

import kotlinx.coroutines.flow.Flow

/**
 * De Database functies waaraan de [AfspraakDao] aan moet voldoen.
 */
interface AfsprakenRepository {

    suspend fun insertAppointment(afspraak: Afspraak)

    fun getAllAppointmentByUser(oppasser: String): Flow<List<Afspraak?>>

    suspend fun updateAppointment(afspraak: Afspraak)

    suspend fun deleteAppointment(afspraak: Afspraak)
}