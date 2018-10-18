package io.rybalkinsd.kotlinbootcamp.server

import io.rybalkinsd.kotlinbootcamp.util.logger
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@SpringBootTest
class ChatControllerTest {

    val log = logger()
    @Autowired
    lateinit var context: WebApplicationContext

    @Test
    fun `history test`() {
        val mockMvc: MockMvc = MockMvcBuilders
                .webAppContextSetup(context).build()

        mockMvc.perform(get("/chat/history"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo {
                    result -> log.info(result.response.contentAsString)
                }
        }

    @Test
    fun `online and login test`() {
        val mockMvc: MockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()

        mockMvc.perform(post("/chat/login")
                .contentType("application/x-www-form-urlencoded")
                .content("name=User1")).andExpect(MockMvcResultMatchers.status().isOk)

        mockMvc.perform(post("/chat/login")
                .contentType("application/x-www-form-urlencoded")
                .content("name=User2")).andExpect(MockMvcResultMatchers.status().isOk)

        val result = mockMvc.perform(get("/chat/online")
                .contentType("text/plain")).andExpect(MockMvcResultMatchers.status().isOk)
                .andDo { result -> log.info(result.response.contentAsString) }.andReturn().response
        Assert.assertEquals("User2\nUser1", result.contentAsString)
    }

    @Test
    fun `login test`() {
        val mockMvc: MockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()

        mockMvc.perform(post("/chat/login")
                .contentType("application/x-www-form-urlencoded")
                .content("name=User1")).andExpect(MockMvcResultMatchers.status().`is`(400))
                // .andDo { result -> log.info("@@@" + result.response.contentAsString) }
    }

    @Test
    fun `logout test`() {
        val mockMvc: MockMvc = MockMvcBuilders.webAppContextSetup(context).build()

        mockMvc.perform(post("/chat/logout")
                .contentType("application/x-www-form-urlencoded")
                .content("name=User1")).andExpect(MockMvcResultMatchers.status().isOk)

        val result_logout = mockMvc.perform(get("/chat/online")
                .contentType("text/plain")).andExpect(MockMvcResultMatchers.status().isOk)
                .andDo { result -> log.info(result.response.contentAsString) }.andReturn().response
        Assert.assertEquals("User2", result_logout.contentAsString)

        mockMvc.perform(post("/chat/say").contentType("application/x-www-form-urlencoded")
                .content("name=User2&msg=msgToTest")).andExpect(MockMvcResultMatchers.status().isOk)
    }
}