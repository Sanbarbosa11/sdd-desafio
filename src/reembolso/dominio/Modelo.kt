package reembolso.dominio

import java.time.LocalDate

// Nomes = códigos de negócio da spec; ordem de declaração = ordem da §8 (OrdemMotivosSecao8Test).
enum class Motivo { FORA_DA_COMPETENCIA, CATEGORIA_INELEGIVEL, SEM_NOTA_FISCAL, DUPLICATA }

// IDENTIFICADOR..NOTA_FISCAL na ordem da tabela de despesa da §4.1 (AMB-021, OrdemCamposSecao41Test).
// DESPESA = elemento da relação que não é despesa (AMB-020).
enum class CampoDespesa { DESPESA, IDENTIFICADOR, DATA, CATEGORIA, FORNECEDOR, VALOR, NOTA_FISCAL }

enum class Falha { AUSENTE, TIPO_INCOMPATIVEL, DATA_INEXISTENTE, VALOR_NAO_POSITIVO }

data class CausaErro(val campo: CampoDespesa, val falha: Falha)

sealed interface CausaLote {
    data object EntradaIlegivel : CausaLote
    data object PeriodoAusente : CausaLote
    data object PeriodoTipoIncompativel : CausaLote
    data object InicioAusente : CausaLote
    data object InicioInvalido : CausaLote
    data object FimAusente : CausaLote
    data object FimInvalido : CausaLote
    data object InicioPosteriorAoFim : CausaLote
    data object DespesasAusentes : CausaLote
    data object DespesasTipoIncompativel : CausaLote
    data class IdentificadorRepetido(val identificadores: List<String>) : CausaLote
}

// Validada e normalizada.
data class Despesa(
    val id: String,
    val data: LocalDate,
    val categoria: String,
    val fornecedor: String,
    val valor: Valor,
    val temNotaFiscal: Boolean,
)

// AMB-020: sem identificador utilizável, a despesa é referida pela posição, contada a partir de 1.
sealed interface Referencia {
    data class Id(val id: String) : Referencia
    data class Posicao(val posicao: Int) : Referencia
}
