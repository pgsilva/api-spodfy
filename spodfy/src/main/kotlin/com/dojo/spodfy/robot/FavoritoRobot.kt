package com.dojo.spodfy.robot

import com.dojo.spodfy.model.PesquisaSpotifyApiDto
import com.dojo.spodfy.model.PodcastPesquisaDto
import com.dojo.spodfy.model.TokenSpotifyApiDto
import com.dojo.spodfy.service.FavoritoService
import com.dojo.spodfy.service.UsuarioService
import com.dojo.spodfy.service.api.spotify.SpotifyRequestUtil
import com.dojo.spodfy.table.Favorito
import com.dojo.spodfy.table.Podcast
import com.dojo.spodfy.util.*
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FavoritoRobot {

    @set:Autowired
    lateinit var favoritoService: FavoritoService

    @set:Autowired
    lateinit var usuarioService: UsuarioService

    private val gson = Gson()
    private val util: SpotifyRequestUtil = SpotifyRequestUtil()

    private val logger = LoggerFactory.getLogger(FavoritoRobot::class.java)

    /**
     * Metodo responsavel por fazer o Favorito dos novos podcast
     * a cada hora faz um levantamento e notifica os usuarios correspondentes
     * */
    @Scheduled(fixedRate = 3600000)
    fun gerarNotificacoes() {

        logger.info("############################################################################")
        logger.info("EXECUCAO ROBO DE NOTIFICACOES")

        val usuarioGenerico: TokenSpotifyApiDto? = recuperarTokenGenerico()


        val favoritoTotal: List<Favorito> = favoritoService.listarTodosFavoritos()
        val agrupados: Map<String?, List<Favorito>> = favoritoTotal.groupBy { it.podcast?.idPodcastSpotify }

        agrupados.forEach { idPodcast, Favoritos ->
            val podcastFisico: Podcast? = favoritoService.pesquisarPodcastPorIDSpotify(idPodcast)

            val uri = String.format(SPOTIFY_API_SEARCH_SHOW_ROBOT, podcastFisico?.nomeLower())
            val (_, response, result) = Fuel.get(uri)
                .header(Headers.AUTHORIZATION, "Bearer ${usuarioGenerico?.access_token}")
                .responseString()

            if (response.statusCode != HttpStatus.OK.value()) {
                logger.info("Nao foi possivel encontrar o podcast: $idPodcast pela API")
                logger.error("EXECUCAO ROBO DE NOTIFICACOES FINALIZADO COM ERRO!")
                logger.info("############################################################################")
                throw Exception("Nao foi possivel encontrar o podcast: $idPodcast pela API")
            }

            val pesquisaDto = gson.fromJson(result.get(), PesquisaSpotifyApiDto::class.java)
            if (pesquisaDto.shows.items?.size!! == 1) {
                //apenas continua se retornou um resultado
                val podcastApi: PodcastPesquisaDto = pesquisaDto.shows.items[0]

                //COMPARACAO
                if ((podcastApi.name == podcastFisico?.nome)
                    && (podcastApi.total_episodes!! > podcastFisico?.totalEpisodios!!)
                ) {
                    //encontrou diff faz as notificacoes e atualiza o total
                    favoritoService.salvarNumeroTotalDePodcasts(
                        pesquisaPodcast = podcastApi,
                        podcastFisico = podcastFisico
                    )

                    Favoritos.forEach { Favorito ->
                        println("Favorito: ${Favorito.idFavorito}")
                        println("Usuario: ${Favorito.usuario?.nomeExibicao} - Email: ${Favorito.usuario?.email}")
                        println("Podcast: ${Favorito.podcast?.nome} - ID: ${Favorito.podcast?.idPodcastSpotify}")
                    }
                }

            }

        }

        logger.info("EXECUCAO ROBO DE NOTIFICACOES FINALIZADO!")
        logger.info("############################################################################")
    }

    private fun recuperarTokenGenerico(): TokenSpotifyApiDto? {
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
                usuarioService.salvarSessionUserGenerico(tokenDto)
                return tokenDto
            }
        }

    }
}