package com.entourageapp.features.rooms.domain

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

fun getRoomIcon(code: Int?): DrawableResource {
    return when (code) {
        1 -> kitchen
        2 -> kidsroom
        3 -> bathroom
        4 -> bedroom
        5 -> livingroom
        6 -> dressingroom
        7 -> techroom
        8 -> hallway
        else -> room
    }
}