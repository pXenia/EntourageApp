package com.entourageapp.features.projects.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.features.projects.presentation.statistics.SummaryItem

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
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = EntourageTeal
        )
        Text(
            text = amount,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
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
    items: List<SummaryItem>,
    modifier: Modifier = Modifier
) {
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
                        text = textColumn(item.name),
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

@Composable
fun SectionTitle(
    title: String
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
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

private fun textColumn(text: String): String {
    return when (text) {
        "Материал" -> "Материалы"
        "Комплектующее" -> "Комплектующие"
        "Работа" -> "Работы"
        else -> text
    }
}
