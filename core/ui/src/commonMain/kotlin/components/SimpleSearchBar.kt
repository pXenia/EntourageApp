package com.entourageapp.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.cross
import org.jetbrains.compose.resources.painterResource

@Composable
fun SimpleSearchBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Поиск..."
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SimpleSearchField(
            searchQuery = searchQuery,
            onQueryChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = placeholder
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onCloseClick,
            modifier = Modifier.size(48.dp).clip(CircleShape)
                .background(EntourageBlack.copy(alpha = 0.05f))
        ) {
            Icon(
                painter = painterResource(cross),
                contentDescription = "Close search",
                modifier = Modifier.size(24.dp),
                tint = EntourageBlack
            )
        }
    }
}

@Composable
private fun SimpleSearchField(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    val customTextSelectionColors = TextSelectionColors(
        handleColor = EntourageTeal, backgroundColor = EntourageTeal.copy(alpha = 0.4f)
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        BasicTextField(
            value = searchQuery,
            onValueChange = { onQueryChange(it) },
            modifier = modifier.height(48.dp)
                .background(Color.Transparent, RoundedCornerShape(24.dp))
                .border(1.dp, EntourageBlack, RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp),
            singleLine = true,
            cursorBrush = SolidColor(EntourageBlack),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = EntourageBlack, fontSize = 16.sp
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart
                ) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = EntourageBlack.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}
