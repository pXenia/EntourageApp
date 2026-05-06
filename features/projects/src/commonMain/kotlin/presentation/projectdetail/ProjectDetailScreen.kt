package com.entourageapp.features.projects.presentation.projectdetail

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.blueprint
import com.entourageapp.core.ui.components.ProgressBar
import com.entourageapp.core.ui.components.ScreenTitleTwoButtons
import com.entourageapp.core.ui.document
import com.entourageapp.core.ui.folder
import com.entourageapp.core.ui.gallery
import com.entourageapp.core.ui.info
import com.entourageapp.core.ui.stats
import com.entourageapp.core.ui.tools.formatAmountWithCurrency
import com.entourageapp.core.ui.tools.getPlural
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
    onProjectInfoClick: (Int) -> Unit = {},
    onProjectStatsClick: (Int) -> Unit = {},
    viewModel: ProjectDetailVM = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(ProjectDetailIntent.LoadProject(projectId))
        animationProgress.animateTo(
            targetValue = 1f, animationSpec = tween(durationMillis = 1500)
        )
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
                    .systemBarsPadding()
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
                    onRightButtonClick = { onProjectInfoClick(projectId) }
                )

                DaysProgress(
                    startDate = project.startDateFormatted,
                    endDate = project.endDateFormatted ?: "—",
                    pastDays = project.pastDays,
                    allDays = project.allDays,
                    progress = project.progress,
                    animationP = animationProgress.value
                )

                Spacer(modifier = Modifier.height(24.dp))

                ProjectInfoCard(
                    roomsCount = project.roomsCount,
                    animationP = animationProgress.value,
                    budget = project.budget,
                    totalSpent = project.totalSpent,
                    onRoomListClick = { onRoomListClick(projectId) },
                    onProjectStatsClick = { onProjectStatsClick(projectId) }
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
    progress: Float,
    animationP: Float = 0.5f
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CircularDaysProgress(pastDays, allDays, progress, animationP)
        Column(
            modifier = Modifier.width(IntrinsicSize.Min),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DateBadge(startDate)
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = EntourageBlack
            )
            DateBadge(endDate)
        }
    }
}

@Composable
private fun CircularDaysProgress(
    pastDays: Int,
    allDays: Int,
    progress: Float,
    animationP: Float,
) {
    val animatedPastDays = (pastDays * animationP).toInt()

    Box(
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { progress * animationP },
            modifier = Modifier.size(200.dp),
            color = EntourageBlack,
            strokeWidth = 20.dp,
            trackColor = EntourageBlack.copy(alpha = 0.05f),
            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
            gapSize = (-16).dp
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy((-6).dp)
        ) {
            Text(
                text = "$animatedPastDays/$allDays",
                color = EntourageTeal,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 32.sp),
            )
            Text(
                text = "дни",
                color = EntourageTeal,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
            )
        }
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .innerShadow(
                    shape = CircleShape,
                    shadow = Shadow(
                        radius = 8.dp,
                        spread = 4.dp,
                        color = EntourageWhite.copy(alpha = 0.3f),
                        offset = DpOffset(x = 2.dp, 2.dp)
                    )
                )
        )
    }
}

@Composable
private fun DateBadge(
    date: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .innerShadow(
                shape = RoundedCornerShape(32.dp),
                shadow = Shadow(
                    radius = 20.dp,
                    spread = 4.dp,
                    color = EntourageWhite.copy(alpha = 0.4f),
                    offset = DpOffset(x = 4.dp, 4.dp)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 18.dp),
            text = date,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            color = EntourageBlack
        )
    }
}

@Composable
private fun ProjectInfoCard(
    roomsCount: Int,
    animationP: Float,
    budget: Float,
    totalSpent: Float,
    onRoomListClick: () -> Unit = {},
    onProjectStatsClick: () -> Unit = {},
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .innerShadow(
                shape = RoundedCornerShape(32.dp),
                shadow = Shadow(
                    radius = 36.dp,
                    spread = 8.dp,
                    color = EntourageWhite.copy(alpha = 0.2f),
                    offset = DpOffset(x = 20.dp, 20.dp)
                )
            ),
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
                StatsButton(onClick = onProjectStatsClick)
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = EntourageBlack
            )
            CostProgress(
                budget = budget,
                totalSpent = totalSpent,
                animationP = animationP
            )
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
            .border(1.dp, EntourageBlack, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
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
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 32.sp),
                    color = EntourageTeal,
                    modifier = Modifier.alignByBaseline()
                )
                Text(
                    text = getPlural(roomsCount, "комната", "комнаты", "комнат"),
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
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .background(EntourageBlack.copy(alpha = 0.85f))
            .border(1.dp, EntourageBlack, RoundedCornerShape(16.dp))
            .innerShadow(
                shape = RoundedCornerShape(16.dp),
                shadow = Shadow(
                    radius = 16.dp,
                    spread = 16.dp,
                    color = EntourageBlack.copy(alpha = 0.5f),
                    offset = DpOffset(x = 0.dp, 0.dp)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(blueprint),
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp),
                tint = EntourageWhite
            )
            Text(
                text = "список\nкомнат",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                color = EntourageWhite
            )
        }
    }
}

@Composable
private fun StatsButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .background(EntouragePeach.copy(alpha = 0.7f))
            .border(1.dp, EntourageBlack, RoundedCornerShape(16.dp))
            .innerShadow(
                shape = RoundedCornerShape(16.dp),
                shadow = Shadow(
                    radius = 16.dp,
                    spread = 16.dp,
                    color = EntouragePeach.copy(alpha = 0.5f),
                    offset = DpOffset(x = 0.dp, 0.dp)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Icon(
                painter = painterResource(stats),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
private fun CostProgress(
    budget: Float,
    totalSpent: Float,
    animationP: Float = 0.5f
) {
    val dif = ( budget - totalSpent)
    val progress = if (budget > 0 ) (totalSpent / budget).coerceIn(0f, 1f) else 1f

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProgressBar(
            color = EntourageBlack,
            height = 30.dp,
            progress = progress,
            animationP = animationP
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TextMoney("Потрачено", formatAmountWithCurrency(totalSpent))
            TextMoney("Осталось", formatAmountWithCurrency(dif))
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
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .clickable { onClick() }
            .background(EntourageTeal.copy(alpha = 0.2f))
            .innerShadow(
                shape = RoundedCornerShape(32.dp),
                shadow = Shadow(
                    radius = 16.dp,
                    spread = 16.dp,
                    color = EntourageTeal.copy(alpha = 0.06f),
                    offset = DpOffset(x = 0.dp, 0.dp)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 16.dp),
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