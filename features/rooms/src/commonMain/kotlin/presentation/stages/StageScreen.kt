package com.entourageapp.features.rooms.presentation.stages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.EntouragePeach
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.components.AddRoundButton
import com.entourageapp.core.ui.components.ScreenTitle
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StageScreen(
    onBackClick: () -> Unit = {},
    onAddTaskClick: () -> Unit = {},
    viewModel: StageVM = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showSheet by remember { mutableStateOf(false) }
    var selectedStage by remember { mutableStateOf<Stage?>(null) }
    val sheetState = rememberModalBottomSheetState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 16.dp),
    ) {
        Column {
            ScreenTitle(
                title = "Список этапов и задач",
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(4.dp))

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .verticalScroll(rememberScrollState())
                    .padding(top = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                state.stages.forEach { stage ->
                    StageSection(
                        stage = stage,
                        onTaskToggle = { taskId ->
                            viewModel.handleIntent(StageIntent.ToggleTask(stage.id, taskId))
                        },
                        onStatusClick = {
                            selectedStage = stage
                            showSheet = true
                        }
                    )
                }
            }
        }

        AddRoundButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp),
            onClick = onAddTaskClick
        )

        if (showSheet) {
            UpdateStatusStageBottomSheet(
                onDismiss = { showSheet = false },
                onSelected = { id, status ->
                    viewModel.handleIntent(
                        StageIntent.UpdateStageStatus(id, status)
                    )
                },
                sheetState = sheetState,
                selectedStage = selectedStage
            )

        }
    }
}

@Composable
private fun StageSection(
    stage: Stage,
    onTaskToggle: (Int) -> Unit,
    onStatusClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatusBadge(
                status = stage.status,
                onStatusClick = onStatusClick,
            )
            Text(
                text = buildAnnotatedString {
                    append(stage.title + " ")

                    withStyle(
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = EntourageBlack
                        ).toSpanStyle()
                    ) {
                        append("(${stage.deadline})")
                    }
                },
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 18.sp,
                    color = EntourageTeal,
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            stage.tasks.forEach { task ->
                TaskRow(
                    task = task,
                    onClick = { onTaskToggle(task.id) }
                )
            }
        }
    }
}

@Composable
fun StatusBadge(
    status: StageStatus,
    onStatusClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (status) {
        StageStatus.COMPLETED -> EntourageBlack.copy(alpha = 0.25f)
        StageStatus.IN_PROGRESS -> EntourageTeal.copy(alpha = 0.25f)
        StageStatus.NOT_STARTED -> EntouragePeach.copy(alpha = 0.5f)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .clickable{ onStatusClick() }
            .background(backgroundColor)
            .innerShadow(
                shape = RoundedCornerShape(32.dp),
                shadow = Shadow(
                    radius = 20.dp,
                    spread = 4.dp,
                    color = EntourageWhite.copy(alpha = 0.3f),
                    offset = DpOffset(x = 4.dp, 4.dp)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp),
            text = status.label,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            color = EntourageBlack
        )
    }
}

@Composable
private fun TaskRow(
    task: Task,
    onClick: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { onClick(!task.isCompleted) },
            modifier = Modifier.size(36.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = EntourageTeal,
                uncheckedColor = EntourageTeal
            )
        )

        Text(
            text = buildAnnotatedString {
                append(task.title + " ")

                withStyle(
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        color = EntourageTeal
                    ).toSpanStyle()
                ) {
                    append("(${task.deadline})")
                }
            },
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 16.sp,
                color = EntourageBlack
            )
        )
    }
}
