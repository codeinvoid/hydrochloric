package com.pkgho.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

@Serializable
data class Player(val uuid: String, @BsonId val _id: Id<Player>, @Contextual val state: State, val code: Int)

@Serializable
data class State(val banned: Banned, val whitelist: Whitelist, val integration: Integration, val qq: Long)

@Serializable
data class Integration(val count: Long, val time: Long, val active: Boolean, val nanoid: String)

@Serializable
data class Banned(val active: Boolean, val time: Long, val reason: String, val operator: String, val nanoid: String)

@Serializable
data class Whitelist(val active: Boolean, val time: Long)

@Serializable
data class New(val uuid: String, val code: Int, val qq: Long)

@Serializable
data class Valid(val code: Int)