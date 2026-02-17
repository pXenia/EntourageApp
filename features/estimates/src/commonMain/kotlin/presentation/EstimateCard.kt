package presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.components.Badge
import com.entourageapp.core.ui.more
import com.entourageapp.core.ui.tag
import org.jetbrains.compose.resources.painterResource

@Composable
fun EstimateCard(
    modifier: Modifier = Modifier,
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) -180f else 0f, label = "Rotation"
    )

    Surface(
        modifier = modifier.animateContentSize(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            )
        ),
        color = EntourageBlack.copy(alpha = 0.1f),
        shape = RoundedCornerShape(32.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Headline(
                text = "материал",
                onClick = { expandedState = !expandedState },
                rotationState = rotationState
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = EntourageBlack
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Обои флизелиновые Palitra Home Taina Lesa зеленые",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                )
                Text(
                    text = "12 500₽",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    color = EntourageTeal
                )
            }

            if (expandedState) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoCard(
                        text = "Ед. измерения",
                        value = "рулон",
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        text = "Кол-во",
                        value = "20",
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        text = "Цена ед.",
                        value = "1800₽",
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Комната:",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                    )
                    Badge(tag, "Спальня")
                }

            }
        }
    }
}

@Composable
private fun Headline(
    text: String,
    number: Int = 1,
    onClick: () -> Unit = {},
    rotationState: Float = 0f
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(24.dp),
            shape = CircleShape,
            contentColor = EntourageWhite,
            color = EntourageBlack
        ) {
            Text(
                text = number.toString(),
                modifier = Modifier.wrapContentSize(Alignment.Center),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
            )
        }

        Text(
            text = text.uppercase(),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
        )

        IconButton(
            onClick = onClick,
        ) {
            Icon(
                painter = painterResource(more),
                contentDescription = null,
                modifier = Modifier.size(24.dp).rotate(rotationState)
            )
        }

    }
}

@Composable
private fun InfoCard(
    text: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = EntouragePeach.copy(alpha = 0.3f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, EntourageBlack)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .wrapContentSize(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = EntourageBlack.copy(alpha = 0.8f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
            )
        }
    }
}