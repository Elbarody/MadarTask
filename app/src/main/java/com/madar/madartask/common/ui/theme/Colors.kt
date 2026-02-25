package com.madar.madartask.common.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light Theme Colors
val PrimaryLight = Color(0xFF6650a4)
val SecondaryLight = Color(0xFF625b71)
val TertiaryLight = Color(0xFF7D5260)
val BackgroundLight = Color(0xFFFFFBFE)
val SurfaceLight = Color(0xFFFFFBFE)
val ErrorLight = Color(0xFFB3261E)
val OnPrimaryLight = Color(0xFFFFFFFF)
val OnSecondaryLight = Color(0xFFFFFFFF)
val OnTertiaryLight = Color(0xFFFFFFFF)
val OnBackgroundLight = Color(0xFF1C1B1F)
val OnSurfaceLight = Color(0xFF1C1B1F)
val OnSurfaceVariantLight = Color(0xFF49454F)
val OnErrorLight = Color(0xFFFFFFFF)

// Dark Theme Colors
val PrimaryDark = Color(0xFFD0BCFF)
val SecondaryDark = Color(0xFFCCC2DC)
val TertiaryDark = Color(0xFFEFB8C8)
val BackgroundDark = Color(0xFF1C1B1F)
val SurfaceDark = Color(0xFF1C1B1F)
val ErrorDark = Color(0xFFF2B8B5)
val OnPrimaryDark = Color(0xFF381E72)
val OnSecondaryDark = Color(0xFF332D41)
val OnTertiaryDark = Color(0xFF492532)
val OnBackgroundDark = Color(0xFFE6E1E5)
val OnSurfaceDark = Color(0xFFE6E1E5)
val OnSurfaceVariantDark = Color(0xFFCAC4D0)
val OnErrorDark = Color(0xFF601410)

// App Colors Object for direct access
object AppColors {
    val primary @Composable get() = MaterialTheme.colorScheme.primary
    val secondary @Composable get() = MaterialTheme.colorScheme.secondary
    val tertiary @Composable get() = MaterialTheme.colorScheme.tertiary
    val background @Composable get() = MaterialTheme.colorScheme.background
    val surface @Composable get() = MaterialTheme.colorScheme.surface
    val error @Composable get() = MaterialTheme.colorScheme.error
    val onPrimary @Composable get() = MaterialTheme.colorScheme.onPrimary
    val onSecondary @Composable get() = MaterialTheme.colorScheme.onSecondary
    val onBackground @Composable get() = MaterialTheme.colorScheme.onBackground
    val onSurface @Composable get() = MaterialTheme.colorScheme.onSurface
    val onSurfaceVariant @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant
    val onError @Composable get() = MaterialTheme.colorScheme.onError
}

