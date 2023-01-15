package com.pkgho.plugins

import io.ktor.server.auth.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureSecurity() {
    install(Authentication){
        bearer("auth-bearer") {
            realm = "Access to the '/' path"
            authenticate { tokenCredential ->
                if (tokenCredential.token == "abc123") {
                    UserIdPrincipal("jetbrains")
                } else {
                    null
                }
            }
        }
    }


    routing {
        authenticate("auth-bearer") {
            get("/v1/players") {}
            post("/v1/players/{uuid?}") {}
            put("/v1/players/{uuid}") {}
            put("/v1/players/{uuid}/banned") {}
            put("/v1/players/{uuid}/whitelist") {}
            put("/v1/players/{uuid}/integration") {}
            delete("/v1/players/{uuid}") {}
        }
    }
}
