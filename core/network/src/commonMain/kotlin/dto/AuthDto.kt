package com.entourageapp.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(val email: String, val password: String)

@Serializable
data class RegisterRequestDto(val name: String, val email: String, val password: String)

@Serializable
data class MessageDto(val message: String)