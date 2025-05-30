package com.example.dogwalkingservice.data

class OfflineHondenRepository(private val hondDao: HondDao) : HondenRepository {

    override suspend fun insertDog(hond: Hond) = hondDao.insert(hond)

    override fun getDogByPrimaryKey(chipnummer: String) = hondDao.getDogByPrimaryKey(chipnummer)

    override fun getAllDogsFromOwner(eigenaar: String) = hondDao.getAllDogsFromOwner(eigenaar)

    override suspend fun updateDog(hond: Hond) = hondDao.update(hond)

    override suspend fun deleteDog(hond: Hond) = hondDao.delete(hond)
}