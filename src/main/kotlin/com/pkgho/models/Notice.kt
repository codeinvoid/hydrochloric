package com.pkgho.models

import kotlinx.serialization.Serializable

@Serializable
data class Notice(val id: String, val time: String, val context: String)
val dataStorage = mutableListOf<Notice>()