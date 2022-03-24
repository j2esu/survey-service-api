package com.survey4all

import com.survey4all.models.User
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.*
import io.ktor.util.pipeline.*

private val userKey = AttributeKey<User>("user")

fun Application.configureAuth(repo: Repo) {
    intercept(ApplicationCallPipeline.Call) {
        val path = call.request.path()
        val needAuth = Route.values().filter { !it.auth }.none { it.path == path }
        if (needAuth) {
            val token = call.request.authorization()?.removePrefix("Bearer")?.trim()
            val user = token?.let { repo.getUser(it) }
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "No or invalid token")
            } else {
                call.attributes.put(userKey, user)
            }
        }
    }
}

val PipelineContext<Unit, ApplicationCall>.currentUser: User
    get() = call.attributes.getOrNull(userKey) ?: error("No user")
