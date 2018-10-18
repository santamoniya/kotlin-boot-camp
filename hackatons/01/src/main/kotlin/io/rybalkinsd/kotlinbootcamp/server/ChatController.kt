package io.rybalkinsd.kotlinbootcamp.server

import com.google.gson.Gson
import io.rybalkinsd.kotlinbootcamp.util.logger
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.io.File
import java.util.Queue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

@Controller
@RequestMapping("/chat")
class ChatController {
    val log = logger()
    val messages: Queue<Message> = ConcurrentLinkedQueue()
    val usersOnline: MutableMap<String, String> = ConcurrentHashMap()

    @RequestMapping(
            path = ["/login"],
            method = [RequestMethod.POST],
            consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE]
    )
    fun login(@RequestParam("name") name: String): ResponseEntity<String> = when {
        name.isEmpty() -> ResponseEntity.badRequest().body("Name is too short").also { log.error("Login method: name is too short") }
        name.length > 20 -> ResponseEntity.badRequest().body("Name is too long").also { log.error("Login method: name is too long") }
        usersOnline.contains(name) -> ResponseEntity.badRequest().body("Already logged in").also { log.error("Login method: already logged in") }
        else -> {
            usersOnline[name] = name
            val message = Message(name, "logged in")
            messages += message.also { log.info(it.toString()) }
            addTextTohistory(message)
            ResponseEntity.ok().build()
        }
    }

    /**
     *
     * Well formatted sorted list of online users
     *
     * curl -i localhost:8080/chat/online
     */
    @RequestMapping(
            path = ["online"],
            method = [RequestMethod.GET],
            produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun online(): ResponseEntity<String> = when {
        usersOnline.isEmpty() -> ResponseEntity("No logged in users", HttpStatus.OK)
        else -> ResponseEntity(usersOnline.values.joinToString("\n"), HttpStatus.OK)
    }

    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=MY_NAME"
     */
    @RequestMapping(
            path = ["/logout"],
            method = [RequestMethod.POST],
            consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE]
    )
    fun logout(@RequestParam("name") name: String): ResponseEntity<String> = when {
        name.isEmpty() -> ResponseEntity.badRequest().body("No name provided").also { log.error("Logout method: No name provided") }
        !usersOnline.contains(name) -> ResponseEntity.badRequest().body("User is not logged in yet").also { log.error("Logout method: User is not logged in yet") }
        else -> {
            usersOnline.remove(name)
            val message = Message(name, "logged out")
            messages += message.also { log.info(it.toString()) }
            addTextTohistory(message)
            ResponseEntity.ok().build()
        }
    }

    /**
     * curl -X POST -i localhost:8080/chat/say -d "name=MY_NAME&msg=Hello everyone in this chat"
     */
    @RequestMapping(
            path = ["/say"],
            method = [RequestMethod.POST]
    )
    fun say(@RequestParam("name") name: String, @RequestParam("msg") msg: String): ResponseEntity<String> = when {
        name.isEmpty() -> ResponseEntity.badRequest().body("No name provided").also { log.error("Say method: No name provided") }
        !usersOnline.contains(name) -> ResponseEntity.badRequest().body("User is not logged in yet").also { log.error("Say method: User is not logged in yet") }
        else -> {
            val message = Message(name, msg)
            messages += message.also { log.info(it.toString()) }
            addTextTohistory(message)
            ResponseEntity.ok().build()
        }
    }

    /**
     * curl -i localhost:8080/chat/history
     */
    @RequestMapping(
            path = ["history"],
            method = [RequestMethod.GET],
            produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    @ResponseBody
    fun history(): String {
        val gson = Gson()
        val file = File("history.txt")
        file.createNewFile()
        return file.readLines()
                .map { str ->
                    val msg = gson.fromJson(str, Message::class.java)
                    paintMsg(msg)
                }
                .joinToString("")
    }

    private fun addTextTohistory(msg: Message) {
        val gson = Gson()
        val jsonString = gson.toJson(msg)
        File("history.txt").appendText("$jsonString\n")
    }

    private fun paintMsg(msg: Message): String =
            "<span style=\"color:blue\">" + msg.time +
            " </span>" + "<span style=\"color:red\">" + msg.name + " </span> " +
            "<span style=\"color:black\">" + Jsoup.clean(msg.msg, Whitelist.relaxed()) + " </span><br />"
}
