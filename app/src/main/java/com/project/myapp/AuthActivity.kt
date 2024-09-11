package com.project.myapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.project.myapp.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private val binding: ActivityAuthBinding by lazy {
        ActivityAuthBinding.inflate(layoutInflater)
    }
    private var isUserEmailValid = false
    private var isUserPasswordValid = false

    private var wasFocusEmail = false
    private var firstFocusEmail = false
    private var wasFocusPassword = false
    private var firstFocusPassword = false

    private var wasRegisterButtonClicked = false
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setFocusListener()
        setTextChangedListener()
        setOnClickListener()
    }

    /* Processes register button
    if email and password are correct - starts next activity and gives name from email
    else shows errors
     * */
    private fun setOnClickListener() {
        binding.apply {
            buttonAuthRegister.setOnClickListener {
                validateEmail(textInputEditTextAuthEmail.text)
                validatePassword(textInputEditTextAuthPassword.text)
                if (isEmailAndPasswordCorrect()) {
                    getName(textInputEditTextAuthEmail.text.toString())
                    val intent = Intent(this@AuthActivity, MainActivity::class.java)
                    intent.putExtra("userName", userName)
                    startActivity(intent)
                    finish()
                } else {
                    wasRegisterButtonClicked = true
                    Toast
                        .makeText(
                            this@AuthActivity,
                            getString(R.string.error_invalid_email_or_password),
                            Toast.LENGTH_SHORT,
                        ).show()
                    showErrorIfEmpty()
                }
            }
        }
    }

    /*
    This method validates email and password if their InputEditText was focused,
    but lost it.
     */
    private fun setFocusListener() {
        binding.apply {
            textInputEditTextAuthEmail.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    firstFocusEmail = true
                    wasFocusEmail = false
                } else if (!v.isFocused && firstFocusEmail) {
                    wasFocusEmail = true
                }
                if (wasFocusEmail) {
                    validateEmail(textInputEditTextAuthEmail.text)
                }
            }
            textInputEditTextAuthPassword.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    firstFocusPassword = true
                } else if (!v.isFocused && firstFocusPassword) {
                    wasFocusPassword = true
                }
                if (wasFocusPassword) {
                    validatePassword(textInputEditTextAuthPassword.text.toString())
                }
            }
        }
    }

    /*
    If register button was pressed or textInputEditText was focused and lost focus,
    error will be shown if input text did not pass validation
    when user is correcting:
    email - error will not be shown
    password - error will be shown until password is correct
     */
    private fun setTextChangedListener() {
        binding.apply {
            textInputEditTextAuthEmail.doOnTextChanged { text: CharSequence?, _, _, _ ->
                if (wasFocusEmail) {
                    validateEmail(text)
                } else {
                    textInputLayoutAuthEmail.helperText = null
                }
            }
            textInputEditTextAuthPassword.doOnTextChanged { text: CharSequence?, _, _, _ ->
                if (wasFocusPassword || wasRegisterButtonClicked) {
                    validatePassword(text)
                }
            }
        }
    }

    private fun validateEmail(text: CharSequence?) {
        val pattern = Regex("^[\\w-.]+@([\\w-]+\\.)+\\w{2,}\$")
        if (!pattern.containsMatchIn(text.toString()) && text.toString().isNotEmpty()) {
            isUserEmailValid = false
            binding.textInputLayoutAuthEmail.helperText = getString(R.string.error_e_mail_address)
        } else {
            isUserEmailValid = true
            binding.textInputLayoutAuthEmail.helperText = null
        }
    }

    private fun validatePassword(text: CharSequence?) {
        // pattern if special_symbol can be present
        val pattern = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[\\w#?!@\$%^&*-]{6,}$")
        val patternExpectedSymbols = Regex("^[\\w#?!@\$%^&*-]+\$")
        val patternDigit = Regex("(?=.*[0-9])")
        val patternLetterLC = Regex("(?=.*[a-z])")
        val patternLetterUC = Regex("(?=.*[A-Z])")
        val patternCount = Regex("([\\w#?!@\$%^&*-]{8,})")

        if (!pattern.containsMatchIn(text.toString()) && text.toString().isNotEmpty()) {
            isUserPasswordValid = false
            when {
                !patternExpectedSymbols.containsMatchIn(text.toString()) ->
                    binding.textInputLayoutAuthPassword.helperText =
                        getString(R.string.error_password_unpredictable_symbols)

                !patternLetterLC.containsMatchIn(text.toString()) ->
                    binding.textInputLayoutAuthPassword.helperText =
                        getString(R.string.error_password_lower_case)

                !patternLetterUC.containsMatchIn(text.toString()) ->
                    binding.textInputLayoutAuthPassword.helperText =
                        getString(R.string.error_password_upper_case)

                !patternDigit.containsMatchIn(text.toString()) ->
                    binding.textInputLayoutAuthPassword.helperText =
                        getString(R.string.error_password_digit)

                !patternCount.containsMatchIn(text.toString()) ->
                    binding.textInputLayoutAuthPassword.helperText =
                        getString(R.string.error_password_minimum_characters)
            }
        } else {
            binding.textInputLayoutAuthPassword.helperText = null
            isUserPasswordValid = true
        }
    }

    private fun showErrorIfEmpty() {
        binding.apply {
            if (textInputEditTextAuthEmail.text.toString().isEmpty()
            ) {
                isUserEmailValid = false
                textInputLayoutAuthEmail.helperText = getString(R.string.error_email_empty)
            }
            if (textInputEditTextAuthPassword.text.toString().isEmpty()
            ) {
                isUserPasswordValid = false
                textInputLayoutAuthPassword.helperText = getString(R.string.error_password_empty)
            }
        }
    }

    private fun isEmailAndPasswordCorrect(): Boolean =
        isUserEmailValid &&
            isUserPasswordValid &&
            binding.textInputEditTextAuthPassword.text
                .toString()
                .isNotEmpty() &&
            binding.textInputEditTextAuthPassword.text
                .toString()
                .isNotEmpty()

    /* Receives name from Email
     */
    private fun getName(email: String): String {
        val partOfEmail =
            email.substring(0, email.indexOf('@')).replace("[._]".toRegex(), " ").lowercase()
        userName =
            partOfEmail
                .trim()
                .split("\\s+".toRegex())
                .joinToString(" ") { it -> it.replaceFirstChar { it.uppercaseChar() } }
        return userName
    }
}
