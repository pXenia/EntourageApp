package com.entourageapp.features.rooms.presentation.stages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageLightBlueGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun UpdateStatusStageBottomSheet(
    onDismiss: () -> Unit,
    onSelected: (Int, StageStatus) -> Unit,
    sheetState: SheetState,
    selectedStage: Stage?
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        tonalElevation = 2.dp,
        containerColor = EntourageLightBlueGray,
        contentWindowInsets = {
            BottomSheetDefaults.windowInsets
        },
        dragHandle = {},
        properties = ModalBottomSheetProperties(
            isAppearanceLightStatusBars = false,
            isAppearanceLightNavigationBars = false
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Выберите статус",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                color = EntourageBlack,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StageStatus.entries.forEach { status ->
                    StatusBadge(
                        status = status,
                        onStatusClick = {
                            onSelected(selectedStage?.id ?: 0, status)
                        }
                    )
                }
            }
        }
    }
}