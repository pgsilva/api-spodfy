package com.dojo.spodfy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpodfyApplication

fun main(args: Array<String>) {
	runApplication<SpodfyApplication>(*args)
}
