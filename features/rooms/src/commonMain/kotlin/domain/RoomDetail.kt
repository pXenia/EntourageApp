package com.entourageapp.features.rooms.domain

import com.entourageapp.core.network.dto.rooms.RoomDetailDto

data class RoomDetail(
    val id: Int,
    val title: String,
    val typeCode: Int?,
    val description: String?,
    val square: Float?,
    val ceilingHeight: Float?,
    val furnitureTotal: Float,
    val componentsTotal: Float,
    val workTotal: Float,
    val roomTotal: Float,
    val projectSharePercent: Float,
    val estimateItemsCount: Int,
    val photoCount: Int,
    val stagesCount: Int,
    val tasksCount: Int
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
    estimateItemsCount = estimateItemsCount,
    photoCount = photoCount,
    stagesCount = stagesCount,
    tasksCount = tasksCount
)