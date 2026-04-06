package com.entourageapp.core.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageRed
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite

@Composable
fun CustomDateField(
    label: String = "",
    value: String = "",
    onValueChange: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isError = value.length == 8 && !isValidDate(value)

    val colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = EntourageWhite.copy(alpha = 0.6f),
        unfocusedContainerColor = EntourageWhite.copy(alpha = 0.6f),
        focusedBorderColor = EntourageTeal,
        unfocusedBorderColor = Color.Transparent,
        focusedTextColor = EntourageBlack,
        unfocusedTextColor = EntourageBlack,
        errorBorderColor = EntourageRed,
        errorContainerColor = EntourageWhite.copy(alpha = 0.6f),
        cursorColor = EntourageTeal,
        errorCursorColor = EntourageTeal
    )

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                color = EntourageTeal,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp),
                modifier = Modifier.padding(start = 20.dp, bottom = 2.dp)
            )
        }

        BasicTextField(
            value = value,
            onValueChange = { input ->
                val digitsOnly = input.filter { it.isDigit() }
                if (digitsOnly.length <= 8) {
                    onValueChange(digitsOnly)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = EntourageBlack
            ),
            interactionSource = interactionSource,
            visualTransformation = DateTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = SolidColor(EntourageTeal),
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = DateTransformation(),
                    interactionSource = interactionSource,
                    isError = isError,
                    placeholder = {
                        Text(
                            text = "ДД.ММ.ГГГГ",
                            modifier = Modifier.fillMaxWidth(),
                            color = EntourageBlack.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    },
                    colors = colors,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    container = {
                        OutlinedTextFieldDefaults.Container(
                            enabled = true,
                            isError = isError,
                            interactionSource = interactionSource,
                            colors = colors,
                            shape = RoundedCornerShape(50)
                        )
                    }
                )
            }
        )

        if (isError) {
            Text(
                modifier = Modifier.padding(start = 20.dp, top = 2.dp),
                text = getDateErrorText(value),
                color = EntourageRed,
                fontSize = 12.sp
            )
        }
    }
}

// Проверка корректности даты
fun isValidDate(digits: String): Boolean {
    if (digits.length != 8) return false
    val day = digits.substring(0, 2).toIntOrNull() ?: return false
    val month = digits.substring(2, 4).toIntOrNull() ?: return false
    val year = digits.substring(4, 8).toIntOrNull() ?: return false

    if (month < 1 || month > 12) return false
    if (day < 1) return false
    if (year < 1900 || year > 2100) return false

    val isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

    val maxDay = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear) 29 else 28
        else -> return false
    }

    return day <= maxDay
}

// Текст ошибки
fun getDateErrorText(digits: String): String {
    if (digits.length != 8) return "Введите полную дату"
    val day = digits.substring(0, 2).toIntOrNull() ?: return "Некорректный день"
    val month = digits.substring(2, 4).toIntOrNull() ?: return "Некорректный месяц"
    val year = digits.substring(4, 8).toIntOrNull() ?: return "Некорректный год"

    if (month < 1 || month > 12) return "Месяц от 01 до 12"
    if (day < 1) return "День не может быть 0"
    if (year < 1900 || year > 2100) return "Год от 1900 до 2100"

    val isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

    val maxDay = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear) 29 else 28
        else -> return "Некорректный месяц"
    }

    if (day > maxDay) return if (month == 2) {
        if (isLeapYear) "В феврале $year максимум 29 дней"
        else "В феврале $year максимум 28 дней"
    } else {
        "В этом месяце максимум $maxDay дней"
    }

    return "Некорректная дата"
}

class DateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 1 || i == 3) out += "."
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 3) return offset + 1
                if (offset <= 8) return offset + 2
                return 10
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                if (offset <= 10) return offset - 2
                return 8
            }
        }

        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}