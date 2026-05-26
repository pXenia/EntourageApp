package com.entourageapp.features.rooms.presentation.stages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.components.CustomDateField
import com.entourageapp.core.ui.components.CustomTextBar
import com.entourageapp.core.ui.components.DialogButton

@Composable
fun EditStageDialog(
    stage: Stage,
    onDismiss: () -> Unit,
    onConfirm: (title: String, deadline: String) -> Unit,
) {
    var title by remember { mutableStateOf(stage.title) }
    var deadline by remember { mutableStateOf(stage.deadline.replace(".", "")) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(EntourageLightBlueGray)
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Редактирование этапа",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 22.sp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            CustomTextBar(
                label = "Название этапа",
                value = title,
                onValueChange = { title = it },
                placeholder = "Текст"
            )

            Spacer(modifier = Modifier.height(12.dp))

            CustomDateField(
                label = "Дата завершения",
                value = deadline,
                onValueChange = { deadline = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DialogButton(
                    text = "Отменить",
                    onClick = onDismiss
                )

                Spacer(modifier = Modifier.width(16.dp))

                val isEnabled = title.isNotBlank() && deadline.length == 8

                DialogButton(
                    text = "Сохранить",
                    onClick = {
                        onConfirm(title, deadline)
                    },
                    enabled = isEnabled
                )
            }
        }
    }
}
