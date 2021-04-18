package com.dojo.spodfy.service

import com.dojo.spodfy.model.FavoritoDto
import com.dojo.spodfy.model.PodcastPesquisaDto
import com.dojo.spodfy.model.TokenSpotifyApiDto
import com.dojo.spodfy.repository.FavoritoRepository
import com.dojo.spodfy.repository.PodcastRepository
import com.dojo.spodfy.repository.SessionUserRepository
import com.dojo.spodfy.repository.UsuarioRepository
import com.dojo.spodfy.service.api.spotify.SpotifyService
import com.dojo.spodfy.table.Favorito
import com.dojo.spodfy.table.Podcast
import com.dojo.spodfy.table.SessionUserSpotify
import com.dojo.spodfy.table.Usuario
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class FavoritoService(
    val db: FavoritoRepository,
    val dbPodcast: PodcastRepository,
    val dbUsuario: UsuarioRepository
) {

    @set:Autowired
    lateinit var podcastService: PodcastService

    fun listarTodosFavoritos(): List<Favorito> {
        return db.findAll().toList()
    }

    fun salvarFavorito(dto: FavoritoDto, idUsuario: Long): Favorito? {
        //identificar o IP
        val usuario: Optional<Usuario> = dbUsuario.findById(idUsuario)

        //logica de salvamento
        val favorito: Favorito = Favorito(
            observacao = dto.observacao,
            diaSemana = dto.diaSemana,
            horario = dto.horario
        )
        favorito.usuario = usuario.get()
        dto.podcast?.let {
            favorito.podcast = podcastService.atualizarPodcast(it)
        }

        //salvar se por acaso o form ja existir
        favorito.idFavorito = dto.idFavorito

        return db.save(favorito)
    }

    fun excluirFavorito(idFavorito: Long): Any {
        return db.deleteById(idFavorito)
    }

    fun pesquisarPodcastPorIDSpotify(idPodcast: String?): Podcast? {
        return dbPodcast.findByIdPodcastSpotify(idPodcast)
    }

    fun salvarNumeroTotalDePodcasts(pesquisaPodcast: PodcastPesquisaDto, podcastFisico: Podcast) {
        podcastFisico.totalEpisodios = pesquisaPodcast.total_episodes
        dbPodcast.saveAndFlush(podcastFisico)

    }

    fun listarTodosFavoritosPorUsuarioID(idUsuario: Long): List<Favorito?>? {
        return db.findAllByUsuarioIdUsuario(idUsuario).toList()
    }

    fun listarFavoritoPorId(idFavorito: Long): Any {
        return db.findById(idFavorito)
    }


}