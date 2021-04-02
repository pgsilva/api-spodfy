package com.dojo.spodfy.service

import com.dojo.spodfy.model.AcompanhamentoDto
import com.dojo.spodfy.model.PodcastPesquisaDto
import com.dojo.spodfy.model.TokenSpotifyApiDto
import com.dojo.spodfy.repository.AcompanhamentoRepository
import com.dojo.spodfy.repository.PodcastRepository
import com.dojo.spodfy.repository.SessionUserRepository
import com.dojo.spodfy.repository.UsuarioRepository
import com.dojo.spodfy.table.Acompanhamento
import com.dojo.spodfy.table.Podcast
import com.dojo.spodfy.table.SessionUserSpotify
import com.dojo.spodfy.table.Usuario
import org.springframework.stereotype.Service
import java.util.*

@Service
class AcompanhamentoService(
    val db: AcompanhamentoRepository,
    val dbSessionUserRepository: SessionUserRepository,
    val dbPodcast: PodcastRepository,
    val dbUsuario: UsuarioRepository
) {

    fun listarTodosAcompanhamentos(): List<Acompanhamento> {
        return db.findAll().toList()
    }

    fun salvarAcompanhamento(dto: AcompanhamentoDto, idUsuario: Long): Acompanhamento? {
        //identificar o IP
        val usuario: Optional<Usuario> = dbUsuario.findById(idUsuario)

        //logica de salvamento
        val acompanhamento: Acompanhamento = Acompanhamento(
            observacao = dto.observacao,
            diaSemana = dto.diaSemana,
            horario = dto.horario
        )
        acompanhamento.usuario = usuario.get()
        dto.podcast?.let {
            acompanhamento.podcast = atualizarPodcast(it)
        }

        //salvar se por acaso o form ja existir
        acompanhamento.idAcompanhamento = dto.idAcompanhamento

        return db.save(acompanhamento)
    }

    fun excluirAcompanhamento(idAcompanhamento: Long): Any {
        return db.deleteById(idAcompanhamento)
    }

    private fun atualizarPodcast(podcastForm: PodcastPesquisaDto): Podcast {
        /**nao atualizamos o total de episodio pq ele serve de referencia p o robo notificar */
        val podcast: Podcast = dbPodcast.findByIdPodcastSpotify(podcastForm.id) ?: Podcast()
        podcast.idPodcastSpotify = podcastForm.id
        podcast.nome = podcastForm.name
        podcast.descricao = podcastForm.description
        podcast.href = podcastForm.href
        podcast.urlImagem = podcastForm.images?.firstOrNull()?.url
        podcast.publicadora = podcastForm.publisher
        podcast.conteudoExplicito = podcastForm.explicit

        dbPodcast.saveAndFlush(podcast)
        return podcast
    }

    fun pesquisarPodcastPorIDSpotify(idPodcast: String?): Podcast? {
        return dbPodcast.findByIdPodcastSpotify(idPodcast)
    }

    fun salvarNumeroTotalDePodcasts(pesquisaPodcast: PodcastPesquisaDto, podcastFisico: Podcast) {
        podcastFisico.totalEpisodios = pesquisaPodcast.total_episodes
        dbPodcast.saveAndFlush(podcastFisico)

    }

    fun salvarSessionUserGenerico(tokenDto: TokenSpotifyApiDto) {
        val session: SessionUserSpotify = dbSessionUserRepository.findByNrIpUser("127.0.0.1") ?: SessionUserSpotify()
        session.nrIpUser = "127.0.0.1"
        session.accessToken = tokenDto.access_token
        session.tokenType = tokenDto.token_type
        session.expiresIn = tokenDto.expires_in
        session.refreshToken = tokenDto.refresh_token
        session.scope = tokenDto.scope

        dbSessionUserRepository.saveAndFlush(session)

    }


}