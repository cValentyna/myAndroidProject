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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.myapp.R
import com.project.myapp.data.contacts.User
import com.project.myapp.databinding.FragmentContactBinding
import com.project.myapp.ext.view.snackBar
import com.project.myapp.imageloader.ImageLoader
import com.project.myapp.screens.contacts.ContactAdapter
import com.project.myapp.screens.contacts.ContactDialogFragment
import com.project.myapp.screens.contacts.ContactDialogFragment.Companion.KEY_NAME
import com.project.myapp.screens.contacts.ContactDialogFragment.Companion.KEY_PROFESSION
import com.project.myapp.screens.contacts.ContactDialogFragment.Companion.REQUEST_KEY
import com.project.myapp.screens.contacts.ContactViewModel
import com.project.myapp.screens.contacts.ItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ContactFragment : Fragment(R.layout.fragment_contact) {
    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContactViewModel by viewModels()

    @Inject
    lateinit var imageLoader: ImageLoader
    private val contactsAdapter by lazy {
        ContactAdapter(
            imageLoader,
            onDeleteUser = { user ->
                viewModel.deleteUser(user)
                showUndoSnackBar(user)
            },
            onOpenDetails = { user ->
                openDetailsFragment(user)
            },
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        collectUserList()
        swipeToDelete()
        setupAddContactDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() =
        with(binding.recyclerViewContacts) {
            addItemDecoration(ItemDecorator(resources.getDimensionPixelSize(R.dimen.gap_item)))
            layoutManager = LinearLayoutManager(requireContext())
            adapter = contactsAdapter
        }

    private fun openDetailsFragment(user: User) {
        val fragment =
            ContactProfileFragment.newInstance(
                name = user.name,
                profession = user.profession,
                photoUrl = user.photoUrl,
            )

        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_view, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun collectUserList() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userList
                    .collectLatest { users ->
                        contactsAdapter.update(users)
                        scrollToRestorePosition()
                    }
            }
        }
    }

    /*
      Scrolls RecyclerView to the restored user if it was last or first
      or if the restored position is currently invisible.
      Added LayoutChangeListener in Fragment version to ensure
      scroll restore after submitList.
      Activity version: Used post  instead of LayoutChangeListener
     */

    private fun scrollToRestorePosition() {
        binding.recyclerViewContacts.apply {
            addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                override fun onLayoutChange(
                    v: View?, left: Int, top: Int, right: Int, bottom: Int,
                    oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int,
                ) {
                    val restoredIndex = viewModel.getLastRestoredIndex() ?: return
                    val layoutManager = layoutManager as LinearLayoutManager
                    val firstVisible = layoutManager.findFirstVisibleItemPosition()
                    val lastVisible = layoutManager.findLastVisibleItemPosition()
                    if (restoredIndex < firstVisible || restoredIndex > lastVisible) {
                        smoothScrollToPosition(restoredIndex)
                    }
                    removeOnLayoutChangeListener(this)
                }
            })
        }
    }

    private fun showUndoSnackBar(deletedUser: User) {
        binding.root.snackBar(
            getString(R.string.contact_has_been_removed),
            getString(R.string.contact_restore_information),
            resources.getInteger(R.integer.duration_snackbar_5sec),
            onDismiss = { viewModel.clearIfSame(deletedUser) },
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
                    showUndoSnackBar(item)
                }
            },
        ).attachToRecyclerView(binding.recyclerViewContacts)
    }

    private fun setupAddContactDialog() {
        binding.contactAddContacts.setOnClickListener {
            val dialog = ContactDialogFragment()
            dialog.show(childFragmentManager, "customDialog")
        }
        setAddUserResultListener()
    }

    private fun setAddUserResultListener() {
        childFragmentManager.setFragmentResultListener(REQUEST_KEY, this) { _, bundle ->
            val name = bundle.getString(KEY_NAME)
            val profession = bundle.getString(KEY_PROFESSION)
            viewModel.addUser(name.toString(), profession.toString())
        }
    }
}
