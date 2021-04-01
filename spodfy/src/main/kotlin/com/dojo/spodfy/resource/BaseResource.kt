package com.dojo.spodfy.resource

import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.HttpServletRequest


open class BaseResource {
    companion object {
        const val TOKEN_HEADER = "Authorization"
        const val MEDIA_TYPE_HEADER = "media-type"
    }

    @set:Autowired
    lateinit var request: HttpServletRequest

    protected fun getCompactToken(): String? {
        return request.getHeader(TOKEN_HEADER)
    }

    protected fun getMediaType(): String? {
        return request.getHeader(MEDIA_TYPE_HEADER)
    }

    protected fun getNrIpAdress(): String? {
        return request.remoteAddr
    }

    protected fun getDsUserAgent(): String? {
        return request.getHeader("User-Agent")
    }
}