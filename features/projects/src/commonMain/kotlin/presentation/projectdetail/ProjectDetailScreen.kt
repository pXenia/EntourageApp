package com.entourageapp.features.projects.presentation.projectdetail

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeachAlpha80
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.blueprint
import com.entourageapp.core.ui.components.ScreenTitleTwoButtons
import com.entourageapp.core.ui.document
import com.entourageapp.core.ui.folder
import com.entourageapp.core.ui.gallery
import com.entourageapp.core.ui.info
import com.entourageapp.core.ui.stats
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProjectDetailScreen(
    projectId: Int,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEstimateClick: (Int) -> Unit = {},
    onGalleryClick: (Int) -> Unit = {},
    onDocumentsClick: (Int) -> Unit = {},
    onRoomListClick: (Int) -> Unit = {},
    onProjectInfoClick: () -> Unit = {},
    viewModel: ProjectDetailVM = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(ProjectDetailIntent.LoadProject(projectId))
    }

    when {
        state.isLoading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = EntourageBlack)
            }
        }

        state.error != null -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.error ?: "Ошибка", color = EntourageBlack)
            }
        }

        state.project != null -> {
            val project = state.project!!

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ScreenTitleTwoButtons(
                    modifier = Modifier.padding(bottom = 16.dp),
                    title = project.title,
                    leftIcon = arrowLeft,
                    rightIcon = info,
                    onLeftButtonClick = onBackClick,
                    onRightButtonClick = {}
                )

                DaysProgress(
                    startDate = project.startDateFormatted,
                    endDate = project.endDateFormatted ?: "—",
                    pastDays = project.pastDays,
                    allDays = project.allDays,
                    progress = project.progress
                )

                Spacer(modifier = Modifier.height(24.dp))

                ProjectInfoCard(
                    roomsCount = project.roomsCount,
                    onRoomListClick = { onRoomListClick(projectId) }
                )

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = EntourageBlack
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SectionButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onDocumentsClick(projectId) },
                        title = "Документ",
                        icon = document
                    )
                    SectionButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onGalleryClick(projectId) },
                        title = "Галерея",
                        icon = gallery
                    )
                    SectionButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onEstimateClick(projectId) },
                        title = "Смета",
                        icon = folder
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = EntourageBlack
                )
            }
        }
    }
}

@Composable
private fun DaysProgress(
    startDate: String,
    endDate: String,
    pastDays: Int,
    allDays: Int,
    progress: Float
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CircularDaysProgress(pastDays, allDays, progress)
        Column(
            modifier = Modifier.width(IntrinsicSize.Min),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DateBadge(startDate)
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = EntourageBlack)
            DateBadge(endDate)
        }
    }
}

@Composable
private fun CircularDaysProgress(
    pastDays: Int,
    allDays: Int,
    progress: Float
) {
    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = progress,
            modifier = Modifier.size(200.dp),
            color = EntourageBlack,
            trackColor = EntourageBlack.copy(alpha = 0.2f),
            strokeWidth = 18.dp
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy((-6).dp)
        ) {
            Text(
                text = "$pastDays/$allDays",
                color = EntourageTeal,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 32.sp),
            )
            Text(
                text = "дни",
                color = EntourageTeal,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
            )
        }
    }
}

@Composable
private fun DateBadge(
    date: String = "27.02.2022"
) {
    Surface(
        color = EntourageWhite.copy(alpha = 0.6f),
        shape = RoundedCornerShape(32.dp)
    ) {
        Box(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 18.dp)
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            )
        }
    }
}

@Composable
private fun ProjectInfoCard(
    roomsCount: Int,
    onRoomListClick: () -> Unit = {}
) {
    Surface(
        color = EntourageBlack.copy(alpha = 0.1f),
        shape = RoundedCornerShape(32.dp),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row {
                RoomsInfo(roomsCount = roomsCount)
                RoomsButton(onClick = onRoomListClick)
                Spacer(modifier = Modifier.weight(1f))
                StatsButton(onClick = {})
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = EntourageBlack)
            CostProgress()
        }
    }
}

@Composable
private fun RoomsInfo(
    roomsCount: Int
) {
    Surface(
        modifier = Modifier
            .height(80.dp)
            .border(1.dp, EntourageBlack, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        color = Color.Transparent,
    ) {
        Column(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "$roomsCount",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 32.sp),
                    color = EntourageTeal,
                    modifier = Modifier.alignByBaseline()
                )
                Text(
                    text = "комнат",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
                    modifier = Modifier.alignByBaseline()
                )
            }
        }
    }
}

@Composable
private fun RoomsButton(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        containerColor = EntourageBlack,
        contentColor = EntourageWhite,
        modifier = Modifier.size(80.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(blueprint),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
            )
            Text(
                text = "список комнат",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
            )
        }
    }
}

@Composable
private fun StatsButton(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        containerColor = EntouragePeachAlpha80,
        contentColor = EntourageBlack,
        modifier = Modifier
            .size(80.dp)
            .border(1.dp, EntourageBlack, RoundedCornerShape(8.dp))
    ) {
        Column {
            Icon(
                painter = painterResource(stats),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun CostProgress() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LinearProgressIndicator(
            progress = { 0.5f },
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp),
            color = EntourageBlack,
            trackColor = EntourageBlack.copy(alpha = 0.2f),
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TextMoney("Потрачено", "2,00 млн. ₽")
            TextMoney("Осталось", "700 тыс. ₽")
        }
    }

}

@Composable
private fun TextMoney(
    title: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = EntourageTeal,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
        )
    }
}

@Composable
private fun SectionButton(
    title: String,
    icon: DrawableResource,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        color = EntourageTeal.copy(alpha = 0.2f),
        shape = RoundedCornerShape(32.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.clickable { onClick() }.padding(vertical = 24.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            )
        }
    }
}