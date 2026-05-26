package com.entourageapp.features.projects.presentation.projectlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.components.AddRoundButton
import com.entourageapp.core.ui.components.TabButton
import com.entourageapp.core.ui.components.TopScreenTitle
import com.entourageapp.features.projects.presentation.components.ProjectCard
import entourageapp.features.projects.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProjectListScreen(
    modifier: Modifier = Modifier,
    onCardClick: (Int) -> Unit = {},
    onAddProjectClick: () -> Unit = {},
    viewModel: ProjectListVM = koinViewModel()
) {
    val scrollState = rememberLazyListState()
    val state by viewModel.state.collectAsState()
    val isCollapsed by remember {
        derivedStateOf {
            (scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 50) && state.projectCards.size > 4
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is ProjectListSideEffect.NavigateToProject -> onCardClick(sideEffect.id)
                is ProjectListSideEffect.NavigateToCreateProject -> onAddProjectClick()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(ProjectListIntent.LoadProjects)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        AnimatedVisibility(
            visible = !isCollapsed,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            TopScreenTitle(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.projects_title).uppercase()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TabButton(
                modifier = Modifier.weight(1f),
                title = stringResource(Res.string.filter_current),
                isSelected = state.projectFilter == ProjectFilter.CURRENT,
                onClick = { viewModel.handleIntent(ProjectListIntent.ChangeFilter(ProjectFilter.CURRENT)) }
            )
            TabButton(
                modifier = Modifier.weight(1f),
                title = stringResource(Res.string.filter_archive),
                isSelected = state.projectFilter == ProjectFilter.ARCHIVE,
                onClick = { viewModel.handleIntent(ProjectListIntent.ChangeFilter(ProjectFilter.ARCHIVE)) }
            )
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                state.isLoading && state.projectCards.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = EntourageTeal,
                        trackColor = EntourageWhite.copy(alpha = 0.6f),
                    )
                }

                state.error != null && state.projectCards.isEmpty() -> {
                    Text(
                        text = state.error ?: stringResource(Res.string.error_occurred),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        state = scrollState,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(32.dp)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(
                            items = state.projectCards,
                            key = { it.id }) { project ->
                            ProjectCard(
                                modifier = Modifier
                                    .animateItem(
                                        fadeInSpec = tween(500),
                                        placementSpec = spring(stiffness = Spring.StiffnessLow)
                                    ),
                                onCardClick = { viewModel.handleIntent(ProjectListIntent.OpenProject(project.id)) },
                                title = project.title,
                                area = project.square,
                                years = project.years,
                                rooms = project.roomsCount,
                                participants = project.membersCount
                            )
                        }
                    }
                }
            }

            AddRoundButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(bottom = 80.dp),
                onClick = { viewModel.handleIntent(ProjectListIntent.CreateProject) }
            )
        }
    }
}
