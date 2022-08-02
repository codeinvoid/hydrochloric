package com.pkgho.hoige

import com.pkgho.hoige.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.pkgho.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureHTTP()
        configureTemplating()
        configureSockets()
        configureSerialization()
        configureSecurity()
        configureRouting()
    }.start(wait = true)
}
