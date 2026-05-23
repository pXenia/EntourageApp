package com.entourageapp.features.rooms.presentation.roomlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.entourageapp.core.navigation.Role
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.components.ScreenTitle
import com.entourageapp.core.ui.dialogs.DeleteDialog
import com.entourageapp.features.rooms.presentation.components.AddRoomCard
import com.entourageapp.features.rooms.presentation.components.RoomCard
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomListScreen(
    projectId: Int,
    roleId: Role,
    modifier: Modifier = Modifier,
    onRoomClick: (Int, Int, Role) -> Unit,
    onBackClick: () -> Unit = {},
    onAddRoomClick: (Int) -> Unit = {},
    viewModel: RoomListVM = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(RoomListIntent.LoadRooms(projectId))
    }

    if (state.showDeleteDialog) {
        DeleteDialog(
            onDismiss = { viewModel.handleIntent(RoomListIntent.DismissDeleteDialog) },
            onOkClick = { viewModel.handleIntent(RoomListIntent.DeleteRoom(projectId)) },
            sheetState = sheetState,
            title = "Удаление комнаты",
            text = "Вы действительно хотите удалить комнату \"${state.selectedRoomTitle}\"?",
            buttonTitle = "Удалить"
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ScreenTitle(
            modifier = Modifier.fillMaxWidth(),
            title = "Комнаты",
            onBackClick = onBackClick
        )

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = EntourageTeal,
                trackColor = EntourageWhite.copy(alpha = 0.6f),
            )
        }
        if (state.error != null) {
            Text(
                text = state.error ?: " ",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.rooms) { room ->
                RoomCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = room.icon,
                    roomTitle = room.title,
                    square = room.square,
                    onCardClick = { onRoomClick(projectId, room.id, roleId) },
                    onCardLongClick = {
                        if (roleId != Role.Viewer) {
                            viewModel.handleIntent(RoomListIntent.ShowDeleteDialog(room.id, room.title))
                        }
                    }
                )
            }

            if (roleId != Role.Viewer) {
                item {
                    AddRoomCard(onCardClick = { onAddRoomClick(projectId) })
                }
            }
        }
    }
}