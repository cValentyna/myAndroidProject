package com.project.myapp.screens.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.project.myapp.MainGraphArgs
import com.project.myapp.NavGraphDirections
import com.project.myapp.R
import com.project.myapp.databinding.FragmentMainBinding
import com.project.myapp.screens.MainActivity
import com.project.myapp.screens.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModels()

    private val args: MainGraphArgs by navArgs()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setName()
        setProfilePhoto()
        setOnClickListener()
        setStatesCollectors()
        (requireActivity() as MainActivity).setOnBackPressed()
    }

    private fun setStatesCollectors() {
        collectFlowWithLifecycle(viewModel.logoutSuccessEvent) {
            navigateToAuthFragment()
        }
    }

    private fun setOnClickListener() {
        binding.buttonMainLogout.setOnClickListener {
            viewModel.onLogoutClicked()
        }

        binding.buttonMainContact.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToContactFragment()
            findNavController().navigate(action)
        }
    }

    private fun navigateToAuthFragment() {
        val action = NavGraphDirections.actionGlobalAuthGraph()
        val options =
            navOptions {
                popUpTo(R.id.main_graph) {
                    inclusive = true
                }
            }
        findNavController().navigate(action, options)
    }

    private fun setName() {
        binding.textViewMainUserName.text = args.userName
    }

    private fun setProfilePhoto() {
        binding.imageViewMainPhoto.setImageResource(R.drawable.bee)
    }
}
