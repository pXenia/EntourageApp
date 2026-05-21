package com.entourageapp.features.gallery.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.entourageapp.core.navigation.Role
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.components.AddRoundButton
import com.entourageapp.core.ui.components.ScreenTitleTwoButtons
import com.entourageapp.core.ui.components.SimpleSearchBar
import com.entourageapp.core.ui.cross
import com.entourageapp.core.ui.delete
import com.entourageapp.core.ui.search
import com.entourageapp.core.ui.tools.showToast
import com.entourageapp.features.gallery.presentation.GalleryState.GalleryStatus
import kotlinx.coroutines.flow.collectLatest

@Composable
actual fun GalleryScreen(
    projectId: Int,
    roomId: Int,
    roleId: Role,
    modifier: Modifier,
    onBackClick: () -> Unit,
    viewModel: GalleryVM
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberLazyGridState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val filteredImages = remember(state.images, state.searchQuery) {
        if (state.searchQuery.isBlank()) {
            state.images
        } else {
            state.images.filter { it.note?.contains(state.searchQuery, ignoreCase = true) == true }
        }
    }

    val launcher = rememberImagePickerLauncher { bytes, fileName, mime ->
        viewModel.onIntent(
            GalleryIntent.SetSelectedImageData(
                GalleryState.SelectedImageData(
                    fileBytes = bytes, fileName = fileName, mimeType = mime
                )
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(GalleryIntent.LoadImages(projectId, if (roomId == 0) null else roomId))
        viewModel.onIntent(GalleryIntent.LoadRooms(projectId))
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is GallerySideEffect.ShowError -> {
                    showToast(effect.message)
                }
                GallerySideEffect.NavigateBack -> onBackClick()
                GallerySideEffect.ScrollToTop -> {
                    scrollState.scrollToItem(0)
                }
            }
        }
    }

    val isSelectionMode = state.isSelectionMode && roleId != Role.Viewer

    NavigationBackHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        isBackEnabled = state.status == GalleryStatus.ViewPager || isSelectionMode || state.isSearchVisible
    ) {
        if (isSelectionMode) {
            viewModel.onIntent(GalleryIntent.ClearSelection)
        } else if (state.isSearchVisible) {
            viewModel.onIntent(GalleryIntent.SetSearchVisibility(false))
        } else {
            viewModel.onIntent(GalleryIntent.CloseViewPager)
        }
    }

    if (state.isAddImageVisible && roleId != Role.Viewer) {
        AddImageDialog(
            imageData = state.selectedImageData,
            availableRooms = state.availableRooms,
            initialRoomId = if (roomId == 0) null else roomId,
            onDismiss = {
                viewModel.onIntent(GalleryIntent.SetAddImageVisibility(isVisible = false))
                viewModel.onIntent(GalleryIntent.SetSelectedImageData(null))
            },
            onConfirm = { note, selectedRoomId ->
                state.selectedImageData?.let { data ->
                    viewModel.onIntent(
                        GalleryIntent.UploadImage(
                            projectId = projectId,
                            image = data,
                            roomId = selectedRoomId,
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
                fadeIn(tween(600)) togetherWith fadeOut(tween(600))
            }
        ) { status ->
            when (status) {
                GalleryStatus.ViewPager -> {
                    val initialPage = remember(state.images, state.selectedImageId) {
                        state.images.indexOfFirst { it.id == state.selectedImageId }.coerceAtLeast(0)
                    }
                    val pagerState = rememberPagerState(
                        initialPage = initialPage,
                        pageCount = { state.images.size }
                    )
                    
                    GalleryViewPager(
                        roleId = roleId,
                        images = state.images,
                        pagerState = pagerState,
                        availableRooms = state.availableRooms,
                        onIntent = viewModel::onIntent,
                        projectId = projectId,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent
                    )
                }

                else -> {
                    Column(
                        modifier = modifier.fillMaxSize().systemBarsPadding()
                            .padding(horizontal = 8.dp)
                    ) {
                        ScreenTitleTwoButtons(
                            title = if (isSelectionMode) "Выбрано: ${state.selectedIds.size}" else "Галерея",
                            leftIcon = if (isSelectionMode) cross else arrowLeft,
                            rightIcon = if (isSelectionMode) delete else search,
                            modifier = Modifier,
                            onLeftButtonClick = {
                                if (isSelectionMode) {
                                    viewModel.onIntent(GalleryIntent.ClearSelection)
                                } else {
                                    onBackClick()
                                }
                            },
                            onRightButtonClick = {
                                if (isSelectionMode) {
                                    viewModel.onIntent(GalleryIntent.DeleteSelectedImages)
                                } else {
                                    viewModel.onIntent(GalleryIntent.SetSearchVisibility(!state.isSearchVisible))
                                }
                            }
                        )

                        AnimatedVisibility(
                            visible = state.isSearchVisible,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            SimpleSearchBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                searchQuery = state.searchQuery,
                                onQueryChange = {
                                    viewModel.onIntent(GalleryIntent.UpdateSearchQuery(it))
                                },
                                onCloseClick = {
                                    viewModel.onIntent(GalleryIntent.SetSearchVisibility(false))
                                },
                                placeholder = "Поиск по заметкам..."
                            )
                        }

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
                                ) {
                                    Text(
                                        text = "Нет изображений",
                                        modifier = Modifier.align(Alignment.Center),
                                        color = EntourageBlack
                                    )
                                    if(roleId != Role.Viewer) {
                                        AddRoundButton(
                                            onClick = {
                                                viewModel.onIntent(
                                                    GalleryIntent.SetAddImageVisibility(
                                                        isVisible = true
                                                    )
                                                )
                                            },
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .padding(bottom = 16.dp, end = 8.dp)
                                        )
                                    }
                                }
                            }

                            GalleryStatus.List -> {
                                GalleryGrid(
                                    roleId = roleId,
                                    images = filteredImages,
                                    selectedIds = state.selectedIds,
                                    isSelectionMode = state.isSelectionMode,
                                    onIntent = viewModel::onIntent,
                                    screenWidth = screenWidth,
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

