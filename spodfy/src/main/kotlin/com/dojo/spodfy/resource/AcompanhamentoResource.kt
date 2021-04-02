package com.dojo.spodfy.resource

import com.dojo.spodfy.model.AcompanhamentoDto
import com.dojo.spodfy.service.AcompanhamentoService
import com.dojo.spodfy.table.Acompanhamento
import com.dojo.spodfy.util.BaseResource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/acompanhamento")
class AcompanhamentoResource(val acompanhamentoService: AcompanhamentoService) : BaseResource() {

    @GetMapping
    fun listarTodosAcompanhamentos(): List<Acompanhamento> =
        acompanhamentoService.listarTodosAcompanhamentos()

    @DeleteMapping("/{idAcompanhamento}")
    fun deletarAcompanhamento(@PathVariable idAcompanhamento: Long): Any =
        acompanhamentoService.excluirAcompanhamento(idAcompanhamento)


    @PostMapping("/usuario/{idUsuario}")
    fun adicionarAcompanhamento(
        @RequestBody dto: AcompanhamentoDto,
        @PathVariable idUsuario: Long
    ): ResponseEntity<Acompanhamento>? {
        return try {
            ResponseEntity.ok(acompanhamentoService.salvarAcompanhamento(dto, idUsuario))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PutMapping("/usuario/{idUsuario}")
    fun atualizarAcompanhamento(
        @RequestBody dto: AcompanhamentoDto,
        @PathVariable idUsuario: Long
    ): ResponseEntity<Acompanhamento>? {
        return try {
            ResponseEntity.ok(acompanhamentoService.salvarAcompanhamento(dto, idUsuario))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

}