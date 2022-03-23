package com.survey4all

import java.util.*

class Repo {

    private val users = mutableSetOf<User>()

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

    private fun updateUser(user: User): User {
        users.removeIf { it.id == user.id }
        users.add(user)
        return user
    }
}

private fun token() = UUID.randomUUID().toString()
private fun secret(email: String, password: String) = email + password
