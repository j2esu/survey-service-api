package com.survey4all

import com.survey4all.models.*
import kotlin.random.Random

fun defaultUsers() = arrayOf(user_1, user_2, user_3)

fun defaultSurveys() = Array(100) {
    when (it % 10) {
        9 -> user_1.createDefaultSurvey(it, upBy = listOf(user_2, user_3))
        8 -> user_1.createDefaultSurvey(it, downBy = listOf(user_2, user_3))
        7 -> user_1.createDefaultSurvey(it, upBy = listOf(user_2), downBy = listOf(user_3))
        6 -> user_2.createDefaultSurvey(it, upBy = listOf(user_1, user_3))
        5 -> user_2.createDefaultSurvey(it, downBy = listOf(user_1, user_3))
        4 -> user_2.createDefaultSurvey(it, upBy = listOf(user_1), downBy = listOf(user_3))
        3 -> user_3.createDefaultSurvey(it, upBy = listOf(user_1, user_2))
        2 -> user_3.createDefaultSurvey(it, downBy = listOf(user_1, user_2))
        1 -> user_3.createDefaultSurvey(it, upBy = listOf(user_1), downBy = listOf(user_2))
        else -> defaultUsers().random().createDefaultSurvey(it)
    }
}

private val user_1 = createDefaultUser(1)
private val user_2 = createDefaultUser(2)
private val user_3 = createDefaultUser(3)

private fun createDefaultUser(id: Int) = User(
    "default_user_$id",
    "default_token_$id",
    UserData(
        "User $id",
        "user$id@gmail.com",
        Random.nextInt(18, 60),
        Sex.values().random(),
        listOf("ru", "us", "it", "fr", "es").random()
    ),
    "password"
)

private fun User.createDefaultSurvey(
    id: Int,
    upBy: List<User> = emptyList(),
    downBy: List<User> = emptyList()
) = Survey(
    "default_survey_$id",
    this.id,
    SurveyData(
        "Title $id",
        "Description $id"
    ),
    upVotes = upBy.map { it.id }.toSet(),
    downVotes = downBy.map { it.id }.toSet()
)
