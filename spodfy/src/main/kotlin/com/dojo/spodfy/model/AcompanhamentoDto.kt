package com.dojo.spodfy.model

data class AcompanhamentoDto(
    var idAcompanhamento: Long?,
    var observacao: String?,
    var diaSemana: String?,
    var horario: String?,

    var podcast: PodcastPesquisaDto? = null

)
