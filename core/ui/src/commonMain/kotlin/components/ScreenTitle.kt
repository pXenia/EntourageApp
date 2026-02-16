package com.entourageapp.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults.elevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.arrowLeft
import org.jetbrains.compose.resources.painterResource

@Composable
fun ScreenTitle(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            FloatingActionButton(
                onClick = onBackClick,
                modifier = Modifier.size(42.dp),
                shape = RoundedCornerShape(24.dp),
                containerColor = EntourageWhite.copy(alpha = 0.6f),
                elevation = elevation(
                    defaultElevation = 0.dp,
                )
            ) {
                Icon(
                    painter = painterResource(arrowLeft),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.titleMedium
            )
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = EntourageBlack
        )
    }
}