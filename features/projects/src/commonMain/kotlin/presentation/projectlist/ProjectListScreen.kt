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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.entourageapp.core.ui.add
import com.entourageapp.core.ui.components.TabButton
import com.entourageapp.core.ui.components.TopScreenTitle
import com.entourageapp.features.projects.presentation.components.ProjectCard
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProjectListScreen(
    modifier: Modifier = Modifier,
    onCardClick: (projectId: Int) -> Unit = {},
    onAddProjectClick: () -> Unit = {},
    viewModel: ProjectListVM = koinViewModel()
) {
    val scrollState = rememberLazyListState()
    val isCollapsed by remember {
        derivedStateOf { scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 50 }
    }
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.handleIntent(ProjectListIntent.LoadProjects)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = !isCollapsed,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            TopScreenTitle(
                modifier = Modifier
                    .fillMaxWidth(),
                title = "проекты".uppercase()
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
                title = "Все",
                isSelected = state.projectFilter == ProjectFilter.ALL,
                onClick = {viewModel.handleIntent(ProjectListIntent.FilterProjects(ProjectFilter.ALL))}
            )
            TabButton(
                modifier = Modifier.weight(1f),
                title = "Текущие",
                isSelected = state.projectFilter == ProjectFilter.CURRENT,
                onClick = {viewModel.handleIntent(ProjectListIntent.FilterProjects(ProjectFilter.CURRENT))}
            )
        }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = EntourageTeal,
                    trackColor = EntourageWhite.copy(alpha = 0.6f),
                )
            }
            if (state.error != null) {
                Text(
                    text = state.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(32.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(state.projectCards) { project ->
                    ProjectCard(
                        modifier = Modifier
                            .animateItem(
                                fadeInSpec = tween(500),
                                placementSpec = spring(stiffness = Spring.StiffnessLow)
                            ),
                        onCardClick = { onCardClick(project.id) },
                        title = project.title,
                        area = project.square,
                        years = project.years,
                        rooms = project.roomsCount,
                        participants = project.membersCount
                    )
                }
            }

            FloatingActionButton(
                onClick = onAddProjectClick,
                containerColor = EntourageTeal.copy(alpha = 0.9f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 80.dp)
                    .size(64.dp),
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