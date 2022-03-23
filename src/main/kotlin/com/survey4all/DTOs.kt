package com.survey4all

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val password: String,
    val data: UserData
)

@Serializable
data class AuthResponse(
    val token: String
)