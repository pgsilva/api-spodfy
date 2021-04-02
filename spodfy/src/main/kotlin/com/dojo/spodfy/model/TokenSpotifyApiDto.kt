package com.dojo.spodfy.model

data class TokenSpotifyApiDto(
    val access_token: String? = null,
    val token_type: String? = null,
    val expires_in: Long? = null,
    val refresh_token: String? = null,
    val scope: String? = null
)
