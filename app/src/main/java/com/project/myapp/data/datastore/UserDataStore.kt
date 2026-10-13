package com.project.myapp.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun saveUserData(
        isChecked: Boolean,
        name: String,
    ) {
        dataStore.edit { userData ->
            userData[CHECKBOX_IS_CHECKED] = isChecked
            userData[USER_NAME] = name
        }
    }

    suspend fun isUserRemembered(): Boolean =
        try {
            dataStore.data.first()[CHECKBOX_IS_CHECKED] ?: false
        } catch (e: IOException) {
            false
        }

    suspend fun getSavedUserName(): String =
        try {
            dataStore.data.first()[USER_NAME] ?: ""
        } catch (e: IOException) {
            ""
        }

    suspend fun clearPreferences() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object DataStoreKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val CHECKBOX_IS_CHECKED = booleanPreferencesKey("checkbox_is_checked")
    }
}
