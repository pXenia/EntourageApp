package com.entourageapp.features.projects.presentation.projectinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntouragePeachAlpha30
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.components.AccentButton
import com.entourageapp.core.ui.components.InfoRow
import com.entourageapp.core.ui.components.ScreenTitle
import com.entourageapp.core.ui.components.SectionTitle
import com.entourageapp.core.ui.user
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProjectInfoScreen(
    projectId: Int,
    onBackClick: () -> Unit = {},
    onEditClick: (Int) -> Unit = {},
    viewModel: ProjectInfoVM = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(projectId) {
        viewModel.handleIntent(ProjectInfoIntent.LoadProject(projectId))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = EntourageTeal
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ScreenTitle(
                title = "Информация о проекте",
                onBackClick = onBackClick
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .clip(RoundedCornerShape(16.dp)),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                state.project?.let { project ->
                    SectionTitle(modifier = Modifier.padding(top = 4.dp), title = "О проекте")

                    InfoRow("Название", project.title)
                    InfoRow("Дата начала", project.startDateFormatted)
                    project.endDateFormatted?.let { 
                        InfoRow("Дата окончания", it) 
                    }
                    project.square?.let { 
                        InfoRow("Площадь, кв.м", it.toString()) 
                    }
                    InfoRow("Всего комнат", project.roomsCount.toString())
                    project.budget?.let { 
                        InfoRow("Бюджет", "${it.toLong()} ₽") 
                    }
                }

                if (state.members.isNotEmpty()) {
                    SectionTitle(modifier = Modifier.padding(top = 4.dp), title = "Участники")

                    state.members.forEach { member ->
                        MemberCard(
                            name = member.name,
                            role = member.roleId,
                            email = member.email
                        )
                    }
                }

                state.project?.description?.let { description ->
                    if (description.isNotBlank()) {
                        SectionTitle(modifier = Modifier.padding(top = 4.dp), title = "Описание")

                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp,
                                lineHeight = 22.sp
                            ),
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }
                }
                
                if (state.error != null) {
                    Text(
                        text = state.error ?: "Произошла ошибка",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }

            if (state.project?.role != 3) {
                AccentButton(
                    modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth().height(56.dp),
                    text = "Редактировать",
                    onClick = { onEditClick(projectId) },
                    elevation = 0.dp,
                    containerColor = EntouragePeach.copy(alpha = 0.7f),
                    contentColor = EntourageBlack
                )
            }
        }
    }
}

@Composable
private fun MemberCard(
    name: String,
    role: Int,
    email: String
) {
    val roleTitle = when (role) {
        1 -> "владелец"
        2 -> "редактор"
        3 -> "читатель"
        else -> ""
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = EntouragePeachAlpha30,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(user),
                contentDescription = null,
                modifier = Modifier.padding(8.dp),
                tint = EntourageBlack
            )
        }

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                )
                Text(
                    text = " ($roleTitle)",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = EntourageTeal.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                )
            }
            Text(
                text = email,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = EntourageBlack.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            )
        }
    }
}
