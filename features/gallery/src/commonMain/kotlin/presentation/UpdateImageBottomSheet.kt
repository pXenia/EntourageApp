package com.entourageapp.features.gallery.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.network.dto.ImageDto
import com.entourageapp.core.network.dto.RoomShortDto
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeachAlpha30
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.add
import com.entourageapp.core.ui.components.AccentButton
import com.entourageapp.core.ui.components.Badge
import com.entourageapp.core.ui.components.CustomTextBar
import com.entourageapp.core.ui.cross
import com.entourageapp.core.ui.delete
import com.entourageapp.core.ui.dialogs.SelectRoomDialog
import com.entourageapp.core.ui.tag
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UpdateImageBottomSheet(
    image: ImageDto,
    availableRooms: List<RoomShortDto>,
    projectId: Int,
    sheetState: SheetState,
    onIntent: (GalleryIntent) -> Unit,
    onDismissRequest: () -> Unit,
    onEditingChange: (Boolean) -> Unit = {}
) {
    var isEditing by remember { mutableStateOf(false) }
    var showRoomDialog by remember { mutableStateOf(false) }
    
    var editedNote by remember(image.id) { mutableStateOf(image.note ?: "") }
    var editedRoomId by remember(image.id) { mutableStateOf(image.roomId) }

    if (showRoomDialog) {
        SelectRoomDialog(
            rooms = availableRooms,
            onDismiss = { showRoomDialog = false },
            onSelect = { room ->
                editedRoomId = room.id
                showRoomDialog = false
            }
        )
    }

    ModalBottomSheet(
        onDismissRequest = {
            isEditing = false
            onEditingChange(false)
            onDismissRequest()
        },
        sheetState = sheetState,
        containerColor = EntourageWhite,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(EntourageBlack.copy(alpha = 0.2f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isEditing) "Редактирование" else "Детали изображения",
                    style = MaterialTheme.typography.headlineSmall,
                    color = EntourageBlack
                )

                if (!isEditing) {
                    Icon(
                        painter = painterResource(delete),
                        contentDescription = "Удалить",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                onDismissRequest()
                                onIntent(GalleryIntent.DeleteImage(projectId, image.id))
                            },
                        tint = Color.Red
                    )
                }
            }

            if (isEditing) {
                CustomTextBar(
                    value = editedNote,
                    onValueChange = { editedNote = it },
                    label = "Заметка",
                    placeholder = "Добавьте описание..."
                )

                Row(
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
                        modifier = Modifier.clip(CircleShape).clickable {
                            if (editedRoomId != null) {
                                editedRoomId = null
                            } else {
                                showRoomDialog = true
                            }
                        }
                    ) {
                        Icon(
                            modifier = Modifier.padding(8.dp).size(16.dp),
                            painter = if (editedRoomId != null) painterResource(cross) else painterResource(add),
                            contentDescription = null,
                            tint = EntourageBlack
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AccentButton(
                        modifier = Modifier.weight(1f).height(48.dp),
                        onClick = {
                            isEditing = false
                            onEditingChange(false)
                            editedNote = image.note ?: ""
                            editedRoomId = image.roomId
                        },
                        text = "Отмена",
                        containerColor = EntourageWhite,
                        contentColor = EntourageBlack
                    )
                    AccentButton(
                        modifier = Modifier.weight(1f).height(48.dp),
                        onClick = {
                            onIntent(GalleryIntent.UpdateImage(projectId, image.id, editedNote, editedRoomId))
                            isEditing = false
                            onEditingChange(false)
                        },
                        text = "Сохранить",
                        containerColor = EntourageBlack,
                        contentColor = EntourageWhite
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val note = image.note
                    if (!note.isNullOrBlank()) {
                        Text(
                            text = note,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                            color = EntourageBlack
                        )
                    } else {
                        Text(
                            text = "Заметка отсутствует",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                            color = EntourageBlack.copy(alpha = 0.5f)
                        )
                    }

                    val roomName = availableRooms.find { it.id == image.roomId }?.title
                    if (roomName != null) {
                        Badge(tag, roomName)
                    } else {
                        Text(
                            text = "Помещение не указано",
                            style = MaterialTheme.typography.bodySmall,
                            color = EntourageBlack.copy(alpha = 0.5f)
                        )
                    }
                }

                AccentButton(
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    onClick = { 
                        isEditing = true 
                        onEditingChange(true)
                    },
                    text = "Редактировать",
                    containerColor = EntourageBlack,
                    contentColor = EntourageWhite
                )
            }
        }
    }
}
