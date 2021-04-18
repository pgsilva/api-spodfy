package com.dojo.spodfy.repository

import com.dojo.spodfy.table.Favorito
import org.springframework.data.jpa.repository.JpaRepository


interface FavoritoRepository : JpaRepository<Favorito, Long> {

    fun findAllByUsuarioIdUsuario(idUsuario: Long): List<Favorito?>
}