package com.dojo.spodfy.resource

import com.dojo.spodfy.service.UsuarioService
import com.dojo.spodfy.table.Usuario
import com.dojo.spodfy.util.BaseResource
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuario")
class UsuarioResource(val usuarioService: UsuarioService) : BaseResource() {

    @GetMapping
    fun listarTodosUsuarios(): List<Usuario>? = usuarioService.listarTodosUsuarios()

    @GetMapping("/{idUsuario}")
    fun buscarPorUsuarioId(@PathVariable idUsuario: Long): Usuario? =
        usuarioService.buscarPorUsuarioId(idUsuario)

    @DeleteMapping("/{idUsuario}")
    fun deletarUsuario(@PathVariable idUsuario: Long): Any = usuarioService.deletarUsuario(idUsuario)


}