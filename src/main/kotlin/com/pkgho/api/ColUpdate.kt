package com.pkgho.api

import com.pkgho.models.*
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.setValue
import java.time.Instant

abstract class ColUpdate {
    abstract fun integration(from: Integration) : Boolean
    abstract fun whitelist(from: Whitelist) : Boolean
    abstract fun banned(from: Banned) : Boolean
    abstract fun qq(from: New) : Boolean

    val col = data()
    val nanoId = NanoIds().nanoid()
}

class Updater(private val uuid: String) : ColUpdate(){
    private val filter = Player::uuid eq uuid
    override fun integration(from: Integration): Boolean {
        if (uuidChecker(uuid)) {
            col.updateOne(
                filter,
                setValue(
                    Player::state / State::integration / Integration::active,
                    from.active
                )
            )
            col.updateOne(
                filter,
                setValue(
                    Player::state / State::integration / Integration::time,
                    Instant.now().toEpochMilli()
                )
            )
            col.updateOne(
                filter,
                setValue(
                    Player::state / State::integration / Integration::count,
                    from.count
                )
            )
            return true
        }
        return false
    }

    override fun whitelist(from: Whitelist): Boolean {
        val filter = Player::uuid eq uuid
        if (uuidChecker(uuid)) {
            col.updateOne(
                filter,
                setValue(
                    Player::state / State::whitelist / Whitelist::active,
                    from.active
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

    override fun banned(from: Banned): Boolean {
        val filter = Player::uuid eq uuid
        if (uuidChecker(uuid)) {
            col.updateOne(
                filter,
                setValue(
                    Player::state / State::banned / Banned::active,
                    from.active
                )
            )
            col.updateOne(
                filter,
                setValue(
                    Player::state / State::banned / Banned::time,
                    Instant.now().toEpochMilli()
                )
            )
            col.updateOne(
                filter,
                setValue(
                    Player::state / State::banned / Banned::reason,
                    from.reason
                )
            )
            col.updateOne(
                filter,
                setValue(
                    Player::state / State::banned / Banned::operator,
                    from.operator
                )
            )
            col.updateOne(
                filter,
                setValue(
                    Player::state / State::banned / Banned::nanoid,
                    nanoId
                )
            )
            return true
        }
        return false
    }

    override fun qq(from: New): Boolean {
        if (uuidChecker(uuid)) {
            col.updateOne(
                filter,
                setValue(
                    Player::state / State::qq,
                    from.qq
                )
            )
            return true
        }
        return false
    }
}

