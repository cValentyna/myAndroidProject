package com.project.myapp.screens.contacts

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.myapp.R
import com.project.myapp.databinding.ActivityContactsBinding
import com.project.myapp.ext.componentactivity.enableEdgeToEdgeGrayStatusBar
import com.project.myapp.ext.view.initializeWindowInsetsHandling
import com.project.myapp.ext.view.snackBar
import com.project.myapp.imageloader.ImageLoader
import com.project.myapp.screens.contacts.ContactDialogFragment.Companion.KEY_NAME
import com.project.myapp.screens.contacts.ContactDialogFragment.Companion.KEY_PROFESSION
import com.project.myapp.screens.contacts.ContactDialogFragment.Companion.REQUEST_KEY
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
            showUndoSnackbar()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
        initRecyclerView()
        collectUserList()
        swipeToDelete()
        setupAddContactDialog()
    }

    private fun setView() {
        enableEdgeToEdgeGrayStatusBar()
        setContentView(binding.root)
        binding.root.initializeWindowInsetsHandling()
    }

    private fun initRecyclerView() =
        with(binding.recyclerViewContacts) {
            addItemDecoration(ItemDecorator(resources.getDimensionPixelSize(R.dimen.gap_item)))
            layoutManager = LinearLayoutManager(this@ContactActivity)
            adapter = contactsAdapter
            itemAnimator = null
        }

    private fun collectUserList() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userList.collectLatest { users ->
                    contactsAdapter.update(users) {
                        scrollToRestorePosition()
                    }
                }
            }
        }
    }

    /*
      Scrolls RecyclerView to the restored user if it was last or first.
      Uses "post" to make sure LayoutManager position are updated after
      the list has been drawn again.
     */

    private fun scrollToRestorePosition() {
        val restoredIndex = viewModel.getLastRestoredIndex() ?: return
        binding.recyclerViewContacts.post {
            val layoutManager =
                binding.recyclerViewContacts.layoutManager as LinearLayoutManager
            val firstVisible = layoutManager.findFirstVisibleItemPosition()
            val lastVisible = layoutManager.findLastVisibleItemPosition()
            if (restoredIndex < firstVisible || restoredIndex > lastVisible) {
                binding.recyclerViewContacts.smoothScrollToPosition(restoredIndex)
            }
        }
    }

    private fun showUndoSnackbar() {
        binding.root.snackBar(
            getString(R.string.contact_has_been_removed),
            getString(R.string.contact_restore_information),
            resources.getInteger(R.integer.duration_snackbar_5sec),
            onDismiss = { viewModel.clearLastDeleted() },
        ) {
            viewModel.restoreUser()
        }
    }

    private fun swipeToDelete() {
        ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ): Boolean = false

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int,
                ) {
                    val position = viewHolder.adapterPosition
                    val item = contactsAdapter.currentList[position]
                    viewModel.deleteUser(item)
                    showUndoSnackbar()
                }
            },
        ).attachToRecyclerView(binding.recyclerViewContacts)
    }

    private fun setupAddContactDialog() {
        binding.contactAddContacts.setOnClickListener {
            val dialog = ContactDialogFragment()
            dialog.show(supportFragmentManager, "customDialog")
        }
        setAddUserResultListener()
    }

    private fun setAddUserResultListener() {
        supportFragmentManager.setFragmentResultListener(REQUEST_KEY, this) { _, bundle ->
            val name = bundle.getString(KEY_NAME)
            val profession = bundle.getString(KEY_PROFESSION)
            viewModel.addUser(name.toString(), profession.toString())
        }
    }
}
