package com.entourageapp.features.gallery.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.entourageapp.core.navigation.Role
import org.koin.compose.viewmodel.koinViewModel

@Composable
expect fun GalleryScreen(
    projectId: Int,
    roomId: Int,
    roleId: Role,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: GalleryVM = koinViewModel()
)
