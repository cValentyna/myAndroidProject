package com.project.myapp.screens.contacts

import androidx.lifecycle.ViewModel
import com.project.myapp.data.contacts.User
import com.project.myapp.data.contacts.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val usersRepository: UsersRepository ) : ViewModel() {
    val userList: StateFlow<List<User>> get() = usersRepository.userList
}
