package com.dojo.spodfy.resource.api.spotify

import com.dojo.spodfy.model.ListaEpisodiosDto
import com.dojo.spodfy.model.PesquisaSpotifyApiDto
import com.dojo.spodfy.service.api.spotify.SpotifyShowService
import com.dojo.spodfy.util.BaseResource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/show")
class SpotifyShowResource(val spotifyShowService: SpotifyShowService) : BaseResource() {

    @PutMapping()
    fun tocarPodcastNoSpotify(
        @RequestParam idUsuario: Long?
    ) = spotifyShowService.tocarShowNoSpotify(idUsuario)
}