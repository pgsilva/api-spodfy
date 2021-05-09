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
import com.mailjet.client.ClientOptions
import com.mailjet.client.MailjetClient
import com.mailjet.client.transactional.*
import com.mailjet.client.transactional.response.SendEmailsResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

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
        logger.info("push notification robot starting")

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
                logger.info("couldn't find podcast: $idPodcast from API")
                logger.error("push notification robot finalized with errors!")
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

                    Favoritos.forEach { favorito ->
                        //prepar envio de emails
                        enviarEmailNotificao(favorito)
                    }
                }

            }

        }

        logger.info("push notification robot finalized")
        logger.info("############################################################################")
    }

    private fun enviarEmailNotificao(favorito: Favorito) {
        val options: ClientOptions = ClientOptions.builder()
            .apiKey(MAIILJET_KEY)
            .apiSecretKey(MAILJET_SECRET_KEY)
            .build()

        val client = MailjetClient(options)
        val message1: TransactionalEmail = TransactionalEmail
            .builder()
            .to(SendContact(favorito.usuario?.email, favorito.usuario?.nomeExibicao))
            .from(SendContact("pgsilva0698@gmail.com", "Equipe Spodfy"))
            .htmlPart(TEMPLATE_EMAIL.format(favorito.usuario?.nomeExibicao, favorito.podcast?.nome))
            .subject("Podcast quentinho!")
            .trackOpens(TrackOpens.ENABLED)
            //.attachment(Attachment.fromFile(attachmentPath))
            .header("id-podcast", favorito.podcast?.idPodcastSpotify)
            .customID(UUID.randomUUID().toString())
            .build()

        val request: SendEmailsRequest = SendEmailsRequest
            .builder()
            .message(message1) // you can add up to 50 messages per request
            .build()

        println("Favorito: ${favorito.idFavorito}")
        println("Usuario: ${favorito.usuario?.nomeExibicao} - Email: ${favorito.usuario?.email}")
        println("Podcast: ${favorito.podcast?.nome} - ID: ${favorito.podcast?.idPodcastSpotify}")
        // act
        val response: SendEmailsResponse = request.sendWith(client)
        println(response.messages.toString())
        println("email sent")

    }

    private fun recuperarTokenGenerico(): TokenSpotifyApiDto? {
        logger.info("retrieving generic user ...")

        val (_, _, result) = Fuel.post(SPOTIFY_API_TOKEN, util.preparaBodyRequisicaoTokenCredentialsID())
            .header(Headers.CONTENT_TYPE, "application/x-www-form-urlencoded")
            .header(Headers.AUTHORIZATION, util.prepararHeaderCredentialsID())
            .responseString()

        when (result) {
            is Result.Failure -> {
                val ex = result.getException()
                logger.error("was not possible to recover the generic user")
                throw Exception(ex)
            }
            is Result.Success -> {
                logger.info("generic user successfully recovered!")
                val tokenDto: TokenSpotifyApiDto = gson.fromJson(result.get(), TokenSpotifyApiDto::class.java)
                usuarioService.salvarSessionUserGenerico(tokenDto)
                return tokenDto
            }
        }

    }
}