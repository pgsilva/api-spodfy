package com.dojo.spodfy.resource.api.spotify

import com.dojo.spodfy.model.PesquisaSpotifyApiDto
import com.dojo.spodfy.util.BaseResource
import com.dojo.spodfy.service.api.spotify.SpotifyPesquisaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/pesquisa")
class SpotifyPesquisaResource(val spotifyPesquisaService: SpotifyPesquisaService) : BaseResource() {

    @GetMapping
    fun pesquisarApiSpotify(
        @RequestParam keyword: String?,
        @RequestParam id: Long?
    ): ResponseEntity<PesquisaSpotifyApiDto>? {
        return try {
            ResponseEntity.ok(spotifyPesquisaService.pesquisarPodcasts(keyword, id))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }


    @GetMapping("/episodio/{idPodcast}")
    fun pesquisarEpisodiosApiSpotify(
        @PathVariable idPodcast: String?,
        @RequestParam id: Long?
    ): ResponseEntity<Any>? {
        return try {
            ResponseEntity.ok(spotifyPesquisaService.pesquisarEpisodios(idPodcast, id))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/episodio/{idPodcast}/pagina/{numberPage}")
    fun pesquisarEpisodiosApiSpotifyPaginado(
        @PathVariable idPodcast: String?,
        @PathVariable numberPage: String?,
        @RequestParam id: Long?
    ): ResponseEntity<Any>? {
        return try {
            ResponseEntity.ok(spotifyPesquisaService.pesquisarEpisodiosPaginado(idPodcast, id, numberPage))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }


}