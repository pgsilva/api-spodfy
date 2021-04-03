package com.dojo.spodfy.resource.api.spotify

import com.dojo.spodfy.model.ListaEpisodiosDto
import com.dojo.spodfy.service.api.spotify.SpotifyPlayerService
import com.dojo.spodfy.util.BaseResource
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/player")
class SpotifyPlayerResource(val playerService: SpotifyPlayerService) : BaseResource() {

    @PutMapping("/play")
    fun tocarPodcastNoSpotify(
        @RequestBody listaEpisodios: ListaEpisodiosDto,
        @RequestParam idUsuario: Long?
    ) = playerService.tocarPodcastNoSpotify(listaEpisodios, idUsuario)

    @PutMapping("/pause")
    fun pausarPodcastNoSpotify(@RequestParam idUsuario: Long?) = playerService.pausarPodcastNoSpotify(idUsuario)
}