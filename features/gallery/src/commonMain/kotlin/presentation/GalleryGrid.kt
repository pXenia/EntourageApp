package com.entourageapp.features.gallery.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.entourageapp.core.navigation.Role
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.components.AddRoundButton
import com.entourageapp.core.ui.delete
import com.entourageapp.features.gallery.domain.GalleryImage
import org.jetbrains.compose.resources.painterResource

private val OverlayGrad = Brush.verticalGradient(
    0f to Color.Transparent,
    1f to EntourageBlack,
)

@Composable
internal fun GalleryGrid(
    roleId: Role,
    images: List<GalleryImage>,
    selectedIds: Set<Int>,
    isSelectionMode: Boolean,
    onIntent: (GalleryIntent) -> Unit,
    screenWidth: Dp,
    scrollState: LazyGridState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Box(
        modifier = Modifier.fillMaxSize().clipToBounds()
    ) {
        LazyVerticalGrid(
            state = scrollState,
            columns = GridCells.Fixed(3),
            modifier = Modifier.padding(top = 12.dp).fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val difPattern = images.size % 9
            images.forEachIndexed { index, image ->
                val remaining = images.size - index

                val span = when {
                    index % 9 == 0 -> 3
                    index % 9 == 4 -> 2
                    else -> 1
                }

                val spanDif = when (difPattern) {
                    1 -> 3
                    2 -> if (remaining == 2) 2 else 1
                    3 -> 1
                    4 -> if (remaining == 4) 3 else 1
                    5 -> if (remaining == 5) 2 else 1
                    6 -> if (remaining == 6) 3 else if (remaining == 2) 2 else 1
                    7 -> if (remaining == 7 || remaining == 1) 3 else if (remaining == 3) 2 else 1
                    8 -> if (remaining == 8 || remaining == 2) 2 else if (remaining == 3) 3 else 1
                    else -> 1
                }

                item(
                    key = image.id,
                    span = { GridItemSpan(if (remaining <= difPattern) spanDif else span) }
                ) {
                    val isSelected = selectedIds.contains(image.id) && roleId != Role.Viewer

                    val w: Float
                    val h: Float

                    val wide = 16f to 9f
                    val tall = 1f to (2f + 4.dp / (screenWidth / 3))
                    val square = 1f to 1f

                    if (remaining > difPattern) {
                        val pair = when {
                            index % 9 == 0 -> wide
                            index % 9 == 5 -> tall
                            else -> square
                        }
                        w = pair.first
                        h = pair.second
                    } else {
                        val pair = when (difPattern) {
                            1 -> wide
                            2 -> if (remaining == 2) square else tall
                            3 -> square
                            4 -> if (remaining == 4) wide else square
                            5 -> if (remaining == 4) tall else square
                            6 -> if (remaining == 6) wide else if (remaining == 1) tall else square
                            7 -> if (remaining == 7 || remaining == 1) wide else if (remaining == 2) tall else square
                            8 -> if (remaining == 7 || remaining == 1) tall else if (remaining == 3) wide else square
                            else -> square
                        }
                        w = pair.first
                        h = pair.second
                    }

                    ItemImage(
                        id = image.id,
                        imageUrl = image.url ?: "",
                        note = image.note,
                        heigh = h,
                        width = w,
                        isSelected = isSelected,
                        onImageClick = { id ->
                            if (isSelectionMode && roleId != Role.Viewer) {
                                onIntent(GalleryIntent.ToggleSelection(id))
                            } else {
                                onIntent(GalleryIntent.SelectImage(id))
                            }
                        },
                        onImageLongClick = { id ->
                            if (roleId != Role.Viewer) {
                                onIntent(GalleryIntent.ToggleSelection(id))
                            }
                        },
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = !scrollState.isScrollInProgress,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = 300),
                initialOffsetY = { it }
            ) + fadeIn(animationSpec = tween(durationMillis = 300)),

            exit = slideOutVertically(
                animationSpec = tween(durationMillis = 300),
                targetOffsetY = { it }
            ) + fadeOut(animationSpec = tween(durationMillis = 300)),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 8.dp)
        ) {
            if (roleId != Role.Viewer) {
                AddRoundButton(
                    onClick = {
                        onIntent(GalleryIntent.SetAddImageVisibility(true))
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class)
@Composable
private fun ItemImage(
    id: Int,
    imageUrl: String,
    note: String?,
    heigh: Float = 1f,
    width: Float = 1f,
    isSelected: Boolean = false,
    onImageClick: (Int) -> Unit,
    onImageLongClick: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(width / heigh)
            .clip(RoundedCornerShape(8.dp))
            .background(EntourageBlack.copy(alpha = 0.05f))
            .combinedClickable(
                onClick = { onImageClick(id) },
                onLongClick = { onImageLongClick(id) }
            )
    ) {
        with(sharedTransitionScope) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = note,
                modifier = Modifier
                    .fillMaxSize()
                    .sharedElement(
                        rememberSharedContentState(key = "image_$id"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                contentScale = ContentScale.Crop
            )
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(EntourageWhite),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(delete),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = EntourageBlack
                    )
                }
            }
        }
        if (!note.isNullOrBlank()) {
            Box(modifier = Modifier.fillMaxSize().background(OverlayGrad))
            Text(
                text = note,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                color = EntourageWhite,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
