package com.survey4all

import com.survey4all.models.*
import java.util.*

class Repo {

    private val users = mutableSetOf(*defaultUsers())
    private val surveys = mutableListOf(*defaultSurveys())

    fun addUser(data: UserData, password: String): User {
        require(users.none { it.data.email == data.email }) { "User already exists" }
        val user = User(users.size.toString(), token(), data, password)
        users.add(user)
        return user
    }

    fun updateToken(email: String, password: String): String {
        val user = users.find { it.data.email == email }
        if (user?.password == password) {
            return updateUser(user.copy(token = token())).token
        }
        error("Invalid email or password")
    }

    fun getUser(token: String): User? {
        return users.find { it.token == token }
    }

    fun updateUser(
        user: User,
        name: String? = null,
        age: Int? = null,
        sex: Sex? = null,
        countryCode: String? = null
    ): User {
        val newUser = user.copy(
            data = user.data.copy(
                name = name ?: user.data.name,
                age = age ?: user.data.age,
                sex = sex ?: user.data.sex,
                countryCode = countryCode ?: user.data.countryCode
            )
        )
        return updateUser(newUser)
    }

    fun addSurvey(user: User, data: SurveyData): Survey {
        val id = System.nanoTime().toString()
        val survey = Survey(id, user.id, data)
        surveys.add(survey)
        return survey
    }

    fun getFeed(user: User, count: Int, startAfter: String?): List<Survey>? {
        val userSurveys = surveys.reversed().filterNot { it.ownerId == user.id }
        return when {
            startAfter == null -> userSurveys.take(count)
            userSurveys.none { it.id == startAfter } -> null
            else -> userSurveys.dropWhile { it.id != startAfter }.drop(1).take(count)
        }
    }

    fun getSurvey(id: String): Survey? {
        return surveys.find { it.id == id }
    }

    fun getUserSurveys(user: User): List<Survey> {
        return surveys.filter { it.ownerId == user.id }
    }

    fun getUserVotes(user: User): UserVotesData {
        val up = surveys.filter { it.upVotes.contains(user.id) }.map { it.id }
        val down = surveys.filter { it.downVotes.contains(user.id) }.map { it.id }
        return UserVotesData(up, down)
    }

    fun vote(user: User, surveyId: String, vote: Vote): Survey? {
        val survey = getSurvey(surveyId) ?: return null
        val newSurvey = when (vote) {
            Vote.Up -> survey.copy(upVotes = survey.upVotes + user.id, downVotes = survey.downVotes - user.id)
            Vote.Down -> survey.copy(upVotes = survey.upVotes - user.id, downVotes = survey.downVotes + user.id)
            Vote.None -> survey.copy(upVotes = survey.upVotes - user.id, downVotes = survey.downVotes - user.id)
        }
        return updateSurvey(newSurvey)
    }

    fun updateSurvey(user: User, surveyId: String, title: String?, desc: String?): Survey? {
        val survey = getSurvey(surveyId) ?: return null
        require(user.id == survey.ownerId) { "User is not an owner" }
        val newData = SurveyData(
            title ?: survey.data.title,
            desc ?: survey.data.desc
        )
        return updateSurvey(survey.copy(data = newData))
    }

    private fun updateUser(user: User): User {
        users.removeIf { it.id == user.id }
        users.add(user)
        return user
    }

    private fun updateSurvey(survey: Survey): Survey? {
        val index = surveys.indexOfFirst { it.id == survey.id }
        return if (index != -1) {
            surveys.removeAt(index)
            surveys.add(index, survey)
            return survey
        } else {
            null
        }
    }
}

private fun token() = UUID.randomUUID().toString()
