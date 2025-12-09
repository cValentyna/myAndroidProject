package com.project.myapp.data.contacts

import kotlinx.coroutines.flow.StateFlow

interface IUsersRepository {
    val userList: StateFlow<List<User>>
}
