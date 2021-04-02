package com.dojo.spodfy.model


class EpisodioDto(
    val audio_preview_url: String?,
    val description: String?,
    val duration_ms: Int?,
    val explicit: Boolean?,
    //key: spotify
    val external_urls: Map<String, String>?,
    val href: String?,
    val id: String?,
    val images: List<ImagemSpotifyDto>?,
    val is_externally_hosted: Boolean?,
    val is_playable: Boolean?,
    val language: String?,
    val languages: List<String>?,
    val name: String?,
    val release_date: String?,
    val release_date_precision: String?,
    //key: fully_played resume_position_ms
    val resume_point: Map<String, String>?,
    val type: String?,
    val uri: String?,

    )
