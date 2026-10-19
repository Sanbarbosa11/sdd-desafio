package reembolso.dominio

// Plan §4: a política inteira vive neste arquivo; mudá-la é mudar a spec.
enum class Categoria(val codigo: String, val limite: Valor) {
    ALIMENTACAO("alimentacao", Valor.de("60.00")),
    TRANSPORTE_URBANO("transporte_urbano", Valor.de("80.00")),
    HOSPEDAGEM("hospedagem", Valor.de("250.00"));

    companion object {
        fun porCodigo(codigo: String): Categoria? = entries.find { it.codigo == codigo }
    }
}

object Politica {
    val LIMIAR_NOTA_FISCAL = Valor.de("100.00")
}

// Único ponto de consulta do limite (RN-001, RN-002).
fun limiteAplicavel(categoria: Categoria): Valor = categoria.limite
