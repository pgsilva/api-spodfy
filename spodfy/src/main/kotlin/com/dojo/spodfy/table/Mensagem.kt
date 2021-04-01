package com.dojo.spodfy.table

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("MENSAGEM")
data class Mensagem(@Id val id: Int?, val texto: String)
