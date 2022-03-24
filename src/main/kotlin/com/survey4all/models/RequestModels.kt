package com.survey4all.models

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val password: String,
    val data: UserData
)

@Serializable
data class AuthRequest(
    val email: String,
    val password: String
)

@Serializable
data class EditProfileRequest(
    val name: String? = null,
    val age: Int? = null,
    val sex: Sex? = null,
    val countryCode: String? = null
)

@Serializable
data class CreateSurveyRequest(
    val data: SurveyData
)

@Serializable
data class VoteRequest(
    val surveyId: String,
    val vote: Vote
)

@Serializable
data class EditSurveyRequest(
    val title: String? = null,
    val desc: String? = null
)
