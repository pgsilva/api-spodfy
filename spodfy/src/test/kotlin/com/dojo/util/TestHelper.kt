package com.dojo.util

import com.dojo.spodfy.table.Usuario
import java.io.FileNotFoundException

fun loadResource(name: String): String {
    return object {}.javaClass.classLoader?.getResource(name)?.readText() ?: throw FileNotFoundException()
}

fun prepararJsonPutPlay(): String = loadResource("payload/put_play.json")

fun prepararListaFavorito(): String = loadResource("payload/favoritos.json")

fun prepararFavorito(): String = loadResource("payload/favorito.json")

fun prepararFavoritoForm(): String = loadResource("payload/form_favorito.json")

fun prepararFavoritoFormPut(): String = loadResource("payload/form_favorito_put.json")

