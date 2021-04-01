package com.dojo.spodfy.service.api.spotify

import com.dojo.spodfy.model.EpisodioDetalhe
import com.dojo.spodfy.model.PesquisaSpotifyApi
import com.dojo.spodfy.model.PodcastDetalhe
import com.dojo.spodfy.model.TokenSpotifyApi
import com.dojo.spodfy.repository.SessionUserRepository
import com.dojo.spodfy.table.SessionUserSpotify
import com.dojo.spodfy.util.SPOTIFY_API_SEARCH
import com.dojo.spodfy.util.SPOTIFY_API_SEARCH_PAGE
import com.dojo.spodfy.util.SPOTIFY_API_SEARCH_SHOWS
import com.dojo.spodfy.util.SPOTIFY_API_TOKEN
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.ResponseResultOf
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class SpotifyPesquisaService(val db: SessionUserRepository) {

    private val gson = Gson()
    private val util: SpotifyRequestUtil = SpotifyRequestUtil()

    /**
     * @apiNote Esse meteodo identifica o usuario e faz a requisicao para a API do Spotify para buscar os podcast
     */
    fun pesquisarPodcasts(keyword: String?, nrIpAdress: String?): PesquisaSpotifyApi? {
        //identificar o IP, regatar o token e fazer a pesquisa
        val sessionUser: SessionUserSpotify? = db.findbyNrIpUser(nrIpAdress)
        val (request, response, result) = Fuel.get("$SPOTIFY_API_SEARCH?q=$keyword&type=show&market=BR")
            .header(Headers.AUTHORIZATION, "Bearer ${sessionUser?.access_token}")
            .responseString()

        if (response.statusCode != HttpStatus.OK.value()) {
            val refreshResponse = tratarResponse(response, request, sessionUser)
            println(refreshResponse.third.get())
            return gson.fromJson(refreshResponse.third.get(), PesquisaSpotifyApi::class.java)
        }

        return gson.fromJson(result.get(), PesquisaSpotifyApi::class.java)


    }

    fun pesquisarEpisodios(id: String?, nrIpAdress: String?): Any? {
        //identificar o IP, regatar o token e fazer a pesquisa
        val sessionUser: SessionUserSpotify? = db.findbyNrIpUser(nrIpAdress)
        val (request, response, result) = Fuel.get("$SPOTIFY_API_SEARCH_SHOWS$id")
            .header(Headers.AUTHORIZATION, "Bearer ${sessionUser?.access_token}")
            .responseString()

        if (response.statusCode != HttpStatus.OK.value()) {
            val refreshResponse = tratarResponse(response, request, sessionUser)
            println(refreshResponse.third.get())
            return gson.fromJson(refreshResponse.third.get(), PodcastDetalhe::class.java)
        }

        return gson.fromJson(
            result.get(),
            PodcastDetalhe::class.java
        ).episodes?.items?.sortedByDescending { it.release_date }

    }

    fun pesquisarEpisodiosPaginado(id: String?, nrIpAdress: String?, page: String?): Any? {
        //identificar o IP, regatar o token e fazer a pesquisa
        val sessionUser: SessionUserSpotify? = db.findbyNrIpUser(nrIpAdress)

        var path: String = String.format(SPOTIFY_API_SEARCH_PAGE, id)
        path += "/?offset=${page ?: "0"}&limit=20"

        val (request, response, result) = Fuel.get(path)
            .header(Headers.AUTHORIZATION, "Bearer ${sessionUser?.access_token}")
            .responseString()

        if (response.statusCode != HttpStatus.OK.value()) {
            val refreshResponse = tratarResponse(response, request, sessionUser)
            println(refreshResponse.third.get())
            return gson.fromJson(refreshResponse.third.get(), EpisodioDetalhe::class.java)
        }

        return gson.fromJson(
            result.get(),
            EpisodioDetalhe::class.java
        ).items?.sortedByDescending { it.release_date }

    }

    private fun tratarResponse(
        response: Response,
        request: Request,
        sessionUser: SessionUserSpotify?
    ): ResponseResultOf<String> {
        when (response.statusCode) {
            HttpStatus.BAD_REQUEST.value() -> throw Exception("Response Spotify Api: ${response.data}")
            HttpStatus.UNAUTHORIZED.value() -> {
                //refresh token pls
                val newToken: String? = atualizarTokenDeAcesso(sessionUser)
                request[Headers.AUTHORIZATION] = "Bearer $newToken"

                return request.responseString()
            }
        }

        return request.responseString()
    }

    private fun atualizarTokenDeAcesso(sessionUser: SessionUserSpotify?): String? {
        val refreshToken = sessionUser?.refresh_token ?: throw Exception("Token expirado, necessario login novamente")

        val (_, response, result) = Fuel.post(
            SPOTIFY_API_TOKEN,
            util.preparaBodyRequisicaoRefreshToken(refreshToken)
        )
            .header(Headers.CONTENT_TYPE, "application/x-www-form-urlencoded")
            .header(Headers.AUTHORIZATION, util.prepararHeaderCredentialsID())
            .responseString()

        when (result) {
            is Result.Success -> {
                // requisicao ocorreu com sucesso, transformar em um objeto e salvar
                val token: TokenSpotifyApi = gson.fromJson(result.get(), TokenSpotifyApi::class.java)

                sessionUser.access_token = token.access_token
                sessionUser.refresh_token = token.refresh_token
                sessionUser.expires_in = token.expires_in
                db.save(sessionUser)


            }
            else -> throw Exception("Response Spotify Api: ${response.data}")
        }

        return sessionUser.access_token
    }

}
