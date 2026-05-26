package com.entourageapp.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageWhite

@Composable
fun AccentButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String,
    containerColor: Color,
    contentColor: Color,
    shadowColor: Color = EntourageWhite.copy(alpha = 0.2f),
    elevation: Dp = 0.dp,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .clickable{ if (enabled) onClick() }
            .background(containerColor)
            .innerShadow(
                shape = RoundedCornerShape(32.dp),
                shadow = Shadow(
                    radius = 36.dp,
                    spread = 8.dp,
                    color = shadowColor,
                    offset = DpOffset(x = 10.dp, 10.dp)
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
            color = if (enabled) contentColor else contentColor.copy(alpha = 0.7f)
        )
    }
}