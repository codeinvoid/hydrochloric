package com.pkgho.hoige

import com.pkgho.hoige.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.pkgho.hoige.plugins.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureHTTP()
        configureTemplating()
        configureSockets()
        configureSerialization()
        configureSecurity()
        configureRouting()
        routing {
            get("/") {
                call.respondText("Hello, world!")
            }
        }
    }.start(wait = true)
}
