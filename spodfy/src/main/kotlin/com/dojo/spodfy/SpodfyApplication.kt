package com.dojo.spodfy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class SpodfyApplication

fun main(args: Array<String>) {
	runApplication<SpodfyApplication>(*args)
}
