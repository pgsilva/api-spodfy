package com.dojo.spodfy.service.api.spotify

import com.dojo.spodfy.model.TokenSpotifyApi
import com.dojo.spodfy.repository.SessionUserRepository
import com.dojo.spodfy.table.SessionUserSpotify
import com.dojo.spodfy.util.SPOTIFY_API_TOKEN
import com.dojo.spodfy.util.SPOTIFY_MESSAGE_LOGIN
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import org.springframework.stereotype.Service


@Service
class SpotifyService(val db: SessionUserRepository) {

    private val gson = Gson()
    private val util: SpotifyRequestUtil = SpotifyRequestUtil()

    /**
     * @apiNote Esse meteodo recebe o code de permissao que a api retornou
     * e faz a requisicao para o token do oauth inclusive salva no banco
     */
    fun atualizaPermissaoUsuarioLogado(code: String?, nrIpUser: String?): String {

        val (_, _, result) = Fuel.post(SPOTIFY_API_TOKEN, util.preparaBodyRequisicaoToken(code))
            .header(Headers.CONTENT_TYPE, "application/x-www-form-urlencoded")
            .header(Headers.AUTHORIZATION, util.prepararHeaderCredentialsID())
            .responseString()

        when (result) {
            is Result.Failure -> {
                val ex = result.getException()
                throw Exception(ex)
            }
            is Result.Success -> {
                // requisicao ocorreu com sucesso, transformar em um objeto e salvar
                val token: TokenSpotifyApi = gson.fromJson(result.get(), TokenSpotifyApi::class.java)
                val entity = SessionUserSpotify(
                    id = null,
                    nr_ip_user = nrIpUser,
                    access_token = token.access_token,
                    token_type = token.token_type,
                    expires_in = token.expires_in,
                    refresh_token = token.refresh_token,
                    scope = token.scope
                )

                db.deleteByIp(nrIpUser)
                db.save(entity)
            }
        }
        return SPOTIFY_MESSAGE_LOGIN
    }


    fun findAllSessionUsers(): List<SessionUserSpotify> {
        return db.findAll().toList()
    }

}