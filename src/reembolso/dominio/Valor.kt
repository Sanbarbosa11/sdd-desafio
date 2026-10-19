package reembolso.dominio

import java.math.BigDecimal
import java.math.RoundingMode

// DT-003: único caminho de criação é normalizar; escala sempre 2, HALF_UP (afasta do zero).
@JvmInline
value class Valor private constructor(val decimal: BigDecimal) : Comparable<Valor> {
    // Escala 2 nos dois lados: soma e diferença continuam em escala 2.
    operator fun plus(outro: Valor) = Valor(decimal + outro.decimal)
    operator fun minus(outro: Valor) = Valor(decimal - outro.decimal)
    override fun compareTo(other: Valor) = decimal.compareTo(other.decimal)

    companion object {
        val ZERO = de("0.00")

        fun normalizar(bruto: BigDecimal): Valor = Valor(bruto.setScale(2, RoundingMode.HALF_UP))

        // Para as constantes da política; passa por normalizar como todo outro valor.
        fun de(texto: String): Valor = normalizar(BigDecimal(texto))
    }
}
