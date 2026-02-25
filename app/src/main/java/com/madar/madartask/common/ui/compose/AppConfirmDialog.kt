package com.madar.madartask.common.ui.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.madar.madartask.R
import com.madar.madartask.common.ui.theme.AppColors
import com.madar.madartask.common.ui.theme.AppTypography

@Composable
fun AppConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String = stringResource(id = R.string.button_cancel),
    isDestructive: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = AppTypography.titleLarge,
                color = AppColors.onSurface
            )
        },
        text = {
            Text(
                text = message,
                style = AppTypography.bodyLarge,
                color = AppColors.onSurfaceVariant
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    style = AppTypography.labelMedium,
                    color = if (isDestructive) AppColors.error else AppColors.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = dismissText,
                    style = AppTypography.labelMedium,
                    color = AppColors.onSurface
                )
            }
        },
        containerColor = AppColors.surface
    )
}
