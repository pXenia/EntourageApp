package com.entourageapp.features.projects.presentation.statistics

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.components.ScreenTitle
import com.entourageapp.features.projects.presentation.components.BarChart

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
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 16.dp),
    ) {
        ScreenTitle(
            title = "Статистика проекта",
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(4.dp))

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .verticalScroll(rememberScrollState())
                .padding(top = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BudgetOverview(
                spent = "742 500 ₽",
                planned = "850 000 ₽",
                progress = 0.87f,
                animationP = animationProgress.value
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SectionTitle("Затраты по комнатам")

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

            BudgetSummaryTable()
        }
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

@Composable
fun BudgetOverview(
    spent: String,
    planned: String,
    progress: Float,
    animationP: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SectionTitle("План-факт")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BudgetCard(
                title = "Потрачено",
                amount = spent,
                modifier = Modifier.weight(1f)
            )
            BudgetCard(
                title = "Запланировано",
                amount = planned,
                modifier = Modifier.weight(1f)
            )
        }
        BudgetProgressBar(progress = progress, animationP = animationP)
    }
}

@Composable
fun BudgetCard(
    title: String,
    amount: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            color = EntourageTeal
        )
        Text(
            text = amount,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
            color = EntourageBlack
        )
    }
}

@Composable
fun BudgetProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    animationP: Float
) {
    Box {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp)
            ) {
                val width = size.width
                val height = size.height
                val corner = CornerRadius(height * 0.5f, height * 0.5f)

                val currentValue = width * progress * (animationP / 3f).coerceIn(0f, 1f)
                drawRoundRect(
                    color = EntouragePeach,
                    size = Size(currentValue, height),
                    cornerRadius = corner
                )
            }
            Text(
                text = "${(progress * 100).toInt()}% бюджета потрачено",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                color = EntourageBlack
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(26.dp)
                .innerShadow(
                    shape = RoundedCornerShape(16.dp),
                    shadow = Shadow(
                        radius = 6.dp,
                        spread = 2.dp,
                        color = EntourageWhite.copy(alpha = 0.2f),
                        offset = DpOffset(x = 6.dp, 7.dp)
                    )
                )
        )
    }
}

@Composable
fun BudgetSummaryTable(
    modifier: Modifier = Modifier
) {
    val items = listOf(
        SummaryItem("Материалы", 0.45f, "334 125 ₽", EntourageTeal),
        SummaryItem("Комплектующие", 0.30f, "222 750 ₽", EntouragePeach),
        SummaryItem("Мебель", 0.25f, "185 625 ₽", EntourageBlack)
    )

    Column(
        modifier = Modifier.padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionTitle("Затраты по категориям")

        Column(
            modifier = modifier
                .fillMaxWidth()
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(item.color)
                            .innerShadow(
                                shape = CircleShape,
                                shadow = Shadow(
                                    radius = 4.dp,
                                    spread = 2.dp,
                                    color = EntourageWhite.copy(alpha = 0.2f),
                                    offset = DpOffset(x = 2.dp, 2.dp)
                                )
                            )
                    )
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 12.dp).weight(1f),
                        color = EntourageBlack.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "${(item.percent * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = EntourageBlack.copy(alpha = 0.5f)
                    )
                    Text(
                        text = item.amount,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = EntourageBlack
                    )
                }
                if (index < items.size - 1) {
                    HorizontalDivider(
                        color = EntourageBlack.copy(alpha = 0.1f),
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

private data class SummaryItem(
    val name: String,
    val percent: Float,
    val amount: String,
    val color: Color
)

@Composable
fun SectionTitle(
    title: String
){
    Column(
        modifier = Modifier.fillMaxWidth().padding(top= 8.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = EntourageTeal,
                fontSize = 18.sp,
            ),
        )
        HorizontalDivider(thickness = 1.dp, color = EntourageBlack)
    }
}
