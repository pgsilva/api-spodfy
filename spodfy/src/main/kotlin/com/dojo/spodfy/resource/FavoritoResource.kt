package com.dojo.spodfy.resource

import com.dojo.spodfy.model.FavoritoDto
import com.dojo.spodfy.service.FavoritoService
import com.dojo.spodfy.table.Favorito
import com.dojo.spodfy.util.BaseResource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/favorito")
class FavoritoResource(val favoritoService: FavoritoService) : BaseResource() {

    @GetMapping
    fun listarTodosFavoritos(): List<Favorito> =
        favoritoService.listarTodosFavoritos()

    @DeleteMapping("/{idFavorito}")
    fun deletarFavorito(@PathVariable idFavorito: Long): Any =
        favoritoService.excluirFavorito(idFavorito)

    @GetMapping("/{idFavorito}")
    fun listarFavoritoPorId(@PathVariable idFavorito: Long): Any =
        favoritoService.listarFavoritoPorId(idFavorito)



    @PostMapping("/usuario/{idUsuario}")
    fun adicionarFavorito(
        @RequestBody dto: FavoritoDto,
        @PathVariable idUsuario: Long
    ): ResponseEntity<Favorito>? {
        return try {
            ResponseEntity.ok(favoritoService.salvarFavorito(dto, idUsuario))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    fun listarTodosFavoritosPorUsuarioID(
        @PathVariable idUsuario: Long
    ): ResponseEntity<List<Favorito?>>? {
        return try {
            ResponseEntity.ok(favoritoService.listarTodosFavoritosPorUsuarioID(idUsuario))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PutMapping("/usuario/{idUsuario}")
    fun atualizarFavorito(
        @RequestBody dto: FavoritoDto,
        @PathVariable idUsuario: Long
    ): ResponseEntity<Favorito>? {
        return try {
            ResponseEntity.ok(favoritoService.salvarFavorito(dto, idUsuario))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

}