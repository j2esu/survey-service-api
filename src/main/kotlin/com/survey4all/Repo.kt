package com.survey4all

import com.survey4all.models.*
import java.util.*

class Repo {

    private val users = mutableSetOf(
        User(
            "0",
            "token_for_admin",
            UserData(
                "Admin",
                "admin@surveys.com",
                30,
                Sex.Male,
                "ru"
            ),
            secret("admin@surveys.com", "password")
        )
    )

    private val surveys = mutableListOf<Survey>()

    fun addUser(data: UserData, password: String): User {
        require(users.none { it.data.email == data.email }) { "User already exists" }
        val user = User(users.size.toString(), token(), data, secret(data.email, password))
        users.add(user)
        return user
    }

    fun updateToken(email: String, password: String): String {
        val user = users.find { it.data.email == email }
        if (user?.secret == secret(email, password)) {
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

    fun getSurveys(count: Int, startAfter: String?): List<Survey> {
        return if (startAfter == null) {
            surveys.reversed().take(count)
        } else {
            surveys.reversed().dropWhile { it.id != startAfter }.drop(1).take(count)
        }
    }

    fun getUserSurveys(user: User): List<Survey> {
        return surveys.filter { it.ownerId == user.id }
    }

    private fun updateUser(user: User): User {
        users.removeIf { it.id == user.id }
        users.add(user)
        return user
    }
}

private fun token() = UUID.randomUUID().toString()
private fun secret(email: String, password: String) = email + password
