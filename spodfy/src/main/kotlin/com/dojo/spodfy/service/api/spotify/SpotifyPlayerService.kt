package com.dojo.spodfy.service.api.spotify

import com.dojo.spodfy.model.ListaEpisodiosDto
import com.dojo.spodfy.model.PesquisaSpotifyApiDto
import com.dojo.spodfy.repository.SessionUserRepository
import com.dojo.spodfy.table.SessionUserSpotify
import com.dojo.spodfy.util.SPOTIFY_API_PLAYER_PAUSE
import com.dojo.spodfy.util.SPOTIFY_API_PLAYER_PLAY
import com.dojo.spodfy.util.SPOTIFY_API_SEARCH
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class SpotifyPlayerService(val db: SessionUserRepository) {

    @set:Autowired
    lateinit var spotifyService: SpotifyService


    fun tocarPodcastNoSpotify(listaEpisodios: ListaEpisodiosDto, idUsuario: Long?): Any {
        val sessionUser: SessionUserSpotify? = db.findByUsuarioIdUsuario(idUsuario)
        val (request, response, result) = Fuel.put(SPOTIFY_API_PLAYER_PLAY)
            .header(Headers.AUTHORIZATION, "Bearer ${sessionUser?.accessToken}")
            .responseString()

        if (response.statusCode != HttpStatus.NO_CONTENT.value()) {
            val refreshResponse = spotifyService.tratarResponse(response, request, sessionUser)
            println(refreshResponse.third.get())
            return result.get()
        }


        return result.get()
    }

    fun pausarPodcastNoSpotify(idUsuario: Long?): Any {
        val sessionUser: SessionUserSpotify? = db.findByUsuarioIdUsuario(idUsuario)
        val (request, response, result) = Fuel.put(SPOTIFY_API_PLAYER_PAUSE)
            .header(Headers.AUTHORIZATION, "Bearer ${sessionUser?.accessToken}")
            .responseString()


        if (response.statusCode != HttpStatus.NO_CONTENT.value()) {
            val refreshResponse = spotifyService.tratarResponse(response, request, sessionUser)
            println(refreshResponse.third.get())
            return result.get()
        }


        return result.get()

    }

}
