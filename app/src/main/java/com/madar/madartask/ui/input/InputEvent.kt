package com.madar.madartask.ui.input

import com.madar.madartask.common.constants.Gender

sealed interface InputEvent {
    data class NameChanged(val value: String) : InputEvent
    data class AgeChanged(val value: String) : InputEvent
    data class JobChanged(val value: String) : InputEvent
    data class GenderChanged(val value: Gender) : InputEvent
    object SaveClicked : InputEvent
    object ViewUsersClicked : InputEvent
}