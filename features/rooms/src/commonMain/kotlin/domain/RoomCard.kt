package com.entourageapp.features.rooms.domain

import com.entourageapp.core.network.dto.RoomDto
import com.entourageapp.core.ui.bathroom
import com.entourageapp.core.ui.bedroom
import com.entourageapp.core.ui.dressingroom
import com.entourageapp.core.ui.hallway
import com.entourageapp.core.ui.kidsroom
import com.entourageapp.core.ui.kitchen
import com.entourageapp.core.ui.livingroom
import com.entourageapp.core.ui.room
import com.entourageapp.core.ui.techroom
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

fun getRoomIcon(code: String): DrawableResource {
    return when (code) {
        "kitchen" -> kitchen
        "kidsroom" -> kidsroom
        "bathroom" -> bathroom
        "bedroom" -> bedroom
        "livingroom" -> livingroom
        "dressingroom" -> dressingroom
        "techroom" -> techroom
        "hallway" -> hallway
        else -> room
    }
}