package com.project.myapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.doOnTextChanged
import com.project.myapp.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private var isUserEmailValid = false
    private var isUserPasswordValid = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /*
        check email and password when they are not empty
         */
        val emailLayout = binding.tilEmail
        binding.etEmail.doOnTextChanged { text: CharSequence?, _, _, _ ->
            validateEmail(text)
        }

        val passwordLayout = binding.lPassword
        binding.etPassword.doOnTextChanged { text: CharSequence?, _, _, _ ->
            validatePassword(text)
        }

        // processing button "Register" if email and password are valid go to Main activity
        binding.btRegister.setOnClickListener {
            if (isEmailAndPasswordCorrect()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                if (binding.etEmail.text.toString().isEmpty()) {
                    isUserEmailValid = false
                    emailLayout.error = getString(R.string.error_email_empty)
                }
                if (binding.etPassword.text.toString().isEmpty()) {
                    isUserPasswordValid = false
                    passwordLayout.error = getString(R.string.error_password_empty)
                }
            }
        }

    }

    private fun validateEmail(text: CharSequence?) {
        val pattern = Regex("^[\\w-.]+@([\\w-]+\\.)+\\w{2,}\$")
        if (!pattern.containsMatchIn(text.toString()) && text.toString().isNotEmpty()) {
            isUserEmailValid = false
            binding.tilEmail.error = getString(R.string.error_e_mail_address)
        } else {
            isUserEmailValid = true
            binding.tilEmail.error = null
        }
    }

    private fun validatePassword(text: CharSequence?) {

        // pattern if special_symbol can be present ^[\w#?!@$%^&*-]+$)
        val pattern = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[\\w#?!@\$%^&*-]{6,}$")

        val patternExpectedSymbols = Regex("^[\\w#?!@\$%^&*-]+\$")
        val patternDigit = Regex("(?=.*[0-9])")
        val patternLetterLC = Regex("(?=.*[a-z])")
        val patternLetterUC = Regex("(?=.*[A-Z])")
        val patternCount = Regex("([\\w#?!@\$%^&*-]{6,})")

        if (!pattern.containsMatchIn(text.toString()) && text.toString().isNotEmpty()) {
            isUserPasswordValid = false
            when {
                !patternExpectedSymbols.containsMatchIn(text.toString()) ->
                    binding.lPassword.error =
                        getString(R.string.error_password_unpredictable_symbols)

                !patternLetterLC.containsMatchIn(text.toString()) ->
                    binding.lPassword.error = getString(R.string.error_password_lower_case)

                !patternLetterUC.containsMatchIn(text.toString()) ->
                    binding.lPassword.error = getString(R.string.error_password_upper_case)

                !patternDigit.containsMatchIn(text.toString()) ->
                    binding.lPassword.error = getString(R.string.error_password_digit)

                !patternCount.containsMatchIn(text.toString()) ->
                    binding.lPassword.error = getString(R.string.error_password_minimum_characters)
            }
        } else {
            binding.lPassword.error = null
            isUserPasswordValid = true
        }
    }


    private fun isEmailAndPasswordCorrect(): Boolean {
        return isUserEmailValid && isUserPasswordValid
                && binding.etEmail.text.toString().isNotEmpty()
                && binding.etPassword.text.toString().isNotEmpty()
    }
}