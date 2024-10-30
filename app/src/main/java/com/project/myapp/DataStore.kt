package com.project.myapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.project.myapp.DataStoreKeys.CHECKBOX_IS_CHECKED
import com.project.myapp.DataStoreKeys.USER_EMAIL
import com.project.myapp.DataStoreKeys.USER_NAME
import com.project.myapp.DataStoreKeys.USER_PASSWORD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.user: DataStore<Preferences> by preferencesDataStore(name = "userInfo")

object DataStoreKeys {
    val USER_NAME = stringPreferencesKey("user_name")
    val USER_EMAIL = stringPreferencesKey("email")
    val USER_PASSWORD = stringPreferencesKey("user_password")
    val CHECKBOX_IS_CHECKED = booleanPreferencesKey("checkbox_is_checked")
}

class DataStore(
    context: Context,
) {
    private val dataStore = context.user

    suspend fun saveData(
        isChecked: Boolean,
        email: String,
        password: String,
        name: String,
    ) {
        dataStore.edit { usrData ->
            usrData[CHECKBOX_IS_CHECKED] = isChecked
            usrData[USER_EMAIL] = email
            usrData[USER_PASSWORD] = password
            usrData[USER_NAME] = name
        }
    }

    fun getWasChecked(): Flow<Boolean> =
        dataStore.data
            .catch { exception ->
                when (exception) {
                    is IOException -> emit(emptyPreferences())
                    else -> throw exception
                }
            }.map { preferences ->
                preferences[CHECKBOX_IS_CHECKED] ?: false
            }

    fun getSavedSting(key: Preferences.Key<String>): Flow<String> =
        dataStore.data
            .catch { exception ->
                when (exception) {
                    is IOException -> emit(emptyPreferences())
                    else -> throw exception
                }
            }.map { preferences ->
                preferences[key] ?: ""
            }

    suspend fun clearPreferences() {
        dataStore.edit {
            it.clear()
        }
    }
}
