package com.dojo.spodfy.resource.api.spotify

import com.dojo.spodfy.SpodfyApplication
import com.dojo.spodfy.repository.SessionUserRepository
import com.dojo.spodfy.repository.UsuarioRepository
import com.dojo.spodfy.table.Usuario
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [SpodfyApplication::class])
@AutoConfigureMockMvc
class UsuarioResourceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var usuarioRepository: UsuarioRepository


    @MockBean
    private lateinit var sessionUserRepository: SessionUserRepository

    @Test
    fun `listar todos usuarios`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/usuario")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `listar usuario por id`() {
        whenever(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(Usuario()))

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/usuario/2")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }


    @Test
    fun `deletar usuario por id`() {
        mockDelete()
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/usuario/2")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    private fun mockDelete() {
        doNothing().`when`(sessionUserRepository).deleteByUsuarioIdUsuario(anyLong())
        doNothing().`when`(usuarioRepository).deleteById(anyLong())
    }

}