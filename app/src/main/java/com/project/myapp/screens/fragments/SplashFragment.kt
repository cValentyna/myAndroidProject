package com.project.myapp.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.project.myapp.R
import com.project.myapp.databinding.FragmentSplashBinding
import com.project.myapp.screens.splashScreen.CredentialsRetrievalState
import com.project.myapp.screens.splashScreen.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cachedCredentialsState.collect { cashedCredentialsState ->
                    when (cashedCredentialsState) {
                        CredentialsRetrievalState.Initial -> {}
                        is CredentialsRetrievalState.Success -> navigateToMainGraph(cashedCredentialsState.savedName)
                        is CredentialsRetrievalState.Fail -> navigateToAuthGraph()
                    }
                }
            }
        }
    }

    private fun navigateToMainGraph(userName: String) {
        val action =
            SplashFragmentDirections
                .actionSplashFragmentToMainGraph(userName)

        val options =
            navOptions {
                popUpTo(R.id.splashFragment) { inclusive = true }
            }

        findNavController().navigate(action, options)
    }

    private fun navigateToAuthGraph() {
        val action = SplashFragmentDirections.actionSplashFragmentToAuthGraph()

        val options =
            navOptions {
                popUpTo(R.id.splashFragment) { inclusive = true }
            }

        findNavController().navigate(action, options)
    }
}
