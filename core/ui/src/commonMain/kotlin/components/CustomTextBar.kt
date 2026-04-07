package com.entourageapp.core.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageRed
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomTextBar(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    label: String = "",
    placeholder: String = "",
    isSingleLine: Boolean = true,
    isEnable: Boolean = true,
    trailingIcon: DrawableResource? = null,
    onTrailingIconClick: () -> Unit = {},
    errorText: String? = null,
    isPassword: Boolean = false,
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
    barModifier: Modifier = Modifier
) {
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

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = barModifier.fillMaxWidth(),
            singleLine = isSingleLine,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                fontSize = 16.sp,
                textAlign = textAlign,
                color = EntourageBlack
            ),
            interactionSource = interactionSource,
            enabled = isEnable,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            cursorBrush = SolidColor(EntourageTeal),
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = isEnable,
                    singleLine = isSingleLine,
                    visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                    interactionSource = interactionSource,
                    isError = errorText != null,
                    placeholder = {
                        Text(
                            text = placeholder,
                            modifier = Modifier.fillMaxWidth(),
                            color = EntourageBlack.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 16.sp,
                                textAlign = textAlign
                            )
                        )
                    },
                    trailingIcon = if (trailingIcon != null) {
                        {
                            IconButton(
                                onClick = onTrailingIconClick,
                                modifier = Modifier.padding(end = 4.dp),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = EntouragePeach.copy(alpha = 0.4f)
                                )
                            ) {
                                Icon(
                                    painter = painterResource(trailingIcon),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    } else null,
                    colors = colors,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    container = {
                        OutlinedTextFieldDefaults.Container(
                            enabled = isEnable,
                            isError = errorText != null,
                            interactionSource = interactionSource,
                            colors = colors,
                            shape = RoundedCornerShape(32.dp)
                        )
                    }
                )
            }
        )

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