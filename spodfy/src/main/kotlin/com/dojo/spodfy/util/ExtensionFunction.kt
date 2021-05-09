package com.dojo.spodfy.util

import com.dojo.spodfy.table.Favorito
import com.dojo.spodfy.util.ExtensionFunction.jsonToFavorito
import com.dojo.spodfy.util.ExtensionFunction.jsonToMutableListFavorito
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object ExtensionFunction {
    private val gson = Gson()

    fun String.jsonToMutableListFavorito(): MutableList<Favorito> =
        gson.fromJson(this, object : TypeToken<MutableList<Favorito?>?>() {}.type)


    fun String.jsonToFavorito(): Favorito = gson.fromJson(this, object : TypeToken<Favorito?>() {}.type)

}