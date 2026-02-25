package com.madar.madartask.ui.input

sealed interface InputEffect {
    data class ShowError(val message: String) : InputEffect
    object NavigateToUsers  : InputEffect
    object ShowSuccess : InputEffect
}