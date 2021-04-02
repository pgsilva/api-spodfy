package com.dojo.spodfy.repository

import com.dojo.spodfy.table.Usuario
import org.springframework.data.jpa.repository.JpaRepository

interface UsuarioRepository : JpaRepository<Usuario, Long> {

    fun existsByIdUsuarioSpotify(id: String?): Boolean

    fun findAllByIdUsuarioSpotify(id: String?): Usuario?
}