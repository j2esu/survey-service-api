package com.survey4all.plugins

import com.survey4all.AuthRequest
import com.survey4all.AuthResponse
import com.survey4all.Repo
import com.survey4all.SignUpRequest
import com.survey4all.plugins.Route.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

enum class Route(val path: String, val auth: Boolean = true) {
    SignIn("/signin", false),
    SignUp("/signup", false),
    Profile("/profile")
}

fun Application.configureRouting(repo: Repo) {

    routing {

        post(SignUp.path) {
            val request = call.receive<SignUpRequest>()
            val token = repo.addUser(request.data, request.password)
            call.respond(AuthResponse(token))
        }

        post(SignIn.path) {
            val request = call.receive<AuthRequest>()
            val token = repo.getToken(request.email, request.password)
            call.respond(AuthResponse(token))
        }

        get(Profile.path) {
            call.respond(currentUser.data)
        }

        install(StatusPages) {
            exception<Exception> { cause ->
                call.respond(HttpStatusCode.InternalServerError, cause.message.toString())
            }
        }
    }
}
