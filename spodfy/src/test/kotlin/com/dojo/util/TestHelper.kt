package com.dojo.util

import com.fasterxml.jackson.databind.JsonNode
import org.apache.logging.log4j.util.Strings
import org.springframework.core.io.ClassPathResource
import java.io.File
import java.io.FileReader

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.FileNotFoundException

fun loadResource(name: String): String {
    return object {}.javaClass.classLoader?.getResource(name)?.readText() ?: throw FileNotFoundException()
}

fun prepararJsonPutPlay(): String = loadResource("payload/put_play.json")


