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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageWhite
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ScreenTitleTwoButtons(
    modifier: Modifier = Modifier,
    title: String,
    leftIcon: DrawableResource,
    rightIcon: DrawableResource,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            FloatingButton(
                onClick = onLeftButtonClick,
                icon = leftIcon
            )

            Text(
                text = title.uppercase(),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            FloatingButton(
                onClick = onRightButtonClick,
                icon = rightIcon
            )
        }
    }
}

@Composable
private fun FloatingButton(
    onClick: () -> Unit,
    icon: DrawableResource
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.size(56.dp),
        shape = RoundedCornerShape(28.dp),
        containerColor = EntourageWhite.copy(alpha = 0.6f),
        elevation = elevation(
            defaultElevation = 0.dp,
        )
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
    }
}