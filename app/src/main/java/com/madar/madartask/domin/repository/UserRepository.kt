package com.madar.madartask.domin.repository

import com.madar.madartask.domin.model.User

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insertUser(user: User): Long

    suspend fun updateUser(user: User)

    suspend fun deleteUser(user: User)

    fun getAllUsers(): Flow<List<User>>

    suspend fun getUserById(userId: Int): User?

    suspend fun deleteAllUsers()

    fun getUserCount(): Flow<Int>
}