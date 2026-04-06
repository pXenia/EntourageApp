package com.entourageapp.features.rooms.domain

import com.entourageapp.core.network.dto.RoomDto
import org.jetbrains.compose.resources.DrawableResource

data class RoomCard(
    val id: Int,
    val title: String,
    val square: String,
    val icon: DrawableResource
)

fun RoomDto.toRoomCard() = RoomCard (
    id = id,
    title = title,
    square = square.toString(),
    icon = getRoomIcon(type)
)