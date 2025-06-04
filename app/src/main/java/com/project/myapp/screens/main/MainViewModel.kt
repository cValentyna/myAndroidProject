package com.project.myapp.screens.main

import androidx.lifecycle.ViewModel
import com.project.myapp.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val dataStore: DataStore) : ViewModel() {
    suspend fun forgetUser() {
        dataStore.clearPreferences()
    }
}
