package com.pkgho.plugins

import com.pkgho.routes.player
import com.pkgho.routes.srvcheck
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureRouting() {
    install(Authentication){
        bearer("auth-bearer") {
            realm = "Access to the '/' path"
            authenticate { tokenCredential ->
                if (tokenCredential.token == "Ybt6mVHCEYXdmqgUFttSX4pLqR6mGjAkmVyy55QRpU5xfU9dBRwLmUbLausg4462") {
                    UserIdPrincipal("rsn")
                } else {
                    null
                }
            }
        }
    }

    routing {
        player()
        srvcheck()
    }
}
