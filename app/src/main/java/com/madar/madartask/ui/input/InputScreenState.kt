package com.madar.madartask.ui.input

import com.madar.madartask.common.constants.Gender


data class InputScreenState(
    val name: String = "",
    val nameError: String? = null,
    val age: String = "",
    val ageError: String? = null,
    val job: String = "",
    val jobError: String? = null,
    val gender: Gender = Gender.MALE,
    val isLoading: Boolean = false
) {
    val isFormValid: Boolean = name.isNotBlank() &&
            age.isNotBlank() &&
            job.isNotBlank() &&
            nameError == null &&
            ageError == null &&
            jobError == null
}