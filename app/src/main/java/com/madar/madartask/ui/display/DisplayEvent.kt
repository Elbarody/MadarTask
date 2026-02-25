package com.madar.madartask.ui.display

import com.madar.madartask.domin.model.User

sealed interface DisplayEvent {
    data class OnDeleteUserClicked(val user: User) : DisplayEvent
    data object OnConfirmDeleteUser : DisplayEvent
    data object OnDismissDeleteDialog : DisplayEvent

    data object OnDeleteAllClicked : DisplayEvent
    data object OnConfirmDeleteAll : DisplayEvent
    data object OnDismissDeleteAllDialog : DisplayEvent

    data object OnNavigateToInput : DisplayEvent
    data object OnRefresh : DisplayEvent
}
