package com.dojo.spodfy.model

data class PodcastDetalheDto(
    val available_markets: List<String>?,
    val copyrights: List<String>?,
    val description: String?,
    val episodes: EpisodioDetalheDto?,
    val is_externally_hosted: Boolean?,
    val languages: List<String>?,
    val media_type: String?,
    val name: String?,
    val publisher: String?,
    val total_episodes: Int?,
    val type: String?,
    val uri: String?,
)