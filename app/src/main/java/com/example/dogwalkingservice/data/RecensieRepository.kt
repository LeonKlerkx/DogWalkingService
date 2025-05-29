package com.example.dogwalkingservice.data

import kotlinx.coroutines.flow.Flow

interface RecensieRepository {

    /**
     * Voegt een [Recensie] aan de Database toe.
     */
    suspend fun insertRecensie(recensie: Recensie)

    /**
     * Vraagt alle [Recensie]s van de oppasser op.
     */
    fun getRecensieByOppasser(oppasser: String) : Flow<List<Recensie?>>
}