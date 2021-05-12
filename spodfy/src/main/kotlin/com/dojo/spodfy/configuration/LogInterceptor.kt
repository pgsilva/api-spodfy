package com.dojo.spodfy.configuration

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class LogInterceptor : HandlerInterceptor {

    val log: Logger = LoggerFactory.getLogger(LogInterceptor::class.java);

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, dataObject: Any): Boolean {

        log.warn("[LOG] Requested at: ${Date()} ${request.method.toUpperCase()} ${request.requestURI} by ${request.remoteAddr}")
        return true
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        dataObject: Any,
        model: ModelAndView?
    ) {
        log.warn("[LOG] Response from ${request.remoteAddr}, status: ${response.status}")
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        dataObject: Any,
        e: Exception?
    ) {
        log.warn(
            "[LOG] Request from ${request.remoteAddr} completed! params: " +
                    "${
                        request.parameterMap.forEach {
                            log.warn("key: ${it.key} |value:${it.value.firstOrNull()}")
                        }
                    }"
        )
        
    }
}