package com.entourageapp.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageRed
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> CustomDropdownBar(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String,
    label: String = "",
    placeholder: String = "",
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = label,
            color = EntourageTeal,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp),
            modifier = Modifier.padding(start = 20.dp, bottom = 2.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedItem?.let { itemLabel(it) } ?: "",
                onValueChange = {},
                readOnly = true,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = EntourageBlack.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp)
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 40.dp)
                    .menuAnchor(),
                shape = RoundedCornerShape(32.dp),
                textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
                isError = errorText != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = EntourageWhite.copy(alpha = 0.6f),
                    unfocusedContainerColor = EntourageWhite.copy(alpha = 0.6f),
                    focusedBorderColor = EntourageTeal,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = EntourageBlack,
                    unfocusedTextColor = EntourageBlack,
                    errorBorderColor = EntourageRed,
                    errorContainerColor = EntourageWhite.copy(alpha = 0.6f),
                    cursorColor = EntourageTeal,
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(EntourageWhite)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = itemLabel(item),
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
                                color = EntourageBlack
                            )
                        },
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }

        if (errorText != null) {
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = errorText,
                color = EntourageRed,
                fontSize = 16.sp
            )
        }
    }
}