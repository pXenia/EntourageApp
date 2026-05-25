package com.entourageapp.core.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
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
    isNumeric: Boolean = false,
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
    barModifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    val visualTransformation = when {
        isPassword -> PasswordVisualTransformation()
        isNumeric -> ThousandsSeparatorTransformation()
        else -> VisualTransformation.None
    }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = EntourageTeal,
        backgroundColor = EntourageTeal.copy(alpha = 0.4f)
    )

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
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            BasicTextField(
                value = value,
                onValueChange = { newValue ->
                    if (isNumeric) {
                        if (newValue.all { it.isDigit() || it == '.' }) {
                            if (newValue.count { it == '.' } <= 1) {
                                onValueChange(newValue)
                            }
                        }
                    } else {
                        onValueChange(newValue)
                    }
                },
                modifier = barModifier.fillMaxWidth(),
                singleLine = isSingleLine,
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 16.sp,
                    textAlign = textAlign,
                    color = EntourageBlack
                ),
                interactionSource = interactionSource,
                enabled = isEnable,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (isNumeric) KeyboardType.Number else KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                visualTransformation = visualTransformation,
                cursorBrush = SolidColor(EntourageTeal),
                decorationBox = { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = value,
                        innerTextField = innerTextField,
                        enabled = isEnable,
                        singleLine = isSingleLine,
                        visualTransformation = visualTransformation,
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
                    modifier = Modifier.padding(start = 20.dp, top = 2.dp),
                    text = errorText,
                    color = EntourageRed,
                    fontSize = 14.sp
                )
            }
        }
    }
}

class ThousandsSeparatorTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val parts = originalText.split(".")
        val integerPart = parts[0]
        val fractionalPart = if (parts.size > 1) "." + parts[1] else ""

        val formattedInteger = integerPart
            .reversed()
            .chunked(3)
            .joinToString(" ")
            .reversed()

        val formattedText = formattedInteger + fractionalPart

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                
                if (offset <= integerPart.length) {
                    val originalToRightInInteger = integerPart.length - offset
                    val spacesToRight = originalToRightInInteger / 3
                    return formattedInteger.length - originalToRightInInteger - spacesToRight
                } else {
                    val offsetInFraction = offset - integerPart.length
                    return formattedInteger.length + offsetInFraction
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= formattedInteger.length) {
                    val spacesBefore = formattedInteger.substring(0, offset).count { it == ' ' }
                    return offset - spacesBefore
                } else {
                    val offsetInFraction = offset - formattedInteger.length
                    return integerPart.length + offsetInFraction
                }
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}