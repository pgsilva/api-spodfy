package com.dojo.spodfy.service

import com.dojo.spodfy.repository.MensagemRepository
import com.dojo.spodfy.table.Mensagem
import org.springframework.stereotype.Service

@Service
class MensagemService(val db: MensagemRepository) {

    fun findMessages(): List<Mensagem> = db.findMessages()

    fun post(message: Mensagem){
        db.save(message)
    }
}