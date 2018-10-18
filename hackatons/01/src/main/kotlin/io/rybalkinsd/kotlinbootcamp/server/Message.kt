package io.rybalkinsd.kotlinbootcamp.server

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class Message(val name: String, val msg: String) {
    val time: String = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC).format(Instant.now())

    override fun toString(): String {
        return "[$name $time] $msg"
    }
}