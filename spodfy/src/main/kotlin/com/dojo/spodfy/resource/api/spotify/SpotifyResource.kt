package com.dojo.spodfy.resource.api.spotify

import com.dojo.spodfy.util.BaseResource
import com.dojo.spodfy.service.api.spotify.SpotifyService
import com.dojo.spodfy.table.SessionUserSpotify
import com.dojo.spodfy.table.Usuario
import com.dojo.spodfy.util.SPOTIFY_CLIENT_ID
import com.dojo.spodfy.util.SPOTIFY_REDIRECT_URI
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@RestController
class SpotifyResource(val spotifyService: SpotifyService) : BaseResource() {

    @GetMapping("/redirect_login")
    fun redirectLoginSpotify(): ResponseEntity<Any>? {
        val scopes: String =
            "user-read-private " +
                    "user-read-email " +
                    "user-read-playback-state " +
                    "user-modify-playback-state " +
                    "user-read-currently-playing " +
                    "streaming app-remote-control " +
                    "user-follow-read " +
                    "user-top-read " +
                    "user-library-read"

        var projectUrl: String? = "https://accounts.spotify.com/authorize?response_type=code"
        projectUrl += "&client_id=$SPOTIFY_CLIENT_ID"
        projectUrl += "&scope=${URLEncoder.encode(scopes, StandardCharsets.UTF_8.toString())}"
        projectUrl += "&redirect_uri=${URLEncoder.encode(SPOTIFY_REDIRECT_URI, StandardCharsets.UTF_8.toString())}"

        return try {
            //ResponseEntity.status(302).header("Location", projectUrl).build()
            ResponseEntity.ok(projectUrl)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(500).build()
        }
    }

    @GetMapping("/callback")
    fun callbackLoginSpotify(
        @RequestParam code: String?,
        @RequestParam(required = false) error: String?,
        @RequestParam(required = false) state: String?
    ): ResponseEntity<Usuario>? {
        return try {
            if (!error.isNullOrEmpty())
                throw Exception("Ocorreu um erro ao recuperar permissão.")

            ResponseEntity.ok(spotifyService.atualizaPermissaoUsuarioLogado(code, getNrIpAdress()))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/sessoes")
    fun buscarTodasAsSessionsUsers(): List<SessionUserSpotify> = spotifyService.buscarTodasAsSessionsUsers()

    @GetMapping("/sessoes/usuario/{idUsuario}")
    fun buscarTodasAsSessionsUsersPorUsuarioId(@PathVariable idUsuario: Long): SessionUserSpotify? =
        spotifyService.buscarSessionUserPorUsuarioId(idUsuario)
}