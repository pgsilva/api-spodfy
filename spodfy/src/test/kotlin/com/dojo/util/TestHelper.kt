package com.dojo.util

import com.fasterxml.jackson.databind.JsonNode
import org.apache.logging.log4j.util.Strings
import org.springframework.core.io.ClassPathResource
import java.io.File
import java.io.FileReader

import com.google.gson.Gson
import com.google.gson.stream.JsonReader


fun prepararJsonPutPlay(): String {
    val gson = Gson()
    val reader = JsonReader(FileReader("resources/payload/put_play.json"))
    return gson.toJson(reader)
}

