package reembolso.dominio

import java.time.LocalDate

// Cada variante carrega só os campos que a §4.2 diz que aparecem na sua classificação.
sealed interface ResultadoDespesa {
    // Integral sem excedente, parcial com excedente.
    data class Aceita(
        val id: String,
        val valor: Valor,
        val categoria: String,
        val limite: Valor,
        val reembolsado: Valor,
        val excedente: Valor?,
        val notaExigida: Boolean,
    ) : ResultadoDespesa

    data class Recusada(
        val id: String,
        val valor: Valor,
        val categoria: String,
        val notaExigida: Boolean,
        val motivos: List<Motivo>,
        val mantida: String?,
    ) : ResultadoDespesa

    data class ComErro(
        val ref: Referencia,
        val valorConsiderado: Valor?,
        val categoriaConsiderada: String?,
        val causas: List<CausaErro>,
    ) : ResultadoDespesa
}

data class Periodo(val inicio: LocalDate, val fim: LocalDate) {
    // RN-005: as duas bordas pertencem ao período.
    operator fun contains(data: LocalDate): Boolean = data in inicio..fim
}

data class Consolidado(
    val totalReembolsado: Valor,
    val integral: Int,
    val parcial: Int,
    val recusada: Int,
    val erro: Int,
) {
    val total: Int get() = integral + parcial + recusada + erro
}

sealed interface ResultadoLote {
    data class Avaliado(
        val periodo: Periodo,
        val despesas: List<ResultadoDespesa>,
        val consolidado: Consolidado,
    ) : ResultadoLote

    data class Rejeitado(val causas: List<CausaLote>) : ResultadoLote
}
