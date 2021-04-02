package com.dojo.spodfy.robot

import com.dojo.spodfy.model.PesquisaSpotifyApiDto
import com.dojo.spodfy.model.PodcastPesquisaDto
import com.dojo.spodfy.model.TokenSpotifyApiDto
import com.dojo.spodfy.service.AcompanhamentoService
import com.dojo.spodfy.service.api.spotify.SpotifyRequestUtil
import com.dojo.spodfy.table.Acompanhamento
import com.dojo.spodfy.table.Podcast
import com.dojo.spodfy.table.SessionUserSpotify
import com.dojo.spodfy.util.SPOTIFY_API_SEARCH
import com.dojo.spodfy.util.SPOTIFY_API_SEARCH_SHOWS
import com.dojo.spodfy.util.SPOTIFY_API_TOKEN
import com.dojo.spodfy.util.SPOTIFY_REDIRECT_USER_GENERIC
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletRequest

@Component
class AcompanhamentoRobot {

    @set:Autowired
    lateinit var acompanhamentoService: AcompanhamentoService

    private val gson = Gson()
    private val util: SpotifyRequestUtil = SpotifyRequestUtil()

    private val logger = LoggerFactory.getLogger(AcompanhamentoRobot::class.java)

    /**
     * Metodo responsavel por fazer o acompanhamento dos novos podcast
     * a cada hora faz um levantamento e notifica os usuarios correspondentes
     * */
    @Scheduled(fixedRate = 3600000)
    fun gerarNotificacoes() {

        logger.info("############################################################################")
        logger.info("EXECUCAO ROBO DE NOTIFICACOES")

        val usuarioGenerico: TokenSpotifyApiDto = recuperarTokenGenerico()


        val acompanhamentoTotal: List<Acompanhamento> = acompanhamentoService.listarTodosAcompanhamentos()
        val agrupados: Map<String?, List<Acompanhamento>> = acompanhamentoTotal.groupBy { it.podcast?.idPodcastSpotify }

        agrupados.forEach { idPodcast, acompanhamentos ->
            //comparar se o total do banco esta diferente da  API e entao notificar


            logger.info("URL API: $SPOTIFY_API_SEARCH_SHOWS$idPodcast")
            val (request, response, result) = Fuel.get(SPOTIFY_REDIRECT_USER_GENERIC)
                .header(Headers.AUTHORIZATION, "Bearer ${usuarioGenerico.access_token}")
                .responseString()

            if (response.statusCode != HttpStatus.OK.value()) {
                logger.error("Pesquisa pelo podcast $idPodcast falhou!")
                throw Exception("Não foi possivel encontrar o podcast $idPodcast pela API Spotify")
            }

            println(result.get())
            val pesquisaPodcast: PodcastPesquisaDto = gson.fromJson(result.get(), PodcastPesquisaDto::class.java)
            logger.info("PODCAST: ${pesquisaPodcast.name} -- recuperado com sucesso da API")

            val podcastFisico: Podcast? = acompanhamentoService.pesquisarPodcastPorIDSpotify(idPodcast)
            logger.info("PODCAST: ${podcastFisico?.nome} -- recuperado com sucesso do BD")

            //COMPARACAO
            try {
                //necessario try pq nao tem como prever o NULL POINTER

                if (pesquisaPodcast.total_episodes!! > podcastFisico?.totalEpisodios!!) {
                    //Notificar o usuario e atualizar o podcast
                    logger.info("ENCONTRADO EPISÓDIO NOVO, NOTIFICANDO...")

                    acompanhamentoService.salvarNumeroTotalDePodcasts(pesquisaPodcast, podcastFisico)
                    logger.info("Atualizado numero total de episodios!")

                    acompanhamentos.forEach { acompanhamento ->
                        println("Usuario: ${acompanhamento.usuario?.nomeExibicao} - ${acompanhamento.usuario?.idUsuarioSpotify}")
                        println("Podcast: ${pesquisaPodcast.name} - ${pesquisaPodcast.id}")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                logger.info("EXECUCAO ROBO DE NOTIFICACOES FINALIZADO COM ERROS!")
            }

            logger.info("############################################################################")
        }


    }

    private fun recuperarTokenGenerico(): TokenSpotifyApiDto {
        logger.info("Recuperando usuario generico ...")

        val (_, _, result) = Fuel.post(SPOTIFY_API_TOKEN, util.preparaBodyRequisicaoTokenCredentialsID())
            .header(Headers.CONTENT_TYPE, "application/x-www-form-urlencoded")
            .header(Headers.AUTHORIZATION, util.prepararHeaderCredentialsID())
            .responseString()

        when (result) {
            is Result.Failure -> {
                val ex = result.getException()
                logger.error("Não foi possivel recuperar o usuário generico!")
                throw Exception(ex)
            }
            is Result.Success -> {
                logger.info("Usuário generico recuperado com sucesso!")
                val tokenDto: TokenSpotifyApiDto = gson.fromJson(result.get(), TokenSpotifyApiDto::class.java)
                acompanhamentoService.salvarSessionUserGenerico(tokenDto)
                return tokenDto
            }
        }

    }
}