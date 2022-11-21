package com.pkgho.models

import com.mongodb.client.MongoDatabase
import kotlinx.serialization.Serializable
import org.litote.kmongo.*

@Serializable
data class Notice(val id: String, val time: String, val context: String)
val client = KMongo.createClient("mongodb://root:KW2p5yMSjJ9EmRpc@101.32.221.189:27017")
val database: MongoDatabase = client.getDatabase("web")
val dataStorage = database.getCollection<Notice>()
