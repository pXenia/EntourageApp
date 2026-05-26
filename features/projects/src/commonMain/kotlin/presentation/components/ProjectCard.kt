package com.entourageapp.features.projects.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.tools.getPlural

@Composable
fun ProjectCard(
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
    title: String = "КВАРТИРА НА ЛЕНИНСКОМ",
    area: String = "80",
    years: String = "2024-2026",
    participants: Int = 2,
    rooms: Int = 5
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .innerShadow(
                shape = RoundedCornerShape(32.dp),
                shadow = Shadow(
                    radius = 36.dp,
                    spread = 8.dp,
                    color = EntourageWhite.copy(alpha = 0.2f),
                    offset = DpOffset(x = 10.dp, 10.dp)
                )
            )
            .clickable{ onCardClick() },
        colors = CardDefaults.cardColors(containerColor = EntourageBlack.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                CardTitle(modifier = Modifier.weight(1f), title = title)
                SquareBadge(area)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DateBox(modifier = Modifier.weight(1f), value = years)

                Spacer(modifier = Modifier.width(16.dp))

                InfoBox(
                    value = participants.toString(),
                    label = getPlural(participants, "участник", "участника", "участников"),
                    backgroundColor = Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                )
                InfoBox(
                    value = rooms.toString(),
                    label = getPlural(rooms, "комната", "комнаты", "комнат"),
                    backgroundColor = EntouragePeach.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
    }
}

@Composable
private fun InfoBox(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    backgroundColor: Color,
    shape: Shape,
) {
    Surface(
        shape = shape,
        color = backgroundColor,
        border = BorderStroke(1.dp, EntourageBlack)
    ) {
        Column(
            modifier = modifier
                .defaultMinSize(minWidth = 65.dp, minHeight = 65.dp)
                .padding(vertical = 4.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(-6.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun DateBox(
    modifier: Modifier = Modifier,
    value: String
) {
    Box(
        modifier = modifier
            .background(Color.Transparent, RoundedCornerShape(16.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
            .padding(horizontal = 24.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            color = EntourageBlack,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            autoSize = TextAutoSize.StepBased(
                maxFontSize = 16.sp
            )
        )
    }
}

@Composable
private fun CardTitle(
    modifier: Modifier = Modifier,
    title: String
){
    Column(
        modifier = modifier,
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = EntourageBlack
        )
    }
}

@Composable
private fun SquareBadge(
    area: String
){
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = EntourageWhite.copy(alpha = 0.3f),
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(-6.dp)
        ) {
            Text(
                text = area,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "кв. м",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}