package com.dojo.spodfy.model

data class PesquisaDto(
    val href: String?,
    val items: List<PodcastPesquisaDto>?,
    val limit: Int?,
    val next: String?,
    val offset: Int?,
    val previous: String?,
    val total: Int?,
)