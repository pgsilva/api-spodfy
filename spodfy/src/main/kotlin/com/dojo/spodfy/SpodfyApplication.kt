package com.dojo.spodfy

import com.dojo.spodfy.configuration.LogInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
class SpodfyApplication

fun main(args: Array<String>) {
    runApplication<SpodfyApplication>(*args)
}


@Configuration
class AppConfig : WebMvcConfigurer {

    @Autowired
    lateinit var logInterceptor: LogInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(logInterceptor);
    }
}