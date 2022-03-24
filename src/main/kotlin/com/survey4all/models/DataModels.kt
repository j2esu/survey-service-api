package com.survey4all.models

import kotlinx.serialization.Serializable

@Serializable
data class Survey(
    val id: String,
    val ownerId: String,
    val data: SurveyData,
    val upVotes: Set<String> = emptySet(),
    val downVotes: Set<String> = emptySet()
)

@Serializable
data class SurveyData(
    val title: String,
    val desc: String
)

enum class Sex { Male, Female }

enum class Vote { Up, Down, None }

data class User(
    val id: String,
    val token: String,
    val data: UserData,
    val secret: String
)

@Serializable
data class UserVotesData(
    val up: List<String> = emptyList(),
    val down: List<String> = emptyList()
)

@Serializable
data class UserData(
    val name: String,
    val email: String,
    val age: Int,
    val sex: Sex,
    val countryCode: String
)
