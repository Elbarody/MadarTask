package com.madar.madartask.ui.display

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madar.madartask.domin.model.User
import com.madar.madartask.domin.usecase.DeleteAllUsersUseCase
import com.madar.madartask.domin.usecase.DeleteUserUseCase
import com.madar.madartask.domin.usecase.GetAllUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisplayViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val deleteAllUsersUseCase: DeleteAllUsersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DisplayScreenState())
    val state: StateFlow<DisplayScreenState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<DisplayScreenEffect>()
    val effect: SharedFlow<DisplayScreenEffect> = _effect.asSharedFlow()

    init {
        loadUsers()
    }

    fun onEvent(event: DisplayEvent) {
        when (event) {
            is DisplayEvent.OnDeleteUserClicked -> showDeleteDialog(event.user)
            DisplayEvent.OnConfirmDeleteUser -> confirmDeleteUser()
            DisplayEvent.OnDismissDeleteDialog -> dismissDeleteDialog()

            DisplayEvent.OnDeleteAllClicked -> showDeleteAllDialog()
            DisplayEvent.OnConfirmDeleteAll -> confirmDeleteAll()
            DisplayEvent.OnDismissDeleteAllDialog -> dismissDeleteAllDialog()

            DisplayEvent.OnNavigateToInput -> navigateToInput()
            DisplayEvent.OnRefresh -> loadUsers()
        }
    }

    private fun loadUsers() {
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                getAllUsersUseCase().collect { userList ->
                    _state.value = _state.value.copy(
                        users = userList,
                        userCount = userList.size,
                        isEmpty = userList.isEmpty(),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false)
                _effect.emit(DisplayScreenEffect.ShowError(e.message ?: "Unknown error"))
            }
        }
    }

    private fun showDeleteDialog(user: User) {
        _state.value = _state.value.copy(userToDelete = user)
    }

    private fun dismissDeleteDialog() {
        _state.value = _state.value.copy(userToDelete = null)
    }

    private fun confirmDeleteUser() {
        val userToDelete = _state.value.userToDelete ?: return

        viewModelScope.launch {
            try {
                val result = deleteUserUseCase(userToDelete)
                result.onSuccess {
                    _state.value = _state.value.copy(userToDelete = null)
                    _effect.emit(DisplayScreenEffect.ShowDeleteSuccess(userToDelete.name))
                }.onFailure { error ->
                    _state.value = _state.value.copy(userToDelete = null)
                    _effect.emit(DisplayScreenEffect.ShowError(error.message ?: "Failed to delete"))
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(userToDelete = null)
                _effect.emit(DisplayScreenEffect.ShowError(e.message ?: "Unknown error"))
            }
        }
    }

    private fun showDeleteAllDialog() {
        _state.value = _state.value.copy(showDeleteAllDialog = true)
    }

    private fun dismissDeleteAllDialog() {
        _state.value = _state.value.copy(showDeleteAllDialog = false)
    }

    private fun confirmDeleteAll() {
        val userCount = _state.value.users.size

        viewModelScope.launch {
            try {
                val result = deleteAllUsersUseCase()
                result.onSuccess {
                    _state.value = _state.value.copy(showDeleteAllDialog = false)
                    _effect.emit(DisplayScreenEffect.ShowDeleteAllSuccess(userCount))
                }.onFailure { error ->
                    _state.value = _state.value.copy(showDeleteAllDialog = false)
                    _effect.emit(DisplayScreenEffect.ShowError(error.message ?: "Failed to delete all"))
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(showDeleteAllDialog = false)
                _effect.emit(DisplayScreenEffect.ShowError(e.message ?: "Unknown error"))
            }
        }
    }

    private fun navigateToInput() {
        viewModelScope.launch {
            _effect.emit(DisplayScreenEffect.NavigateToInput)
        }
    }
}
