package com.entourageapp.features.rooms.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.add
import org.jetbrains.compose.resources.painterResource

@Composable
fun AddRoomCard(
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier.clickable { onCardClick() },
        color = EntourageBlack.copy(alpha = 0.1f),
        shape = RoundedCornerShape(32.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(8.dp).height(80.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(add),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = EntourageTeal
            )
        }
    }
}