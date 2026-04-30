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