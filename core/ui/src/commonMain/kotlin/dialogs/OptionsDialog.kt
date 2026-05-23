package com.entourageapp.core.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageRed

@Composable
fun OptionsDialog(
    title: String,
    onDismiss: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        shape = RoundedCornerShape(28.dp),
        containerColor = EntourageLightBlueGray,
        title = {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(
                    onClick = {
                        onEditClick()
                        onDismiss()
                    },
                    shape = RoundedCornerShape(16.dp),
                    color = EntouragePeach.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "Редактировать",
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                        textAlign = TextAlign.Center
                    )
                }

                Surface(
                    onClick = {
                        onDeleteClick()
                        onDismiss()
                    },
                    shape = RoundedCornerShape(16.dp),
                    color = EntourageRed.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "Удалить",
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp, color = EntourageRed),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}
