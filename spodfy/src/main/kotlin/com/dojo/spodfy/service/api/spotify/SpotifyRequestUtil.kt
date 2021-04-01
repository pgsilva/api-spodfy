package com.dojo.spodfy.service.api.spotify

import com.dojo.spodfy.util.SPOTIFY_CLIENT_ID
import com.dojo.spodfy.util.SPOTIFY_CLIENT_SECRET
import com.dojo.spodfy.util.SPOTIFY_REDIRECT_URI
import org.springframework.stereotype.Component
import java.util.*

@Component
class SpotifyRequestUtil() {

    fun prepararHeaderCredentialsID(): String {
        val credentials: String = "$SPOTIFY_CLIENT_ID:$SPOTIFY_CLIENT_SECRET"
        return "Basic ${Base64.getEncoder().encodeToString(credentials.toByteArray())}"

    }

    fun preparaBodyRequisicaoToken(code: String?): List<Pair<String, String?>> {
        return listOf(
            "grant_type" to "authorization_code",
            "code" to code,
            "redirect_uri" to SPOTIFY_REDIRECT_URI
        )

    }

    fun preparaBodyRequisicaoRefreshToken(refreshToken: String?): List<Pair<String, String?>> {
        return listOf(
            "grant_type" to "refresh_token",
            "refresh_token" to refreshToken,
            "redirect_uri" to SPOTIFY_REDIRECT_URI
        )
    }


}