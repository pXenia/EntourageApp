package com.entourageapp.features.gallery.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import com.entourageapp.core.navigation.Role
import com.entourageapp.core.network.dto.RoomShortDto
import com.entourageapp.core.network.dto.gallery.ImageDto

@Composable
expect fun GalleryViewPager(
    images: List<ImageDto>,
    pagerState: PagerState,
    availableRooms: List<RoomShortDto>,
    onIntent: (GalleryIntent) -> Unit,
    projectId: Int,
    roleId: Role,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
)