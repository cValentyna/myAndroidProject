package com.project.myapp

import android.content.Context
import android.util.Patterns
import androidx.core.content.ContextCompat.getString

object Validation {
    fun validatePassword(
        text: String?,
        context: Context,
    ): String? {
        // pattern if special_symbol can be present
        val pattern = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[\\w#?!@\$%^&*-]{8,}$")
        val patternExpectedSymbols = Regex("^[\\w#?!@\$%^&*-]+\$")
        val patternDigit = Regex("(?=.*[0-9])")
        val patternLetterLC = Regex("(?=.*[a-z])")
        val patternLetterUC = Regex("(?=.*[A-Z])")
        val patternCount = Regex("([\\w#?!@\$%^&*-]{8,})")

        // var emailError: String? = null

        if (!pattern.containsMatchIn(text.toString()) && text.toString().isNotEmpty()) {
            return when {
                !patternExpectedSymbols.containsMatchIn(text.toString()) ->
                    getString(context, R.string.error_password_unpredictable_symbols)

                !patternLetterLC.containsMatchIn(text.toString()) ->
                    getString(context, R.string.error_password_lower_case)

                !patternLetterUC.containsMatchIn(text.toString()) ->
                    getString(context, R.string.error_password_upper_case)

                !patternDigit.containsMatchIn(text.toString()) ->
                    getString(context, R.string.error_password_digit)

                !patternCount.containsMatchIn(text.toString()) ->
                    getString(context, R.string.error_password_minimum_characters)

                else -> null
            }
        }
        return null
    }

    fun validateEmail(
        text: CharSequence,
        context: Context,
    ): String? {
        val pattern = Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()
        return if (!pattern && text.toString().isNotEmpty()) {
            getString(context, R.string.error_e_mail_address)
        } else {
            null
        }
    }

    fun emptyEmail(
        text: CharSequence,
        context: Context,
    ): String? =
        if (text.isEmpty()) {
            getString(context, R.string.error_email_empty)
        } else {
            null
        }

    fun emptyPassword(
        text: CharSequence,
        context: Context,
    ): String? {
        if (text.isEmpty()) {
            return getString(context, R.string.error_password_empty)
        }
        return null
    }
}
