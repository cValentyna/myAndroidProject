package com.project.myapp.screens.main

import androidx.lifecycle.ViewModel
import com.project.myapp.data.datastore.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val dataStore: UserDataStore) : ViewModel() {
    suspend fun isUserRemembered(): Boolean = dataStore.isUserRemembered()

    suspend fun forgetUser() {
        dataStore.clearPreferences()
    }
}
