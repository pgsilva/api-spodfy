package com.dojo.spodfy.repository

import com.dojo.spodfy.table.SessionUserSpotify
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface SessionUserRepository : CrudRepository<SessionUserSpotify, String> {

    @Transactional
    @Modifying
    @Query("delete from SESSIONUSERSPOTIFY s where s.nr_ip_user = :nrIpUser")
    fun deleteByIp(@Param("nrIpUser") nrIpUser: String?)

    @Query("select * from SESSIONUSERSPOTIFY s where s.nr_ip_user = :nrIpUser ")
    fun findbyNrIpUser(@Param("nrIpUser") nrIpUser: String?): SessionUserSpotify?


}