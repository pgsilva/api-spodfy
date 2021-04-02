package com.dojo.spodfy.model

data class PodcastPesquisaDto(
    val available_markets: List<String>?,
    val copyrights: List<String>?,
    val description: String?,
    val episodes: Map<String, Any>? = null,
    val explicit: Boolean?,
    val external_urls: Map<String, String>?,
    val href: String?,
    val id: String?,
    val images: List<ImagemSpotifyDto>?,
    val is_externally_hosted: Boolean?,
    val languages: List<String>?,
    val media_type: String?,
    val name: String?,
    val publisher: String?,
    val total_episodes: Long?,
    val type: String?,
    val uri: String?,
)