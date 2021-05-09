package com.dojo.spodfy.resource.api.spotify

import com.dojo.spodfy.SpodfyApplication
import com.dojo.spodfy.repository.FavoritoRepository
import com.dojo.spodfy.table.Favorito
import com.dojo.spodfy.util.ExtensionFunction.jsonToFavorito
import com.dojo.spodfy.util.ExtensionFunction.jsonToMutableListFavorito
import com.dojo.util.prepararFavorito
import com.dojo.util.prepararFavoritoForm
import com.dojo.util.prepararFavoritoFormPut
import com.dojo.util.prepararListaFavorito
import com.nhaarman.mockitokotlin2.any
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [SpodfyApplication::class])
@AutoConfigureMockMvc
class FavoritoResourceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var favoritoRepository: FavoritoRepository

    @Test
    fun `recuperar lista de favoritos`() {
        mockServiceListaDeFavoritos()
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/favorito")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].idFavorito").value(1))
    }

    @Test
    fun `deleta lista de favoritos por id`() {
        mockServiceDeletePorUsuarioId()
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/favorito/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun `recuperar favorito por id`() {
        mockServiceListarPorFavoritoId()
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/favorito/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.idFavorito").value(1))
    }


    @Test
    fun `salvar favorito`() {
        mockServiceSalvarFavorito()
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/favorito/usuario/2")
                .content(prepararFavoritoForm())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `atualizar favorito`() {
        mockServiceAtualizarFavorito()
        mockMvc.perform(
            MockMvcRequestBuilders
                .put("/favorito/usuario/2")
                .content(prepararFavoritoFormPut())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `recuperar lista de favoritos por usuario id`() {
        mockServiceListaDeFavoritos()
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/favorito/usuario/2")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    private fun mockServiceSalvarFavorito() {
        whenever(favoritoRepository.save(prepararFavoritoForm().jsonToFavorito())).thenReturn(prepararFavoritoForm().jsonToFavorito())
    }

    private fun mockServiceAtualizarFavorito() {
        whenever(favoritoRepository.save(prepararFavoritoForm().jsonToFavorito())).thenReturn(prepararFavoritoForm().jsonToFavorito())
    }

    private fun mockServiceListarPorFavoritoId() {
        val favorito: Favorito = prepararFavorito().jsonToFavorito()
        whenever(favoritoRepository.findById(anyLong())).thenReturn(Optional.of(favorito))
    }

    private fun mockServiceDeletePorUsuarioId() {
        doNothing().`when`(favoritoRepository).deleteById(anyLong())
    }

    private fun mockServiceListaDeFavoritos() {
        val favoritos: MutableList<Favorito> = prepararListaFavorito().jsonToMutableListFavorito()
        whenever(favoritoRepository.findAll()).thenReturn(favoritos)
    }


}