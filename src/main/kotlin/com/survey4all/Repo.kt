package com.survey4all

import java.util.*

class Repo {

    private val users = mutableSetOf<User>()
    private val surveys = mutableListOf<Survey>()

    fun addUser(data: UserData, password: String): User {
        require(users.none { it.data.email == data.email }) { "User already exists" }
        val user = User("id_${users.size}", token(), data, secret(data.email, password))
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

    fun editProfile(
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
        val survey = Survey(id, user.id, data, emptyList(), emptyList())
        surveys.add(survey)
        return survey
    }

    fun getSurveys(count: Int, startAfter: String?): List<Survey> {
        return if (startAfter == null || surveys.none { it.id == startAfter }) {
            surveys.reversed().take(count)
        } else {
            surveys.reversed().dropWhile { it.id != startAfter }.drop(1).take(count)
        }
    }

    private fun updateUser(user: User): User {
        users.removeIf { it.id == user.id }
        users.add(user)
        return user
    }
}

private fun token() = UUID.randomUUID().toString()
private fun secret(email: String, password: String) = email + password
