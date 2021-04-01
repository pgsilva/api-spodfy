package com.dojo.spodfy.resource

import com.dojo.spodfy.service.MensagemService
import com.dojo.spodfy.table.Mensagem
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping("/mensagem")
class MensagemResource(val service: MensagemService) : BaseResource() {

    @GetMapping
    fun index(): List<Mensagem> = service.findMessages()

    @PostMapping
    fun post(@RequestBody message: Mensagem) {
        service.post(message)
    }
}