package com.project.myapp.screens.auth

import android.util.Patterns
import androidx.annotation.StringRes
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.myapp.R

/**
 * ViewModel for saving AuthActivity state
 */

class AuthViewModel : ViewModel() {
    /**
     * Variables to track the states
     */
    private val _credentialsState = MutableLiveData<AuthState>()
    val credentialsState: LiveData<AuthState> get() = _credentialsState

    private val _emailState = MutableLiveData<AuthState.EmailState>()
    val emailState: LiveData<AuthState.EmailState> get() = _emailState

    private val _passwordState = MutableLiveData<AuthState.PasswordState>()
    val passwordState: LiveData<AuthState.PasswordState> get() = _passwordState

    /**
     * Validates email when email is not empty  and changes _emailState.value
     */

    fun validateEmail(email: String) {
        val pattern = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (!pattern && email.isNotEmpty()){
            _emailState.value = AuthState.EmailState.Error(getEmailError())
        } else if (pattern) {
            _emailState.value = AuthState.EmailState.Valid
        }
    }

    /**
     * Changes emailState.value to AuthState.EmailState.InvisibleError
     * when validation is not required
     */
    fun pauseCheckEmail() {
        _emailState.value = AuthState.EmailState.InvisibleError
    }

    /**
     * Gets an email error when  email is invalid
     */
    @StringRes
    private fun getEmailError(): Int {
        return R.string.error_e_mail_address
    }

    /**
     * Validates password when it is not empty  and changes _passwordState.value
     */

    fun validatePassword(password: String) {
        val isFailed =
            password.isNotEmpty() && (
                    !password.validSigns() ||
                            password.isDigitsOnly() ||
                            password.onlyLetters() ||
                            password.length < MINIMUM_PASSWORD_SIZE)

        if (isFailed) {
            _passwordState.value = AuthState.PasswordState.Error(getPasswordError(password))
        } else {
            _passwordState.value =AuthState.PasswordState.Valid
        }
    }

    /**
     * Gets a password error when  filled password is invalid, depends on the reason for the error
     */
    @StringRes

    private fun getPasswordError(text: String): Int? {
        return when {
            !text.validSigns() ->
                R.string.error_password_unpredictable_symbols

            text.onlyLetters() ->
                R.string.error_password_digit

            text.isDigitsOnly() ->
                R.string.error_password_letters

            text.length < MINIMUM_PASSWORD_SIZE ->
                R.string.error_password_minimum_characters

            else -> null
        }
    }


    /**
     * Changes _passwordState.value to AuthState.EmailState.InvisibleError
     * when validation is not required
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

    fun checkCredentials(email: String, password: String) {

        checkEmailByClick(email)
        checkPasswordByClick(password)

        if (_passwordState.value is AuthState.PasswordState.Valid
            && _emailState.value is AuthState.EmailState.Valid) {
            _credentialsState.value = AuthState.Valid
        } else {
            _credentialsState.value = AuthState.Error(R.string.error_invalid_email_or_password)
        }
    }

    /**
     * Validates
     * @param email
     * when user clicked authorization button
     */

    private fun checkEmailByClick(email:String) {
        if (email.isEmpty()) {
            _emailState.value = AuthState.EmailState.Empty(R.string.error_email_empty)
        } else {
            validateEmail(email)
        }
    }

    /**
     * Validates
     * @param password
     * when user clicked authorization button
     */
    private fun checkPasswordByClick(password: String) {
        if (password.isEmpty()) {
            _passwordState.value = AuthState.PasswordState.Empty(R.string.error_password_empty)
        } else {
            validatePassword(password)
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

    /**
     * Companion object for password validation
     */

    companion object {
        const val MINIMUM_PASSWORD_SIZE = 8

        private fun String.onlyLetters() = all { it.isLetter() }

        private fun String.validSigns() = all { it.isLetterOrDigit() }
    }
}
