package com.project.myapp.screens.splashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.myapp.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val dataStore: DataStore) : ViewModel() {
    private val _cachedCredentialsState =
        MutableStateFlow<CredentialsRetrievalState>(CredentialsRetrievalState.Initial)
    val cachedCredentialsState get() = _cachedCredentialsState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            initializeCredentials()
        }
    }

    private suspend fun initializeCredentials() {
        _cachedCredentialsState.update {
            getCachedCredentialsState()
        }
    }

    private suspend fun getCachedCredentialsState(): CredentialsRetrievalState{
        return if (wasSavedUser()) {
            CredentialsRetrievalState.Success(getSavedName())
        } else {
            CredentialsRetrievalState.Fail
        }
    }

    private suspend fun wasSavedUser(): Boolean {
        return withContext(Dispatchers.IO) {
            dataStore.getWasChecked().first()
        }
    }

    private suspend fun getSavedName(): String {
        return withContext(Dispatchers.IO) {
            dataStore.getSavedString().first()
        }
    }
}
