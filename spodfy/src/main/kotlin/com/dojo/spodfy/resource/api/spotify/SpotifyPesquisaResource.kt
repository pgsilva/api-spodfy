package com.dojo.spodfy.resource.api.spotify

import com.dojo.spodfy.model.PesquisaSpotifyApi
import com.dojo.spodfy.resource.BaseResource
import com.dojo.spodfy.service.api.spotify.SpotifyPesquisaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/pesquisa")
class SpotifyPesquisaResource(val spotifyPesquisaService: SpotifyPesquisaService) : BaseResource() {

    @GetMapping
    fun pesquisarApiSpotify(@RequestParam keyword: String?): ResponseEntity<PesquisaSpotifyApi>? {
        return try {
            ResponseEntity.ok(spotifyPesquisaService.pesquisarPodcasts(keyword, getNrIpAdress()))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }


    @GetMapping("/episodio/{id}")
    fun pesquisarEpisodiosApiSpotify(
        @PathVariable id: String?
    ): ResponseEntity<Any>? {
        return try {
            ResponseEntity.ok(spotifyPesquisaService.pesquisarEpisodios(id, getNrIpAdress()))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/episodio/{id}/pagina/{numberPage}")
    fun pesquisarEpisodiosApiSpotifyPaginado(
        @PathVariable id: String?,
        @PathVariable numberPage: String?,
    ): ResponseEntity<Any>? {
        return try {
            ResponseEntity.ok(spotifyPesquisaService.pesquisarEpisodiosPaginado(id, getNrIpAdress(), numberPage))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }


}