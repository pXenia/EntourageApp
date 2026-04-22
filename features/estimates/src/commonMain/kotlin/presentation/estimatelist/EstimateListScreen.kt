package com.entourageapp.features.estimates.presentation.estimatelist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults.elevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.add
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.components.ScreenTitleTwoButtons
import com.entourageapp.core.ui.components.SearchBar
import com.entourageapp.core.ui.filter
import com.entourageapp.core.ui.print
import com.entourageapp.core.ui.tools.formatTwoDecimals
import com.entourageapp.features.estimates.presentation.EstimateCard
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EstimateListScreen(
    projectId: Int,
    onAddPosition: (Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: EstimateListVM = koinViewModel()
) {
    val scrollState = rememberLazyListState()
    val state by viewModel.state.collectAsState()
    val isCollapsedHeader by remember {
        derivedStateOf { scrollState.firstVisibleItemScrollOffset > 0 }
    }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(EstimateListIntent.LoadData(projectId))
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    ) {
        AnimatedVisibility(
            visible = !isCollapsedHeader,
            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ScreenTitleTwoButtons(
                    modifier = Modifier.padding(bottom = 8.dp),
                    title = "Смета по проекту",
                    leftIcon = arrowLeft,
                    rightIcon = print,
                    onLeftButtonClick = onBackClick,
                    onRightButtonClick = {
                        viewModel.handleIntent(EstimateListIntent.ExportXlsx(projectId))
                    }
                )
                TotalCard(string = "Итого", value = "${state.totalSum.formatTwoDecimals()} ₽")
                TotalCard(string = "Позиций", value = state.itemsCount.toString())

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = EntourageBlack
                )
            }
        }
        SearchBar(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            text = state.searchQuery,
            onTextChange = { viewModel.handleIntent(EstimateListIntent.UpdateSearch(it)) },
            iconSecond = filter
        )
        Box(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(32.dp)),
                state = scrollState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(
                    items = state.filteredItems,
                    key = { _, item -> item.id }
                ) { index, item ->
                    EstimateCard(
                        modifier = Modifier.fillMaxWidth(),
                        number = index + 1,
                        type = item.itemType,
                        name = item.name,
                        units = item.unit,
                        price = item.price.formatTwoDecimals(),
                        quantity = item.quantity.formatTwoDecimals(),
                        total = item.total.formatTwoDecimals(),
                        room = item.room
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            FloatingActionButton(
                onClick = { onAddPosition(projectId) },
                containerColor = EntourageTeal.copy(alpha = 0.9f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 8.dp)
                    .size(64.dp),
                elevation = elevation(
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
private fun TotalCard(
    string: String,
    value: String
) {
    Surface(
        color = EntourageBlack.copy(alpha = 0.1f),
        shape = RoundedCornerShape(32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = string.uppercase(),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp),
                color = EntourageTeal
            )
        }
    }
}