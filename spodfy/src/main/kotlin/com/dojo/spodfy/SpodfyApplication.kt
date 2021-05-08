package com.dojo.spodfy

import com.dojo.spodfy.configuration.LogInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableScheduling
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

@Configuration
@EnableSwagger2
class SwaggerConfiguration {
    @Bean
    open fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.dojo.spodfy.resource"))
        .paths(PathSelectors.any())
        .build()
}