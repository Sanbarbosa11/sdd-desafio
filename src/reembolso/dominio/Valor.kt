package reembolso.dominio

import java.math.BigDecimal
import java.math.RoundingMode

// DT-003: único caminho de criação é normalizar; escala sempre 2, HALF_UP (afasta do zero).
@JvmInline
value class Valor private constructor(val decimal: BigDecimal) {
    companion object {
        fun normalizar(bruto: BigDecimal): Valor = Valor(bruto.setScale(2, RoundingMode.HALF_UP))

        // Para as constantes da política; passa por normalizar como todo outro valor.
        fun de(texto: String): Valor = normalizar(BigDecimal(texto))
    }
}
