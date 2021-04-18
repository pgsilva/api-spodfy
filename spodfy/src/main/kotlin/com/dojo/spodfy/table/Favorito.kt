package com.dojo.spodfy.table

import javax.persistence.*

@Entity
data class Favorito(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    var idFavorito: Long? = 0,

    @ManyToOne(fetch = FetchType.EAGER, cascade = arrayOf(CascadeType.PERSIST))
    @JoinColumn(name = "idUsuario")
    var usuario: Usuario? = null,

    @ManyToOne(fetch = FetchType.EAGER, cascade = arrayOf(CascadeType.PERSIST))
    @JoinColumn(name = "idPodcast")
    var podcast: Podcast? = null,

    var observacao: String?,
    var diaSemana: String?,
    var horario: String?
)