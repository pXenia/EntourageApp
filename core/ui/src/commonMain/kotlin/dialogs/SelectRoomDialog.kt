package com.entourageapp.core.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.network.dto.rooms.RoomShortDto
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.EntourageTeal

@Composable
fun SelectRoomDialog(
    rooms: List<RoomShortDto>,
    onDismiss: () -> Unit,
    onSelect: (RoomShortDto) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        shape = RoundedCornerShape(28.dp),
        containerColor = EntourageLightBlueGray,
        title = {
            Text(
                text = "Выберите помещение",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 22.sp),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                rooms.forEach { room ->
                    Surface(
                        onClick = {
                            onSelect(room)
                            onDismiss()
                        },
                        shape = RoundedCornerShape(32.dp),
                        color = EntourageTeal.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = room.title,
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                        )
                    }
                }
            }
        }
    )
}
