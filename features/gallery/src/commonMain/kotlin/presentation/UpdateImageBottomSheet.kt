package com.entourageapp.features.gallery.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.network.dto.ImageDto
import com.entourageapp.core.network.dto.RoomShortDto
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.EntouragePeachAlpha30
import com.entourageapp.core.ui.EntourageRed
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.add
import com.entourageapp.core.ui.components.Badge
import com.entourageapp.core.ui.components.CustomTextBar
import com.entourageapp.core.ui.cross
import com.entourageapp.core.ui.delete
import com.entourageapp.core.ui.dialogs.SelectRoomDialog
import com.entourageapp.core.ui.done
import com.entourageapp.core.ui.edit
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
        tonalElevation = 2.dp,
        containerColor = EntourageLightBlueGray,
        dragHandle = {},
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top
            ) {
                if (!isEditing) {
                    Icon(
                        painter = painterResource(edit),
                        contentDescription = "Редактировать",
                        modifier = Modifier
                            .clip(CircleShape)
                            .innerShadow(
                                shape = RoundedCornerShape(20.dp),
                                shadow = Shadow(
                                    radius = 5.dp,
                                    spread = 5.dp,
                                    color = EntourageWhite.copy(alpha = 0.5f),
                                    offset = DpOffset(x = 0.dp, 0.dp)
                                )
                            )
                            .clickable {
                                isEditing = true
                                onEditingChange(true)
                            }
                            .padding(12.dp)
                            .size(24.dp),
                    )
                    Spacer(Modifier.width(12.dp))
                    Icon(
                        painter = painterResource(delete),
                        contentDescription = "Удалить",
                        modifier = Modifier
                            .clip(CircleShape)
                            .innerShadow(
                                shape = CircleShape,
                                shadow = Shadow(
                                    radius = 5.dp,
                                    spread = 5.dp,
                                    color = EntourageWhite.copy(alpha = 0.5f),
                                    offset = DpOffset(x = 0.dp, 0.dp)
                                )
                            )
                            .clickable {
                                onDismissRequest()
                                onIntent(GalleryIntent.DeleteImage(projectId, image.id))
                            }
                            .padding(12.dp)
                            .size(24.dp),
                        tint = EntourageRed
                    )
                } else {
                    Icon(
                        painter = painterResource(cross),
                        contentDescription = "Отмена",
                        modifier = Modifier
                            .clip(CircleShape)
                            .innerShadow(
                                shape = CircleShape,
                                shadow = Shadow(
                                    radius = 5.dp,
                                    spread = 5.dp,
                                    color = EntourageWhite.copy(alpha = 0.5f),
                                    offset = DpOffset(x = 0.dp, 0.dp)
                                )
                            )
                            .clickable {
                                isEditing = false
                                onEditingChange(false)
                                editedNote = image.note ?: ""
                                editedRoomId = image.roomId
                            }
                            .padding(8.dp)
                            .size(24.dp),
                    )
                    Spacer(Modifier.width(12.dp))
                    Icon(
                        painter = painterResource(done),
                        contentDescription = "Сохранить",
                        modifier = Modifier
                            .clip(CircleShape)
                            .innerShadow(
                                shape = RoundedCornerShape(20.dp),
                                shadow = Shadow(
                                    radius = 5.dp,
                                    spread = 5.dp,
                                    color = EntourageWhite.copy(alpha = 0.5f),
                                    offset = DpOffset(x = 0.dp, 0.dp)
                                )
                            )
                            .clickable {
                                onIntent(
                                    GalleryIntent.UpdateImage(
                                        projectId,
                                        image.id,
                                        editedNote,
                                        editedRoomId
                                    )
                                )
                                isEditing = false
                                onEditingChange(false)
                            }
                            .padding(8.dp)
                            .size(24.dp),
                        tint = EntourageTeal
                    )
                }
            }

            if (isEditing) {
                CustomTextBar(
                    value = editedNote,
                    onValueChange = { editedNote = it },
                    label = "Заметка",
                    placeholder = "Добавьте описание...",
                    isSingleLine = false
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
                            painter = if (editedRoomId != null) painterResource(cross) else painterResource(
                                add
                            ),
                            contentDescription = null,
                            tint = EntourageBlack
                        )
                    }
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
            }
        }
    }
}
