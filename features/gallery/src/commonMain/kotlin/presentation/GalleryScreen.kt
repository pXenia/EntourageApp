package com.entourageapp.features.gallery.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.add
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.components.ScreenTitleTwoButtons
import org.koin.compose.viewmodel.koinViewModel

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

    Column(modifier = modifier.fillMaxSize()) {
        ScreenTitleTwoButtons(
            title = "Галерея",
            leftIcon = arrowLeft,
            rightIcon = add,
            onLeftButtonClick = onBackClick,
            onRightButtonClick = { launcher() }
        )

        when {
            state.isLoading || state.isUploading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = EntourageBlack)
                }
            }

            state.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.error ?: "Ошибка", color = EntourageBlack)
                }
            }

            state.images.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Нет изображений", color = EntourageBlack)
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(state.images) { image ->
                        GalleryImageItem(
                            imageUrl = image.url ?: "",
                            note = image.note,
                            onDeleteClick = {
                                viewModel.handleIntent(
                                    GalleryIntent.DeleteImage(projectId, image.id)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GalleryImageItem(
    imageUrl: String,
    note: String?,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = note,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}