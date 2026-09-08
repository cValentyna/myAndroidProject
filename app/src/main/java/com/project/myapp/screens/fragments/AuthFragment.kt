package com.project.myapp.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.project.myapp.NavGraphDirections
import com.project.myapp.R
import com.project.myapp.databinding.FragmentAuthBinding
import com.project.myapp.ext.context.toast
import com.project.myapp.screens.MainActivity
import com.project.myapp.screens.auth.AuthState
import com.project.myapp.screens.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFocusListener()
        setTextChangedListener()
        setOnClickListener()
        setStatesCollectors()
        (requireActivity() as MainActivity).setOnBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Sets clickListener
     * when button is clicked - validates credentials
     */
    private fun setOnClickListener() {
        binding.apply {
            buttonAuthRegister.setOnClickListener {
                viewModel.checkCredentials(
                    textInputEditTextAuthEmail.text.toString(),
                    textInputEditTextAuthPassword.text.toString(),
                )
            }
        }
    }

    /**
     * Sets TextChangedListeners
     * pauses email and password validation and hides error if it was shown
     * when text inside fields changes
     */

    private fun setTextChangedListener() {
        binding.apply {
            textInputEditTextAuthEmail.doOnTextChanged { _, _, _, _ ->
                viewModel.pauseCheckEmail()
            }
            textInputEditTextAuthPassword.doOnTextChanged { _, _, _, _ ->
                viewModel.pauseCheckPassword()
            }
        }
    }

    /**
     * Sets focusListeners
     * validates email and password when they lost focus
     */
    private fun setFocusListener() {
        binding.apply {
            textInputEditTextAuthEmail.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    viewModel.validateEmail(textInputEditTextAuthEmail.text.toString())
                }
                textInputEditTextAuthPassword.setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        viewModel.validatePassword(textInputEditTextAuthPassword.text.toString())
                    }
                }
            }
        }
    }

    /**
     * Sets collectors for monitored states
     */
    private fun setStatesCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.passwordState.collect { state ->
                    binding.textInputLayoutAuthPassword.helperText =
                        when (state) {
                            is AuthState.PasswordState.ErrorInvalidSign ->
                                getString(R.string.error_password_unpredictable_symbols)

                            is AuthState.PasswordState.ErrorLessCharacters ->
                                getString(R.string.error_password_minimum_characters)

                            is AuthState.PasswordState.ErrorNoNumber ->
                                getString(R.string.error_password_digit)

                            is AuthState.PasswordState.ErrorNoLetter ->
                                getString(R.string.error_password_letters)

                            is AuthState.PasswordState.ErrorEmpty ->
                                getString(R.string.error_password_empty)

                            else -> null
                        }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.emailState.collect { state ->
                    binding.textInputLayoutAuthEmail.helperText = when (state) {
                        is AuthState.EmailState.Error -> getString(R.string.error_incorrect_e_mail_address)
                        is AuthState.EmailState.ErrorEmpty -> getString(R.string.error_email_empty)
                        else -> null
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.credentialsState.collect { state ->
                when (state) {
                    is AuthState.Error -> requireContext().toast(getString(R.string.error_invalid_email_or_password))

                    is AuthState.Valid -> goToNextFragment()
                    else -> {}
                }
            }
        }
    }

    private fun goToNextFragment() {
        val userName = viewModel.parseName(binding.textInputEditTextAuthEmail.text.toString())
        viewLifecycleOwner.lifecycleScope.launch {
            binding.apply {
                if (checkboxAuth.isChecked) {
                    viewModel.saveUser(userName, checkboxAuth.isChecked)
                }
            }

            val action = NavGraphDirections.actionGlobalMainGraph(userName)
            val options =
                navOptions {
                    popUpTo(R.id.auth_graph) {
                        inclusive = true
                    }
                }
            findNavController().navigate(action, options)
        }
    }
}
