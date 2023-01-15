package com.pkgho

import com.pkgho.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.sentry.Sentry

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
fun Application.module() {
    install(CORS){
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }
    configureRouting()
    configureSerialization()
    Sentry.init { options ->
        options.dsn = "https://69d00044d21d443f8435effbab652886@o4504178838077440.ingest.sentry.io/4504178841354241"
        // Set tracesSampleRate to 1.0 to capture 100% of transactions for performance monitoring.
        // We recommend adjusting this value in production.
        options.tracesSampleRate = 1.0
        // When first trying Sentry it's good to see what the SDK is doing:
        options.isDebug = false
    }
}

