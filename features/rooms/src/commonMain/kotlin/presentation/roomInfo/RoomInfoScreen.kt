package com.entourageapp.features.rooms.presentation.roomInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.navigation.Role
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeachAlpha80
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.components.AccentButton
import com.entourageapp.core.ui.components.InfoRow
import com.entourageapp.core.ui.components.ScreenTitle
import com.entourageapp.core.ui.components.SectionTitle
import com.entourageapp.features.rooms.presentation.components.RoomPlan
import com.entourageapp.features.rooms.presentation.components.drawplan.fmt
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun RoomInfoScreen(
    projectId: Int,
    roomId: Int,
    roleId: Role,
    onBackClick: () -> Unit = {},
    onEditClick: (Int, Int) -> Unit = { _, _ -> },
    viewModel: RoomInfoVM = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(projectId, roomId) {
        viewModel.handleIntent(RoomInfoIntent.LoadRoom(projectId, roomId))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = EntourageTeal
            )
        } else if (state.error != null) {
            Text(
                text = state.error ?: "Произошла ошибка",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ScreenTitle(
                    title = "Информация о комнате",
                    onBackClick = onBackClick,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
                        .weight(1f)
                        .padding(top = 4.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    InfoRow("Высота потолков", "${state.ceilingHeight?.roundToInt() ?: 0} см")
                    HorizontalDivider(thickness = 1.dp, color = EntourageBlack)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (state.points.isNotEmpty()) {
                            RoomPlan(
                                points = state.points
                            )
                        } else {
                            Text(
                                "План комнаты не обозначен",
                                color = EntourageTeal.copy(alpha = 0.5f)
                            )
                        }
                    }

                    HorizontalDivider(thickness = 1.dp, color = EntourageBlack)

                    state.walls.forEach { wall ->
                        InfoRow("Стена ${wall.index}", "${(wall.lengthM * 100).roundToInt()} см")
                    }

                    HorizontalDivider(thickness = 1.dp, color = EntourageBlack)

                    InfoRow("Периметр", "${(state.perimeter * 100).roundToInt()} см")
                    InfoRow("Площадь стен", "${state.wallArea.fmt()} кв. м")
                    InfoRow("Площадь помещения", "${state.square?.fmt() ?: "0"} кв. м")

                    HorizontalDivider(thickness = 1.dp, color = EntourageBlack)

                    if (!state.description.isNullOrBlank()) {
                        SectionTitle(modifier = Modifier.padding(top = 4.dp), title = "Описание")
                        Text(
                            text = state.description!!,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp,
                                lineHeight = 22.sp
                            ),
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }
                }

                if (roleId == Role.Owner) {
                    AccentButton(
                        modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth().height(56.dp),
                        text = "Редактировать",
                        onClick = { onEditClick(projectId, roomId) },
                        elevation = 0.dp,
                        containerColor = EntouragePeachAlpha80,
                        contentColor = EntourageBlack
                    )
                }
            }
        }
    }
}
