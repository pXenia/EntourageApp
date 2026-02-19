package presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.entourageapp.core.ui.bathroom
import com.entourageapp.core.ui.bedroom
import com.entourageapp.core.ui.components.ScreenTitle
import com.entourageapp.core.ui.dressingroom
import com.entourageapp.core.ui.hallway
import com.entourageapp.core.ui.kidsroom
import com.entourageapp.core.ui.kitchen
import com.entourageapp.core.ui.techroom
import presentation.components.AddRoomCard
import presentation.components.RoomCard

@Composable
fun RoomListScreen (
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onAddRoomClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ScreenTitle(
            modifier = Modifier.fillMaxWidth(),
            title = "Комнаты",
            onBackClick = onBackClick
        )
        LazyColumn(
            modifier = Modifier.clip(RoundedCornerShape(32.dp)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                RoomCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = kitchen,
                    roomTitle = "Кухня",
                    square = "12,0 кв.м"
                )
            }
            item {
                RoomCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = bedroom,
                    roomTitle = "Спальня",
                    square = "8,0 кв.м"
                )
            }
            item {
                RoomCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = kidsroom,
                    roomTitle = "Детская",
                    square = "16,2 кв.м"
                )
            }
            item {
                RoomCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = bathroom,
                    roomTitle = "Ванная",
                    square = "16,2 кв.м"
                )
            }
            item {
                RoomCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = dressingroom,
                    roomTitle = "Гардероб",
                    square = "9,0 кв.м"
                )
            }
            item {
                RoomCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = hallway,
                    roomTitle = "Коридор",
                    square = "8,0 кв.м"
                )
            }
            item {
                RoomCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = techroom,
                    roomTitle = "Тех помещение",
                    square = "4,0 кв.м"
                )
            }
            item {
                AddRoomCard {  }
            }
            item {
                PaddingValues(16.dp)
            }
        }
    }
}