package com.dojo.spodfy.repository

import com.dojo.spodfy.table.Mensagem
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository

interface MensagemRepository : CrudRepository<Mensagem, String> {

    @Query("select * from MENSAGEM")
    fun findMessages(): List<Mensagem>
}
