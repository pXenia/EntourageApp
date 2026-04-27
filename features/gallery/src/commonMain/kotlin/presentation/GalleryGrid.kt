package com.entourageapp.features.gallery.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults.elevation
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.entourageapp.core.network.dto.ImageDto
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.add
import com.entourageapp.core.ui.delete
import org.jetbrains.compose.resources.painterResource

private val OverlayGrad = Brush.verticalGradient(
    0f to Color.Transparent,
    1f to EntourageBlack,
)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun GalleryGrid(
    images: List<ImageDto>,
    selectedIds: Set<Int>,
    onImageClick: (Int) -> Unit,
    onImageLongClick: (Int) -> Unit,
    onAddClick: () -> Unit,
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
            images.forEachIndexed { index, image ->
                item(
                    key = image.id,
                    span = {
                        when (index % 9) {
                            0 -> GridItemSpan(3)
                            4 -> GridItemSpan(2)
                            else -> GridItemSpan(1)
                        }
                    }
                ) {
                    val isSelected = selectedIds.contains(image.id)
                    when (index % 9) {
                        0 -> {
                            ItemImage(
                                id = image.id,
                                imageUrl = image.url ?: "",
                                note = image.note,
                                heigh = 9f,
                                width = 16f,
                                isSelected = isSelected,
                                onImageClick = onImageClick,
                                onImageLongClick = onImageLongClick,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        }

                        5 -> {
                            ItemImage(
                                id = image.id,
                                imageUrl = image.url ?: "",
                                note = image.note,
                                heigh = 2.0375f,
                                width = 1f,
                                isSelected = isSelected,
                                onImageClick = onImageClick,
                                onImageLongClick = onImageLongClick,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        }

                        else -> {
                            ItemImage(
                                id = image.id,
                                imageUrl = image.url ?: "",
                                note = image.note,
                                isSelected = isSelected,
                                onImageClick = onImageClick,
                                onImageLongClick = onImageLongClick,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        }
                    }
                }
            }

        }
        AnimatedVisibility(
            visible = !scrollState.isScrollInProgress,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 18.dp, end = 8.dp)
        ) {
            FloatingActionButton(
                onClick = { onAddClick() },
                containerColor = EntourageTeal.copy(alpha = 0.9f),
                modifier = Modifier.size(64.dp),
                elevation = elevation(
                    defaultElevation = 0.dp
                ),
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(add),
                    modifier = Modifier.size(12.dp),
                    contentDescription = null,
                    tint = EntourageWhite
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