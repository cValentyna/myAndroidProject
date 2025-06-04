package com.project.myapp.screens.splashScreen

import androidx.lifecycle.ViewModel
import com.project.myapp.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SplashModel @Inject constructor(private val dataStore: DataStore) : ViewModel() {
    suspend fun wasSavedUser(): Boolean = dataStore.getWasChecked().first()

    suspend fun getSavedName(): String = dataStore.getSavedString().first()
}
