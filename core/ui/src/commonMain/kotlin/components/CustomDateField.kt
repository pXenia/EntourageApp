package com.entourageapp.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

@Composable
fun CustomDateField(
    label: String = "",
    value: Long? = 1771228634,
    onDateSelected: (Long?) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(50)
    val currentBorderColor = if (showDatePicker) EntourageTeal else Color.Transparent

    Column(modifier = modifier) {
        Text(
            text = label,
            color = EntourageTeal,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp),
            modifier = Modifier.padding(start = 22.dp)
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = value.toFormattedDate(),
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 40.dp),
                readOnly = true,
                shape = shape,
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = EntourageWhite.copy(alpha = 0.6f),
                    unfocusedContainerColor = EntourageWhite.copy(alpha = 0.6f),
                    focusedBorderColor = EntourageTeal,
                    unfocusedBorderColor = currentBorderColor,
                    focusedTextColor = EntourageBlack,
                    unfocusedTextColor = EntourageBlack,
                    disabledBorderColor = currentBorderColor
                )
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(shape)
                    .clickable { showDatePicker = true }
            )
        }
    }

    if (showDatePicker) {
        CustomDatePickerModal(
            initialDate = value,
            onDateSelected = {
                onDateSelected(it)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

fun Long?.toFormattedDate(): String {
    if (this == null) return ""
    val instant = Instant.fromEpochMilliseconds(this)
    val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    return "${date.dayOfMonth.toString().padStart(2, '0')}.${
        date.month.number.toString().padStart(2, '0')
    }.${date.year}"
}