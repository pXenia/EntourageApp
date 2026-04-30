package com.entourageapp.core.ui.components

import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.add
import org.jetbrains.compose.resources.painterResource

@Composable
fun AddRoundButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable(
                onClick = onClick
            )
            .background(EntourageTeal.copy(0.9f))
            .innerShadow(
                shape = CircleShape,
                shadow = Shadow(
                    radius = 20.dp,
                    spread = 4.dp,
                    color = EntourageWhite.copy(alpha = 0.3f),
                    offset = DpOffset(x = 4.dp, 4.dp)
                )
            )
            .size(64.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(add),
            modifier = Modifier.size(12.dp),
            contentDescription = null,
            tint = EntourageWhite
        )
    }
}