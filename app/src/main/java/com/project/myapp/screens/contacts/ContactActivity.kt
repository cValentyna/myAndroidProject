package com.project.myapp.screens.contacts

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.myapp.R
import com.project.myapp.databinding.ActivityContactsBinding
import com.project.myapp.ext.componentactivity.enableEdgeToEdgeGrayStatusBar
import com.project.myapp.ext.view.initializeWindowInsetsHandling
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactActivity : AppCompatActivity() {
    private val binding: ActivityContactsBinding by lazy {
        ActivityContactsBinding.inflate(layoutInflater)
    }
    private val viewModel: ContactViewModel by viewModels()
    private val contactsAdapter = ContactAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
        initRecyclerView()
        collectUserList()
    }

    private fun setView() {
        enableEdgeToEdgeGrayStatusBar()
        setContentView(binding.root)
        binding.root.initializeWindowInsetsHandling()
    }

    private fun initRecyclerView() {
        with(binding) {
            recyclerViewContacts.addItemDecoration(
                ItemDecorator(
                    resources.getDimensionPixelSize(R.dimen.gap_item),
                    resources.getDimension(R.dimen.radius_item),
                    R.color.auth_underline,
                    resources.getDimension(R.dimen.width_item_stroke),
                ),
            )

            recyclerViewContacts.layoutManager = LinearLayoutManager(this@ContactActivity)
            recyclerViewContacts.adapter = contactsAdapter
        }
    }

    private fun collectUserList() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userList
                    .collect { users ->
                        contactsAdapter.update(users)
                    }
            }
        }
    }
}
