package com.example.dogwalkingservice.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.dogwalkingservice.ui.home.HomeScreenUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val EMAIL_ADDRESS = stringPreferencesKey("email_address")
        val USER_ROLE = stringPreferencesKey("user_role")
    }

    /**
     * Read the [EMAIL_ADDRESS] from the DataStore.
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
     * Read the [USER_ROLE] from the DataStore.
     */
    val getUserRole: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { userInformation ->
            userInformation[USER_ROLE] ?: ""
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
     * Save the [userRoleValue] into the DataStore.
     */
    suspend fun saveUserRole(userRoleValue: String) {
        dataStore.edit { userRole ->
            userRole[USER_ROLE] = userRoleValue
        }
    }

    /**
     * Delete the value of [EMAIL_ADDRESS] and the value of [USER_ROLE] from the DataStore.
     */
    suspend fun deleteEmailaddressAndUserRole() {
        dataStore.edit {
            it.clear()
        }
    }
}