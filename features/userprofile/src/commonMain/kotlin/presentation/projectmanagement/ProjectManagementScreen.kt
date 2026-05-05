package com.entourageapp.features.userprofile.presentation.projectmanagement

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.EntourageRed
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.calendar
import com.entourageapp.core.ui.components.ScreenTitle
import com.entourageapp.core.ui.components.SectionTitle
import com.entourageapp.core.ui.dialogs.DeleteDialog
import com.entourageapp.core.ui.tools.showToast
import com.entourageapp.core.ui.user
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectManagementScreen(
    onBackClick: () -> Unit = {},
    viewModel: ProjectManagementVM = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.onIntent(ProjectManagementIntent.LoadProjects)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is ProjectManagementSideEffect.ShowError -> showToast(sideEffect.message)
                is ProjectManagementSideEffect.ShowMessage -> showToast(sideEffect.message)
            }
        }
    }

    if (state.isConfirmSheetVisible && state.selectedProject != null) {
        val isOwner = state.selectedProject!!.role == "owner"
        DeleteDialog(
            onDismiss = { viewModel.onIntent(ProjectManagementIntent.HideConfirmSheet) },
            onOkClick = { viewModel.onIntent(ProjectManagementIntent.ConfirmAction)},
            text = if (isOwner)
                "Вы действительно хотите удалить проект \"${state.selectedProject!!.title}\"?"
            else "Вы действительно хотите выйти из проекта \"${state.selectedProject!!.title}\"?",
            title = if (isOwner) "Удаление проекта" else "Выход из проекта",
            buttonTitle = if (isOwner) "Удалить" else "Выйти",
            sheetState =  sheetState
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        ScreenTitle(
            modifier = Modifier.fillMaxWidth(),
            title = "УПРАВЛЕНИЕ УЧАСТИЕМ\nВ ПРОЕКТАХ",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            if (state.ownedProjects.isNotEmpty()) {
                item {
                    SectionTitle("Удаление проектов")
                }
                items(state.ownedProjects) { project ->
                    ProjectActionCard(
                        projectName = project.title,
                        participantsCount = project.membersCount,
                        dateRange = project.years,
                        role = project.role,
                        buttonText = "УДАЛИТЬ",
                        color = EntourageTeal.copy(alpha = 0.2f),
                        onButtonClick = {
                            viewModel.onIntent(ProjectManagementIntent.ShowConfirmSheet(project))
                        }
                    )
                }
            }

            if (state.memberProjects.isNotEmpty()) {
                item {
                    SectionTitle("Выход из проектов")
                }
                items(state.memberProjects) { project ->
                    ProjectActionCard(
                        projectName = project.title,
                        participantsCount = project.membersCount,
                        dateRange = project.years,
                        role = project.role,
                        buttonText = "ВЫЙТИ",
                        color = EntourageBlack.copy(alpha = 0.1f),
                        onButtonClick = {
                            viewModel.onIntent(ProjectManagementIntent.ShowConfirmSheet(project))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectActionCard(
    projectName: String,
    participantsCount: Int,
    dateRange: String,
    role: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    color: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .innerShadow(
                shape = RoundedCornerShape(32.dp),
                shadow = Shadow(
                    radius = 36.dp,
                    spread = 8.dp,
                    color = EntourageWhite.copy(alpha = 0.2f),
                    offset = DpOffset(x = 10.dp, 10.dp)
                )
            ),
        shape = RoundedCornerShape(32.dp),
        color = color
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = projectName.uppercase(),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = EntourageBlack.copy(alpha = 0.6f)
            )

            ProjectInfoRow(icon = user, text = "$participantsCount участника")
            ProjectInfoRow(icon = calendar, text = dateRange)

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Ваша роль",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                RoleBadge(role)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(EntourageRed.copy(alpha = 0.2f))
                        .clickable(onClick = onButtonClick),
                ) {
                    Text(
                        text = buttonText.uppercase(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = EntourageRed,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectInfoRow(icon: DrawableResource, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = EntourageBlack
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun RoleBadge(role: String) {
    val roleTitle = when (role) {
        "owner" -> "Владелец"
        "editor" -> "Редактор"
        "viewer" -> "Читатель"
        else -> ""
    }

    Surface(
        color = EntourageLightBlueGray.copy(alpha = 0.6f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = roleTitle,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = EntourageBlack.copy(alpha = 0.7f)
        )
    }
}
