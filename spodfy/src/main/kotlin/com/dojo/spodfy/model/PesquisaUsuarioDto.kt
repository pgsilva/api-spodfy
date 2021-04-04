package com.dojo.spodfy.model

data class PesquisaUsuarioDto(
    val href: String?,
    val items: List<PodcastPesquisaUsuarioDto>?,
    val limit: Int?,
    val next: String?,
    val offset: Int?,
    val previous: String?,
    val total: Int?,
)