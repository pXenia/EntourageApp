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
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageWhite

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
            .clickable{ onCardClick() },
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = EntourageBlack.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
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
                    label = "участники",
                    backgroundColor = Color.Transparent
                )
                InfoBox(
                    value = rooms.toString(),
                    label = "комнат",
                    backgroundColor = EntouragePeach.copy(alpha = 0.8f)
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
    backgroundColor: Color
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, EntourageBlack)
    ) {
        Column(
            modifier = modifier
                .defaultMinSize(minWidth = 75.dp, minHeight = 75.dp)
                .padding(vertical = 4.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(-6.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 40.sp)
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
            .background(Color.Transparent, RoundedCornerShape(8.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
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
            style = MaterialTheme.typography.titleLarge,
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
private fun SquareBadge (
    area: String
){
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = EntourageWhite.copy(alpha = 0.3f),
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(-10.dp)
        ) {
            Text(
                text = area,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "кв.м",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}