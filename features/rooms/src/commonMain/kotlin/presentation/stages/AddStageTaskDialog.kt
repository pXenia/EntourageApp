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
import androidx.compose.material3.TextButton
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
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.components.CustomDateField
import com.entourageapp.core.ui.components.CustomDropdownBar
import com.entourageapp.core.ui.components.CustomTextBar
import com.entourageapp.core.ui.components.TabButton

@Composable
fun AddStageTaskDialog(
    stages: List<Stage>,
    onDismiss: () -> Unit,
    onConfirmStage: (title: String, deadline: String) -> Unit,
    onConfirmTask: (stageId: Int, title: String, deadline: String) -> Unit,
) {
    var isStageMode by remember { mutableStateOf(true) }
    
    var title by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var selectedStage by remember { mutableStateOf<Stage?>(null) }

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
                text = "Добавление",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 22.sp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TabButton(
                    modifier = Modifier.weight(1f),
                    title = "Этап",
                    isSelected = isStageMode,
                    onClick = { isStageMode = true }
                )
                TabButton(
                    modifier = Modifier.weight(1f),
                    title = "Задача",
                    isSelected = !isStageMode,
                    onClick = { isStageMode = false }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextBar(
                label = if (isStageMode) "Название этапа" else "Название задачи",
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

            if (!isStageMode) {
                Spacer(modifier = Modifier.height(12.dp))
                CustomDropdownBar(
                    label = "Этап",
                    items = stages,
                    selectedItem = selectedStage,
                    onItemSelected = { selectedStage = it },
                    itemLabel = { it.title },
                    placeholder = "Текст"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDismiss) {
                    Text(
                        "Отменить",
                        color = EntourageTeal,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                val isEnabled = title.isNotBlank() && deadline.length == 8 && (isStageMode || selectedStage != null)

                TextButton(
                    onClick = {
                        if (isStageMode) {
                            onConfirmStage(title, deadline)
                        } else {
                            selectedStage?.let { onConfirmTask(it.id, title, deadline) }
                        }
                    },
                    enabled = isEnabled
                ) {
                    Text(
                        "Сохранить",
                        color = if (isEnabled) EntourageTeal else EntourageTeal.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
                    )
                }
            }
        }
    }
}
