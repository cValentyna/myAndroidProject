package com.project.myapp.screens.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.project.myapp.R
import com.project.myapp.databinding.FragmentSplashBinding
import com.project.myapp.screens.splashScreen.CredentialsRetrievalState
import com.project.myapp.screens.splashScreen.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        collectFlowWithLifecycle(viewModel.cachedCredentialsState) { cashedCredentialsState ->
            when (cashedCredentialsState) {
                CredentialsRetrievalState.Initial -> {}
                is CredentialsRetrievalState.Success -> navigateToMainGraph(cashedCredentialsState.savedName)
                is CredentialsRetrievalState.Fail -> navigateToAuthGraph()
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
