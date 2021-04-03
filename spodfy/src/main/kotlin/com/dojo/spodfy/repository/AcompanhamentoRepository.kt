package com.dojo.spodfy.repository

import com.dojo.spodfy.table.Acompanhamento
import org.springframework.data.jpa.repository.JpaRepository


interface AcompanhamentoRepository : JpaRepository<Acompanhamento, Long> {

    fun findAllByUsuarioIdUsuario(idUsuario: Long): List<Acompanhamento?>
}