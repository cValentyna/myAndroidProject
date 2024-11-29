package com.project.myapp.screens.auth

import android.app.Application
import android.util.Patterns
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.AndroidViewModel
import com.project.myapp.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for saving AuthActivity state
 */

class AuthViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> get() = _authState.asStateFlow()

    private fun updateState(reducer: AuthState.() -> AuthState) {
        _authState.update(reducer)
    }

    fun focusEmailUpdate(
        hasFocus: Boolean,
        isFocused: Boolean,
    ) {
        if (hasFocus) {
            updateState {
                copy(firstFocusEmail = true, wasFocusEmail = false)
            }
        } else if (!isFocused && authState.value.firstFocusEmail) {
            updateState {
                copy(wasFocusEmail = true)
            }
        }
    }

    fun focusPasswordUpdate(
        hasFocus: Boolean,
        isFocused: Boolean,
    ) {
        if (hasFocus) {
            updateState {
                copy(firstFocusPassword = true)
            }
        } else if (!isFocused && authState.value.firstFocusPassword) {
            updateState {
                copy(wasFocusPassword = true)
            }
        }
    }

    fun wasEmailFocus(): Boolean = authState.value.wasFocusEmail

    fun wasPasswordFocus(): Boolean = authState.value.wasFocusPassword

    fun wasRegisterButtonClicked(): Boolean = authState.value.wasRegisterButtonClicked

    fun isPasswordCorrect(): Boolean = authState.value.isUserPasswordValid

    fun isEmailCorrect(): Boolean = authState.value.isUserEmailValid

    fun updateRegistrationButtonClicked() {
        if (!authState.value.wasRegisterButtonClicked) {
            updateState {
                copy(wasRegisterButtonClicked = true)
            }
        }
    }

    private fun getString(int: Int): String = getApplication<Application>().getString(int)

    fun validatePassword(text: CharSequence): String? {
        val check =
            text.toString().isNotEmpty() &&
                (
                    !text.toString().validSigns() ||
                        text.toString().isDigitsOnly() ||
                        text.toString().onlyLetters() ||
                        text.toString().length < MINIMUM_PASSWORD_SIZE
                )

        if (check) {
            updateState { copy(isUserPasswordValid = false) }
            return when {
                !text.toString().validSigns() ->
                    getString(R.string.error_password_unpredictable_symbols)

                text.toString().onlyLetters() ->
                    getString(R.string.error_password_digit)

                text.toString().isDigitsOnly() ->
                    getString(R.string.error_password_letters)

                text.toString().length < MINIMUM_PASSWORD_SIZE ->
                    getString(R.string.error_password_minimum_characters)

                else -> null
            }
        }
        updateState { copy(isUserPasswordValid = true) }
        return null
    }

    fun validateEmail(text: CharSequence): String? {
        val pattern = Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()
        return if (!pattern && text.toString().isNotEmpty()) {
            updateState { copy(isUserEmailValid = false) }
            getString(R.string.error_e_mail_address)
        } else {
            updateState { copy(isUserEmailValid = true) }
            null
        }
    }

    fun checkEmailByClick(text: CharSequence): String? {
        if (text.isEmpty()) {
            updateState { copy(isUserEmailValid = false) }
            return getString(R.string.error_email_empty)
        } else {
            return validateEmail(text)
        }
    }

    fun checkPasswordByClick(text: CharSequence): String? {
        if (text.isEmpty()) {
            updateState { copy(isUserPasswordValid = false) }
            return getString(R.string.error_password_empty)
        } else {
            return validatePassword(text)
        }
    }

    /**
     * Receives name from
     * @param email
     */
    fun getName(email: String): String =
        email
            .substringBefore("@")
            .split(".", "_")
            .joinToString(" ")
            { it -> it.lowercase().replaceFirstChar{ it.uppercaseChar() } }

    companion object {
        const val MINIMUM_PASSWORD_SIZE = 8

        private fun String.onlyLetters() = all { it.isLetter() }

        private fun String.validSigns() = all { it.isLetterOrDigit() }
    }
}
