package com.madar.madartask.ui.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madar.madartask.common.constants.AppConstants.MAX_AGE
import com.madar.madartask.common.constants.AppConstants.MIN_AGE
import com.madar.madartask.domin.usecase.SaveUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class InputViewModel @Inject constructor(
    private val saveUserUseCase: SaveUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(InputScreenState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<InputEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: InputEvent) {
        when (event) {
            is InputEvent.NameChanged -> {
                update { copy(name = event.value, nameError = validateName(event.value)) }
            }
            is InputEvent.AgeChanged -> {
                update { copy(age = event.value, ageError = validateAge(event.value)) }
            }
            is InputEvent.JobChanged -> {
                update { copy(job = event.value, jobError = validateJob(event.value)) }
            }
            is InputEvent.GenderChanged -> update { copy(gender = event.value) }
            InputEvent.SaveClicked -> saveUser()
            InputEvent.ViewUsersClicked -> emitEffect(InputEffect.NavigateToUsers)
        }
    }

    private fun validateName(name: String): String? {
        return if (name.isBlank()) "Name is required" else null
    }

    private fun validateAge(age: String): String? {
        if (age.isBlank()) return "Age is required"
        val ageInt = age.toIntOrNull()
            ?: return "Age must be a number"
        if (ageInt !in MIN_AGE..MAX_AGE)
            return "Age must be between $MIN_AGE and $MAX_AGE"
        return null
    }

    private fun validateJob(job: String): String? {
        return if (job.isBlank()) "Job title is required" else null
    }

    private fun saveUser() = viewModelScope.launch {
        update { copy(isLoading = true) }

        saveUserUseCase(
            name = state.value.name,
            age = state.value.age,
            job = state.value.job,
            gender = state.value.gender.label
        ).onSuccess {
            update { InputScreenState() }
            emitEffect(InputEffect.ShowSuccess)
        }.onFailure {
            update { copy(isLoading = false) }
            emitEffect(InputEffect.ShowError(it.message ?: "Error"))
        }
    }

    private fun update(block: InputScreenState.() -> InputScreenState) {
        _state.value = _state.value.block()
    }

    private fun emitEffect(effect: InputEffect) = viewModelScope.launch {
        _effect.emit(effect)
    }
}