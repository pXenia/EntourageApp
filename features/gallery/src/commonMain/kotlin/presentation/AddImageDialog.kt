package com.entourageapp.features.gallery.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.entourageapp.core.network.dto.RoomShortDto
import com.entourageapp.features.gallery.domain.GalleryRoom
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.EntouragePeachAlpha30
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.add
import com.entourageapp.core.ui.components.Badge
import com.entourageapp.core.ui.components.CustomTextBar
import com.entourageapp.core.ui.cross
import com.entourageapp.core.ui.dialogs.SelectRoomDialog
import com.entourageapp.core.ui.tag
import org.jetbrains.compose.resources.painterResource

@Composable
fun AddImageDialog(
    imageData: GalleryState.SelectedImageData?,
    availableRooms: List<GalleryRoom>,
    initialRoomId: Int? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, Int?) -> Unit,
    launcher: () -> Unit,
) {
    var note by remember { mutableStateOf("") }
    var editedRoomId by remember { mutableStateOf(initialRoomId) }
    var showRoomDialog by remember { mutableStateOf(false) }

    if (showRoomDialog) {
        SelectRoomDialog(
            rooms = availableRooms.map { RoomShortDto(it.id, it.title) },
            onDismiss = { showRoomDialog = false },
            onSelect = { room ->
                editedRoomId = room.id
                showRoomDialog = false
            }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(EntourageLightBlueGray)
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Добавление фото",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 22.sp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (imageData != null) {
                AsyncImage(
                    model = imageData.fileBytes,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { launcher() },
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .border(1.dp, EntourageTeal, RoundedCornerShape(32.dp))
                        .clip(RoundedCornerShape(32.dp))
                        .clickable { launcher() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Выбрать фото",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                        color = EntourageTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextBar(
                label = "Заметка",
                onValueChange = { note = it },
                value = note,
                isSingleLine = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val roomName = availableRooms.find { it.id == editedRoomId }?.title
                if (roomName != null) {
                    Badge(tag, roomName)
                } else {
                    Text(
                        text = "Помещение не выбрано",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                        color = EntourageBlack.copy(alpha = 0.6f)
                    )
                }

                Surface(
                    color = EntouragePeachAlpha30,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            if (editedRoomId != null) {
                                editedRoomId = null
                            } else {
                                showRoomDialog = true
                            }
                        }
                ) {
                    Icon(
                        painter = if (editedRoomId != null) painterResource(cross) else painterResource(add),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(12.dp)
                            .size(16.dp),
                        tint = EntourageBlack,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDismiss) {
                    Text(
                        "Отменить",
                        color = EntourageTeal,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                TextButton(
                    onClick = { onConfirm(note, editedRoomId) },
                    enabled = imageData != null,
                    modifier = Modifier.background(Color.Transparent)
                ) {
                    Text(
                        "Добавить",
                        color = if (imageData != null) EntourageTeal else Color.Gray,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
                    )
                }
            }
        }
    }
}
