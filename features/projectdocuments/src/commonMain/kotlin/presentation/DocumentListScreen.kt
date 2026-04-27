package com.entourageapp.features.projectdocuments.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.add
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.arrowRight
import com.entourageapp.core.ui.components.ScreenTitleTwoButtons
import com.entourageapp.core.ui.components.SearchBar
import com.entourageapp.core.ui.cross
import com.entourageapp.core.ui.document
import com.entourageapp.core.ui.search
import com.entourageapp.core.ui.tools.formatUrl
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DocumentListScreen(
    projectId: Int,
    onBackClick: () -> Unit,
    viewModel: DocumentListVM = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val uriHandler = LocalUriHandler.current
    val showSearch = remember { mutableStateOf(false) }
    val showAddDialog = remember { mutableStateOf(false) }

    LaunchedEffect(projectId) {
        viewModel.handleIntent(DocumentListIntent.LoadDocuments(projectId))
    }

    if (showAddDialog.value) {
        AddDocDialog(
            onDismiss = { showAddDialog.value = false },
            onConfirm = { title, url ->
                viewModel.handleIntent(DocumentListIntent.AddDocument(projectId, title, url))
                showAddDialog.value = false
            }
        )
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(horizontal = 16.dp)
    ) {
        ScreenTitleTwoButtons(
            modifier = Modifier.padding(bottom = 8.dp),
            title = "Документы",
            leftIcon = arrowLeft,
            rightIcon = search,
            onLeftButtonClick = onBackClick,
            onRightButtonClick = { showSearch.value = !showSearch.value }
        )

        if (showSearch.value) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                text = "",
                iconSecond = cross,
                onTextChange = { },
                onIconSecondClick = { showSearch.value = !showSearch.value }
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = EntourageBlack
                )
            } else if (state.error != null) {
                Text(
                    text = state.error ?: "Ошибка загрузки",
                    color = EntourageBlack,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    items(state.documents) { doc ->
                        DocumentCard(
                            title = doc.title,
                            onClick = {
                                val formattedUrl = formatUrl(doc.url)
                                if (formattedUrl.isNotEmpty()) {
                                    uriHandler.openUri(formattedUrl)
                                }
                            }
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = { showAddDialog.value = true },
                containerColor = EntourageTeal.copy(alpha = 0.9f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp)
                    .size(64.dp),
                elevation = FloatingActionButtonDefaults.elevation(
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

@Composable
private fun DocumentCard(
    title: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(32.dp),
        color = EntourageBlack.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(document),
                modifier = Modifier.size(20.dp),
                contentDescription = null
            )
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                color = EntourageBlack,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                painter = painterResource(arrowRight),
                modifier = Modifier.size(20.dp),
                contentDescription = null
            )
        }
    }
}