package com.dojo.spodfy.resource

import com.dojo.spodfy.util.BaseResource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckResource() : BaseResource() {

    @GetMapping("/isAlive")
    fun getHealthCheck(): String = "Im alive"

}