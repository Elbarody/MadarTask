package com.madar.madartask.common.constants

import android.content.Context
import com.madar.madartask.R

enum class Gender(val resId: Int) {
    MALE(R.string.gender_male),
    FEMALE(R.string.gender_female);

    fun getLabel(context: Context): String = context.getString(resId)

    companion object {
        fun fromLabel(label: String, context: Context): Gender {
            return entries.firstOrNull { context.getString(it.resId) == label } ?: MALE
        }
    }
}