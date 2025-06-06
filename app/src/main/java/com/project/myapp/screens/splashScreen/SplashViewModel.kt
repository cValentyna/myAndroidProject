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
    private val _getCachedCredentials =
        MutableStateFlow<GetCachedCredentials>(GetCachedCredentials.Initial)
    val getCachedCredentials get() = _getCachedCredentials.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            initializeCredentials()
        }
    }

    private suspend fun initializeCredentials() {
        _getCachedCredentials.update {
            getCachedUserCredentials()
        }
    }

    private suspend fun getCachedUserCredentials(): GetCachedCredentials {
        return if (wasSavedUser()) {
            GetCachedCredentials.Success(getSavedName())
        } else {
            GetCachedCredentials.Fail
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
