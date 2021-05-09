package com.dojo.spodfy.resource.api.spotify

import com.dojo.spodfy.SpodfyApplication
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [SpodfyApplication::class])
@AutoConfigureMockMvc
class HealthCheckResourceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `is alive`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/isAlive")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

}