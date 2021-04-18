package com.dojo.spodfy.model

data class FavoritoDto(
    var idFavorito: Long?,
    var observacao: String?,
    var diaSemana: String?,
    var horario: String?,

    var podcast: PodcastPesquisaDto? = null

)
