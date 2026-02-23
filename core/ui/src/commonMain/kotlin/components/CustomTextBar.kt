package com.entourageapp.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeach
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
    trailingIcon:  DrawableResource? = null,
    onTrailingIconClick: () -> Unit = {},
    //textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = EntourageTeal,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp),
            modifier = Modifier.padding(start = 22.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    modifier = Modifier.fillMaxWidth(),
                    color = EntourageBlack.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 40.dp),
            shape = RoundedCornerShape(32.dp),
            singleLine = isSingleLine,
            textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            trailingIcon = {
                if (trailingIcon != null) {
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
            },
            colors = OutlinedTextFieldDefaults.colors(
                // Фон
                focusedContainerColor = EntourageWhite.copy(alpha = 0.6f),
                unfocusedContainerColor = EntourageWhite.copy(alpha = 0.6f),

                // Границы
                focusedBorderColor = EntourageTeal,
                unfocusedBorderColor = Color.Transparent,

                // Текст
                focusedTextColor = EntourageBlack,
                unfocusedTextColor = EntourageBlack,
            )
        )
    }
}