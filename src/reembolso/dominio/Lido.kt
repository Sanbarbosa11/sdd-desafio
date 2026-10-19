package reembolso.dominio

import java.math.BigDecimal
import java.time.LocalDate

// Plan §3.1: o que o JSON disse, campo a campo, sem decidir nada.
sealed interface Lido<out T> {
    data class Presente<out T>(val valor: T) : Lido<T>
    data object Ausente : Lido<Nothing>
    data object TipoIncompativel : Lido<Nothing>
    data object DataInexistente : Lido<Nothing>
}

// Entrada ilegível não chega aqui: o adaptador devolve Rejeitado(EntradaIlegivel) direto.
data class LoteLido(val periodo: PeriodoLido, val despesas: DespesasLidas)

sealed interface PeriodoLido {
    data object Ausente : PeriodoLido
    data object TipoIncompativel : PeriodoLido
    data class Presente(val inicio: Lido<LocalDate>, val fim: Lido<LocalDate>) : PeriodoLido
}

sealed interface DespesasLidas {
    data object Ausente : DespesasLidas
    data object TipoIncompativel : DespesasLidas
    data class Presente(val elementos: List<ElementoLido>) : DespesasLidas
}

sealed interface ElementoLido {
    data object NaoEDespesa : ElementoLido
    data class DespesaLida(
        val id: Lido<String>,
        val data: Lido<LocalDate>,
        val categoria: Lido<String>,
        val fornecedor: Lido<String>,
        val valor: Lido<BigDecimal>,
        val temNotaFiscal: Lido<Boolean>,
    ) : ElementoLido
}
