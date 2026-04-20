package com.entourageapp.features.rooms.presentation.roomdetil

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeachAlpha80
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.components.ScreenTitleTwoButtons
import com.entourageapp.core.ui.info

@Composable
fun RoomDetailScreen(
    projectId: Int,
    roomId: Int,
    onBackClick: () -> Unit = {},
    onEstimateClick: (Int, Int) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ScreenTitleTwoButtons(
            modifier = Modifier.padding(bottom = 16.dp),
            title = "",
            leftIcon = arrowLeft,
            rightIcon = info,
            onLeftButtonClick = onBackClick,
            onRightButtonClick = {}
        )
        RoomInfo(20)
        HorizontalDivider(thickness = 1.dp, color = EntourageBlack)
        CardButton(
            onClick = { onEstimateClick(projectId, roomId) },
            title = "Смета",
            text = "N позиций"
        )
    }
}

@Composable
private fun RoomInfo(
    percent: Int,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = EntourageBlack.copy(alpha = 0.1f),
        shape = RoundedCornerShape(32.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RoomPercent(32)
                Label(EntourageTeal, "материалы")
                Label(EntouragePeachAlpha80, "комплектующие")
                Label(EntourageBlack, "работы")
            }
            ChartColumn(color = EntourageTeal, percent = 0.55f)
            ChartColumn(color = EntouragePeachAlpha80, percent = 0.15f)
            ChartColumn(color = EntourageBlack, percent = 0.3f)
        }
    }
}

@Composable
fun RoomPercent(
    percent: Int
) {
    Column(
        modifier = Modifier
            .border(1.dp, EntourageBlack, RoundedCornerShape(8.dp))
            .padding(vertical = 4.dp, horizontal = 12.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 56.sp)
                        .toSpanStyle()
                ) {
                    append("$percent")
                }
                withStyle(
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp)
                        .toSpanStyle()
                ) {
                    append("%")
                }
            }
        )

        Text(
            text = "всех затрат",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
        )
    }
}

@Composable
private fun Label(
    color: Color,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp)
        )
    }
}

@Composable
private fun ChartColumn(
    percent: Float,
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(56.dp))
            .background(color.copy(alpha = 0.3f))
            .fillMaxHeight()
            .width(35.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(56.dp))
                .background(color)
                .fillMaxHeight(percent)
                .width(35.dp)
        )
    }
}

@Composable
private fun CardButton(
    onClick: () -> Unit,
    title: String,
    text: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = EntourageBlack.copy(alpha = 0.1f),
        shape = RoundedCornerShape(32.dp),
    ) {
        Column(
            modifier = Modifier.clickable { onClick() }.padding(18.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                color = EntourageTeal
            )
        }
    }
}
