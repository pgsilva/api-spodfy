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
class PodcastResourceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `listar todos podcasts`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/podcast")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `listar todos podcasts por usuario`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/podcast/usuario/2")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

}