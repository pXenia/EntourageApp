package com.entourageapp.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.search
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    text: String = "",
    onTextChange: (String) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onIconSecondClick: () -> Unit = {},
    color: Color = Color.Transparent,
    borderColor: Color = EntourageBlack,
    iconSecond: DrawableResource
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Поиск",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
                    color = EntourageBlack
                )
            },
            textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
            shape = RoundedCornerShape(32.dp),
            singleLine = true,
            trailingIcon = {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onSearchClick,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(search),
                            contentDescription = "Search",
                            modifier = Modifier.size(20.dp),
                            tint = EntourageBlack
                        )
                    }
//                    IconButton(
//                        onClick = onIconSecondClick,
//                        modifier = Modifier.size(20.dp)
//                    ) {
//                        Icon(
//                            painter = painterResource(iconSecond),
//                            contentDescription = "Filter",
//                            modifier = Modifier.size(height = 16.dp, width = 20.dp),
//                            tint = EntourageBlack
//                        )
//                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = color,
                unfocusedContainerColor = color,
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,
                cursorColor = EntourageBlack,
                focusedTextColor = EntourageBlack,
                unfocusedTextColor = EntourageBlack
            )
        )
    }
}