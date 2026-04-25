package com.entourageapp.features.rooms.presentation.roomdetil

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.components.ScreenTitleTwoButtons
import com.entourageapp.core.ui.folder
import com.entourageapp.core.ui.info
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun RoomDetailScreen(
    projectId: Int,
    roomId: Int,
    onBackClick: () -> Unit = {},
    onEstimateClick: (Int, Int) -> Unit,
    onGalleryClick: (Int, Int) -> Unit,
    viewModel: RoomDetailVM = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(RoomDetailIntent.LoadRoom(projectId, roomId))
    }

    when {
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = EntourageBlack)
            }
        }

        state.error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.error ?: "Ошибка", color = EntourageBlack)
            }
        }

        state.room != null -> {
            val room = state.room!!

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ScreenTitleTwoButtons(
                    modifier = Modifier.padding(bottom = 8.dp),
                    title = room.title,
                    leftIcon = arrowLeft,
                    rightIcon = info,
                    onLeftButtonClick = onBackClick,
                    onRightButtonClick = {}
                )
                RoomInfo(
                    percent = room.projectSharePercent.toInt(),
                    workTotal = room.workTotal,
                    componentsTotal = room.componentsTotal,
                    furnitureTotal = room.furnitureTotal,
                    roomTotal = room.roomTotal,
                )
                HorizontalDivider(thickness = 1.dp, color = EntourageBlack)
                CardButton(
                    onClick = { },
                    title = "Этапы и задачи",
                    text = "N этапов и N задач"
                )
                CardButton(
                    onClick = { onGalleryClick(projectId, roomId) },
                    title = "Галерея идей",
                    text = "N идей"
                )
                CardButton(
                    onClick = { onEstimateClick(projectId, roomId) },
                    title = "Смета",
                    text = "N позиций"
                )
            }
        }
    }
}

@Composable
private fun RoomInfo(
    percent: Int,
    workTotal: Float,
    componentsTotal: Float,
    furnitureTotal: Float,
    roomTotal: Float,
) {
    val workPercent = if (roomTotal > 0) workTotal / roomTotal else 0f
    val furniturePercent = if (roomTotal > 0) furnitureTotal / roomTotal else 0f
    val componentsPercent = if (roomTotal > 0) 1f - workPercent - furniturePercent else 0f

    val workInt = (workPercent * 100).roundToInt()
    val furnitureInt = (furniturePercent * 100).roundToInt()
    val componentsInt = 100 - workInt - furnitureInt

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = EntourageBlack.copy(alpha = 0.1f),
        shape = RoundedCornerShape(32.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .height(IntrinsicSize.Min),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RoomPercent(percent)
                Label(EntourageBlack, "работы")
                Label(EntourageTeal, "материалы")
                Label(EntouragePeach, "комплектующие")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ChartColumn(color = EntourageBlack, percent = workPercent, displayPercent = workInt)
                ChartColumn(color = EntourageTeal, percent = furniturePercent, displayPercent = furnitureInt)
                ChartColumn(color = EntouragePeach, percent = componentsPercent, displayPercent = componentsInt)
            }
        }
    }
}

@Composable
private fun ChartColumn(
    percent: Float,
    color: Color,
    displayPercent: Int = (percent * 100).roundToInt()
) {
    Box(contentAlignment = Alignment.BottomCenter) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(56.dp))
                .background(color.copy(alpha = 0.3f))
                .fillMaxHeight()
                .width(35.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(56.dp))
                    .background(color)
                    .innerShadow(
                        shape = RoundedCornerShape(20.dp),
                        shadow = Shadow(
                            radius = 10.dp,
                            spread = 2.dp,
                            color = EntourageWhite.copy(alpha = 0.3f),
                            offset = DpOffset(x = 6.dp, 7.dp)
                        )
                    )
                    .fillMaxHeight(percent)
                    .width(35.dp)
            )
        }
        val glassBrush = linearGradient(
            colors = listOf(
                color.copy(alpha = 0.4f),
                EntourageWhite.copy(alpha = 0.8f)
            )
        )

        Box(
            modifier = Modifier
                .offset(x = (-12).dp, y = (-25).dp)
                .clip(RoundedCornerShape(16.dp))
                .background(glassBrush)
                .width(if (displayPercent == 100) 48.dp else 40.dp)
                .height(30.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 16.sp,
                            color = EntourageBlack
                        ).toSpanStyle()
                    ) {
                        append("$displayPercent")
                    }
                    withStyle(
                        MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 10.sp,
                            color = EntourageBlack
                        ).toSpanStyle()
                    ) {
                        append("%")
                    }
                },
            )
        }
    }
}
@Composable
fun RoomPercent(
    percent: Int,
) {
    Box(
        modifier = Modifier
            .border(1.dp, EntourageBlack, RoundedCornerShape(8.dp))
            .innerShadow(
                shape = RoundedCornerShape(8.dp),
                shadow = Shadow(
                    radius = 32.dp,
                    spread = 0.dp,
                    color = EntourageWhite.copy(alpha = 0.1f),
                )
            )
            .padding(vertical = 4.dp, horizontal = 12.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 56.sp)
                        .toSpanStyle()
                ) {
                    append("$percent")
                }
                withStyle(
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
                        .toSpanStyle()
                ) {
                    append("%")
                }
            },
        )

        Text(
            text = "всех затрат",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
            modifier = Modifier.padding(top = 56.dp)
        )
    }
}

@Composable
private fun Label(
    color: Color,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(color)
                .innerShadow(
                    shape = CircleShape,
                    shadow = Shadow(
                        radius = 6.dp,
                        spread = 2.dp,
                        color = EntourageWhite.copy(alpha = 0.2f),
                        offset = DpOffset(x = 3.dp, 4.dp)
                    )
                )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp)
        )
    }
}

@Composable
private fun CardButton(
    onClick: () -> Unit,
    title: String,
    text: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = EntourageBlack.copy(alpha = 0.1f),
        shape = RoundedCornerShape(32.dp),
    ) {
        Row(
            Modifier.padding(horizontal = 18.dp, vertical = 24.dp).clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(folder),
                modifier = Modifier.size(36.dp),
                contentDescription = null
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp)
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    color = EntourageTeal
                )
            }
        }
    }
}
