package com.pkgho.routes

import com.pkgho.api.*
import com.pkgho.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.*

fun Route.player() {
    val version = "1"
    val col = data()
    val uuidBase = "/{uuid}"

    route("/v${version}/players") {

        get(uuidBase) {
            val uuid = uuid(this)
            val player: Player? = col.findOne(Player::uuid eq uuid)

            if (player != null) {
                call.respond(player.json)
            } else {
                call.respondText("Not Found UUID", status = HttpStatusCode.NotFound)
            }
        }

        get("/{uuid}/{type}") {
            val uuid = uuid(this)

            when (call.parameters["type"].toString()) {

                "banned" -> { call.respond(StateChecker().banned(uuid)) }
                "integration" -> { call.respond(StateChecker().integration(uuid)) }
                "whitelist" -> { call.respond(StateChecker().whitelist(uuid)) }
                "all" -> { call.respond(StateChecker().all(uuid)) }

                else -> { call.respondText("Invalid Type", status = HttpStatusCode.BadRequest) }
            }
        }

        authenticate("auth-bearer") {
            post("/{uuid}/code") {
                val uuid = uuid(this)
                val from = call.receive<Valid>()

                createCall(this, Create(uuid).code(from.code))
            }

            get {
                val player = col.find()

                player.asIterable().map { call.respond(it.json) }
            }

            post {
                val from = call.receive<New>()

                postCall(this, Create(from.uuid).playerBuilder(from))
            }

            put(uuidBase) {
                call.respondText(
                    "Bad Request",
                    status = HttpStatusCode.BadRequest
                )
            }

            put("/{uuid}/qq") {
                val uuid = uuid(this)
                val from = call.receive<New>()
                val qq = Updater(uuid).qq(from)

                call(this, qq)
            }

            put("/{uuid}/banned") {
                val uuid = uuid(this)
                val from = call.receive<Banned>()
                val banned = Updater(uuid).banned(from)

                call(this, banned)
            }

            put("/{uuid}/whitelist") {
                val uuid = uuid(this)
                val from = call.receive<Whitelist>()
                val whitelist = Updater(uuid).whitelist(from)

                call(this, whitelist)
            }

            put("/{uuid}/integration") {
                val uuid = uuid(this)
                val from = call.receive<Integration>()
                val integration = Updater(uuid).integration(from)

                call(this, integration)
            }

            delete(uuidBase) {
                val uuid = uuid(this)

                if (uuidChecker(uuid)) {
                    col.deleteOne(Player::uuid eq uuid)
                    call(this, true)
                } else {
                    call(this, false)
                }
            }
        }
    }
}
