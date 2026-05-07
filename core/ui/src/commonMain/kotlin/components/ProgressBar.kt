package com.entourageapp.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.entourageapp.core.ui.EntourageWhite

@Composable
fun ProgressBar(
    color: Color,
    height: Dp,
    progress: Float,
    animationP: Float
) {
    Box {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        ) {
            val width = size.width
            val height = size.height
            val corner = CornerRadius(height * 0.5f, height * 0.5f)

            val currentValue = width * progress * (animationP).coerceIn(0f, 1f)
            drawRoundRect(
                color = EntourageWhite.copy(alpha = 0.05f),
                size = Size(width, height),
                cornerRadius = corner
            )
            drawRoundRect(
                color = color,
                size = Size(currentValue, height),
                cornerRadius = corner
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .innerShadow(
                    shape = RoundedCornerShape(16.dp),
                    shadow = Shadow(
                        radius = 8.dp,
                        spread = 4.dp,
                        color = EntourageWhite.copy(alpha = 0.17f),
                        offset = DpOffset(x = 6.dp, 7.dp)
                    )
                )
        )
    }
}