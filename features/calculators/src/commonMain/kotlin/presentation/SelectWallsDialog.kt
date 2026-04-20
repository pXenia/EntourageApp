package com.entourageapp.features.calculators.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.network.dto.WallDto
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.EntourageTeal

@Composable
fun SelectWallsDialog(
    walls: List<WallDto>,
    selectedIds: Set<Int>,
    onToggle: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Готово", color = EntourageTeal)
            }
        },
        shape = RoundedCornerShape(28.dp),
        containerColor = EntourageLightBlueGray,
        title = {
            Text(
                text = "Выберите стены",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 22.sp),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                walls.forEachIndexed { index, wall ->
                    val isSelected = wall.id in selectedIds
                    Surface(
                        onClick = { onToggle(wall.id) },
                        shape = RoundedCornerShape(32.dp),
                        color = if (isSelected) EntourageTeal.copy(alpha = 0.5f)
                        else EntourageTeal.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Стена ${index + 1}, ${((wall.length * 100).toInt())} см",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = EntourageTeal
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}