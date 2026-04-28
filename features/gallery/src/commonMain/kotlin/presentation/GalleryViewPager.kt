package com.entourageapp.features.gallery.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import com.entourageapp.core.network.dto.ImageDto
import com.entourageapp.core.network.dto.RoomShortDto

@Composable
expect fun GalleryViewPager(
    images: List<ImageDto>,
    pagerState: PagerState,
    availableRooms: List<RoomShortDto>,
    onIntent: (GalleryIntent) -> Unit,
    projectId: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
)