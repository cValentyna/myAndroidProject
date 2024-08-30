package com.project.myapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.project.myapp.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(binding.activityAuth) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

       /* shows errors just for visualize
        * */
        binding.textInputEditTextAuthEmail.doOnTextChanged { text: CharSequence?, _, _, _ ->
            if (text != null) {
                if (text.isNotEmpty()) {
                    binding.textInputLayoutAuthEmail.helperText = "Incorrect E-Mail address"
                } else {
                    binding.textInputLayoutAuthEmail.helperText = null
                }
            }
        }

        binding.textInputEditTextAuthPassword.doOnTextChanged { text: CharSequence?, _, _, _ ->
            if (text != null) {
                if (text.isNotEmpty()) {
                    binding.textInputLayoutAuthPassword.helperText =
                        "Your password must include a minimum of 8 characters."
                } else {
                    binding.textInputLayoutAuthPassword.helperText = null
                }
            }
        }

        binding.buttonAuthRegister.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
