package com.pkgho.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

@Serializable
data class Player(val uuid: String, @BsonId val _id: Id<Player>, @Contextual val state: State)

@Serializable
data class State(val banned: Banned, val whitelist: Whitelist, val integration: Integration)

@Serializable
data class Integration(val count: Int, val time: Int, val active: Boolean)

@Serializable
data class Banned(val active: Boolean, val time: Int, val reason: String, val operator: String)

@Serializable
data class Whitelist(val active: Boolean, val time: Int)