package com.entourageapp.features.rooms.presentation.roomlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.components.ScreenTitle
import com.entourageapp.features.rooms.presentation.components.AddRoomCard
import com.entourageapp.features.rooms.presentation.components.RoomCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RoomListScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onAddRoomClick: () -> Unit = {},
    viewModel: RoomsVM = koinViewModel()
) {
    val state by viewModel.state.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.handleIntent(RoomListIntent.LoadRooms(1))
    }

    Column(
        modifier = modifier.fillMaxSize(),
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
                .clip(RoundedCornerShape(32.dp)),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.rooms) { room ->
                RoomCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = room.icon,
                    roomTitle = room.title,
                    square = room.square
                )
            }

            item {
                AddRoomCard(onCardClick = onAddRoomClick)
            }
        }
    }
}