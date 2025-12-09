package com.project.myapp.di

import com.project.myapp.data.contacts.IUsersRepository
import com.project.myapp.data.contacts.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUsersRepository(usersRepository: UsersRepository): IUsersRepository
}
