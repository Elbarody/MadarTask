package com.madar.madartask.domin.usecase

import com.madar.madartask.domin.model.User
import com.madar.madartask.domin.repository.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> = try {
        userRepository.deleteUser(user)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}