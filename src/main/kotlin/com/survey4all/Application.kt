package com.survey4all

import com.survey4all.plugins.configureRouting
import com.survey4all.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost") {
        configureRouting()
        configureSerialization()
//        configureSecurity()
    }.start(wait = true)
}
