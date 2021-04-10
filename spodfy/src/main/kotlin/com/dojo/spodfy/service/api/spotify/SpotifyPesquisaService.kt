package com.dojo.spodfy.service.api.spotify

import com.dojo.spodfy.model.EpisodioDetalheDto
import com.dojo.spodfy.model.PesquisaSpotifyApiDto
import com.dojo.spodfy.model.PodcastDetalheDto
import com.dojo.spodfy.repository.SessionUserRepository
import com.dojo.spodfy.table.SessionUserSpotify
import com.dojo.spodfy.util.SPOTIFY_API_SEARCH
import com.dojo.spodfy.util.SPOTIFY_API_SEARCH_PAGE
import com.dojo.spodfy.util.SPOTIFY_API_SEARCH_SHOWS
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class SpotifyPesquisaService(val db: SessionUserRepository) {

    private val gson = Gson()

    @set:Autowired
    lateinit var spotifyService: SpotifyService


    /**
     * @apiNote Esse meteodo identifica o usuario e faz a requisicao para a API do Spotify para buscar os podcast
     */
    fun pesquisarPodcasts(keyword: String?, idUsuario: Long?): PesquisaSpotifyApiDto? {
        val sessionUser: SessionUserSpotify? = db.findByUsuarioIdUsuario(idUsuario)

        val (request, response, result) = Fuel.get("$SPOTIFY_API_SEARCH?q=$keyword&type=show&market=BR&include_external=N")
            .header(Headers.AUTHORIZATION, "Bearer ${sessionUser?.accessToken}")
            .responseString()

        if (response.statusCode != HttpStatus.OK.value()) {
            val refreshResponse = spotifyService.tratarResponse(response, request, sessionUser)
            println(refreshResponse.third.get())
            return gson.fromJson(refreshResponse.third.get(), PesquisaSpotifyApiDto::class.java)
        }

        return gson.fromJson(result.get(), PesquisaSpotifyApiDto::class.java)


    }

    fun pesquisarEpisodios(idPodcast: String?, idUsuario: Long?): Any? {
        val sessionUser: SessionUserSpotify? = db.findByUsuarioIdUsuario(idUsuario)
        val (request, response, result) = Fuel.get("$SPOTIFY_API_SEARCH_SHOWS$idPodcast")
            .header(Headers.AUTHORIZATION, "Bearer ${sessionUser?.accessToken}")
            .responseString()

        if (response.statusCode != HttpStatus.OK.value()) {
            val refreshResponse = spotifyService.tratarResponse(response, request, sessionUser)
            println(refreshResponse.third.get())
            return gson.fromJson(refreshResponse.third.get(), PodcastDetalheDto::class.java)
        }

        return gson.fromJson(
            result.get(),
            PodcastDetalheDto::class.java
        ).episodes?.items?.sortedByDescending { it.release_date }

    }

    fun pesquisarEpisodiosPaginado(idPodcast: String?, idUsuario: Long?, page: String?): Any? {
        val sessionUser: SessionUserSpotify? = db.findByUsuarioIdUsuario(idUsuario)

        var path: String = String.format(SPOTIFY_API_SEARCH_PAGE, idPodcast)
        path += "/?offset=${page ?: "0"}&limit=20"

        val (request, response, result) = Fuel.get(path)
            .header(Headers.AUTHORIZATION, "Bearer ${sessionUser?.accessToken}")
            .responseString()

        if (response.statusCode != HttpStatus.OK.value()) {
            val refreshResponse = spotifyService.tratarResponse(response, request, sessionUser)
            println(refreshResponse.third.get())
            return gson.fromJson(refreshResponse.third.get(), EpisodioDetalheDto::class.java)
        }

        return gson.fromJson(
            result.get(),
            EpisodioDetalheDto::class.java
        ).items?.sortedByDescending { it.release_date }

    }


}
