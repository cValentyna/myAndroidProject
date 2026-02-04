package com.project.myapp.validation

import com.project.myapp.R


sealed class ValidationResult {
    data object Success : ValidationResult()

    data class Error(val messageRId: Int) : ValidationResult()
}


class NameValidator {
    fun validateName(name: String): ValidationResult {
        val trimmedName = name.trim()

        if (trimmedName.isEmpty()) return ValidationResult.Error(R.string.error_contact_name_empty)

        if (trimmedName.length < 2 || trimmedName.length > 20) return ValidationResult.Error(R.string.error_contact_name_length)

        var hasLetter = false
        var prevWasApostrophe = false
        var prevWasHyphen = false
        val apostrophes = setOf('\'', '’', 'ʼ', '‘')

        for (ch in trimmedName) {
            val isApostrophe = ch in apostrophes
            when {
                ch.isLetter() -> {
                    hasLetter = true
                    prevWasApostrophe = false
                    prevWasHyphen = false
                }

                ch == ' ' -> {
                    prevWasApostrophe = false
                    prevWasHyphen = false
                }

                ch == '-' -> {
                    if (prevWasHyphen|| prevWasApostrophe) return ValidationResult.Error(R.string.error_contact_name)
                    prevWasHyphen = true
                    prevWasApostrophe = false
                }

                isApostrophe -> {
                    if (prevWasApostrophe|| prevWasHyphen ) return ValidationResult.Error(R.string.error_contact_name)
                    prevWasApostrophe = true
                    prevWasHyphen = false
                }

                else -> return ValidationResult.Error(R.string.error_contact_name)
            }
        }

        return if (!hasLetter) ValidationResult.Error(R.string.error_contact_name)
        else ValidationResult.Success
    }
}
