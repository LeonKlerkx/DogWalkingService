package com.example.dogwalkingservice.data

/**
 * Repository die de Database functies biedt voor de tabel Recensie.
 */
class OfflineRecensieRepository(private val recensieDao: RecensieDao) : RecensieRepository {

    override suspend fun insertRecensie(recensie: Recensie) = recensieDao.insert(recensie)

    override fun getRecensieByOppasser(oppasser: String) = recensieDao.getRecensiesByOppasser(oppasser)
}