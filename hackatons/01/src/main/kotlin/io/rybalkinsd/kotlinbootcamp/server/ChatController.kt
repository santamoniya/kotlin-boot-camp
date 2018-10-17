package io.rybalkinsd.kotlinbootcamp.server

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
import java.time.format.DateTimeFormatter
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
        name.isEmpty() -> ResponseEntity.badRequest().body("Name is too short")
        name.length > 20 -> ResponseEntity.badRequest().body("Name is too long")
        usersOnline.contains(name) -> ResponseEntity.badRequest().body("Already logged in")
        else -> {
            usersOnline[name] = name
            messages += Message(name, "logged in").also { log.info(it.toString()) }
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
        usersOnline.isEmpty() -> ResponseEntity.badRequest().body("No logged in users")
        else -> ResponseEntity(usersOnline.values.joinToString(",\n ", "logged in users:\n "), HttpStatus.OK)
    }

    fun paintMsg(messages: Queue<Message>): String {
        var str:String = ""
        for (msg in messages) {
            //StringBuilder str = new StringBuilder();
            str += "<span style=\"color:blue\">" +
                    msg.time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                    " </span>" + "<span style=\"color:red\">" + msg.name+ " </span> " +
                    "<span style=\"color:blue\">" + Jsoup.clean(msg.msg, Whitelist.relaxed()) +" </span><br />"
        }
        return str
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
        name.isEmpty() -> ResponseEntity.badRequest().body("No name provided")
        !usersOnline.contains(name) -> ResponseEntity.badRequest().body("User is not logged in yet")
        else -> {
            usersOnline.remove(name)
            messages += Message(name, "logged out").also { log.info(it.toString()) }
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
        name.isEmpty() -> ResponseEntity.badRequest().body("No name provided")
        !usersOnline.contains(name) -> ResponseEntity.badRequest().body("User is not logged in yet")
        else -> {
            messages += Message(name, msg).also { log.info(it.toString()) }
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
    fun history(): String = paintMsg(messages)
}
