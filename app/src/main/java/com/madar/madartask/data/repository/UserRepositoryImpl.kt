package com.madar.madartask.data.repository

import com.madar.madartask.data.local.dao.UserDao
import com.madar.madartask.data.mapper.toUser
import com.madar.madartask.data.mapper.toEntity
import com.madar.madartask.domin.model.User
import com.madar.madartask.domin.repository.UserRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao): UserRepository {
    override suspend fun insertUser(user: User): Long = userDao.insertUser(user.toEntity())

    override suspend fun updateUser(user: User) = userDao.updateUser(user.toEntity())

    override suspend fun deleteUser(user: User) = userDao.deleteUser(user.toEntity())

    override fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map { entities ->
            entities.map { entity ->
                entity.toUser()
            }
        }
    }

    override suspend fun getUserById(userId: Int): User? = userDao.getUserById(userId)?.toUser()

    override suspend fun deleteAllUsers() = userDao.deleteAllUsers()

    override fun getUserCount(): Flow<Int> = userDao.getUserCount()
}