package com.entourageapp.features.rooms.domain

import com.entourageapp.core.network.dto.RoomDetailDto

data class RoomDetail(
    val id: Int,
    val title: String,
    val typeCode: String?,
    val description: String?,
    val square: Float?,
    val ceilingHeight: Float?,
    val furnitureTotal: Float,
    val componentsTotal: Float,
    val workTotal: Float,
    val roomTotal: Float,
    val projectSharePercent: Float,
)

fun RoomDetailDto.toRoomDetail() = RoomDetail(
    id = id,
    title = title,
    typeCode = typeCode,
    description = description,
    square = square,
    ceilingHeight = ceilingHeight,
    furnitureTotal = furnitureTotal,
    componentsTotal = componentsTotal,
    workTotal = workTotal,
    roomTotal = roomTotal,
    projectSharePercent = projectSharePercent,
)