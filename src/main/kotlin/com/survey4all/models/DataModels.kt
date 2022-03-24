package com.survey4all.models

import kotlinx.serialization.Serializable

@Serializable
data class Survey(
    val id: String,
    val ownerId: String,
    val data: SurveyData,
    val upVotes: List<String>,
    val downVotes: List<String>
)

@Serializable
data class SurveyData(
    val title: String,
    val desc: String
)

enum class Sex { Male, Female }

data class User(
    val id: String,
    val token: String,
    val data: UserData,
    val secret: String
)

@Serializable
data class UserData(
    val name: String,
    val email: String,
    val age: Int,
    val sex: Sex,
    val countryCode: String
)
