package com.example.dogwalkingservice.data

class OfflineAfbeeldingHondenRepository(private val afbeeldingHondDao: AfbeeldingHondDao)
    : AfbeeldingHondenRepository
{
    override suspend fun insert(afbeeldingHond: AfbeeldingHond) =
        afbeeldingHondDao.insert(afbeeldingHond)

    override fun getDogPictureByChipnummer(chipnummer: String) =
        afbeeldingHondDao.getDogPictureByChipnummer(chipnummer)

    override suspend fun update(afbeeldingHond: AfbeeldingHond) =
        afbeeldingHondDao.update(afbeeldingHond)
}