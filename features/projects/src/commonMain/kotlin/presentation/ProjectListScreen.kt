package com.entourageapp.features.projects.presentation

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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

@Composable
fun ProjectListScreen(
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
    onAddProjectClick: () -> Unit = {}
) {
    val isSelected = remember { mutableIntStateOf(1) }
    val scrollState = rememberLazyListState()
    val isCollapsed by remember {
        derivedStateOf { scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 50 }
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
                isSelected = isSelected.intValue == 1,
                onClick = { isSelected.intValue = 1 }
            )
            TabButton(
                modifier = Modifier.weight(1f),
                title = "Текущие",
                isSelected = isSelected.intValue == 2,
                onClick = { isSelected.intValue = 2 }
            )
        }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(32.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(
                    count = 10,
                    key = { it }
                ) { index ->
                    ProjectCard(
                        modifier = Modifier
                            .animateItem(
                                fadeInSpec = tween(500),
                                placementSpec = spring(stiffness = Spring.StiffnessLow)
                            ),
                        onCardClick = onCardClick
                    )
                }
            }

            FloatingActionButton(
                onClick = onAddProjectClick,
                containerColor = EntourageTeal.copy(alpha = 0.9f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 96.dp)
                    .size(64.dp),
                shape = RoundedCornerShape(16.dp)
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