package com.survey4all

import java.util.*

class Repo {

    private val users = mutableSetOf<User>()

    private val tokens = mutableMapOf<User, String>()

    fun addUser(data: UserData, password: String): String {
        require(users.none { it.data.email == data.email }) { "User already exists" }

        val user = User("id_${users.size}", data, data.email + password)
        users.add(user)
        val token = UUID.randomUUID().toString()
        tokens[user] = token
        return token
    }
}
