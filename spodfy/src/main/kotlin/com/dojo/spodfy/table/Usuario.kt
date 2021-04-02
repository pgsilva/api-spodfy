package com.dojo.spodfy.table

import javax.persistence.*

@Entity
data class Usuario(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(insertable = false, updatable = false)
    var idUsuario: Long? = 0,

    var idUsuarioSpotify: String? = "",
    var pais: String? = "",
    var nomeExibicao: String? = "",
    var email: String? = "",
    var urlImagem: String? = "",
    var uri: String? = ""
)