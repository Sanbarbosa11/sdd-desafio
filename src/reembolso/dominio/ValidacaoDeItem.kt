package reembolso.dominio

sealed interface ItemValidado {
    data class Valido(val despesa: Despesa) : ItemValidado
    data class Invalido(val erro: ResultadoDespesa.ComErro) : ItemValidado
}

// RN-010 (erro de item). Causas na ordem de CampoDespesa, que é a da §4.1 (AMB-021).
// `posicao` conta a partir de 1 e só aparece na saída quando falta identificador (AMB-020).
fun validarDespesa(lida: ElementoLido.DespesaLida, posicao: Int): ItemValidado {
    val causas = mutableListOf<CausaErro>()

    val id = texto(lida.id, CampoDespesa.IDENTIFICADOR, causas)
    val data = valorDe(lida.data, CampoDespesa.DATA, causas)
    val categoria = texto(lida.categoria, CampoDespesa.CATEGORIA, causas)
    val fornecedor = texto(lida.fornecedor, CampoDespesa.FORNECEDOR, causas)
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

// §4.1: texto obrigatório vazio após aparar() é tratado como ausente.
private fun texto(lido: Lido<String>, campo: CampoDespesa, causas: MutableList<CausaErro>): String? {
    val aparado = valorDe(lido, campo, causas)?.aparar() ?: return null
    if (aparado.isEmpty()) {
        causas += CausaErro(campo, Falha.AUSENTE)
        return null
    }
    return aparado
}
