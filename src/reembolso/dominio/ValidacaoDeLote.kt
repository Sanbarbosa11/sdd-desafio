package reembolso.dominio

// RN-010 (erro de lote) e RN-012. Devolve a rejeição com todas as causas detectáveis,
// ou null se o lote pode ser avaliado.
fun validarLote(lote: LoteLido): ResultadoLote.Rejeitado? {
    val causas = causasDoPeriodo(lote.periodo) + causasDasDespesas(lote.despesas)
    return if (causas.isEmpty()) null else ResultadoLote.Rejeitado(causas)
}

private fun causasDoPeriodo(periodo: PeriodoLido): List<CausaLote> = when (periodo) {
    PeriodoLido.Ausente -> listOf(CausaLote.PeriodoAusente)
    PeriodoLido.TipoIncompativel -> listOf(CausaLote.PeriodoTipoIncompativel)
    is PeriodoLido.Presente -> buildList {
        causaDaData(periodo.inicio, CausaLote.InicioAusente, CausaLote.InicioInvalido)?.let(::add)
        causaDaData(periodo.fim, CausaLote.FimAusente, CausaLote.FimInvalido)?.let(::add)
        val inicio = periodo.inicio
        val fim = periodo.fim
        if (inicio is Lido.Presente && fim is Lido.Presente && inicio.valor > fim.valor) {
            add(CausaLote.InicioPosteriorAoFim)
        }
    }
}

private fun causaDaData(data: Lido<*>, ausente: CausaLote, invalida: CausaLote): CausaLote? = when (data) {
    is Lido.Presente -> null
    Lido.Ausente -> ausente
    Lido.TipoIncompativel, Lido.DataInexistente -> invalida
}

private fun causasDasDespesas(despesas: DespesasLidas): List<CausaLote> = when (despesas) {
    DespesasLidas.Ausente -> listOf(CausaLote.DespesasAusentes)
    DespesasLidas.TipoIncompativel -> listOf(CausaLote.DespesasTipoIncompativel)
    is DespesasLidas.Presente -> {
        val repetidos = identificadoresRepetidos(despesas.elementos)
        if (repetidos.isEmpty()) emptyList() else listOf(CausaLote.IdentificadorRepetido(repetidos))
    }
}

// RN-012: só identificadores informados contam; vazio após aparar() é ausente (§4.1, AMB-020).
private fun identificadoresRepetidos(elementos: List<ElementoLido>): List<String> =
    elementos
        .filterIsInstance<ElementoLido.DespesaLida>()
        .mapNotNull { (it.id as? Lido.Presente)?.valor?.aparar()?.takeIf(String::isNotEmpty) }
        .groupingBy { it }
        .eachCount()
        .filterValues { it > 1 }
        .keys
        .toList()
