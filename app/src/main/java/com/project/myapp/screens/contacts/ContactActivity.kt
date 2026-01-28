package com.project.myapp.screens.contacts

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.myapp.R
import com.project.myapp.data.contacts.User
import com.project.myapp.databinding.ActivityContactsBinding
import com.project.myapp.ext.componentactivity.enableEdgeToEdgeGrayStatusBar
import com.project.myapp.ext.view.initializeWindowInsetsHandling
import com.project.myapp.ext.view.snackBar
import com.project.myapp.imageloader.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ContactActivity : AppCompatActivity() {
    private val binding: ActivityContactsBinding by lazy {
        ActivityContactsBinding.inflate(layoutInflater)
    }
    private val viewModel: ContactViewModel by viewModels()

    @Inject
    lateinit var imageLoader: ImageLoader
    private val contactsAdapter by lazy {
        ContactAdapter(imageLoader) { user ->
            viewModel.deleteUser(user)
            showUndoSnackbar(user)
        }
    }

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

    private fun initRecyclerView() =
        with(binding) {
            recyclerViewContacts.addItemDecoration(ItemDecorator(resources.getDimensionPixelSize(R.dimen.gap_item)))
            recyclerViewContacts.layoutManager = LinearLayoutManager(this@ContactActivity)
            recyclerViewContacts.adapter = contactsAdapter
            recyclerViewContacts.itemAnimator = null
        }

    private fun collectUserList() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userList
                    .collectLatest { users ->
                        contactsAdapter.update(users)
                    }
            }
        }
    }
    private fun showUndoSnackbar(user: User) {
        binding.root.snackBar(
            getString(R.string.contact_has_been_removed),
            getString(R.string.contact_restore_information),
            resources.getInteger(R.integer.duration_snackbar_5sec),
        ) {
            viewModel.restoreUser()
            val restoredIndex = viewModel.getLastRestoredIndex()
            restoredIndex?.let { index ->
                contactsAdapter.submitList(viewModel.userList.value) {
                    binding.recyclerViewContacts.post {
                        val layoutManager = binding.recyclerViewContacts.layoutManager as LinearLayoutManager
                        val firstVisible = layoutManager.findFirstVisibleItemPosition()
                        val lastVisible = layoutManager.findLastVisibleItemPosition()
                        if (index < firstVisible || index > lastVisible) {
                            binding.recyclerViewContacts.smoothScrollToPosition(index)
                        }
                    }
                }
            }
        }
    }
}
