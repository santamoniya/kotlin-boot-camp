package io.rybalkinsd.kotlinbootcamp.model

import io.rybalkinsd.kotlinbootcamp.dao.Messages
import io.rybalkinsd.kotlinbootcamp.dao.UserDao
import io.rybalkinsd.kotlinbootcamp.dao.Users
import org.jetbrains.exposed.sql.ResultRow

internal fun ResultRow.toUser() = User(this[Users.id], this[Users.login])

internal fun ResultRow.toMessage(): Message {
    val userId = this[Messages.user]
    return Message(
            this[Messages.id],
            UserDao().findById(userId)!!,
            this[Messages.time],
            this[Messages.value]
    )
}