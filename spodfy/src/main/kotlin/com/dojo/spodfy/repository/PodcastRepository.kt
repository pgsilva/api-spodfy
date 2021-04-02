package com.dojo.spodfy.repository

import com.dojo.spodfy.table.Podcast
import org.springframework.data.jpa.repository.JpaRepository

interface PodcastRepository : JpaRepository<Podcast, Long> {

    fun findByIdPodcastSpotify(id: String?): Podcast?


}