package com.madar.madartask.ui.display

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.madar.madartask.common.ui.compose.AppConfirmDialog
import com.madar.madartask.common.ui.theme.AppTypography
import com.madar.madartask.domin.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayScreen(
    onNavigateToInput: () -> Unit,
    viewModel: DisplayViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DisplayScreenEffect.ShowDeleteSuccess -> {
                    Toast.makeText(context, "${effect.userName} deleted", Toast.LENGTH_SHORT).show()
                }

                is DisplayScreenEffect.ShowDeleteAllSuccess -> {
                    Toast.makeText(context, "${effect.count} users deleted", Toast.LENGTH_SHORT).show()
                }

                is DisplayScreenEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                DisplayScreenEffect.NavigateToInput -> onNavigateToInput()
            }
        }
    }

    Scaffold(
        topBar = { DisplayTopBar(userCount = state.userCount) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            DisplayContent(
                state = state,
                onEvent = viewModel::onEvent
            )
        }
    }

    // Delete user confirmation dialog
    if (state.userToDelete != null) {
        AppConfirmDialog(
            title = "Delete User",
            message = "Are you sure you want to delete ${state.userToDelete!!.name}?",
            confirmText = "Delete",
            isDestructive = true,
            onConfirm = {
                viewModel.onEvent(DisplayEvent.OnConfirmDeleteUser)
            },
            onDismiss = {
                viewModel.onEvent(DisplayEvent.OnDismissDeleteDialog)
            }
        )
    }

    // Delete all users confirmation dialog
    if (state.showDeleteAllDialog) {
        AppConfirmDialog(
            title = "Delete All Users",
            message = "Are you sure you want to delete all users? This action cannot be undone.",
            confirmText = "Delete All",
            isDestructive = true,
            onConfirm = {
                viewModel.onEvent(DisplayEvent.OnConfirmDeleteAll)
            },
            onDismiss = {
                viewModel.onEvent(DisplayEvent.OnDismissDeleteAllDialog)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DisplayTopBar(userCount: Int) {
    TopAppBar(
        title = { Text("All Users ($userCount)", style = AppTypography.titleLarge) }
    )
}

@Composable
private fun DisplayContent(
    state: DisplayScreenState,
    onEvent: (DisplayEvent) -> Unit
) {
    when {
        state.isLoading -> LoadingState()
        state.shouldShowEmptyState -> EmptyState(onEvent = onEvent)
        state.shouldShowUserList -> UserListContent(
            users = state.users,
            onEvent = onEvent
        )
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyState(onEvent: (DisplayEvent) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No users found",
                style = AppTypography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onEvent(DisplayEvent.OnNavigateToInput) }
            ) {
                Text("Add First User", style = AppTypography.labelMedium)
            }
        }
    }
}

@Composable
private fun UserListContent(
    users: List<User>,
    onEvent: (DisplayEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        UsersList(
            users = users,
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        ActionButtons(
            onEvent = onEvent,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun UsersList(
    users: List<User>,
    onEvent: (DisplayEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users) { user ->
            UserListItem(
                user = user,
                onDelete = { onEvent(DisplayEvent.OnDeleteUserClicked(user)) }
            )
        }
    }
}

@Composable
private fun UserListItem(
    user: User,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            UserInfo(user = user, modifier = Modifier.weight(1f))
            DeleteButton(onDelete = onDelete)
        }
    }
}

@Composable
private fun UserInfo(user: User, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = user.name,
            style = AppTypography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        UserDetailText(label = "Age", value = user.age.toString())
        UserDetailText(label = "Job", value = user.jobTitle)
        UserDetailText(label = "Gender", value = user.gender)
    }
}

@Composable
private fun UserDetailText(label: String, value: String) {
    Text(
        text = "$label: $value",
        style = AppTypography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun DeleteButton(onDelete: () -> Unit) {
    IconButton(onClick = onDelete) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete user",
            tint = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun ActionButtons(
    onEvent: (DisplayEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = { onEvent(DisplayEvent.OnNavigateToInput) },
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Text("Add User", style = AppTypography.labelMedium)
        }

        OutlinedButton(
            onClick = { onEvent(DisplayEvent.OnDeleteAllClicked) },
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Text("Delete All", style = AppTypography.labelMedium)
        }
    }
}

