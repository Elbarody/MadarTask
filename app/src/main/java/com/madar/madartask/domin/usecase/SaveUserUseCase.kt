package com.madar.madartask.domin.usecase

import com.madar.madartask.common.constants.AppConstants.MAX_AGE
import com.madar.madartask.common.constants.AppConstants.MIN_AGE
import com.madar.madartask.domin.model.User
import com.madar.madartask.domin.repository.UserRepository
import javax.inject.Inject


class SaveUserUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(
        name: String,
        age: String,
        job: String,
        gender: String
    ): Result<Unit> {

        if (name.isBlank())
            return Result.failure(IllegalArgumentException("Name is required"))

        val ageInt = age.toIntOrNull()
            ?: return Result.failure(IllegalArgumentException("Age must be a number"))

        if (ageInt !in MIN_AGE..MAX_AGE)
            return Result.failure(IllegalArgumentException("Age must be between 18 and 65"))

        if (job.isBlank())
            return Result.failure(IllegalArgumentException("Job title is required"))

        if (gender.isBlank())
            return Result.failure(IllegalArgumentException("Gender is required"))

        return runCatching {
            repository.insertUser(
                User(
                    id = 0,
                    name = name.trim(),
                    age = ageInt,
                    jobTitle = job.trim(),
                    gender = gender
                )
            )
        }
    }
}