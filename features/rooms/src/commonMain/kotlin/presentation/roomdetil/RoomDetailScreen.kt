package com.entourageapp.features.rooms.presentation.roomdetil

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.entourageapp.core.navigation.Role
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.components.ScreenTitleTwoButtons
import com.entourageapp.core.ui.folder
import com.entourageapp.core.ui.info
import com.entourageapp.core.ui.tools.getPlural
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun RoomDetailScreen(
    projectId: Int,
    roomId: Int,
    roleId: Role,
    onBackClick: () -> Unit = {},
    onEstimateClick: (Int, Int, Role) -> Unit,
    onGalleryClick: (Int, Int, Role) -> Unit,
    onRoomInfoClick: (Int, Int, Role) -> Unit,
    onStagesClick: (Int, Role) -> Unit,
    viewModel: RoomDetailVM = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(RoomDetailIntent.LoadRoom(projectId, roomId))
        animationProgress.animateTo(
            targetValue = 1f, animationSpec = tween(durationMillis = 1000)
        )
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
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ScreenTitleTwoButtons(
                    title = room.title,
                    leftIcon = arrowLeft,
                    rightIcon = info,
                    onLeftButtonClick = onBackClick,
                    onRightButtonClick = { onRoomInfoClick(projectId, roomId, roleId) }
                )
                RoomInfo(
                    percent = room.projectSharePercent.toInt(),
                    workTotal = room.workTotal,
                    componentsTotal = room.componentsTotal,
                    furnitureTotal = room.furnitureTotal,
                    roomTotal = room.roomTotal,
                    animationP = animationProgress.value
                )
                HorizontalDivider(thickness = 1.dp, color = EntourageBlack)
                CardButton(
                    onClick = { onStagesClick(roomId, roleId) },
                    title = "Этапы и задачи",
                    text = "${room.stagesCount} ${getPlural(room.stagesCount, "этап", "этапа", "этапов")} и ${room.tasksCount} ${getPlural(room.tasksCount, "задача", "задачи", "задач")}",
                )
                CardButton(
                    onClick = { onGalleryClick(projectId, roomId, roleId) },
                    title = "Галерея",
                    text = "${room.photoCount} ${getPlural(room.photoCount, "идея", "идеи", "идей")}",
                )
                CardButton(
                    onClick = { onEstimateClick(projectId, roomId, roleId) },
                    title = "Смета",
                    text = "${room.estimateItemsCount} ${getPlural(room.estimateItemsCount, "позиция", "позиции", "позиций")}",
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
    animationP: Float
) {
    val workInt: Int
    val furnitureInt: Int
    val componentsInt: Int

    if (roomTotal <= 0f) {
        workInt = 0
        furnitureInt = 0
        componentsInt = 0
    } else {
        workInt = (workTotal / roomTotal * 100).roundToInt()
        furnitureInt = (furnitureTotal / roomTotal * 100).roundToInt()
        componentsInt = (100 - workInt - furnitureInt).coerceAtLeast(0)
    }

    val workPercent = if (roomTotal > 0) workInt / 100f else 0f
    val furniturePercent = if (roomTotal > 0) furnitureInt / 100f else 0f
    val componentsPercent = if (roomTotal > 0) componentsInt / 100f else 0f

    Box(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(EntourageBlack.copy(alpha = 0.1f))
            .innerShadow(
                shape = RoundedCornerShape(32.dp),
                shadow = Shadow(
                    radius = 36.dp,
                    spread = 8.dp,
                    color = EntourageWhite.copy(alpha = 0.2f),
                    offset = DpOffset(x = 10.dp, 10.dp)
                )
            )
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .height(IntrinsicSize.Min),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RoomPercent(percent = percent, animationP = animationP)
                Label(EntourageBlack, "работы")
                Label(EntourageTeal, "материалы")
                Label(EntouragePeach, "комплектующие")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ChartColumn(color = EntourageBlack, percent = workPercent, displayPercent = workInt, animationP = animationP)
                ChartColumn(color = EntourageTeal, percent = furniturePercent, displayPercent = furnitureInt, animationP = animationP)
                ChartColumn(color = EntouragePeach, percent = componentsPercent, displayPercent = componentsInt, animationP = animationP)
            }
        }
    }
}

@Composable
private fun ChartColumn(
    percent: Float,
    color: Color,
    displayPercent: Int,
    animationP: Float
) {
    val percentVal = percent * animationP
    val animatedDisplayPercent = (displayPercent * animationP).toInt()

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
                    .fillMaxHeight(percentVal)
                    .width(35.dp)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(56.dp))
                    .fillMaxHeight()
                    .innerShadow(
                        shape = RoundedCornerShape(20.dp),
                        shadow = Shadow(
                            radius = 10.dp,
                            spread = 2.dp,
                            color = EntourageWhite.copy(alpha = 0.3f),
                            offset = DpOffset(x = 6.dp, 7.dp)
                        )
                    )
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
                .width(if (animatedDisplayPercent == 100) 48.dp else 40.dp)
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
                        append("$animatedDisplayPercent")
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
    animationP: Float
) {
    val animatedPercent = (percent* animationP).toInt()
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
                    append("$animatedPercent")
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .clickable{ onClick() }
            .background(EntourageBlack.copy(alpha = 0.1f))
            .innerShadow(
                shape = RoundedCornerShape(32.dp),
                shadow = Shadow(
                    radius = 20.dp,
                    spread = 8.dp,
                    color = EntourageWhite.copy(alpha = 0.2f),
                    offset = DpOffset(x = 8.dp, 6.dp)
                )
            ),
    ) {
        Row(
            Modifier.padding(horizontal = 18.dp, vertical = 24.dp),
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
