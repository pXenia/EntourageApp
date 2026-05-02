package com.entourageapp.core.ui.dialogs

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable

@Composable
@OptIn(ExperimentalMaterial3Api::class)
expect fun DeleteDialog(
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    sheetState: SheetState,
    title: String,
    text: String,
    buttonTitle: String
)