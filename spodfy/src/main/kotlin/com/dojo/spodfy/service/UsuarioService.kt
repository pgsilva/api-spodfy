package com.dojo.spodfy.service

import com.dojo.spodfy.model.TokenSpotifyApiDto
import com.dojo.spodfy.repository.SessionUserRepository
import com.dojo.spodfy.repository.UsuarioRepository
import com.dojo.spodfy.table.SessionUserSpotify
import com.dojo.spodfy.table.Usuario
import org.springframework.stereotype.Service

@Service
class UsuarioService(val db: UsuarioRepository, val dbSession: SessionUserRepository) {

    fun listarTodosUsuarios(): List<Usuario>? {
        return db.findAll().toList()

    }

    fun deletarUsuario(idUsuario: Long): Any {
        dbSession.deleteByUsuarioIdUsuario(idUsuario)
        return db.deleteById(idUsuario)
    }


    fun buscarPorUsuarioId(idUsuario: Long): Usuario? {
        db.findById(idUsuario).let {
            return it.get()
        }
    }

    fun salvarSessionUserGenerico(tokenDto: TokenSpotifyApiDto) {
        val session: SessionUserSpotify = dbSession.findByNrIpUser("127.0.0.1") ?: SessionUserSpotify()
        session.nrIpUser = "127.0.0.1"
        session.accessToken = tokenDto.access_token
        session.tokenType = tokenDto.token_type
        session.expiresIn = tokenDto.expires_in
        session.refreshToken = tokenDto.refresh_token
        session.scope = tokenDto.scope

        dbSession.saveAndFlush(session)

    }


}
