package com.survey4all

import com.survey4all.plugins.configureRouting
import com.survey4all.plugins.configureSecurity
import com.survey4all.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val repo = Repo()
    embeddedServer(Netty, port = 8080, host = "localhost") {
        configureSecurity(repo)
        configureRouting(repo)
        configureSerialization()
    }.start(wait = true)
}
