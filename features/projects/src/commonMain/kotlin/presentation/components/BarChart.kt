package com.entourageapp.features.projects.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.tools.drawSegmentText
import com.entourageapp.core.ui.tools.formatAmountWithCurrency

@Composable
fun BarChart(
    materials: Float,
    materialsSum: Float,
    components: Float,
    componentsSum: Float,
    labor: Float,
    laborSum: Float,
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
        if (componentsSum > 0 || materialsSum > 0 || laborSum > 0) {
            val matText = textMeasurer.measure(formatAmountWithCurrency(materialsSum), textStyle)
            val compText = textMeasurer.measure(formatAmountWithCurrency(componentsSum), textStyle)
            val laborText = textMeasurer.measure(formatAmountWithCurrency(laborSum), textStyle)
            val matTextWidth = matText.size.width
            val laborTextWidth = laborText.size.width

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
                val minTextPadding = 4.dp.toPx()

                if (animationP > 2f && laborSum > 0) {
                    val laborX = matWidth + compWidth - overlapPx
                    val targetLaborWidth = laborWidth + overlapPx
                    val currentLaborWidth = targetLaborWidth * progLabor
                    val len = 0
                    drawRoundRect(
                        color = EntourageBlack,
                        topLeft = Offset(laborX, 0f),
                        size = Size(currentLaborWidth, height),
                        cornerRadius = corner
                    )

                    drawSegmentText(
                        width = width,
                        textLayoutResult = laborText,
                        center = Offset(laborX + currentLaborWidth / 2, textY)
                    )
                }

                if (animationP > 1f && componentsSum > 0) {
                    val compX = matWidth - overlapPx
                    val targetCompWidth = compWidth + overlapPx
                    val currentCompWidth = targetCompWidth * progComp

                    drawRoundRect(
                        color = EntouragePeach,
                        topLeft = Offset(compX, 0f),
                        size = Size(currentCompWidth, height),
                        cornerRadius = corner
                    )

                    val idealCenterX = compX + currentCompWidth / 2f
                    val halfTextWidth = compText.size.width / 2f

                    val matTextCenterX = (matWidth * progMat) / 2f
                    val matTextRightEdge = matTextCenterX + matTextWidth / 2f
                    val minX = matTextRightEdge + minTextPadding + halfTextWidth

                    val maxX = width - laborTextWidth - minTextPadding - halfTextWidth
                    val textCenterX = idealCenterX.coerceIn(minX, maxX)

                    drawSegmentText(
                        width = width,
                        textLayoutResult = compText,
                        center = Offset(textCenterX, textY)
                    )
                }

                val currentMatWidth = matWidth * progMat
                drawRoundRect(
                    color = EntourageTeal,
                    size = Size(currentMatWidth, height),
                    cornerRadius = corner
                )
                if (currentMatWidth > 0.2f && materialsSum > 0) {
                    drawSegmentText(
                        width = width,
                        textLayoutResult = matText,
                        center = Offset(currentMatWidth / 2, textY)
                    )
                }
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