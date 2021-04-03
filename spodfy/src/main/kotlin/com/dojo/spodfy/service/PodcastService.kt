package com.dojo.spodfy.service

import com.dojo.spodfy.model.PesquisaSpotifyApiDto
import com.dojo.spodfy.repository.PodcastRepository
import com.dojo.spodfy.repository.SessionUserRepository
import com.dojo.spodfy.service.api.spotify.SpotifyRequestUtil
import com.dojo.spodfy.service.api.spotify.SpotifyService
import com.dojo.spodfy.table.Podcast
import com.dojo.spodfy.table.SessionUserSpotify
import com.dojo.spodfy.util.SPOTIFY_API_SEARCH
import com.dojo.spodfy.util.SPOTIFY_API_SHOWS_ME
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class PodcastService(val db: PodcastRepository, val dbSessionUserRepository: SessionUserRepository) {


    private val gson = Gson()
    private val util: SpotifyRequestUtil = SpotifyRequestUtil()

    @set:Autowired
    lateinit var spotifyService: SpotifyService

    fun listarTodosPodcast(): List<Podcast> {
        return db.findAll().toList()
    }

    fun listarTodosPodcastSpotifyPorUsuarioID(idUsuario: Long?): PesquisaSpotifyApiDto? {
        val sessionUser: SessionUserSpotify? = dbSessionUserRepository.findByUsuarioIdUsuario(idUsuario)

        val (request, response, result) = Fuel.get(SPOTIFY_API_SHOWS_ME)
            .header(Headers.AUTHORIZATION, "Bearer ${sessionUser?.accessToken}")
            .responseString()

        if (response.statusCode != HttpStatus.OK.value()) {
            val refreshResponse = spotifyService.tratarResponse(response, request, sessionUser)
            println(refreshResponse.third.get())
            return gson.fromJson(refreshResponse.third.get(), PesquisaSpotifyApiDto::class.java)
        }

        return gson.fromJson(result.get(), PesquisaSpotifyApiDto::class.java)

    }
}