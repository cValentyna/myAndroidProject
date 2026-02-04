package com.project.myapp.data.contacts

import kotlinx.coroutines.flow.StateFlow

interface IUsersRepository {
    val userList: StateFlow<List<User>>

    fun addUser(name: String, profession: String)

    fun deleteUser(user: User)

    fun undoDeleteUser()

    fun getLastRestoredIndex(): Int?
}
