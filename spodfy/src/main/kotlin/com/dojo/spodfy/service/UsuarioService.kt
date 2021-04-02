package com.dojo.spodfy.service

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


}
