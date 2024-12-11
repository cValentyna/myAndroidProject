package com.project.myapp.screens.auth

import android.util.Patterns
import androidx.annotation.StringRes
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.project.myapp.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for saving AuthActivity state
 */

class AuthViewModel : ViewModel() {
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

    /**
     * This method checks
     * @param text - password
     * @return Int- the string id if the verification failed,
     * or null if the password is empty or correct
     */
    @StringRes
    fun validatePassword(text: CharSequence): Int? {
        val isCheckFail =
            text.toString().isNotEmpty() &&
                (
                    !text.toString().validSigns() ||
                        text.toString().isDigitsOnly() ||
                        text.toString().onlyLetters() ||
                        text.toString().length < MINIMUM_PASSWORD_SIZE
                )

        if (isCheckFail) {
            updateState { copy(isUserPasswordValid = false) }
        } else {
            updateState { copy(isUserPasswordValid = true) }
        }

        return when {
            !text.toString().validSigns() ->
                R.string.error_password_unpredictable_symbols

            text.toString().onlyLetters() ->
                R.string.error_password_digit

            text.toString().isDigitsOnly() ->
                R.string.error_password_letters

            text.toString().length <= MINIMUM_PASSWORD_SIZE ->
                R.string.error_password_minimum_characters

            else -> null
        }
    }

    /**
     * This method checks
     * @param text - email
     * @return Int- the string id if the verification failed,
     * or null if the password is empty or correct
     */
    @StringRes
    fun validateEmail(text: CharSequence): Int? {
        val pattern = Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()
        return if (!pattern && text.toString().isNotEmpty()) {
            updateState { copy(isUserEmailValid = false) }
            R.string.error_e_mail_address
        } else {
            updateState { copy(isUserEmailValid = true) }
            null
        }
    }

    @StringRes
    fun checkEmailByClick(text: CharSequence): Int? {
        if (text.isEmpty()) {
            updateState { copy(isUserEmailValid = false) }
            return R.string.error_email_empty
        } else {
            return validateEmail(text)
        }
    }

    @StringRes
    fun checkPasswordByClick(text: CharSequence): Int? {
        if (text.isEmpty()) {
            updateState { copy(isUserPasswordValid = false) }
            return R.string.error_password_empty
        } else {
            return validatePassword(text)
        }
    }

    /**
     * Receives name from
     * @param email
     * @return name
     */
    fun getName(email: String): String =
        email
            .substringBefore("@")
            .split(".", "_")
            .joinToString(" ") { it -> it.lowercase().replaceFirstChar { it.uppercaseChar() } }

    companion object {
        const val MINIMUM_PASSWORD_SIZE = 8

        private fun String.onlyLetters() = all { it.isLetter() }

        private fun String.validSigns() = all { it.isLetterOrDigit() }
    }
}
