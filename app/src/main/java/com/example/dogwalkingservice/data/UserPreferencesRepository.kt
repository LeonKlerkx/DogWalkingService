package com.example.dogwalkingservice.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val EMAIL_ADDRESS = stringPreferencesKey("email_address")
    }

    /**
     * Read the [EMAIL_ADDRES] from the DataStore.
     */
    val getEmailAddress: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { emailAddress ->
            emailAddress[EMAIL_ADDRESS] ?: ""
        }

    /**
     * Save the [emailAddressValue] to the DataStore.
     */
    suspend fun saveEmailAddress(emailAddressValue: String) {
        dataStore.edit { emailAddress ->
            emailAddress[EMAIL_ADDRESS] = emailAddressValue
        }
    }

    /**
     * Delete the email address from the DataStore.
     */
    suspend fun deleteEmailaddress() {
        dataStore.edit {
            it.clear()
        }
    }
}