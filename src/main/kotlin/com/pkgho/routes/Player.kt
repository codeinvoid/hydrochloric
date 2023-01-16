package com.pkgho.routes

import com.pkgho.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.*
import java.time.Instant

fun Route.player() {
    val version = "v1"
    val client = KMongo.createClient("mongodb://HoiGe:24VT7rXFJt4vLJ2n@127.0.0.1:27017")
    val database = client.getDatabase("player")
    val col = database.getCollection<Player>()

    route("/${version}/players") {

        get("/{uuid}") {
            val player: Player? = col.findOne(Player::uuid eq call.parameters["uuid"])
            if (player != null) {
                call.respond(player.json)
            } else {
                call.respondText("Not Found UUID", status = HttpStatusCode.NotFound)
            }
        }

        get("/{uuid}/{type}") {
            val uuid = call.parameters["uuid"]
            when (call.parameters["type"].toString()) {
                "banned" -> {
                    val banned = col.find().evaluate {
                        filter { it.uuid == uuid }.first().state.banned
                    }
                    call.respond(banned)
                }

                "integration" -> {
                    val integration = col.find().evaluate {
                        filter { it.uuid == uuid }.first().state.integration
                    }
                    call.respond(integration)
                }

                "whitelist" -> {
                    val whitelist = col.find().evaluate {
                        filter { it.uuid == uuid }.first().state.whitelist
                    }
                    call.respond(whitelist)
                }

                "all" -> {
                    val all = col.find().evaluate {
                        filter { it.uuid == uuid }.first().state
                    }
                    call.respond(all)
                }

                else -> {
                    call.respondText("Invalid Type", status = HttpStatusCode.BadRequest)
                }
            }
        }
        authenticate("auth-bearer") {
            post("{uuid}/code") {
                val uuid = call.parameters["uuid"]
                val filter = Player::uuid eq uuid
                val activeCheck = col.findOne(filter)
                val stateFrom = call.receive<Valid>()
                if (activeCheck != null && stateFrom.code == activeCheck.code) {
                    col.updateOne(
                        filter,
                        setValue(
                            Player::state / State::whitelist / Whitelist::active,
                            true
                        )
                    )
                    col.updateOne(
                        filter,
                        setValue(
                            Player::state / State::whitelist / Whitelist::time,
                            Instant.now().epochSecond
                        )
                    )
                    call.respondText("Success", status = HttpStatusCode.Accepted)
                } else {
                    call.respondText("Not Found", status = HttpStatusCode.NotFound)
                }
            }

            get {
                val player = col.find()
                player.asIterable().map { call.respond(it.json) }
            }

            post {
                val newFrom = call.receive<New>()
                val playerId = newFrom.uuid
                val filter = Player::uuid eq playerId
                val banned = Banned(false, 0, "", "")
                val whitelist = Whitelist(false, 0)
                val integration = Integration(0, 0, false)
                val state = State(banned, whitelist, integration, newFrom.qq)
                val player = Player(_id = newId(), uuid = playerId, state = state, code = newFrom.code)
                val uuidCheck = col.findOne(filter)
                val qqCheck = col.findOne(Player::state / State::qq eq newFrom.qq)
                if (uuidCheck == null && qqCheck == null) {
                    col.insertOne(player)
                    call.respondText(
                        "Player stored correctly",
                        status = HttpStatusCode.Created
                    )
                } else {
                    call.respondText(
                        "This Player is Locked.",
                        status = HttpStatusCode.Locked
                    )
                }
            }

            put("/{uuid}") {
                call.respondText(
                    "Bad Request",
                    status = HttpStatusCode.BadRequest
                )
            }

            put("/{uuid}/qq") {
                val uuid = call.parameters["uuid"]
                val filter = Player::uuid eq uuid
                val activeCheck = col.findOne(filter)
                if (activeCheck != null) {
                    val stateFrom = call.receive<New>()
                    col.updateOne(
                        filter,
                        setValue(
                            Player::state / State::qq,
                            stateFrom.qq
                        )
                    )
                    call.respondText("Success", status = HttpStatusCode.Accepted)
                } else {
                    call.respondText("Not Found", status = HttpStatusCode.NotFound)
                }
            }

            put("/{uuid}/banned") {
                val uuid = call.parameters["uuid"]
                val filter = Player::uuid eq uuid
                val activeCheck = col.findOne(filter)
                if (activeCheck != null) {
                    val bannedFrom = call.receive<Banned>()
                    col.updateOne(
                        filter,
                        setValue(
                            Player::state / State::banned / Banned::active,
                            bannedFrom.active
                        )
                    )
                    col.updateOne(
                        filter,
                        setValue(
                            Player::state / State::banned / Banned::time,
                            bannedFrom.time
                        )
                    )
                    col.updateOne(
                        filter,
                        setValue(
                            Player::state / State::banned / Banned::reason,
                            bannedFrom.reason
                        )
                    )
                    col.updateOne(
                        filter,
                        setValue(
                            Player::state / State::banned / Banned::operator,
                            bannedFrom.operator
                        )
                    )
                    call.respondText("Success", status = HttpStatusCode.Accepted)
                } else {
                    call.respondText("Not Found", status = HttpStatusCode.NotFound)
                }
            }

            put("/{uuid}/whitelist") {
                val uuid = call.parameters["uuid"]
                val filter = Player::uuid eq uuid
                val activeCheck = col.findOne(filter)
                if (activeCheck != null) {
                    val whitelistFrom = call.receive<Whitelist>()
                    col.updateOne(
                        filter,
                        setValue(
                            Player::state / State::whitelist / Whitelist::active,
                            whitelistFrom.active
                        )
                    )
                    col.updateOne(
                        filter,
                        setValue(
                            Player::state / State::whitelist / Whitelist::time,
                            whitelistFrom.time
                        )
                    )
                    call.respondText("Success", status = HttpStatusCode.Accepted)
                } else {
                    call.respondText("Not Found", status = HttpStatusCode.NotFound)
                }
            }

            put("/{uuid}/integration") {
                val uuid = call.parameters["uuid"]
                val filter = Player::uuid eq uuid
                val activeCheck = col.findOne(filter)
                if (activeCheck != null) {
                    val integrationFrom = call.receive<Integration>()
                    col.updateOne(
                        filter,
                        setValue(
                            Player::state / State::integration / Integration::active,
                            integrationFrom.active
                        )
                    )
                    col.updateOne(
                        filter,
                        setValue(
                            Player::state / State::integration / Integration::time,
                            integrationFrom.time
                        )
                    )
                    col.updateOne(
                        filter,
                        setValue(
                            Player::state / State::integration / Integration::count,
                            integrationFrom.count
                        )
                    )
                    call.respondText("Success", status = HttpStatusCode.Accepted)
                } else {
                    call.respondText("Not Found", status = HttpStatusCode.NotFound)
                }
            }

            delete("/{uuid}") {
                val uuid = call.parameters["uuid"]
                val activeCheck = col.findOne(Player::uuid eq uuid)
                if (activeCheck != null) {
                    col.deleteOne(Player::uuid eq uuid)
                    call.respondText("Player removed correctly", status = HttpStatusCode.Accepted)
                } else {
                    call.respondText("Not Found", status = HttpStatusCode.NotFound)
                }
            }
        }
    }
}
