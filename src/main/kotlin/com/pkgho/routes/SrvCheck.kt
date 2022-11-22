package com.pkgho.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.xbill.DNS.Lookup
import org.xbill.DNS.Record
import org.xbill.DNS.SRVRecord
import org.xbill.DNS.Type

fun Route.srvcheck() {
    val version = "v1"
    route("/${version}/srv") {
        get("{ip?}"){
            if (call.parameters["ip"] != null || call.parameters["ip"] != "")
                if (call.parameters["ip"]?.contains(":") == true || call.parameters["ip"]?.contains("：") == true)
                    if (call.parameters["ip"]?.contains("：") == true)
                        call.respond(call.parameters["ip"].toString().replace('：',':'))
                    else call.respond(call.parameters["ip"].toString())
                else call.respond(parseAddress(call.parameters["ip"].toString(),call))
            else
                call.respondText(
                    "Missing id",
                    status = HttpStatusCode.BadRequest
                )
        }
    }
}

@Throws(Exception::class)
suspend fun parseAddress(address: String, call: ApplicationCall): Any {
        val recordName = "_minecraft._tcp.${address}"
        val records: Array<out Record> = Lookup(recordName, Type.SRV).run() ?: return call.respondText(
            "Not a right record",
            status = HttpStatusCode.BadRequest
        )
        val mx = records[0] as SRVRecord
        val removeDot = mx.target.toString().substring(0, mx.target.toString().length - 1)
        return "$removeDot:${mx.port}"
}
