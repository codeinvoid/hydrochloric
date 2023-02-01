package com.pkgho.api

import com.mongodb.client.MongoCollection
import com.pkgho.models.Player
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import org.litote.kmongo.KMongo
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

val nanoId = NanoIds().nanoid()
val iNanoId = NanoIds().inanoid()
fun data(): MongoCollection<Player> {
    val client = KMongo.createClient("mongodb://HoiGe:24VT7rXFJt4vLJ2n@127.0.0.1:27017")
    val database = client.getDatabase("player")
    return database.getCollection()
}
fun uuidChecker(uuid: String): Boolean{
    val filter = Player::uuid eq uuid
    val activeCheck = data().findOne(filter)
    if (activeCheck != null){
        return true
    }
    return false
}

suspend fun call(send: PipelineContext<Unit, ApplicationCall>, state: Boolean) {
    if (state) {
        send.call.respondText("Success", status = HttpStatusCode.Accepted)
    } else {
        send.call.respondText("Not Found", status = HttpStatusCode.NotFound)
    }
}

suspend fun postCall(send: PipelineContext<Unit, ApplicationCall>, state: Boolean) {
    if (state) {
        send.call.respondText("Player stored correctly", status = HttpStatusCode.Created)
    } else {
        send.call.respondText("This Player is Locked.", status = HttpStatusCode.Locked)
    }
}

suspend fun createCall(send: PipelineContext<Unit, ApplicationCall>, state: Boolean) {
    if (state) {
        send.call.respondText("Success", status = HttpStatusCode.OK)
    } else {
        send.call.respondText("Not Found", status = HttpStatusCode.NotFound)
    }
}

fun uuid(send: PipelineContext<Unit, ApplicationCall>) : String {
    return send.call.parameters["uuid"].toString().replace("-", "")
}

