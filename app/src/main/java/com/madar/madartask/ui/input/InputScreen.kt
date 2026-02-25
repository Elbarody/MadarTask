package com.madar.madartask.ui.input

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.madar.madartask.R
import com.madar.madartask.common.constants.Gender
import com.madar.madartask.common.ui.theme.AppColors
import com.madar.madartask.common.ui.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    onNavigateToDisplay: () -> Unit,
    viewModel: InputViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                InputEffect.ShowSuccess -> {
                    Toast.makeText(context, R.string.toast_user_saved, Toast.LENGTH_SHORT).show()
                }
                is InputEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                InputEffect.NavigateToUsers -> onNavigateToDisplay()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.input_screen_title),
                        style = AppTypography.titleLarge,
                        color = AppColors.onSurface
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.surface,
                    titleContentColor = AppColors.onSurface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.onEvent(InputEvent.NameChanged(it)) },
                label = {
                    Text(
                        text = stringResource(id = R.string.label_name),
                        style = AppTypography.labelMedium
                    )
                },
                isError = state.nameError != null,
                supportingText = {
                    state.nameError?.let { error ->
                        Text(
                            text = error,
                            style = AppTypography.bodyLarge,
                            color = AppColors.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.age,
                onValueChange = { viewModel.onEvent(InputEvent.AgeChanged(it)) },
                label = {
                    Text(
                        text = stringResource(id = R.string.label_age),
                        style = AppTypography.labelMedium
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.ageError != null,
                supportingText = {
                    state.ageError?.let { error ->
                        Text(
                            text = error,
                            style = AppTypography.bodyLarge,
                            color = AppColors.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.job,
                onValueChange = { viewModel.onEvent(InputEvent.JobChanged(it)) },
                label = {
                    Text(
                        text = stringResource(id = R.string.label_job_title),
                        style = AppTypography.labelMedium
                    )
                },
                isError = state.jobError != null,
                supportingText = {
                    state.jobError?.let { error ->
                        Text(
                            text = error,
                            style = AppTypography.bodyLarge,
                            color = AppColors.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            GenderDropdown(
                selected = state.gender,
                onSelected = { viewModel.onEvent(InputEvent.GenderChanged(it)) }
            )

            Button(
                onClick = { viewModel.onEvent(InputEvent.SaveClicked) },
                enabled = !state.isLoading && state.isFormValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.primary,
                    contentColor = AppColors.onPrimary,
                    disabledContainerColor = AppColors.onSurfaceVariant.copy(alpha = 0.38f),
                    disabledContentColor = AppColors.onSurfaceVariant.copy(alpha = 0.38f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = AppColors.onPrimary)
                } else {
                    Text(
                        text = stringResource(id = R.string.button_save),
                        style = AppTypography.labelMedium
                    )
                }
            }

            OutlinedButton(
                onClick = { viewModel.onEvent(InputEvent.ViewUsersClicked) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.button_view_users),
                    style = AppTypography.labelMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderDropdown(
    selected: Gender,
    onSelected: (Gender) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val genders = Gender.entries.toTypedArray()
    val context = LocalContext.current

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selected.getLabel(context),
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = stringResource(id = R.string.label_gender),
                    style = AppTypography.labelMedium
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            genders.forEach { gender ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = gender.getLabel(context),
                            style = AppTypography.bodyLarge
                        )
                    },
                    onClick = {
                        onSelected(gender)
                        expanded = false
                    }
                )
            }
        }
    }
}