package com.entourageapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform