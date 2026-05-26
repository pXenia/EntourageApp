package com.entourageapp.features.rooms.presentation.stages

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import com.entourageapp.core.navigation.Role

@OptIn(ExperimentalMaterial3Api::class)
@Composable
expect fun UpdateStatusStageBottomSheet(
    onDismiss: () -> Unit,
    onSelected: (Int, StageStatus) -> Unit,
    sheetState: SheetState,
    selectedStage: Stage?,
    roleId: Role
)