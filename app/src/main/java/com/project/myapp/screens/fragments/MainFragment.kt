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
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.project.myapp.MainGraphArgs
import com.project.myapp.NavGraphDirections
import com.project.myapp.R
import com.project.myapp.databinding.FragmentMainBinding
import com.project.myapp.screens.MainActivity
import com.project.myapp.screens.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    private val args: MainGraphArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setName()
        setProfilePhoto()
        setOnClickListener()
        (requireActivity() as MainActivity).setOnBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClickListener() {
        binding.buttonMainLogout.setOnClickListener {
            viewModel.forgetUser()
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.isCheckedFlow.collect { isChecked ->
                        if (!isChecked) {
                            goToAthFragment()
                        }
                    }
                }
            }
        }

        binding.buttonMainContact.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToContactFragment()
            findNavController().navigate(action)
        }
    }

    private fun goToAthFragment() {
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
