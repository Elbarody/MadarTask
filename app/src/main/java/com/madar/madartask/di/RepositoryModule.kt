package com.madar.madartask.di

import com.madar.madartask.data.local.UserDatabase
import com.madar.madartask.data.repository.UserRepositoryImpl
import com.madar.madartask.domin.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideUserRepository(database: UserDatabase): UserRepository {
        return UserRepositoryImpl(database.userDao())
    }
}