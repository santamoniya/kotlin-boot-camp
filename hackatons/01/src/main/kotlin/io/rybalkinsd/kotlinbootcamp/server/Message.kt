package io.rybalkinsd.kotlinbootcamp.server

import java.time.Instant
import java.time.ZoneOffset
import java.util.Queue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.time.format.DateTimeFormatter

class Message (){
    val time: Instant = Instant.now()
}