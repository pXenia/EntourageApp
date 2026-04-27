package com.entourageapp.features.gallery.presentation

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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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

@Composable
fun GalleryScreen(
    projectId: Int,
    roomId: Int,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val viewModel: GalleryVM = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

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
        isBackEnabled = state.status == GalleryStatus.ViewPager,
        onBackCompleted = {
            viewModel.handleIntent(GalleryIntent.ChangeStatus(GalleryStatus.List))
        }
    )


    when (state.status) {
        GalleryStatus.ViewPager -> GalleryViewPager(
            state.images,
            state.selectedImageId,
            {},
            { viewModel.handleIntent(GalleryIntent.ChangeStatus(GalleryStatus.List)) }
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

                when (state.status) {
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
                        GalleyGrid(
                            images = state.images,
                            onImageClick = { id ->
                                viewModel.handleIntent(GalleryIntent.ChangeSelectedImageId(id = id))
                                viewModel.handleIntent(GalleryIntent.ChangeStatus(GalleryStatus.ViewPager))
                            },
                            onDeleteClick = {
                                viewModel.handleIntent(
                                    GalleryIntent.DeleteImage(
                                        projectId = projectId,
                                        imageId = state.images[state.selectedImageId].id
                                    )
                                )
                            }
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

@Composable
private fun GalleryViewPager(
    images: List<ImageDto>,
    selectedImageId: Int,
    onDeleteClick: () -> Unit,
    onClosesClick: () -> Unit,
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
            AsyncImage(
                model = images[index].url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillWidth
            )
        }

        Icon(
            painter = painterResource(cross),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .systemBarsPadding()
                .padding(16.dp)
                .size(24.dp)
                .clickable { onClosesClick() },
            tint = EntourageBlack,
        )
    }
}

@Composable
private fun GalleyGrid(
    images: List<ImageDto>,
    onImageClick: (Int) -> Unit,
    onDeleteClick: () -> Unit
) {
    LazyVerticalGrid(
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
                        5 -> GridItemSpan(1)
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
                            onDeleteClick = onDeleteClick,
                            onImageClick = onImageClick
                        )
                    }

                    5 -> {
                        ItemImage(
                            id = image.id,
                            imageUrl = image.url ?: "",
                            note = image.note,
                            heigh = 2.0375f,
                            width = 1f,
                            onDeleteClick = onDeleteClick,
                            onImageClick = onImageClick
                        )
                    }

                    else -> {
                        ItemImage(
                            id = image.id,
                            imageUrl = image.url ?: "",
                            note = image.note,
                            onDeleteClick = onDeleteClick,
                            onImageClick = onImageClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemImage(
    id: Int,
    imageUrl: String,
    note: String?,
    heigh: Float = 1f,
    width: Float = 1f,
    onImageClick: (Int) -> Unit,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(width / heigh)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onImageClick(id) }
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = note,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
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