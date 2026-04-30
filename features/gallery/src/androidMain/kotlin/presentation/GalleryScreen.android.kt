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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.components.AddRoundButton
import com.entourageapp.core.ui.components.ScreenTitleTwoButtons
import com.entourageapp.core.ui.cross
import com.entourageapp.core.ui.delete
import com.entourageapp.core.ui.search
import com.entourageapp.core.ui.tools.showToast
import com.entourageapp.features.gallery.presentation.GalleryState.GalleryStatus
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun GalleryScreen(
    projectId: Int,
    roomId: Int,
    modifier: Modifier,
    onBackClick: () -> Unit,
    viewModel: GalleryVM
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberLazyGridState()

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
            }
        }
    }

    NavigationBackHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        isBackEnabled = state.status == GalleryStatus.ViewPager || state.isSelectionMode || state.isSearchVisible
    ) {
        if (state.isSelectionMode) {
            viewModel.onIntent(GalleryIntent.ClearSelection)
        } else if (state.isSearchVisible) {
            viewModel.onIntent(GalleryIntent.SetSearchVisibility(false))
        } else {
            viewModel.onIntent(GalleryIntent.CloseViewPager)
        }
    }

    if (state.isAddImageVisible) {
        AddImageDialog(
            imageData = state.selectedImageData,
            onDismiss = {
                viewModel.onIntent(GalleryIntent.SetAddImageVisibility(isVisible = false))
                viewModel.onIntent(GalleryIntent.SetSelectedImageData(null))
            },
            onConfirm = { note ->
                state.selectedImageData?.let { data ->
                    viewModel.onIntent(
                        GalleryIntent.UploadImage(
                            projectId = projectId,
                            image = data,
                            roomId = if (roomId == 0) null else roomId,
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
                            title = if (state.isSelectionMode) "Выбрано: ${state.selectedIds.size}" else "Галерея",
                            leftIcon = if (state.isSelectionMode) cross else arrowLeft,
                            rightIcon = if (state.isSelectionMode) delete else search,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            onLeftButtonClick = {
                                if (state.isSelectionMode) {
                                    viewModel.onIntent(GalleryIntent.ClearSelection)
                                } else {
                                    onBackClick()
                                }
                            },
                            onRightButtonClick = {
                                if (state.isSelectionMode) {
                                    viewModel.onIntent(GalleryIntent.DeleteSelectedImages(projectId))
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
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                GallerySearchField(
                                    searchQuery = state.searchQuery,
                                    onQueryChange = {
                                        viewModel.onIntent(GalleryIntent.UpdateSearchQuery(it))
                                    },
                                    modifier = Modifier.weight(1f)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                IconButton(
                                    onClick = {
                                        viewModel.onIntent(GalleryIntent.SetSearchVisibility(false))
                                    },
                                    modifier = Modifier.size(48.dp).clip(CircleShape)
                                        .background(EntourageBlack.copy(alpha = 0.05f))
                                ) {
                                    Icon(
                                        painter = painterResource(cross),
                                        contentDescription = "Close search",
                                        modifier = Modifier.size(24.dp),
                                        tint = EntourageBlack
                                    )
                                }
                            }
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
                                    AddRoundButton(
                                        onClick = {
                                            viewModel.onIntent(GalleryIntent.SetAddImageVisibility(isVisible = true))
                                        },
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(bottom = 18.dp, end = 8.dp)
                                    )
                                }
                            }

                            GalleryStatus.List -> {
                                GalleryGrid(
                                    images = filteredImages,
                                    selectedIds = state.selectedIds,
                                    isSelectionMode = state.isSelectionMode,
                                    onIntent = viewModel::onIntent,
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

@Composable
private fun GallerySearchField(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val customTextSelectionColors = TextSelectionColors(
        handleColor = EntourageTeal, backgroundColor = EntourageTeal.copy(alpha = 0.4f)
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        BasicTextField(
            value = searchQuery,
            onValueChange = { onQueryChange(it) },
            modifier = modifier.height(48.dp)
                .background(Color.Transparent, RoundedCornerShape(24.dp))
                .border(1.dp, EntourageBlack, RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp),
            singleLine = true,
            cursorBrush = SolidColor(EntourageBlack),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = EntourageBlack, fontSize = 16.sp
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart
                ) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Поиск по заметкам...",
                            color = EntourageBlack.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}
