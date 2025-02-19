package com.project.myapp.screens.auth

/**
 * Class where ui state is stored
 */

sealed class AuthState {
    data object Valid : AuthState()
    data class Error(val message: Int?) : AuthState()

    sealed class EmailState {
        data object Valid : EmailState()
        data class Error(val message: Int?) : EmailState()
        data class Empty(val message: Int?) : EmailState()
        data object InvisibleError : EmailState()
    }

    sealed class PasswordState {
        data object Valid : PasswordState()
        data class Error(val message: Int?) : PasswordState()
        data class Empty(val message: Int?) : PasswordState()
        data object InvisibleError : PasswordState()
    }
}

