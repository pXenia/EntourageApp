package com.entourageapp.features.userprofile.presentation.editprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.EntouragePeachAlpha80
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.arrowRight
import com.entourageapp.core.ui.components.AccentButton
import com.entourageapp.core.ui.components.Avatar
import com.entourageapp.core.ui.components.TopScreenTitle
import com.entourageapp.core.ui.folder
import com.entourageapp.core.ui.tools.showToast
import com.entourageapp.core.ui.user
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit = {},
    onManageProjectsClick: () -> Unit = {},
    viewModel: UserProfileVM = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.onIntent(UserProfileIntent.LoadProfile)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is UserProfileSideEffect.NavigateToLogin -> onNavigateToLogin()
                is UserProfileSideEffect.ShowError -> showToast(sideEffect.message)
                is UserProfileSideEffect.ShowMessage -> showToast(sideEffect.message)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(32.dp)),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item {
                TopScreenTitle(
                    modifier = Modifier.fillMaxWidth(),
                    title = "профиль".uppercase()
                )
            }

            item {
                UserTitle(
                    modifier = Modifier.fillMaxWidth(),
                    initials = state.initials,
                    name = state.name,
                    email = state.email
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    UserProjectsButton(
                        modifier = Modifier.fillMaxWidth().height(64.dp),
                        text = "Редактировать профиль",
                        icon = user,
                        onClick = { viewModel.onIntent(UserProfileIntent.SetEditDialogVisibility(isVisible = true)) }
                    )
                    UserProjectsButton(
                        modifier = Modifier.fillMaxWidth().height(64.dp),
                        text = "Управление участием в проектах",
                        icon = folder,
                        onClick = { onManageProjectsClick() }
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        color = EntourageBlack,
                        thickness = 1.dp
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier.padding(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AccentButton(
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        text = "выйти из аккаунта",
                        containerColor = EntourageBlack,
                        contentColor = EntourageWhite,
                        onClick = { viewModel.onIntent(UserProfileIntent.Logout) }
                    )
                    AccentButton(
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        text = "удалить аккаунт",
                        containerColor = EntouragePeachAlpha80,
                        contentColor = EntourageBlack,
                        onClick = { viewModel.onIntent(UserProfileIntent.SetDeleteDialogVisibility(isVisible = true)) }
                    )
                }
            }
        }

        if (state.isEditDialogVisible) {
            EditProfileDialog(
                state = state,
                onIntent = viewModel::onIntent
            )
        }

        if (state.isDeleteDialogVisible) {
            DeleteAccountDialog(
                state = state,
                onIntent = viewModel::onIntent
            )
        }
    }
}

@Composable
private fun UserTitle(
    modifier: Modifier = Modifier,
    initials: String = "ПП",
    name: String = "Пётр Петров",
    email: String = "petr@petrov.com"
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Avatar(initials = initials, size = 100)

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp)
        )
        Text(
            text = email,
            color = EntourageTeal,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun UserProjectsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String = "Редактировать профиль",
    icon: DrawableResource
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        color = EntourageTeal.copy(alpha = 0.2f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(EntourageLightBlueGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
            )

            Icon(
                painter = painterResource(arrowRight),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
        }
    }
}

@Composable
private fun InfoButton(
    modifier: Modifier,
    onClick: () -> Unit = {},
    title: String,
    description: String
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
        color = EntourageBlack.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    color = EntourageTeal
                )
            }

            Icon(
                painter = painterResource(arrowRight),
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = Color.Black
            )
        }
    }
}
