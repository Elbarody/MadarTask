package com.madar.madartask.di

import com.madar.madartask.domin.repository.UserRepository
import com.madar.madartask.domin.usecase.DeleteAllUsersUseCase
import com.madar.madartask.domin.usecase.DeleteUserUseCase
import com.madar.madartask.domin.usecase.GetAllUsersUseCase
import com.madar.madartask.domin.usecase.SaveUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideSaveUserUseCase(userRepository: UserRepository): SaveUserUseCase {
        return SaveUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetAllUsersUseCase(userRepository: UserRepository): GetAllUsersUseCase {
        return GetAllUsersUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteUserUseCase(userRepository: UserRepository): DeleteUserUseCase {
        return DeleteUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteAllUsersUseCase(userRepository: UserRepository): DeleteAllUsersUseCase {
        return DeleteAllUsersUseCase(userRepository)
    }

}