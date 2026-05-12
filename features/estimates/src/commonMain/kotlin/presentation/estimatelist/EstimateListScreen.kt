package com.entourageapp.features.estimates.presentation.estimatelist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import com.entourageapp.core.ui.components.SearchBar
import com.entourageapp.core.ui.dialogs.DeleteDialog
import com.entourageapp.core.ui.filter
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
        derivedStateOf { scrollState.firstVisibleItemScrollOffset > 0 }
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
            .animateContentSize(
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
                    title = if (roomId == 0) "Смета по проекту" else "Смета по комнате" ,
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
                AddRoundButton(
                    onClick = { onAddPosition(projectId, roomId) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 8.dp),
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