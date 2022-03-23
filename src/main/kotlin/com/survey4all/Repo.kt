package com.survey4all

import java.util.*

class Repo {

    private val users = mutableSetOf<User>()

    private val tokens = mutableMapOf<User, String>()

    fun addUser(data: UserData, password: String): String {
        require(users.none { it.data.email == data.email }) { "User already exists" }

        val user = User("id_${users.size}", data, secret(data.email, password))
        users.add(user)
        return updateToken(user)
    }

    private fun updateToken(user: User): String {
        val token = UUID.randomUUID().toString()
        tokens[user] = token
        return token
    }

    fun updateToken(email: String, password: String): String {
        val user = users.find { it.data.email == email }
        if (user?.secret == secret(email, password)) {
            return updateToken(user)
        }
        error("Invalid email or password")
    }
}

private fun secret(email: String, password: String) = email + password
