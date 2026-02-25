package com.madar.madartask.common.constants

enum class Gender(val label: String) {
    MALE("Male"),
    FEMALE("Female");

    companion object {
        fun fromLabel(label: String): Gender =
            Gender.entries.firstOrNull { it.label == label } ?: MALE
    }
}