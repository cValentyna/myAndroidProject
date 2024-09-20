package com.project.myapp.screens.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// ViewModel for saving AuthActivity state

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> get() = _authState.asStateFlow()

    fun updateState(reducer: AuthState.() -> AuthState) {
        _authState.update(reducer)
    }
}
