package com.dojo.spodfy.service.api.spotify

import com.dojo.spodfy.model.ListaEpisodiosDto
import com.dojo.spodfy.repository.SessionUserRepository
import com.dojo.spodfy.table.SessionUserSpotify
import com.dojo.spodfy.util.SPOTIFY_API_PLAYER_PLAY
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class SpotifyShowService(val db: SessionUserRepository) {

    @set:Autowired
    lateinit var spotifyService: SpotifyService
    private val gson = Gson()

    fun tocarShowNoSpotify(idUsuario: Long?): Any {
        /*
        *Country roads, take me home
        *To the place I belong
        *West Virginia, mountain momma
        *Take me home, country roads
        * */
        val sessionUser: SessionUserSpotify? = db.findByUsuarioIdUsuario(idUsuario)
        val show: ListaEpisodiosDto = ListaEpisodiosDto(uris = arrayOf<String?>("spotify:track:39q7xibBdRboeMKUbZEB6g"))

        val (request, response, result) = Fuel.put(SPOTIFY_API_PLAYER_PLAY)
            .header(Headers.AUTHORIZATION, "Bearer ${sessionUser?.accessToken}")
            .body(gson.toJson(show))
            .responseString()

        if (response.statusCode != HttpStatus.NO_CONTENT.value()) {
            val refreshResponse = spotifyService.tratarResponse(response, request, sessionUser)
            println(refreshResponse.third.get())
            return result.get()
        }


        return result.get()

    }

}
