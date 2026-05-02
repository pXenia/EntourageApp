package com.entourageapp.features.projects.presentation.statistics

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.components.ScreenTitle

@Composable
fun StatisticsScreen(
    onBackClick: () -> Unit = {},
    projectId: Int,
) {
    val animationProgress = remember { Animatable(0f) }
    val rooms = listOf(
        RoomData("Гостиная", 0.4f, "150к", 0.3f, "110к", 0.3f, "110к"),
        RoomData("Кухня", 0.2f, "80к", 0.6f, "240к", 0.2f, "80к"),
        RoomData("Ванная", 0.3f, "90к", 0.2f, "60к", 0.5f, "150к"),
        RoomData("Спальня", 0.5f, "120к", 0.2f, "50к", 0.3f, "70к"),
        RoomData("Прихожая", 0.4f, "40к", 0.1f, "10к", 0.5f, "50к")
    )

    LaunchedEffect(Unit) {
        animationProgress.animateTo(
            targetValue = 3f, animationSpec = tween(durationMillis = 3000)
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScreenTitle(
            title = "Статистика проекта",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            rooms.forEach { room ->
                Column {
                    Text(
                        text = room.name,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        modifier = Modifier.padding(bottom = 1.dp)
                    )

                    BarChart(
                        materials = room.matPart,
                        materialsSum = room.matSum,
                        components = room.compPart,
                        componentsSum = room.compSum,
                        labor = room.laborPart,
                        laborSum = room.laborSum,
                        animationP = animationProgress.value
                    )
                }
            }

        }
    }
}

@Composable
private fun BarChart(
    materials: Float,
    materialsSum: String,
    components: Float,
    componentsSum: String,
    labor: Float,
    laborSum: String,
    animationP: Float
) {
    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.bodyMedium.copy(
        color = EntourageBlack,
        fontSize = 12.sp,
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp)
        ) {
            val width = size.width
            val height = 16.dp.toPx()
            val corner = CornerRadius(height * 0.5f, height * 0.5f)
            val overlapPx = 40f

            val progMat = (animationP).coerceIn(0f, 1f)
            val progComp = (animationP - 1f).coerceIn(0f, 1f)
            val progLabor = (animationP - 2f).coerceIn(0f, 1f)

            val matWidth = materials * width
            val compWidth = components * width
            val laborWidth = labor * width
            val textY = height + 12.sp.toPx() * 0.6f

            if (animationP > 2f) {
                val laborX = matWidth + compWidth - overlapPx
                val targetLaborWidth = laborWidth + overlapPx
                val currentLaborWidth = targetLaborWidth * progLabor

                drawRoundRect(
                    color = EntourageBlack,
                    topLeft = Offset(laborX, 0f),
                    size = Size(currentLaborWidth, height),
                    cornerRadius = corner
                )

                drawSegmentText(
                    textMeasurer = textMeasurer,
                    text = laborSum,
                    style = textStyle,
                    center = Offset(laborX + currentLaborWidth / 2, textY)
                )
            }

            if (animationP > 1f) {
                val compX = matWidth - overlapPx
                val targetCompWidth = compWidth + overlapPx
                val currentCompWidth = targetCompWidth * progComp

                drawRoundRect(
                    color = EntouragePeach,
                    topLeft = Offset(compX, 0f),
                    size = Size(currentCompWidth, height),
                    cornerRadius = corner
                )

                drawSegmentText(
                    textMeasurer = textMeasurer,
                    text = componentsSum,
                    style = textStyle,
                    center = Offset(compX + currentCompWidth / 2, textY)
                )
            }

            val currentMatWidth = matWidth * progMat
            drawRoundRect(
                color = EntourageTeal,
                size = Size(currentMatWidth, height),
                cornerRadius = corner
            )
            if (progMat > 0.2f) {
                drawSegmentText(
                    textMeasurer = textMeasurer,
                    text = materialsSum,
                    style = textStyle,
                    center = Offset(currentMatWidth / 2, textY)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .innerShadow(
                    shape = RoundedCornerShape(16.dp),
                    shadow = Shadow(
                        radius = 6.dp,
                        spread = 2.dp,
                        color = EntourageWhite.copy(alpha = 0.2f),
                        offset = DpOffset(x = 4.dp, 3.dp)
                    )
                )
        )
    }
}

data class RoomData(
    val name: String,
    val matPart: Float,
    val matSum: String,
    val compPart: Float,
    val compSum: String,
    val laborPart: Float,
    val laborSum: String
)

private fun DrawScope.drawSegmentText(
    textMeasurer: TextMeasurer,
    text: String,
    style: TextStyle,
    center: Offset
) {
    val textLayoutResult = textMeasurer.measure(text, style)
    val textWidth = textLayoutResult.size.width
    val textHeight = textLayoutResult.size.height

    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(
            x = center.x - textWidth / 2,
            y = center.y - textHeight / 2
        )
    )
}