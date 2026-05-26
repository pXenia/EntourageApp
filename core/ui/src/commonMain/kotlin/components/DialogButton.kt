package com.entourageapp.core.ui.components

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageTeal

@Composable
fun DialogButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = EntourageTeal,
    enabled: Boolean = true,
    containerColor: Color = Color.Transparent
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = color,
            disabledContentColor = Color.Gray,
            containerColor = containerColor
        )
    ) {
        Text(
            text = text,
            color = if (enabled) color else Color.Gray,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
        )
    }
}
