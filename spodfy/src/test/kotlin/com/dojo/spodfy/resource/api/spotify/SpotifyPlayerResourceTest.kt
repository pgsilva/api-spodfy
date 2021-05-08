package com.dojo.spodfy.resource.api.spotify

import com.dojo.spodfy.SpodfyApplication
import com.dojo.spodfy.repository.SessionUserRepository
import com.dojo.spodfy.service.api.spotify.SpotifyPlayerService
import com.dojo.spodfy.table.SessionUserSpotify
import com.dojo.util.prepararJsonPutPlay
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [SpodfyApplication::class])
@AutoConfigureMockMvc
class SpotifyPlayerResourceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var sessionUserRepository: SessionUserRepository

    @Test
    fun `executar player_play com sucesso`() {
        mockPlay()
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

    private fun mockPlay() {
        whenever(sessionUserRepository.findByUsuarioIdUsuario(anyLong())).thenReturn(null)
    }


}