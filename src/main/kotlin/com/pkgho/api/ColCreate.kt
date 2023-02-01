package com.pkgho.api

import com.pkgho.models.*
import org.litote.kmongo.*
import java.math.BigDecimal
import java.time.Instant

abstract class ColCreate {
    abstract fun code(code: Int) : Boolean
    val col = data()
}

class Create(private val uuid: String) : ColCreate() {
    private val filter = Player::uuid eq uuid
    private val check = col.findOne(filter)
    override fun code(code: Int): Boolean {
        if (uuidChecker(uuid) && (check?.code ?: Int) == code) {
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
                    Instant.now().toEpochMilli()
                )
            )
            return true
        }
        return false
    }

    fun playerBuilder(newFrom: New): Boolean{
        val banned = Banned(false, 0, "", "", nanoId)
        val whitelist = Whitelist(false, 0)
        val integration = Integration(BigDecimal(0), 0, false, iNanoId)
        val state = State(banned, whitelist, integration, newFrom.qq)
        val player = Player(uuid, newId(), state, newFrom.code)
        val qqCheck = data().findOne(Player::state / State::qq eq newFrom.qq)

        if (!uuidChecker(uuid) && qqCheck == null) {
            data().insertOne(player)
            return true
        }
        return false
    }
}
