package com.entourageapp.features.gallery.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.entourageapp.core.network.dto.ImageDto
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.add
import com.entourageapp.core.ui.appBackground
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.components.ScreenTitleTwoButtons
import com.entourageapp.core.ui.cross
import com.entourageapp.features.gallery.presentation.GalleryState.GalleryStatus
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

private val OverlayGrad = Brush.verticalGradient(
    0f to Color.Transparent,
    1f to Color(0xCC000000)
)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GalleryScreen(
    projectId: Int,
    roomId: Int,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val viewModel: GalleryVM = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberLazyGridState()

    val launcher = rememberImagePickerLauncher { bytes, fileName, mime ->
        viewModel.handleIntent(
            GalleryIntent.UploadImage(
                projectId = projectId,
                fileBytes = bytes,
                fileName = fileName,
                mimeType = mime
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(GalleryIntent.LoadImages(projectId, roomId))
    }


    NavigationBackHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        isBackEnabled = state.status == GalleryStatus.ViewPager
    ) {
        viewModel.handleIntent(GalleryIntent.ChangeStatus(GalleryStatus.List))
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
                GalleryStatus.ViewPager -> GalleryViewPager(
                    images = state.images,
                    selectedImageId = state.selectedImageId,
                    onDeleteClick = {},
                    onClosesClick = { viewModel.handleIntent(GalleryIntent.ChangeStatus(GalleryStatus.List)) },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedContent
                )

                else -> {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .padding(horizontal = 8.dp)
                    ) {
                        ScreenTitleTwoButtons(
                            title = "Галерея",
                            leftIcon = arrowLeft,
                            rightIcon = add,
                            modifier = Modifier.padding(horizontal = 12.dp),
                            onLeftButtonClick = onBackClick,
                            onRightButtonClick = { launcher() }
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
                                    onImageClick = { id ->
                                        viewModel.handleIntent(
                                            GalleryIntent.ChangeSelectedImageId(
                                                id = id
                                            )
                                        )
                                        viewModel.handleIntent(
                                            GalleryIntent.ChangeStatus(
                                                GalleryStatus.ViewPager
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
    selectedImageId: Int,
    onDeleteClick: () -> Unit,
    onClosesClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val initialPage = images.indexOfFirst { it.id == selectedImageId }.coerceAtLeast(0)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .appBackground()
    ) {
        HorizontalPager(
            state = rememberPagerState(initialPage = initialPage, pageCount = { images.size }),
            key = { images[it].id },
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
        ) { index ->
            with(sharedTransitionScope) {
                AsyncImage(
                    model = images[index].url,
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
    onImageClick: (Int) -> Unit,
    scrollState: LazyGridState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
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
                when (index % 9) {
                    0 -> {
                        ItemImage(
                            id = image.id,
                            imageUrl = image.url ?: "",
                            note = image.note,
                            heigh = 9f,
                            width = 16f,
                            onImageClick = onImageClick,
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
                            onImageClick = onImageClick,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    }

                    else -> {
                        ItemImage(
                            id = image.id,
                            imageUrl = image.url ?: "",
                            note = image.note,
                            onImageClick = onImageClick,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ItemImage(
    id: Int,
    imageUrl: String,
    note: String?,
    heigh: Float = 1f,
    width: Float = 1f,
    onImageClick: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(width / heigh)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onImageClick(id) }
    ) {
        with(sharedTransitionScope) {
            AsyncImage(
                model = imageUrl,
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
