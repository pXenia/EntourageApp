package com.entourageapp.core.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.EntourageRed
import com.entourageapp.core.ui.components.DialogButton

@Composable
@OptIn(markerClass = [ExperimentalMaterial3Api::class])
actual fun DeleteDialog(
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    sheetState: SheetState,
    title: String,
    text: String,
    buttonTitle: String
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = EntourageLightBlueGray,
        dragHandle = null,
        properties = ModalBottomSheetProperties(
            isAppearanceLightStatusBars = false,
            isAppearanceLightNavigationBars = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 22.sp),
                textAlign = TextAlign.Center
            )

            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DialogButton(
                    modifier = Modifier.weight(1f),
                    text = "Отменить",
                    onClick = onDismiss
                )

                DialogButton(
                    modifier = Modifier.weight(1f),
                    text = buttonTitle,
                    onClick = onOkClick,
                    color = EntourageRed
                )
            }
        }
    }
}