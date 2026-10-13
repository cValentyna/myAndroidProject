package com.project.myapp.screens.splashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.myapp.data.datastore.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val dataStore: UserDataStore) : ViewModel() {
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

    private suspend fun getCachedCredentialsState(): CredentialsRetrievalState {
        if (isUserRemembered()) {
            val savedName = getSavedUserName()
            return if (savedName.isNotEmpty()) {
                CredentialsRetrievalState.Success(savedName)
            } else {
                CredentialsRetrievalState.Fail
            }
        } else {
            return CredentialsRetrievalState.Fail
        }
    }

    private suspend fun isUserRemembered(): Boolean = dataStore.isUserRemembered()

    private suspend fun getSavedUserName(): String = dataStore.getSavedUserName()
}
