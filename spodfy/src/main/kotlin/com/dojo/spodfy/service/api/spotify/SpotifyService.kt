package com.dojo.spodfy.service.api.spotify

import com.dojo.spodfy.model.TokenSpotifyApiDto
import com.dojo.spodfy.model.UsuarioSpotifyDTO
import com.dojo.spodfy.repository.SessionUserRepository
import com.dojo.spodfy.repository.UsuarioRepository
import com.dojo.spodfy.table.SessionUserSpotify
import com.dojo.spodfy.table.Usuario
import com.dojo.spodfy.util.SPOTIFY_API_ME
import com.dojo.spodfy.util.SPOTIFY_API_TOKEN
import com.dojo.spodfy.util.SPOTIFY_MESSAGE_LOGIN
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import org.springframework.stereotype.Service

import org.springframework.http.HttpStatus
import java.util.*


@Service
class SpotifyService(val db: SessionUserRepository, val dbUsuario: UsuarioRepository) {

    private val gson = Gson()
    private val util: SpotifyRequestUtil = SpotifyRequestUtil()

    /**
     * @apiNote Esse meteodo recebe o code de permissao que a api retornou
     * e faz a requisicao para o token do oauth inclusive salva no banco
     */
    fun atualizaPermissaoUsuarioLogado(code: String?, nrIpUser: String?): Usuario? {

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
                val tokenDto: TokenSpotifyApiDto = gson.fromJson(result.get(), TokenSpotifyApiDto::class.java)
                /*
                * Agora que temos o token eh necessario recuperar as informacoes do usuario
                * evitando criacao de tabelas desnecessarias e aproveitando o controle de usuario do spotify
                *
                * */
                val usuario: Usuario = recuperarInfoUsuarioAutenticado(tokenDto)
                val entity = SessionUserSpotify(
                    idSessionUserSpotify = null,
                    nrIpUser = nrIpUser,
                    accessToken = tokenDto.access_token,
                    tokenType = tokenDto.token_type,
                    expiresIn = tokenDto.expires_in,
                    refreshToken = tokenDto.refresh_token,
                    scope = tokenDto.scope
                )
                entity.usuario = usuario

                db.deleteByUsuarioIdUsuario(usuario.idUsuario)
                db.saveAndFlush(entity)

                return usuario
            }
        }
    }

    private fun recuperarInfoUsuarioAutenticado(tokenDto: TokenSpotifyApiDto?): Usuario {
        /*vamos criar ou atualizar o usuario que permitiu o acesso*/

        val (_, response, result) = Fuel.get(SPOTIFY_API_ME)
            .header(Headers.AUTHORIZATION, "Bearer ${tokenDto?.access_token}")
            .responseString()

        if (response.statusCode != HttpStatus.OK.value()) throw Exception("Response Spotify Api: ${response.data}")

        val usuarioSpotify: UsuarioSpotifyDTO = gson.fromJson(result.get(), UsuarioSpotifyDTO::class.java)

        //verifica se o usuario ja nao existe, se nao cria um novo
        val usuario: Usuario = dbUsuario.findAllByIdUsuarioSpotify(usuarioSpotify.id) ?: Usuario()
        usuario.idUsuarioSpotify = usuarioSpotify.id
        usuario.pais = usuarioSpotify.country
        usuario.nomeExibicao = usuarioSpotify.display_name
        usuario.email = usuarioSpotify.email
        usuario.urlImagem = usuarioSpotify.images?.firstOrNull()?.url
        usuario.uri = usuarioSpotify.uri

        return dbUsuario.saveAndFlush(usuario)
    }


    fun buscarTodasAsSessionsUsers(): List<SessionUserSpotify> {
        return db.findAll().toList()
    }

    fun buscarSessionUserPorUsuarioId(idUsuario: Long): SessionUserSpotify? {
        return db.findByUsuarioIdUsuario(idUsuario)
    }
}