package com.example.dogwalkingservice.data

class OfflineAfsprakenRepository(private val afspraakDao: AfspraakDao) : AfsprakenRepository {

    override suspend fun insertAppointment(afspraak: Afspraak) = afspraakDao.insert(afspraak)

    override fun getAllAppointmentByUser(oppasser: String) = afspraakDao.getAllAppointsmentsByOppasser(oppasser)

    override suspend fun updateAppointment(afspraak: Afspraak) = afspraakDao.update(afspraak)

    override suspend fun deleteAppointment(afspraak: Afspraak) = afspraakDao.delete(afspraak)
}