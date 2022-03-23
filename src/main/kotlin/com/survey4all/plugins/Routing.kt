package com.survey4all.plugins

import com.survey4all.AuthRequest
import com.survey4all.AuthResponse
import com.survey4all.Repo
import com.survey4all.SignUpRequest
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting(repo: Repo) {

    routing {
        post("/signup") {
            val request = call.receive<SignUpRequest>()
            val token = repo.addUser(request.data, request.password)
            call.respond(AuthResponse(token))
        }
        post("/signin") {
            val request = call.receive<AuthRequest>()
            val token = repo.updateToken(request.email, request.password)
            call.respond(AuthResponse(token))
        }
        install(StatusPages) {
            exception<AuthenticationException> {
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> {
                call.respond(HttpStatusCode.Forbidden)
            }
            exception<Exception> { cause ->
                call.respond(HttpStatusCode.InternalServerError, cause.message.toString())
            }
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
