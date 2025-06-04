package com.project.myapp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Singleton


@Singleton
class DataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun saveData(
        isChecked: Boolean,
        name: String,
    ) {
        dataStore.edit { userData ->
            userData[CHECKBOX_IS_CHECKED] = isChecked
            userData[USER_NAME] = name
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

    fun getSavedString(): Flow<String> =
        dataStore.data
            .catch { exception ->
                when (exception) {
                    is IOException -> emit(emptyPreferences())
                    else -> throw exception
                }
            }.map { preferences ->
                preferences[USER_NAME] ?: ""
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
