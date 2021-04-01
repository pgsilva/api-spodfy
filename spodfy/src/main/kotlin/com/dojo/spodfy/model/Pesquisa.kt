package com.dojo.spodfy.model

data class Pesquisa(
    val href: String?,
    val items: List<PodcastsPesquisa>?,
    val limit: Int?,
    val next: String?,
    val offset: Int?,
    val previous: String?,
    val total: Int?,
)