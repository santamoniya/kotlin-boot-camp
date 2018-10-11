package io.rybalkinsd.kotlinbootcamp.practice.client

import com.kohttp.dsl.httpGet
import com.kohttp.dsl.httpPost
import com.kohttp.ext.eager

object ChatClient {
    // Change to server url
    private const val HOST = "localhost"
    private const val PORT = 8080

    /**
     * POST /chat/login?name=my_name
     */
    fun login(name: String) = httpPost {
        host = HOST
        port = PORT
        path = "/chat/login"
        body {
            form {
                "name" to name
            }
        }
    }.eager()

    /**
     * GET /chat/history
     */
    fun viewHistory() = httpGet {
        host = HOST
        port = PORT
        path = "/chat/chat"
    }.eager()

    /**
     * POST /chat/say
     *
     * Body: "name=my_name&msg='my_message'"
     */
    fun say(name: String, msg: String) = httpPost {
        host = HOST
        port = PORT
        path = "/chat/say"
        body {
            form {
                "name" to name
                "msg" to msg
            }
        }
    }.eager()

    /**
     * GET /chat/online
     */
    fun viewOnline() = httpGet {
        host = HOST
        port = PORT
        path = "/chat/online"
    }.eager()

    /**
     * DELETE /chat/logout?name=my_name
     */
/*    fun logout(name: String): Response {
        val request = Request.Builder().apply {
            url("http://$HOST:$PORT/chat/logout?name=$name")
            delete()
        }.build()

        return OkHttpClient.Builder().build()
                .newCall(request)
                .execute()
    }*/
    /**
     * POST /chat/logout?name=my_name
     */
    fun logout(name: String) = httpPost {
        host = HOST
        port = PORT
        path = "/chat/logout"
        body {
            form {
                "name" to name
            }
        }
    }
}
