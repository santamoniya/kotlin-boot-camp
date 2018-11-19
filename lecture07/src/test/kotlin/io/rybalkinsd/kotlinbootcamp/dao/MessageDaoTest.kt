package io.rybalkinsd.kotlinbootcamp.dao

import io.rybalkinsd.kotlinbootcamp.db.DbConnector
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.Test

class MessageDaoTest {

    @Test
    fun `add message`() {
        DbConnector

        transaction {
            addLogger(StdOutSqlLogger)
            val count = Messages.selectAll().count()
            Messages.insert {
                it[id] = count
                it[user] = 0
                it[time] = DateTime.now()
                it[value] = "My first message2"
            }
        }
    }
}