package com.survey4all

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)
