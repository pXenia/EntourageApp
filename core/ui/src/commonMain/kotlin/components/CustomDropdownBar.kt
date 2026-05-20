package com.entourageapp.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageLightBlueGray
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
    modifier: Modifier = Modifier,
    barModifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

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
        errorCursorColor = EntourageTeal,
        selectionColors = TextSelectionColors(
            handleColor = EntourageTeal,
            backgroundColor = EntourageTeal.copy(alpha = 0.2f)
        )
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

        Box(
            modifier = barModifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .clickable { showDialog = true }
        ) {
            BasicTextField(
                value = selectedItem?.let { itemLabel(it) } ?: "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 16.sp,
                    color = EntourageBlack
                ),
                interactionSource = interactionSource,
                cursorBrush = SolidColor(EntourageTeal),
                decorationBox = { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = selectedItem?.let { itemLabel(it) } ?: "",
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        isError = errorText != null,
                        placeholder = {
                            Text(
                                text = placeholder,
                                modifier = Modifier.fillMaxWidth(),
                                color = EntourageBlack.copy(alpha = 0.7f),
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp)
                            )
                        },
                        colors = colors,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        container = {
                            OutlinedTextFieldDefaults.Container(
                                enabled = true,
                                isError = errorText != null,
                                interactionSource = interactionSource,
                                colors = colors,
                                shape = RoundedCornerShape(32.dp)
                            )
                        }
                    )
                }
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {},
                shape = RoundedCornerShape(28.dp),
                containerColor = EntourageLightBlueGray,
                title = {
                    Text(
                        text = label.ifEmpty { placeholder },
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 22.sp),
                        textAlign = TextAlign.Center,
                        color = EntourageBlack
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items.forEach { item ->
                            Surface(
                                onClick = {
                                    onItemSelected(item)
                                    showDialog = false
                                },
                                shape = RoundedCornerShape(32.dp),
                                color = EntourageTeal.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = itemLabel(item),
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                    color = EntourageBlack
                                )
                            }
                        }
                    }
                }
            )
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
