package com.madar.madartask.data.mapper

import com.madar.madartask.data.local.entity.UserEntity
import com.madar.madartask.domin.model.User

fun UserEntity.toUser(): User {
    return User(
        id = id,
        name = name,
        age = age,
        jobTitle = jobTitle,
        gender = gender
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        age = age,
        jobTitle = jobTitle,
        gender = gender
    )
}