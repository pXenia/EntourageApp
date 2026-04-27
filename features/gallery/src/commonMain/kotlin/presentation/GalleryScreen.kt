package com.entourageapp.features.gallery.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults.elevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.entourageapp.core.network.dto.ImageDto
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.add
import com.entourageapp.core.ui.appBackground
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.components.ScreenTitleTwoButtons
import com.entourageapp.core.ui.cross
import com.entourageapp.core.ui.delete
import com.entourageapp.core.ui.search
import com.entourageapp.features.gallery.presentation.GalleryState.GalleryStatus
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

private val OverlayGrad = Brush.verticalGradient(
    0f to Color.Transparent,
    1f to EntourageBlack,
)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GalleryScreen(
    projectId: Int,
    roomId: Int,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: GalleryVM = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberLazyGridState()

    val launcher = rememberImagePickerLauncher { bytes, fileName, mime ->
        viewModel.handleIntent(
            GalleryIntent.SetSelectedImage(
                GalleryState.SelectedImageData(
                    fileBytes = bytes,
                    fileName = fileName,
                    mimeType = mime
                )
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(GalleryIntent.LoadImages(projectId, roomId))
    }


    NavigationBackHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        isBackEnabled = state.status == GalleryStatus.ViewPager || state.isSelectionMode
    ) {
        if (state.isSelectionMode) {
            viewModel.handleIntent(GalleryIntent.ClearSelection)
        } else {
            viewModel.handleIntent(GalleryIntent.ChangeStatus(GalleryStatus.List))
        }
    }

    if (state.isAddImageVisible) {
        AddImageDialog(
            imageData = state.selectedImageData,
            onDismiss = {
                viewModel.handleIntent(GalleryIntent.ChangeAddImageVisibility(isVisible = false))
                viewModel.handleIntent(GalleryIntent.SetSelectedImage(null))
            },
            onConfirm = { note ->
                state.selectedImageData?.let { data ->
                    viewModel.handleIntent(
                        GalleryIntent.UploadImage(
                            projectId = projectId,
                            image = data,
                            roomId = roomId,
                            note = note
                        )
                    )
                }
            },
            launcher = launcher
        )
    }

    SharedTransitionLayout {
        AnimatedContent(
            targetState = state.status,
            label = "gallery_transition",
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(300))
            }
        ) { status ->
            when (status) {
                GalleryStatus.ViewPager -> {
                    val pagerState = rememberPagerState(
                        initialPage = state.images.indexOfFirst { it.id == state.selectedImageId }
                            .coerceAtLeast(0),
                        pageCount = { state.images.size }
                    )
                    GalleryViewPager(
                        images = state.images,
                        pagerState = pagerState,
                        onDeleteClick = {
                            val currentImage = state.images[pagerState.currentPage]
                            viewModel.handleIntent(
                                GalleryIntent.DeleteImage(
                                    projectId,
                                    currentImage.id
                                )
                            )
                        },
                        onClosesClick = {
                            viewModel.handleIntent(
                                GalleryIntent.ChangeStatus(
                                    GalleryStatus.List
                                )
                            )
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent
                    )
                }

                else -> {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .systemBarsPadding()
                            .padding(horizontal = 8.dp)
                    ) {
                        ScreenTitleTwoButtons(
                            title = if (state.isSelectionMode) "Выбрано: ${state.selectedIds.size}" else "Галерея",
                            leftIcon = if (state.isSelectionMode) cross else arrowLeft,
                            rightIcon = if (state.isSelectionMode) delete else search,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            onLeftButtonClick = {
                                if (state.isSelectionMode) {
                                    viewModel.handleIntent(GalleryIntent.ClearSelection)
                                } else {
                                    onBackClick()
                                }
                            },
                            onRightButtonClick = {
                                if (state.isSelectionMode) {
                                    viewModel.handleIntent(
                                        GalleryIntent.DeleteSelectedImages(
                                            projectId
                                        )
                                    )
                                } else {
                                }
                            }
                        )

                        when (status) {
                            GalleryStatus.Loading -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = EntourageBlack)
                                }
                            }

                            GalleryStatus.IsEmpty -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Нет изображений", color = EntourageBlack)
                                }
                            }

                            GalleryStatus.List -> {
                                GalleryGrid(
                                    images = state.images,
                                    selectedIds = state.selectedIds,
                                    onImageClick = { id ->
                                        if (state.isSelectionMode) {
                                            viewModel.handleIntent(GalleryIntent.ToggleSelection(id))
                                        } else {
                                            viewModel.handleIntent(
                                                GalleryIntent.ChangeSelectedImageId(
                                                    id
                                                )
                                            )
                                            viewModel.handleIntent(
                                                GalleryIntent.ChangeStatus(
                                                    GalleryStatus.ViewPager
                                                )
                                            )
                                        }
                                    },
                                    onImageLongClick = { id ->
                                        viewModel.handleIntent(GalleryIntent.ToggleSelection(id))
                                    },
                                    onAddClick = {
                                        viewModel.handleIntent(
                                            GalleryIntent.ChangeAddImageVisibility(
                                                isVisible = true
                                            )
                                        )
                                    },
                                    scrollState = scrollState,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@AnimatedContent
                                )
                            }

                            else -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Ошибка", color = EntourageBlack)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun GalleryViewPager(
    images: List<ImageDto>,
    pagerState: androidx.compose.foundation.pager.PagerState,
    onDeleteClick: () -> Unit,
    onClosesClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .appBackground()
    ) {
        HorizontalPager(
            state = pagerState,
            key = { images[it].id },
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
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

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .systemBarsPadding()
                .padding(16.dp)
                .clip(CircleShape)
                .background(EntourageWhite.copy(alpha = 0.6f))
                .clickable { onDeleteClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(delete),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(16.dp),
                tint = Color.Red,
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .systemBarsPadding()
                .padding(16.dp)
                .clip(CircleShape)
                .background(EntourageWhite.copy(alpha = 0.6f))
                .clickable { onClosesClick() },
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
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun GalleryGrid(
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
