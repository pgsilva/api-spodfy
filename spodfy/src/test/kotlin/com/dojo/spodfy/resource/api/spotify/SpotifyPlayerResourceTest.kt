package com.dojo.spodfy.resource.api.spotify

import com.dojo.spodfy.service.api.spotify.SpotifyPlayerService
import com.dojo.util.prepararJsonPutPlay
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@RunWith(SpringRunner::class)
@WebMvcTest
class SpotifyPlayerResourceTest {

    @InjectMocks
    private lateinit var resource: SpotifyPlayerResource

    @MockBean
    private lateinit var service: SpotifyPlayerService

    @set:Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `executar player_play com sucesso`() {
        println(prepararJsonPutPlay())
        mockMvc.perform(
            MockMvcRequestBuilders
                .put("/player/play")
                .param("idUsuario", "2")
                .content(prepararJsonPutPlay())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }


}