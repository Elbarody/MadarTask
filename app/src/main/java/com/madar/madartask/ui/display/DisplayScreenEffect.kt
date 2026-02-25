package com.madar.madartask.ui.display

sealed class DisplayScreenEffect {
    data class ShowDeleteSuccess(val userName: String) : DisplayScreenEffect()
    data class ShowDeleteAllSuccess(val count: Int) : DisplayScreenEffect()
    data class ShowError(val message: String) : DisplayScreenEffect()
    data object NavigateToInput : DisplayScreenEffect()
}