package reembolso.dominio

sealed interface ItemValidado {
    data class Valido(val despesa: Despesa) : ItemValidado
    data class Invalido(val erro: ResultadoDespesa.ComErro) : ItemValidado
}

// AMB-020: a posição na relação de despesas conta a partir de 1.
fun validarItens(elementos: List<ElementoLido>): List<ItemValidado> =
    elementos.mapIndexed { indice, elemento -> validarElemento(elemento, indice + 1) }

fun validarElemento(elemento: ElementoLido, posicao: Int): ItemValidado = when (elemento) {
    // AMB-020: causa única — a própria despesa, com tipo incompatível.
    ElementoLido.NaoEDespesa -> ItemValidado.Invalido(
        ResultadoDespesa.ComErro(
            ref = Referencia.Posicao(posicao),
            valorConsiderado = null,
            categoriaConsiderada = null,
            causas = listOf(CausaErro(CampoDespesa.DESPESA, Falha.TIPO_INCOMPATIVEL)),
        ),
    )
    is ElementoLido.DespesaLida -> validarDespesa(elemento, posicao)
}

// RN-010 (erro de item). Causas na ordem de CampoDespesa, que é a da §4.1 (AMB-021).
// `posicao` conta a partir de 1 e só aparece na saída quando falta identificador (AMB-020).
fun validarDespesa(lida: ElementoLido.DespesaLida, posicao: Int): ItemValidado {
    val causas = mutableListOf<CausaErro>()

    val id = texto(lida.id, CampoDespesa.IDENTIFICADOR, causas)
    val data = valorDe(lida.data, CampoDespesa.DATA, causas)
    val categoria = texto(lida.categoria, CampoDespesa.CATEGORIA, causas)?.let(::normalizarTermo)
    val fornecedor = texto(lida.fornecedor, CampoDespesa.FORNECEDOR, causas)?.let(::normalizarTermo)
    // RN-011: normaliza antes da validação de faixa.
    val valor = valorDe(lida.valor, CampoDespesa.VALOR, causas)?.let(Valor::normalizar)
    if (valor != null && valor.decimal.signum() <= 0) {
        causas += CausaErro(CampoDespesa.VALOR, Falha.VALOR_NAO_POSITIVO)
    }
    val temNotaFiscal = valorDe(lida.temNotaFiscal, CampoDespesa.NOTA_FISCAL, causas)

    if (causas.isNotEmpty()) {
        val ref = if (id != null) Referencia.Id(id) else Referencia.Posicao(posicao)
        return ItemValidado.Invalido(ResultadoDespesa.ComErro(ref, valor, categoria, causas))
    }
    // Sem causas, todo campo foi lido como Presente e não é nulo.
    return ItemValidado.Valido(Despesa(id!!, data!!, categoria!!, fornecedor!!, valor!!, temNotaFiscal!!))
}

private fun <T> valorDe(lido: Lido<T>, campo: CampoDespesa, causas: MutableList<CausaErro>): T? {
    val falha = when (lido) {
        is Lido.Presente -> return lido.valor
        Lido.Ausente -> Falha.AUSENTE
        Lido.TipoIncompativel -> Falha.TIPO_INCOMPATIVEL
        Lido.DataInexistente -> Falha.DATA_INEXISTENTE
    }
    causas += CausaErro(campo, falha)
    return null
}

// RN-008: categoria e fornecedor em minúsculas pelo mapeamento completo de caixa do Unicode,
// independente de localidade — lowercase() do Kotlin usa Locale.ROOT (DT-005). O identificador
// não passa por aqui: normaliza-se o que é termo de negócio, não o que é chave (RN-012).
private fun normalizarTermo(aparado: String): String = aparado.lowercase()

// §4.1: texto obrigatório vazio após aparar() é tratado como ausente.
private fun texto(lido: Lido<String>, campo: CampoDespesa, causas: MutableList<CausaErro>): String? {
    val aparado = valorDe(lido, campo, causas)?.aparar() ?: return null
    if (aparado.isEmpty()) {
        causas += CausaErro(campo, Falha.AUSENTE)
        return null
    }
    return aparado
}
