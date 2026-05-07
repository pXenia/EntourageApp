package com.entourageapp.features.rooms.presentation.createroom

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.core.network.api.RoomsApi
import com.entourageapp.core.network.dto.OffsetAddDto
import com.entourageapp.core.network.dto.RoomAddDto
import com.entourageapp.core.network.dto.RoomFullUpdateDto
import com.entourageapp.core.network.dto.WallAddDto
import com.entourageapp.features.rooms.presentation.components.drawplan.polygonAreaM2
import com.entourageapp.features.rooms.presentation.components.drawplan.wallLenM
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateRoomVM(
    private val roomsApi: RoomsApi
) : ViewModel() {

    private val _state = MutableStateFlow(CreateRoomState())
    val state: StateFlow<CreateRoomState> = _state.asStateFlow()

    private val _planState = MutableStateFlow(RoomDrawerState())
    val planState: StateFlow<RoomDrawerState> = _planState

    fun handleIntent(intent: CreateRoomIntent) {
        when (intent) {
            is CreateRoomIntent.OnTitleChanged ->
                _state.update { it.copy(title = intent.value, error = null) }
            is CreateRoomIntent.OnDescriptionChanged ->
                _state.update { it.copy(description = intent.value) }
            is CreateRoomIntent.OnCeilingHeightChanged ->
                _state.update { it.copy(ceilingHeight = intent.value) }
            is CreateRoomIntent.OnRoomTypeSelected ->
                _state.update { it.copy(selectedRoomType = intent.roomType) }
            is CreateRoomIntent.OnPlanSaved ->
                _state.update {
                    it.copy(
                        points = intent.points,
                        walls = intent.walls,
                        square = intent.square
                    )
                }
            is CreateRoomIntent.LoadRoomTypes -> loadRoomTypes(intent.projectId)
            is CreateRoomIntent.LoadRoom -> loadRoom(intent.projectId, intent.roomId)
            is CreateRoomIntent.Submit -> submit(intent.projectId)
        }
    }

    fun handlePlanIntent(intent: RoomDrawerIntent) {
        when (intent) {
            is RoomDrawerIntent.AddPoint -> addPoint(intent.offset)
            is RoomDrawerIntent.MovePoint -> movePoint(intent.index, intent.offset)
            is RoomDrawerIntent.RemovePoint -> removePoint(intent.index)
            is RoomDrawerIntent.SetMode -> _planState.update { it.copy(mode = intent.mode) }
            is RoomDrawerIntent.ToggleSnap -> _planState.update { it.copy(snapEnabled = intent.enabled) }
            is RoomDrawerIntent.UpdateCellSize -> _planState.update { s ->
                val updated = s.copy(cellSizePx = intent.sizePx)
                updated.copy(walls = calcWalls(updated), square = calcSquare(updated))
            }
            is RoomDrawerIntent.ClearAll -> _planState.update {
                it.copy(points = emptyList(), walls = emptyList(), square = 0f)
            }
            is RoomDrawerIntent.DragEnd -> _planState.update { it.copy(dragIndex = -1) }
        }
    }

    private fun loadRoomTypes(projectId: Int) {
        if (_state.value.roomTypes.isNotEmpty()) return
        viewModelScope.launch {
            runCatching { roomsApi.getRoomTypes(projectId) }
                .onSuccess { types -> _state.update { it.copy(roomTypes = types) } }
                .onFailure { e -> _state.update { it.copy(error = e.message) } }
        }
    }

    private fun loadRoom(projectId: Int, roomId: Int) {
        if (_state.value.roomId == roomId && _state.value.title.isNotEmpty()) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, roomId = roomId) }
            runCatching {
                val typesDef = async { roomsApi.getRoomTypes(projectId) }
                val detailDef = async { roomsApi.getRoomById(projectId, roomId) }
                val paramsDef = async { roomsApi.getParams(projectId, roomId) }
                val offsetsDef = async { roomsApi.getOffsets(projectId, roomId) }

                val types = typesDef.await()
                val detail = detailDef.await()
                val params = paramsDef.await()
                val offsets = offsetsDef.await()

                val selectedType = types.find { it.id == detail.typeCode }
                val points = offsets.map { Offset(it.x, it.y) }
                
                _state.update { it.copy(
                    title = detail.title,
                    roomTypes = types,
                    selectedRoomType = selectedType,
                    description = detail.description ?: "",
                    ceilingHeight = detail.ceilingHeight?.toString() ?: "",
                    square = detail.square ?: 0f,
                    walls = params.walls.mapIndexed { index, wall -> 
                        WallInfo(index = index + 1, lengthM = wall.length)
                    },
                    points = points
                ) }

                _planState.update { it.copy(
                    points = points,
                    walls = params.walls.mapIndexed { index, wall -> 
                        WallInfo(index = index + 1, lengthM = wall.length)
                    },
                    square = detail.square ?: 0f
                ) }
            }.onFailure { e ->
                _state.update { it.copy(error = e.message) }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun submit(projectId: Int) {
        val s = _state.value
        if (s.title.isBlank()) {
            _state.update { it.copy(error = "Введите название") }
            return
        }
        if (s.walls.isEmpty()) {
            _state.update { it.copy(error = "Нарисуйте план комнаты") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching {
                if (s.roomId == null) {
                    val created = roomsApi.createRoom(
                        projectId = projectId,
                        room = RoomAddDto(
                            title = s.title,
                            typeCode = s.selectedRoomType?.id,
                            description = s.description.ifBlank { null },
                            square = s.square,
                            ceilingHeight = s.ceilingHeight.toFloatOrNull()
                        )
                    )
                    val roomId = created.roomId
                    s.walls.forEach { wall ->
                        roomsApi.addWall(projectId, roomId, WallAddDto(length = wall.lengthM))
                    }
                    s.points.forEach { point ->
                        roomsApi.addOffset(projectId, roomId, OffsetAddDto(x = point.x, y = point.y))
                    }
                } else {
                    roomsApi.updateRoomFull(
                        projectId = projectId,
                        roomId = s.roomId,
                        room = RoomFullUpdateDto(
                            title = s.title,
                            typeCode = s.selectedRoomType?.id,
                            description = s.description.ifBlank { null },
                            square = s.square,
                            ceilingHeight = s.ceilingHeight.toFloatOrNull(),
                            walls = s.walls.map { WallAddDto(length = it.lengthM) },
                            offsets = s.points.map { OffsetAddDto(x = it.x, y = it.y) }
                        )
                    )
                }
            }
                .onSuccess { _state.update { it.copy(isSuccess = true) } }
                .onFailure { e -> _state.update { it.copy(error = e.message) } }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun addPoint(point: Offset) {
        _planState.update { s ->
            val updated = s.copy(points = s.points + point)
            updated.copy(walls = calcWalls(updated), square = calcSquare(updated))
        }
    }

    private fun movePoint(index: Int, newOffset: Offset) {
        _planState.update { s ->
            val newList = s.points.toMutableList()
            if (index in newList.indices) newList[index] = newOffset
            val updated = s.copy(points = newList, dragIndex = index)
            updated.copy(walls = calcWalls(updated), square = calcSquare(updated))
        }
    }

    private fun removePoint(index: Int) {
        _planState.update { s ->
            val newList = s.points.toMutableList()
            if (index in newList.indices) newList.removeAt(index)
            val updated = s.copy(points = newList)
            updated.copy(walls = calcWalls(updated), square = calcSquare(updated))
        }
    }

    private fun calcWalls(s: RoomDrawerState): List<WallInfo> {
        if (s.points.size < 2 || s.cellSizePx == 0f) return emptyList()
        val edges = if (s.points.size >= 3) s.points.size else 1
        return (0 until edges).map { i ->
            val a = s.points[i]
            val b = s.points[(i + 1) % s.points.size]
            WallInfo(index = i + 1, lengthM = wallLenM(a, b, s.cellSizePx))
        }
    }

    private fun calcSquare(s: RoomDrawerState): Float {
        if (s.cellSizePx == 0f) return 0f
        return polygonAreaM2(s.points, s.cellSizePx)
    }
}