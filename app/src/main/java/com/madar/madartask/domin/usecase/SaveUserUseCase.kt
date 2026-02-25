package com.madar.madartask.domin.usecase

import com.madar.madartask.domin.model.User
import com.madar.madartask.domin.repository.UserRepository
import javax.inject.Inject


class SaveUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        name: String,
        age: String,
        jobTitle: String,
        gender: String
    ): Result<Unit> {
        return try {
            if (name.isBlank() || age.isBlank() || jobTitle.isBlank() || gender.isBlank()) {
                return Result.failure(IllegalArgumentException("All fields are required"))
            }

            val ageInt = age.toIntOrNull()
                ?: return Result.failure(IllegalArgumentException("Age must be a valid number"))

            if (ageInt !in 18..65) {
                return Result.failure(IllegalArgumentException("Age must be between 18-65"))
            }

            val user = User(
                id = 0,
                name = name.trim(),
                age = ageInt,
                jobTitle = jobTitle.trim(),
                gender = gender
            )

            userRepository.insertUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}