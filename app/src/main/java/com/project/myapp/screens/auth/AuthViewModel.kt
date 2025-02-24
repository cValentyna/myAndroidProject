package com.project.myapp.screens.auth

import android.util.Patterns
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel for saving AuthActivity state
 */

class AuthViewModel : ViewModel() {
    /**
     * Variables to track the states
     */
    private val _credentialsState = MutableStateFlow<AuthState>(AuthState.Initial)
    val credentialsState: StateFlow<AuthState> get() = _credentialsState

    private val _emailState = MutableStateFlow<AuthState.EmailState>(AuthState.EmailState.Initial)
    val emailState: StateFlow<AuthState.EmailState> get() = _emailState

    private val _passwordState = MutableStateFlow<AuthState.PasswordState>(AuthState.PasswordState.Initial)
    val passwordState: StateFlow<AuthState.PasswordState> get() = _passwordState

    /**
     * Validates email, changes _emailState.value as AuthState.EmailState
     * which depends on filled data
     */
    fun validateEmail(email: String) {
        val pattern = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (!pattern && email.isNotEmpty()) {
            _emailState.value = AuthState.EmailState.Error
        } else if (email.isEmpty()) {
            _emailState.value = AuthState.EmailState.ErrorEmpty
        } else if (pattern) {
            _emailState.value = AuthState.EmailState.Valid
        }
    }

    /**
     * Changes emailState.value to AuthState.EmailState.InvisibleError
     * when validation is not required (user changes text inside chosen field)
     */
    fun pauseCheckEmail() {
        _emailState.value = AuthState.EmailState.InvisibleError
    }

    /**
     * Validates password, changes _passwordState.value as AuthState.PasswordState
     * which depends on filled data
     */

    fun validatePassword(text: String) {
        when {
            text.isEmpty() ->
                _passwordState.value = AuthState.PasswordState.ErrorEmpty

            !text.validSigns() ->
                _passwordState.value = AuthState.PasswordState.ErrorInvalidSign

            text.onlyLetters() ->
                _passwordState.value = AuthState.PasswordState.ErrorNoNumber

            text.isDigitsOnly() ->
                _passwordState.value = AuthState.PasswordState.ErrorNoLetter

            text.length < MINIMUM_PASSWORD_SIZE ->
                _passwordState.value = AuthState.PasswordState.ErrorLessCharacters

            else -> _passwordState.value = AuthState.PasswordState.Valid
        }
    }

    /**
     * Changes _passwordState.value to AuthState.Password.InvisibleError
     * when validation is not required (user changes text inside chosen field)
     */

    fun pauseCheckPassword() {
        _passwordState.value = AuthState.PasswordState.InvisibleError
    }

    /**
     * Checks credentials
     * @param email
     * @param password
     * When both fields need to be checked at the same time
     * Depending on the result, changes _credentialsState.value
     */

    fun checkCredentials(
        email: String,
        password: String,
    ) {
        validateEmail(email)
        validatePassword(password)

        if (_passwordState.value is AuthState.PasswordState.Valid &&
            _emailState.value is AuthState.EmailState.Valid
        ) {
            _credentialsState.value = AuthState.Valid
        } else {
            _credentialsState.value = AuthState.Error
        }
    }

    /**
     * Receives name from
     * @param email
     * @return name
     */

    fun parseName(email: String): String =
        email
            .substringBefore("@")
            .split(".", "_")
            .joinToString(" ") { it -> it.lowercase().replaceFirstChar { it.uppercaseChar() } }

    /**
     * Companion object for password validation
     */

    companion object {
        const val MINIMUM_PASSWORD_SIZE = 8

        private fun String.onlyLetters() = all { it.isLetter() }

        private fun String.validSigns() = all { it.isLetterOrDigit() }
    }
}
