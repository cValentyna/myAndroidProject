package com.project.myapp.screens.auth

sealed class AuthState {
    data object Initial : AuthState()

    data object Valid : AuthState()

    data object Error : AuthState()

    sealed class EmailState : AuthState() {
        data object Initial : EmailState()

        data object Valid : EmailState()

        data object Error : EmailState()

        data object ErrorEmpty : EmailState()

        data object InvisibleError : EmailState()
    }

    sealed class PasswordState : AuthState() {
        data object Initial : PasswordState()

        data object Valid : PasswordState()

        data object ErrorInvalidSign : PasswordState()

        data object ErrorNoLetter : PasswordState()

        data object ErrorNoNumber : PasswordState()

        data object ErrorLessCharacters : PasswordState()

        data object ErrorEmpty : PasswordState()

        data object InvisibleError : PasswordState()
    }
}
