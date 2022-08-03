package com.pkgho.plugins

import com.pkgho.routes.noticeRouting
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        noticeRouting()
    }
}
