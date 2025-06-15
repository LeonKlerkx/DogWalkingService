package com.example.dogwalkingservice.data

import kotlinx.coroutines.flow.Flow

interface HondenRepository {

    suspend fun insertDog(hond: Hond)

    fun getDogByPrimaryKey(chipnummer: String): Flow<Hond>

    fun getAllDogsFromOwner(eigenaar: String): Flow<List<Hond>>

    suspend fun updateDog(hond: Hond)

    suspend fun deleteDog(hond: Hond)
}