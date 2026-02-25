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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.madar.madartask.R
import com.madar.madartask.common.ui.compose.AppConfirmDialog
import com.madar.madartask.common.ui.theme.AppColors
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
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_user_deleted, effect.userName),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is DisplayScreenEffect.ShowDeleteAllSuccess -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_users_deleted, effect.count),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is DisplayScreenEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                DisplayScreenEffect.NavigateToInput -> onNavigateToInput()
            }
        }
    }

    Scaffold(
        topBar = {
            DisplayTopBar(
                userCount = state.userCount,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.surface,
                    titleContentColor = AppColors.onSurface
                )
            )
        }
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
            title = stringResource(id = R.string.dialog_delete_user_title),
            message = stringResource(
                id = R.string.dialog_delete_user_message,
                state.userToDelete!!.name
            ),
            confirmText = stringResource(id = R.string.button_delete),
            dismissText = stringResource(id = R.string.button_cancel),
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
            title = stringResource(id = R.string.dialog_delete_all_title),
            message = stringResource(id = R.string.dialog_delete_all_message),
            confirmText = stringResource(id = R.string.button_delete_all),
            dismissText = stringResource(id = R.string.button_cancel),
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
private fun DisplayTopBar(
    userCount: Int,
    colors: TopAppBarColors
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.display_screen_title, userCount),
                style = AppTypography.titleLarge,
                color = AppColors.onSurface
            )
        },
        colors = colors
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
        CircularProgressIndicator(
            color = AppColors.primary
        )
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
                text = stringResource(id = R.string.empty_state_title),
                style = AppTypography.titleLarge,
                color = AppColors.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onEvent(DisplayEvent.OnNavigateToInput) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.primary,
                    contentColor = AppColors.onPrimary
                )
            ) {
                Text(
                    text = stringResource(id = R.string.button_add_first_user),
                    style = AppTypography.labelMedium
                )
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
        colors = CardDefaults.cardColors(
            containerColor = AppColors.onSecondary
        ),
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
            fontWeight = FontWeight.Bold,
            color = AppColors.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        UserDetailText(
            label = stringResource(id = R.string.user_detail_age, user.age.toString())
        )
        UserDetailText(
            label = stringResource(id = R.string.user_detail_job, user.jobTitle)
        )
        UserDetailText(
            label = stringResource(id = R.string.user_detail_gender, user.gender)
        )
    }
}

@Composable
private fun UserDetailText(label: String) {
    Text(
        text = label,
        style = AppTypography.bodyLarge,
        color = AppColors.onSurfaceVariant
    )
}

@Composable
private fun DeleteButton(onDelete: () -> Unit) {
    IconButton(onClick = onDelete) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(id = R.string.content_desc_delete_user),
            tint = AppColors.error
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
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.primary,
                contentColor = AppColors.onPrimary
            )
        ) {
            Text(
                text = stringResource(id = R.string.button_add_user),
                style = AppTypography.labelMedium
            )
        }

        OutlinedButton(
            onClick = { onEvent(DisplayEvent.OnDeleteAllClicked) },
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Text(
                text = stringResource(id = R.string.button_delete_all),
                style = AppTypography.labelMedium,
                color = AppColors.error
            )
        }
    }
}
