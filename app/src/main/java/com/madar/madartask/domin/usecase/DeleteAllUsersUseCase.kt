package com.madar.madartask.domin.usecase

import com.madar.madartask.domin.repository.UserRepository
import javax.inject.Inject


class DeleteAllUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Unit> = try {
        userRepository.deleteAllUsers()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}