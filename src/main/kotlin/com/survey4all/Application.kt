package com.survey4all

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val repo = Repo()
    val envPort = System.getenv("PORT")?.toIntOrNull()
    embeddedServer(
        Netty,
        port = envPort ?: 8080,
        host = if (envPort == null) "localhost" else "0.0.0.0"
    ) {
        configureAuth(repo)
        configureRouting(repo)
        install(ContentNegotiation) {
            json()
        }
    }.start(wait = true)
}
