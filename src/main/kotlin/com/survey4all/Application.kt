package com.survey4all

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val repo = Repo()
    embeddedServer(Netty, port = 8080, host = "localhost") {
        configureAuth(repo)
        configureRouting(repo)
        install(ContentNegotiation) {
            json()
        }
    }.start(wait = true)
}
