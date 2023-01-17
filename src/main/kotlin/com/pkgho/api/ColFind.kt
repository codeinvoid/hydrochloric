package com.pkgho.api

import com.pkgho.models.*
import org.litote.kmongo.evaluate
abstract class ColFind {
    abstract fun all(uuid: String) : State
    abstract fun integration(uuid: String) : Integration
    abstract fun banned(uuid: String) : Banned
    abstract fun whitelist(uuid: String) : Whitelist
    val col = data()
}
class StateChecker : ColFind() {
    override fun integration(uuid: String): Integration {
        val types = col.find().evaluate {
            filter { it.uuid == uuid }.first().state.integration
        }
        return types
    }

    override fun banned(uuid: String): Banned {
        val types = col.find().evaluate {
            filter { it.uuid == uuid }.first().state.banned
        }
        return types
    }

    override fun whitelist(uuid: String): Whitelist {
        val types = col.find().evaluate {
            filter { it.uuid == uuid }.first().state.whitelist
        }
        return types
    }

    override fun all(uuid: String): State {
        val types = col.find().evaluate {
            filter { it.uuid == uuid }.first().state
        }
        return types
    }
}
