package com.dojo.spodfy.table

import javax.persistence.*

@Entity
data class Podcast(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    var idPodcast: Long? = 0,

    var idPodcastSpotify: String? = "",
    var nome: String? = "",

    @Column(length = 500)
    var descricao: String? = "",
    var href: String? = "",
    var urlImagem: String? = "",
    var publicadora: String? = "",
    var conteudoExplicito: Boolean? = false,

    //validar se corresponde quando um novo é adicionado pela API
    var totalEpisodios: Long? = 0,
)