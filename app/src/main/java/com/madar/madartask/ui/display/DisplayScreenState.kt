package com.madar.madartask.ui.display

import com.madar.madartask.domin.model.User

data class DisplayScreenState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = true,
    val userCount: Int = 0,
    val userToDelete: User? = null,
    val showDeleteAllDialog: Boolean = false
) {
    val shouldShowEmptyState: Boolean
        get() = isEmpty && !isLoading
    
    val shouldShowUserList: Boolean
        get() = users.isNotEmpty() && !isLoading
}