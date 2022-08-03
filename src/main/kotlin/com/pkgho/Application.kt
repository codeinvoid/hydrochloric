package com.pkgho

import com.pkgho.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
fun Application.module() {
    install(CORS){
        allowHost("127.0.0.1:3000")
        allowHeader(HttpHeaders.ContentType)
    }
    configureRouting()
    configureSerialization()
}

