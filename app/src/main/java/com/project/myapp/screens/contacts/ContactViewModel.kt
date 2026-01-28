package com.project.myapp.screens.contacts

import androidx.lifecycle.ViewModel
import com.project.myapp.data.contacts.IUsersRepository
import com.project.myapp.data.contacts.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val usersRepository: IUsersRepository
) : ViewModel() {
    val userList: StateFlow<List<User>> get() = usersRepository.userList

    fun addUser(name: String, profession: String){
        usersRepository.addUser(name, profession, "")
    }

    fun deleteUser(user: User) {
        usersRepository.deleteUser(user)
    }

    fun restoreUser() {
        usersRepository.undoDeleteUser()
    }

    fun getLastRestoredIndex(): Int?{
        return usersRepository.getLastRestoredIndex()
    }
}
