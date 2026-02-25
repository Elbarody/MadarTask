package com.madar.madartask.domin.usecase

import com.madar.madartask.domin.model.User
import com.madar.madartask.domin.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<List<User>> {
        return userRepository.getAllUsers()
    }
}