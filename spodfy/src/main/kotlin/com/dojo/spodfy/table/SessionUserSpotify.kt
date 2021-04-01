package com.dojo.spodfy.table

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table("SESSIONUSERSPOTIFY")
data class SessionUserSpotify(
    @Id var id: Long?,
    val nr_ip_user: String?,
    var access_token: String?,
    val token_type: String?,
    var expires_in: Long?,
    var refresh_token: String?,
    val scope: String?,
)