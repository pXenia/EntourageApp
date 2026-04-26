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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.add
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.components.ScreenTitleTwoButtons
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
                    modifier = Modifier.padding(top = 12.dp).fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    state.images.forEachIndexed { index, image ->
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
                                        imageUrl = image.url ?: "",
                                        note = image.note,
                                        heigh = 9f,
                                        width = 16f,
                                        onDeleteClick = {
                                            viewModel.handleIntent(
                                                GalleryIntent.DeleteImage(projectId, image.id)
                                            )
                                        }
                                    )
                                }

                                5 -> {
                                    ItemImage(
                                        imageUrl = image.url ?: "",
                                        note = image.note,
                                        heigh = 2.0375f,
                                        width = 1f,
                                        onDeleteClick = {
                                            viewModel.handleIntent(
                                                GalleryIntent.DeleteImage(projectId, image.id)
                                            )
                                        }
                                    )
                                }

                                else -> {
                                    ItemImage(
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
        }
    }
}

@Composable
private fun ItemImage(
    imageUrl: String,
    note: String?,
    heigh: Float = 1f,
    width: Float = 1f,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(width / heigh)
            .clip(RoundedCornerShape(8.dp))
            .clickable { }
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
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = EntourageWhite,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}