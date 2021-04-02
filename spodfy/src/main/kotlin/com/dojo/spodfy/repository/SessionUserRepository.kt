package com.dojo.spodfy.repository

import com.dojo.spodfy.table.SessionUserSpotify
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface SessionUserRepository : JpaRepository<SessionUserSpotify, Long> {

    @Transactional
    @Modifying
//  @Query("delete from SessionUserSpotify s where s.usuario.idUsuario = :idUsuario")
    fun deleteByUsuarioIdUsuario(@Param("idUsuario") idUsuario: Long?)

    //@Query("select * from SessionUserSpotify s where s.nrIpUser = :nrIpUser ")
    //fun findByNrIpUser(@Param("nrIpUser") nrIpUser: String?): SessionUserSpotify?

    fun findByUsuarioIdUsuario(idUsuario: Long?): SessionUserSpotify?
    fun findByNrIpUser(s: String): SessionUserSpotify?


}