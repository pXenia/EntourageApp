package com.entourageapp.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageTeal

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    initials: String = "КП",
    size: Int = 100
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .background(EntourageTeal.copy(alpha = 0.2f), CircleShape)
            .border(1.dp, EntourageBlack, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = size.sp / 3),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp),
        )
    }
}