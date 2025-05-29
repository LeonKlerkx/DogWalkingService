package com.example.dogwalkingservice.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository die de Database functies biedt voor de tabel Gebruiker.
 */
class OfflineGebruikersRepository(private val gebruikerDao: GebruikerDao) : GebruikersRepository {

    override suspend fun insertUser(gebruiker: Gebruiker) = gebruikerDao.insert(gebruiker)

    override fun getUserByUsername(username: String) = gebruikerDao.getUsername(username)

    override fun getUserbyEmailAddress(emailAddress: String) = gebruikerDao.getEmailAddress(emailAddress)

    override fun getUserByPhoneNumber(phoneNumber: String) = gebruikerDao.getPhoneNumber(phoneNumber)

    override suspend fun updateUser(gebruiker: Gebruiker) = gebruikerDao.update(gebruiker)

    override suspend fun deleteUser(gebruiker: Gebruiker) = gebruikerDao.delete(gebruiker)
}