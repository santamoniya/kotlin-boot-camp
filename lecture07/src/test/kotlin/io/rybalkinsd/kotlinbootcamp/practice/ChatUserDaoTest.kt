package io.rybalkinsd.kotlinbootcamp.practice

import io.rybalkinsd.kotlinbootcamp.dao.UserDao
import io.rybalkinsd.kotlinbootcamp.dao.Users
import io.rybalkinsd.kotlinbootcamp.db.DbConnector
import io.rybalkinsd.kotlinbootcamp.model.User
import junit.framework.TestCase
import org.jetbrains.exposed.sql.Op
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Created by sergey on 3/25/17.
 */
class ChatUserDaoTest {
    val userDao = UserDao()

    @Before
    fun setUp() {
        DbConnector
    }

    @Test
    fun getAllTest() {
        assertTrue(userDao.all.isNotEmpty())
    }

    @Test
    fun insertTest() {
        val before = userNumber()
        userDao.insert(dummyUser())
        val after = userNumber()
        TestCase.assertTrue(before + 1 <= after)
    }

    @Test
    fun `find lol`() {
        userDao.insert(User(userNumber(), "Lolita${userNumber()}"))
        val lols = userDao.getAllWhere(Op.build { Users.login like "Lol%" })
        assertTrue(
            lols.asSequence()
                .map { it.login }
                .any { it.startsWith("Lol") }
        )
    }

    fun userNumber(): Int = userDao.all.size
    fun dummyUser(): User {
        val num = userNumber()
        return User(num, "login$num")
    }
}