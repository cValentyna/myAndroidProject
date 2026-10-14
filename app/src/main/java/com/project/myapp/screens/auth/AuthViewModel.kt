package com.project.myapp.screens.auth

import android.util.Patterns
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.myapp.data.datastore.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for saving AuthActivity state
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val dataStore: UserDataStore
) : ViewModel() {
    /**
     * Variables to track the states
     */
    private val _credentialsState = Channel<AuthState>(Channel.BUFFERED)
    val credentialsState = _credentialsState.receiveAsFlow()

    private val _emailState = MutableStateFlow<AuthState.EmailState>(AuthState.EmailState.Initial)
    val emailState: StateFlow<AuthState.EmailState> get() = _emailState

    private val _passwordState = MutableStateFlow<AuthState.PasswordState>(AuthState.PasswordState.Initial)
    val passwordState: StateFlow<AuthState.PasswordState> get() = _passwordState

    fun validateEmail(email: String) {
        val pattern = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        _emailState.value =
            if (email.isEmpty()) {
                AuthState.EmailState.ErrorEmpty
            } else if (!pattern) {
                AuthState.EmailState.Error
            } else {
                AuthState.EmailState.Valid
            }
    }

    /**
     * Changes state value when validation is not required
     * (user changes text inside chosen field)
     */
    fun pauseCheckEmail() {
        _emailState.value = AuthState.EmailState.InvisibleError
    }

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
     * Changes state value when validation is not required
     * (user changes text inside chosen field)
     */

    fun pauseCheckPassword() {
        _passwordState.value = AuthState.PasswordState.InvisibleError
    }

    /**
     * Checks credentials when user clicked Register button
     * and saves userData in DataStore if needed
     * @param email
     * @param password
     * @param shouldRememberUser
     */

    fun onLoginClicked(
        email: String,
        password: String,
        shouldRememberUser: Boolean,
    ) {
        validateEmail(email)
        validatePassword(password)

        val isEmailValid = _emailState.value is AuthState.EmailState.Valid
        val isPasswordValid = _passwordState.value is AuthState.PasswordState.Valid
        viewModelScope.launch {
            if (isEmailValid && isPasswordValid
            ) {
                if (shouldRememberUser) {
                    val name = parseName(email)
                    saveUserData(true, name)
                }
                _credentialsState.send(AuthState.Valid)
            } else {
                _credentialsState.send(AuthState.Error)
            }
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

    suspend fun saveUserData(
        isChecked: Boolean,
        name: String,
    ) {
        dataStore.saveUserData(isChecked, name)
    }

    companion object {
        const val MINIMUM_PASSWORD_SIZE = 5

        private fun String.onlyLetters() = all { it.isLetter() }

        private fun String.validSigns() = all { it.isLetterOrDigit() }
    }
}
