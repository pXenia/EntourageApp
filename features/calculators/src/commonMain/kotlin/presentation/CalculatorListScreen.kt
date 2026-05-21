package com.entourageapp.features.calculators.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.entourageapp.core.ui.components.TopScreenTitle
import com.entourageapp.features.calculators.presentation.components.CalculatorCard

@Composable
fun CalculatorListScreen(
    modifier: Modifier = Modifier,
    projectId: Int,
    roomId: Int,
    onWallpaperClick: (Int, Int) -> Unit,
    onPaintClick: (Int, Int) -> Unit,
    onLaminateClick: (Int, Int) -> Unit
) {
    val scrollState = rememberLazyListState()
    val isCollapsed by remember {
        derivedStateOf { scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 50 }
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                title = "калькуляторы".uppercase()
            )
        }

        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(32.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item{
                CalculatorCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(
                            fadeInSpec = tween(500),
                            placementSpec = spring(stiffness = Spring.StiffnessLow)
                        ),
                    onCardClick = { onWallpaperClick(projectId, roomId) },
                    title = "Обои"
                )
            }
            item{
                CalculatorCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(
                            fadeInSpec = tween(500),
                            placementSpec = spring(stiffness = Spring.StiffnessLow)
                        ),
                    onCardClick = { onPaintClick(projectId, roomId) },
                    title = "Краска"
                )
            }
            item{
                CalculatorCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(
                            fadeInSpec = tween(500),
                            placementSpec = spring(stiffness = Spring.StiffnessLow)
                        ),
                    onCardClick = { onLaminateClick(projectId, roomId) },
                    title = "Ламинат"
                )
            }
        }
    }
}
