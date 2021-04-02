package com.dojo.spodfy.model

class UsuarioSpotifyDTO(
    val country: String?,
    val display_name: String?,
    val email: String?,
    val explicit_content: Map<String, String>?,
    val external_urls: Map<String, String>?,
    val followers: Map<String, String>?,
    val href: String?,
    val id: String?,
    val images: List<ImagemSpotifyDto>?,
    val product: String?,
    val type: String?,
    val uri: String?
)