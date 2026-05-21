package com.entourageapp.features.estimates.presentation.estimatelist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.navigation.Role
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.components.AddRoundButton
import com.entourageapp.core.ui.components.ScreenTitleTwoButtons
import com.entourageapp.core.ui.components.SimpleSearchBar
import com.entourageapp.core.ui.dialogs.DeleteDialog
import com.entourageapp.core.ui.print
import com.entourageapp.core.ui.tools.formatTwoDecimals
import com.entourageapp.features.estimates.presentation.EstimateCard
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstimateListScreen(
    projectId: Int,
    roomId: Int,
    roleId: Role,
    onAddPosition: (Int, Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: EstimateListVM = koinViewModel()
) {
    val scrollState = rememberLazyListState()
    val state by viewModel.state.collectAsState()
    val isCollapsedHeader by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 20
        }
    }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(EstimateListIntent.LoadData(projectId, roomId))
    }

    if (state.showDeleteDialog) {
        DeleteDialog(
            onDismiss = { viewModel.handleIntent(EstimateListIntent.DismissDeleteDialog) },
            onOkClick = { viewModel.handleIntent(EstimateListIntent.DeleteItem(projectId, roomId)) },
            sheetState = sheetState,
            title = "Удаление позиции",
            text = "Вы действительно хотите удалить позицию \"${state.selectedItemName}\"?",
            buttonTitle = "Удалить"
        )
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
            .clipToBounds()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedVisibility(
                visible = !isCollapsedHeader,
                enter = expandVertically(
                    animationSpec = tween(400),
                    expandFrom = Alignment.Top
                ) + fadeIn(tween(400)),
                exit = shrinkVertically(
                    animationSpec = tween(400),
                    shrinkTowards = Alignment.Top
                ) + fadeOut(tween(400))
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    ScreenTitleTwoButtons(
                        modifier = Modifier.padding(bottom = 8.dp),
                        title = if (roomId == 0) "Смета по проекту" else "Смета по комнате",
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
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        thickness = 1.dp,
                        color = EntourageBlack
                    )
                }
            }

            SimpleSearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = if (isCollapsedHeader) 4.dp else 12.dp, start = 8.dp, end = 8.dp),
                searchQuery = state.searchQuery,
                onQueryChange = { viewModel.handleIntent(EstimateListIntent.UpdateSearch(it)) },
                onCloseClick = { viewModel.handleIntent(EstimateListIntent.UpdateSearch("")) }
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
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
                        room = item.room,
                        onLongClick = {
                            if (roleId != Role.Viewer) {
                                viewModel.handleIntent(EstimateListIntent.ShowDeleteDialog(item.id, item.name))
                            }
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            if (roleId != Role.Viewer) {
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
                        onClick = { onAddPosition(projectId, roomId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TotalCard(
    string: String,
    value: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .background(EntourageBlack.copy(alpha = 0.1f))
            .innerShadow(
                shape = RoundedCornerShape(32.dp),
                shadow = Shadow(
                    radius = 16.dp,
                    spread = 4.dp,
                    color = EntourageWhite.copy(alpha = 0.2f),
                    offset = DpOffset(x = 8.dp, 6.dp)
                )
            ),
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