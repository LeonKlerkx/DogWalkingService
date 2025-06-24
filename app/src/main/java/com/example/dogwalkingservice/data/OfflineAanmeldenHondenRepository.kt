package com.example.dogwalkingservice.data

class OfflineAanmeldenHondenRepository(private val aanmeldenHondDao: AanmeldenHondDao)
    : AanmeldenHondenRepository {

    override suspend fun insert(aanmeldenHond: AanmeldenHond) =
        aanmeldenHondDao.insert(aanmeldenHond)

    override fun getAllSignInDogsFromAnAppointment(afspraakId: Int) =
        aanmeldenHondDao.getAllSignInDogsFromAnAppointment(afspraakId)

    override fun getAllAppointmentFromOneDog(chipnummer: String) =
        aanmeldenHondDao.getAllAppointmentFromOneDog(chipnummer)

    override suspend fun delete(signInDog: AanmeldenHond) =
        aanmeldenHondDao.delete(signInDog)
}