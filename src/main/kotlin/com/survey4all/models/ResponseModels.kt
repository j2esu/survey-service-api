package com.survey4all.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val userData: UserData,
    val userVotesData: UserVotesData,
    val userSurveys: List<Survey>
)
