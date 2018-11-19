package io.rybalkinsd.kotlinbootcamp.practice

import io.rybalkinsd.kotlinbootcamp.dao.MessageDao
import io.rybalkinsd.kotlinbootcamp.db.DbConnector
import io.rybalkinsd.kotlinbootcamp.model.Message
import io.rybalkinsd.kotlinbootcamp.model.User
import junit.framework.TestCase.assertTrue
import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test

class ChatMessageDaoTest {

    val messageDao = MessageDao()

    @Before
    fun setUp() {
        DbConnector
    }

    @Test
    fun `select * from message`() {
        assertTrue(messageDao.all.isNotEmpty())
    }

    @Test
    fun `insert message`() {
        val before = messagesNumber()
        messageDao.insert(dummyMessage())
        val after = messagesNumber()
        assertTrue(before + 1 <= after)
    }

    fun messagesNumber(): Int = messageDao.all.size
    fun dummyMessage(): Message = Message(messagesNumber(), User(0, "login0"), DateTime.now(), "Hi!")
}