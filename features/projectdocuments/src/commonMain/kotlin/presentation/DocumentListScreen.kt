package com.entourageapp.features.projectdocuments.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.entourageapp.core.navigation.Role
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.arrowRight
import com.entourageapp.core.ui.components.AddRoundButton
import com.entourageapp.core.ui.components.ScreenTitleTwoButtons
import com.entourageapp.core.ui.components.SimpleSearchBar
import com.entourageapp.core.ui.dialogs.DeleteDialog
import com.entourageapp.core.ui.document
import com.entourageapp.core.ui.search
import com.entourageapp.core.ui.tools.formatUrl
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentListScreen(
    projectId: Int,
    roleId: Role,
    onBackClick: () -> Unit,
    viewModel: DocumentListVM = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val uriHandler = LocalUriHandler.current
    val showAddDialog = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scrollState = rememberLazyListState()

    val filteredDocuments = remember(state.documents, state.searchQuery) {
        if (state.searchQuery.isBlank()) {
            state.documents
        } else {
            state.documents.filter { it.title.contains(state.searchQuery, ignoreCase = true) }
        }
    }

    LaunchedEffect(projectId) {
        viewModel.handleIntent(DocumentListIntent.LoadDocuments(projectId))
    }

    if (state.showDeleteDialog) {
        DeleteDialog(
            onDismiss = { viewModel.handleIntent(DocumentListIntent.DismissDeleteDialog) },
            onOkClick = { viewModel.handleIntent(DocumentListIntent.DeleteDocument(projectId)) },
            sheetState = sheetState,
            title = "Удаление документа",
            text = "Вы действительно хотите удалить документ \"${state.selectedDocTitle}\"?",
            buttonTitle = "Удалить"
        )
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
        modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(horizontal = 16.dp).clipToBounds()
    ) {
        ScreenTitleTwoButtons(
            title = "Документы",
            leftIcon = arrowLeft,
            rightIcon = search,
            onLeftButtonClick = onBackClick,
            onRightButtonClick = { viewModel.handleIntent(DocumentListIntent.SetSearchVisibility(!state.isSearchVisible)) }
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
                onQueryChange = { viewModel.handleIntent(DocumentListIntent.UpdateSearchQuery(it)) },
                onCloseClick = { viewModel.handleIntent(DocumentListIntent.SetSearchVisibility(false)) },
                placeholder = "Поиск по документам..."
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
                    state = scrollState,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    items(filteredDocuments) { doc ->
                        DocumentCard(
                            title = doc.title,
                            onClick = {
                                val formattedUrl = formatUrl(doc.url)
                                if (formattedUrl.isNotEmpty()) {
                                    uriHandler.openUri(formattedUrl)
                                }
                            },
                            onLongClick = {
                                if (roleId != Role.Viewer) {
                                    viewModel.handleIntent(DocumentListIntent.ShowDeleteDialog(doc.id, doc.title))
                                }
                            }
                        )
                    }
                }
            }
            if(roleId != Role.Viewer) {
                this@Column.AnimatedVisibility(
                    visible = !scrollState.isScrollInProgress,
                    enter = slideInVertically(
                        animationSpec = tween(durationMillis = 300),
                        initialOffsetY = { it }
                    ) + fadeIn(animationSpec = tween(durationMillis = 300)),

                    exit = slideOutVertically(
                        animationSpec = tween(durationMillis = 300),
                        targetOffsetY = { it }
                    ) + fadeOut(animationSpec = tween(durationMillis = 300)),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 16.dp)
                ) {
                    AddRoundButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd),
                        onClick = { showAddDialog.value = true }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DocumentCard(
    title: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .background(EntourageBlack.copy(alpha = 0.1f))
            .innerShadow(
                shape = RoundedCornerShape(32.dp),
                shadow = Shadow(
                    radius = 16.dp,
                    spread = 6.dp,
                    color = EntourageWhite.copy(alpha = 0.15f),
                    offset = DpOffset(x = 4.dp, 3.dp)
                )
            )
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
