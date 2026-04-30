package com.entourageapp.features.projectdocuments.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.components.CustomTextBar

@Composable
fun AddDocDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { onConfirm(title, url) },
                enabled = title.isNotBlank() && url.isNotBlank()
            ) {
                Text(
                    text = "Добавить",
                    color = if (title.isNotBlank() && url.isNotBlank()) EntourageTeal else EntourageBlack.copy(alpha = 0.5f)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Отмена", color = EntourageBlack)
            }
        },
        shape = RoundedCornerShape(28.dp),
        containerColor = EntourageLightBlueGray,
        title = {
            Text(
                text = "Добавление документа",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 22.sp),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomTextBar(
                    value = title,
                    onValueChange = { title = it },
                    label = "Название",
                    placeholder = "Например, план квартиры"
                )
                CustomTextBar(
                    value = url,
                    onValueChange = { url = it },
                    label = "Ссылка",
                    placeholder = "https://docs.google.com/..."
                )
            }
        }
    )
}