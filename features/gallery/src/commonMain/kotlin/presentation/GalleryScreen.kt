package com.entourageapp.features.gallery.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.components.ScreenTitleTwoButtons
import com.entourageapp.core.ui.cross
import com.entourageapp.core.ui.delete
import com.entourageapp.core.ui.search
import com.entourageapp.features.gallery.presentation.GalleryState.GalleryStatus
import org.koin.compose.viewmodel.koinViewModel

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
