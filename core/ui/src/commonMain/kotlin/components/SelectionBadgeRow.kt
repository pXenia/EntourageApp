package com.entourageapp.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.add
import com.entourageapp.core.ui.cross
import com.entourageapp.core.ui.tag
import org.jetbrains.compose.resources.painterResource

@Composable
fun SelectionBadgeRow(
    selectedName: String?,
    onClear: () -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
    emptyText: String = "Помещение не выбрано"
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (selectedName != null) {
            Badge(tag, selectedName)
        } else {
            Text(
                text = emptyText,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                color = EntourageBlack.copy(alpha = 0.6f)
            )
        }
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    if (selectedName != null) {
                        onClear()
                    } else {
                        onAdd()
                    }
                }
                .background(EntouragePeach.copy(alpha = 0.6f))
                .innerShadow(
                    shape = CircleShape,
                    shadow = Shadow(
                        radius = 10.dp,
                        spread = 8.dp,
                        color = EntourageWhite.copy(alpha = 0.1f),
                        offset = DpOffset(x = 0.dp, 0.dp)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = if (selectedName != null) painterResource(cross) else painterResource(add),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .size(if (selectedName != null) 18.dp else 12.dp),
                tint = EntourageBlack,
            )
        }
    }
}
