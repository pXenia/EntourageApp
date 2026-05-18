package com.entourageapp.features.gallery.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.entourageapp.core.navigation.Role
import com.entourageapp.features.gallery.domain.GalleryImage
import com.entourageapp.features.gallery.domain.GalleryRoom
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.appBackground
import com.entourageapp.core.ui.cross
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun GalleryViewPager(
    images: List<GalleryImage>,
    pagerState: PagerState,
    availableRooms: List<GalleryRoom>,
    onIntent: (GalleryIntent) -> Unit,
    projectId: Int,
    roleId: Role,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    var isEditing by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val currentImage = images.getOrNull(pagerState.currentPage)
    var totalDragY by remember { mutableStateOf(0f) }

    LaunchedEffect(pagerState.currentPage) {
        isEditing = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .appBackground()
            .pointerInput(isEditing) {
                if (!isEditing) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            totalDragY += dragAmount.y
                        },
                        onDragEnd = {
                            if (totalDragY > 200f) {
                                onIntent(GalleryIntent.CloseViewPager)
                            }
                            totalDragY = 0f
                        },
                        onDragCancel = {
                            totalDragY = 0f
                        }
                    )
                }
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (!isEditing) {
                    showBottomSheet = !showBottomSheet
                }
            }
    ) {
        HorizontalPager(
            state = pagerState,
            key = { images[it].id },
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
            userScrollEnabled = !isEditing
        ) { index ->
            with(sharedTransitionScope) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(images[index].url)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .sharedElement(
                            rememberSharedContentState(key = "image_${images[index].id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.FillWidth
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.size(16.dp))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(EntourageWhite.copy(alpha = 0.6f))
                    .clickable { onIntent(GalleryIntent.CloseViewPager) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(cross),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(16.dp),
                    tint = EntourageBlack,
                )
            }
        }

        if (showBottomSheet && currentImage != null) {
            UpdateImageBottomSheet(
                roleId = roleId,
                image = currentImage,
                availableRooms = availableRooms,
                projectId = projectId,
                sheetState = sheetState,
                onIntent = onIntent,
                onDismissRequest = {
                    showBottomSheet = false
                },
                onEditingChange = { isEditing = it }
            )
        }
    }
}