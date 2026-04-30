package com.entourageapp.core.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.appBackground(): Modifier = this.drawBehind {
    drawRect(color = EntourageLightBlueGray)

    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(EntouragePeach.copy(0.4f), Color.Transparent),
            center = Offset(x = 0f, y = 0f),
            radius = size.width * 1f
        )
    )

    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(EntouragePeach.copy(alpha = 0.4f), Color.Transparent),
            center = Offset(x = size.width * 1f, y = size.height * 1f),
            radius = size.width * 1f
        )
    )
}