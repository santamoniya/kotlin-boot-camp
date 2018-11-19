package io.rybalkinsd.kotlinbootcamp.dao

import io.rybalkinsd.kotlinbootcamp.model.Message
import io.rybalkinsd.kotlinbootcamp.model.toMessage
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class MessageDao : Dao<Message> {
    override val all: List<Message>
        get() = transaction { Messages.selectAll().map { it.toMessage() } }

    override fun getAllWhere(vararg conditions: Op<Boolean>): List<Message> {
        val condition = Op.build { conditions.reduce { exp1, exp2 -> exp1 and exp2 } }
        return transaction { Users.select(condition).map { row -> row.toMessage() } }
    }

    override fun insert(t: Message) {
        transaction {
            Messages.insert {
                it[id] = t.id
                it[user] = t.user.id
                it[time] = t.timestamp
                it[value] = t.value
            }
        }
    }

    override fun findById(id: Int): Message? {
        val resultRow = transaction { Messages.select { Messages.id eq id }.firstOrNull() }
        return resultRow?.let { Message(
                resultRow[Messages.id],
                UserDao().findById(resultRow[Messages.user])!!,
                resultRow[Messages.time],
                resultRow[Messages.value])
            }
    }
}
