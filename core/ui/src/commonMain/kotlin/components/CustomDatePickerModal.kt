package com.entourageapp.core.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerModal(
    initialDate: Long?,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
    )
    DatePickerDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
        TextButton(onClick = { onDateSelected(datePickerState.selectedDateMillis) }) {
            Text("OK", color = EntourageTeal, style = MaterialTheme.typography.labelLarge)
        }
    },
    dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Отмена", color = EntourageTeal, style = MaterialTheme.typography.labelLarge)
        }
    },
    colors = DatePickerDefaults.colors(
    containerColor = EntourageLightBlueGray
    )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                // Фон всего календаря внутри диалога
                containerColor = EntourageLightBlueGray,

                // Шапка (Месяц, Год)
                titleContentColor = EntourageBlack,
                headlineContentColor = EntourageBlack,

                // Дни недели и числа
                weekdayContentColor = EntourageTeal.copy(alpha = 0.8f),
                dayContentColor = EntourageBlack,

                // Выбранный день
                selectedDayContainerColor = EntourageTeal,
                selectedDayContentColor = EntourageWhite,

                // Сегодняшний день
                todayContentColor = EntourageTeal,
                todayDateBorderColor = EntourageTeal,

                // Переключения месяца
                navigationContentColor = EntourageBlack
            )
        )
    }
}