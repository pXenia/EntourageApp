package com.entourageapp.core.ui.tools

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText

fun DrawScope.drawSegmentText(
    width: Float,
    textLayoutResult: TextLayoutResult,
    center: Offset
) {
    val textWidth = textLayoutResult.size.width
    val textHeight = textLayoutResult.size.height

    val idealX = center.x - textWidth / 2f
    val x = idealX.coerceIn(0f, width - textWidth)

    val y = center.y - textHeight / 2f

    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(x, y)
    )
}