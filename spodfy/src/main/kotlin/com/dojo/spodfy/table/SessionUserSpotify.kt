package com.dojo.spodfy.table

import javax.persistence.*

@Entity
data class SessionUserSpotify(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(insertable = false, updatable = false)
    var idSessionUserSpotify: Long? = 0,
    var nrIpUser: String? = "",
    var accessToken: String? = "",
    var tokenType: String? = "",
    var expiresIn: Long? = 0,
    var refreshToken: String? = "",
    var scope: String? = "",

    @ManyToOne(fetch = FetchType.EAGER, cascade = arrayOf(CascadeType.PERSIST))
    @JoinColumn(name = "idUsuario")
    var usuario: Usuario? = null,
)