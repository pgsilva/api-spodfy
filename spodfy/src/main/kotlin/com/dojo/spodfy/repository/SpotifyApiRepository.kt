package com.dojo.spodfy.interfaces

import com.dojo.spodfy.model.PesquisaSpotifyApiDto
import com.dojo.spodfy.util.SPOTIFY_API

import com.github.kittinunf.fuel.core.Headers
import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(value = "spotifyApi", url = SPOTIFY_API)
interface SpotifyApiClient {

    @GetMapping("search")
    fun getSearch(
        @RequestHeader(Headers.AUTHORIZATION) token: String,
        @RequestParam("q") query: String,
        @RequestParam("type") type: String,
        @RequestParam("market") market: String,
        @RequestParam("include_external") include_external: String
    ): PesquisaSpotifyApiDto
}