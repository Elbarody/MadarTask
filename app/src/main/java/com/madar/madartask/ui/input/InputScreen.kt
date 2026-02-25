package com.madar.madartask.ui.input

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.madar.madartask.common.constants.Gender
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
        viewModel.effect.collect {
            when (it) {
                InputEffect.ShowSuccess ->
                    Toast.makeText(context, "User saved successfully", Toast.LENGTH_SHORT).show()

                is InputEffect.ShowError ->
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()

                InputEffect.NavigateToUsers -> onNavigateToDisplay()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add User", style = AppTypography.titleLarge) })
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
                label = { Text("Name") },
                isError = state.nameError != null,
                supportingText = {
                    if (state.nameError != null) {
                        Text(
                            text = state.nameError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.age,
                onValueChange = { viewModel.onEvent(InputEvent.AgeChanged(it)) },
                label = { Text("Age") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.ageError != null,
                supportingText = {
                    if (state.ageError != null) {
                        Text(
                            text = state.ageError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.job,
                onValueChange = { viewModel.onEvent(InputEvent.JobChanged(it)) },
                label = { Text("Job Title") },
                isError = state.jobError != null,
                supportingText = {
                    if (state.jobError != null) {
                        Text(
                            text = state.jobError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            GenderDropdown(
                selected = state.gender,
                onSelected = {
                    viewModel.onEvent(InputEvent.GenderChanged(it))
                }
            )

            Button(
                onClick = { viewModel.onEvent(InputEvent.SaveClicked) },
                enabled = !state.isLoading && state.isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) CircularProgressIndicator()
                else Text("Save")
            }

            OutlinedButton(
                onClick = { viewModel.onEvent(InputEvent.ViewUsersClicked) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Users")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdown(
    selected: Gender,
    onSelected: (Gender) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val genders = Gender.entries.toTypedArray()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selected.label,
            onValueChange = {},
            readOnly = true,
            label = { Text("Gender") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor(type=MenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            genders.forEach { gender ->
                DropdownMenuItem(
                    text = { Text(gender.label) },
                    onClick = {
                        onSelected(gender)
                        expanded = false
                    }
                )
            }
        }
    }
}