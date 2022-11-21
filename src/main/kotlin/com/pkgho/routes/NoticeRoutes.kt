package com.pkgho.routes

import com.pkgho.models.Notice
import com.pkgho.models.dataStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.findOne

fun Route.noticeRouting() {
    val version = "v1"
    route("/${version}/notice") {
        get {
            if ((dataStorage.countDocuments().toInt()) != 0) {
                call.respond(dataStorage)
            } else {
                call.respondText("No Notice", status = HttpStatusCode.NoContent)
            }
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val notice =
                dataStorage.findOne(id) ?: return@get call.respondText(
                    "No data with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(notice)
        }
        post {
            val notice = call.receive<Notice>()
            dataStorage.insertOne(notice)
            call.respondText("Data stored correctly", status = HttpStatusCode.Created)
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (dataStorage.findOne(id)?.id?.isEmpty() == false)
                call.respondText("Data removed correctly", status = HttpStatusCode.Accepted)
            else
                call.respondText("Not Found",status = HttpStatusCode.NotFound)
        }
    }
}
