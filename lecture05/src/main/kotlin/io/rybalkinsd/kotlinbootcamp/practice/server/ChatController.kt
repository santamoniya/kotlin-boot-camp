package io.rybalkinsd.kotlinbootcamp.practice.server

import io.rybalkinsd.kotlinbootcamp.util.logger
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.util.Queue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

@Controller
@RequestMapping("/chat")
class ChatController {
    val log = logger()
    val messages: Queue<String> = ConcurrentLinkedQueue()
    val usersOnline: MutableMap<String, String> = ConcurrentHashMap()

    @RequestMapping(
        path = ["/login"],
        method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE]
    )
    fun login(@RequestParam("name") name: String): ResponseEntity<String> = when {
        name.isEmpty() -> ResponseEntity.badRequest().body("Name is too short")
        name.length > 20 -> ResponseEntity.badRequest().body("Name is too long")
        usersOnline.contains(name) -> ResponseEntity.badRequest().body("Already logged in")
        else -> {
            usersOnline[name] = name
            messages += "[$name] logged in".also { log.info(it) }
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
        path = ["/online"],
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun online(): ResponseEntity<String> =
        if (usersOnline.isEmpty()) ResponseEntity.ok("There is not online users\n")
        else ResponseEntity.ok(usersOnline.keys.joinToString { it + "\n"} )

    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = ["/logout"],
            method = [RequestMethod.POST],
            consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE]
    )
    fun logout(@RequestParam("name") name: String): ResponseEntity<String> =
        if (!usersOnline.contains(name)) ResponseEntity.badRequest().body("Unknown user name: $name")
        else {
            usersOnline.remove(name)
            messages += "[$name] logged out".also { log.info(it) }
            ResponseEntity.ok("You are logged out\n")
        }

    /**
     * curl -X POST -i localhost:8080/chat/say -d "name=I_AM_STUPID&msg=Hello everyone in this chat"
     */
    @RequestMapping(
            path = ["/say"],
            method = [RequestMethod.POST],
            consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE]
    )
    fun say(@RequestParam("name") name: String, @RequestParam("msg") msg: String): ResponseEntity<String> =
        if (!usersOnline.contains(name)) ResponseEntity.badRequest().body("Unknown user name: $name\n")
        else {
            messages += "[$name]: $msg".also { log.info(it) }
            ResponseEntity.ok().build()
        }

    /**
     * curl -i localhost:8080/chat/chat
     */
    @RequestMapping(
            path = ["/chat"],
            method = [RequestMethod.GET],
            produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun chat(): ResponseEntity<String> = ResponseEntity.ok(messages.joinToString("") { it + "\n" })
}
