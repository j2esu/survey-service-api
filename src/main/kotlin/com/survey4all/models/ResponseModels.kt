package com.survey4all.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)

@Serializable
data class SurveyResponse(
    val id: String,
    val data: SurveyData,
    val yes: Int,
    val no: Int
)
