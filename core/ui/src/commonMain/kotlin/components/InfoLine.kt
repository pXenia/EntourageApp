package com.entourageapp.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack

@Composable
fun InfoLine(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            color = EntourageBlack,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = EntourageBlack,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp),
        )
    }
}