package com.entourageapp.features.gallery.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import com.entourageapp.core.navigation.Role
import com.entourageapp.features.gallery.domain.GalleryImage
import com.entourageapp.features.gallery.domain.GalleryRoom

@Composable
expect fun GalleryViewPager(
    images: List<GalleryImage>,
    pagerState: PagerState,
    availableRooms: List<GalleryRoom>,
    onIntent: (GalleryIntent) -> Unit,
    projectId: Int,
    roleId: Role,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
)
