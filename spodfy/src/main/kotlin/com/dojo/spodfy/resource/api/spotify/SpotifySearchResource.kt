package com.dojo.spodfy.resource.api.spotify

import com.dojo.spodfy.model.PesquisaSpotifyApiDto
import com.dojo.spodfy.util.BaseResource
import com.dojo.spodfy.service.api.spotify.SpotifyPesquisaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/pesquisa")
class SpotifySearchResource(val spotifyPesquisaService: SpotifyPesquisaService) : BaseResource() {

    @GetMapping
    fun pesquisarApiSpotify(
        @RequestParam keyword: String?,
        @RequestParam idUsuario: Long?
    ): ResponseEntity<PesquisaSpotifyApiDto>? {
        return try {
            ResponseEntity.ok(spotifyPesquisaService.pesquisarPodcasts(keyword, idUsuario))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }


    @GetMapping("/episodio/{idPodcast}")
    fun pesquisarEpisodiosApiSpotify(
        @PathVariable idPodcast: String?,
        @RequestParam idUsuario: Long?
    ): ResponseEntity<Any>? {
        return try {
            ResponseEntity.ok(spotifyPesquisaService.pesquisarEpisodios(idPodcast, idUsuario))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/episodio/{idPodcast}/pagina/{numberPage}")
    fun pesquisarEpisodiosApiSpotifyPaginado(
        @PathVariable idPodcast: String?,
        @PathVariable numberPage: String?,
        @RequestParam idUsuario: Long?
    ): ResponseEntity<Any>? {
        return try {
            ResponseEntity.ok(spotifyPesquisaService.pesquisarEpisodiosPaginado(idPodcast, idUsuario, numberPage))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }


}