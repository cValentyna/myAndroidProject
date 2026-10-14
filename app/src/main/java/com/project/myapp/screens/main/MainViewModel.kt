package com.project.myapp.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.myapp.data.datastore.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val dataStore: UserDataStore) : ViewModel() {
    private val _logoutSuccessEvent = Channel<Unit>()
    val logoutSuccessEvent = _logoutSuccessEvent.receiveAsFlow()

    fun onLogoutClicked() {
        viewModelScope.launch {
            forgetUser()
            _logoutSuccessEvent.send(Unit)
        }
    }

    suspend fun forgetUser() {
        dataStore.clearPreferences()
    }
}
