package io.rybalkinsd.kotlinbootcamp.dao

import io.rybalkinsd.kotlinbootcamp.model.User
import io.rybalkinsd.kotlinbootcamp.model.toUser
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class UserDao : Dao<User> {
    override val all: List<User>
        get() = transaction { Users.selectAll().map { it.toUser() } }

    override fun getAllWhere(vararg conditions: Op<Boolean>): List<User> {
        val condition = Op.build { conditions.reduce { exp1, exp2 -> exp1 and exp2 } }
        return transaction { Users.select(condition).map { row -> row.toUser() } }
    }

    override fun insert(t: User) {
        transaction {
            Users.insert {
                it[id] = t.id
                it[login] = t.login
            }
        }
    }

    override fun findById(id: Int): User? {
        val resultRow = Users.select { Users.id eq id }.firstOrNull()
        return resultRow?.let { User(resultRow[Users.id], resultRow[Users.login]) }
    }
}
