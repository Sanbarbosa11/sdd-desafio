package reembolso.dominio

// DT-006: ler esta função é ler a §8.
object Motor {
    fun avaliar(lote: LoteLido): ResultadoLote {
        // Etapa 0: validação de lote (RN-010, RN-012).
        validarLote(lote)?.let { return it }

        // Lote válido: período e relação de despesas estão presentes e o período é composto de datas.
        val periodoLido = lote.periodo as PeriodoLido.Presente
        val periodo = Periodo(
            (periodoLido.inicio as Lido.Presente).valor,
            (periodoLido.fim as Lido.Presente).valor,
        )
        val elementos = (lote.despesas as DespesasLidas.Presente).elementos

        // Etapa 0: validação de item (RN-010); etapas 1 e 2 só para as válidas.
        val despesas = validarItens(elementos).map { item ->
            when (item) {
                is ItemValidado.Invalido -> item.erro
                is ItemValidado.Valido -> avaliarDespesa(item.despesa, periodo)
            }
        }
        return ResultadoLote.Avaliado(periodo, despesas, consolidar(despesas))
    }

    private fun avaliarDespesa(despesa: Despesa, periodo: Periodo): ResultadoDespesa {
        // Etapa 1 (elegibilidade): todas as regras são avaliadas, sem interrupção, na ordem da §8.
        val motivos = mutableListOf<Motivo>()
        // 1.1 Pertinência ao período (RN-005, RN-006).
        if (despesa.data !in periodo) motivos += Motivo.FORA_DA_COMPETENCIA
        // 1.2 Elegibilidade da categoria, já normalizada (RN-008).
        val categoria = Categoria.porCodigo(despesa.categoria)
        if (categoria == null) motivos += Motivo.CATEGORIA_INELEGIVEL
        // 1.3 e 1.4 entram nas T-011 e T-013.

        // Com qualquer motivo, reembolso 0,00 e nenhuma avaliação contra o limite (§8, etapa 2).
        if (motivos.isNotEmpty() || categoria == null) return recusar(despesa, motivos)

        // Etapa 2 (valor).
        return avaliarValor(despesa, categoria)
    }

    private fun recusar(despesa: Despesa, motivos: List<Motivo>) = ResultadoDespesa.Recusada(
        id = despesa.id,
        valor = despesa.valor,
        categoria = despesa.categoria,
        notaExigida = notaExigida(despesa),
        motivos = motivos,
        mantida = null,
    )

    // RN-003: sobre o valor normalizado, antes de qualquer limite.
    private fun notaExigida(despesa: Despesa) = despesa.valor > Politica.LIMIAR_NOTA_FISCAL

    // RN-001: cada despesa contra o limite da sua categoria, isoladamente. RN-002: o limite é teto.
    private fun avaliarValor(despesa: Despesa, categoria: Categoria): ResultadoDespesa.Aceita {
        val limite = limiteAplicavel(categoria)
        val parcial = despesa.valor > limite
        return ResultadoDespesa.Aceita(
            id = despesa.id,
            valor = despesa.valor,
            categoria = despesa.categoria,
            limite = limite,
            reembolsado = if (parcial) limite else despesa.valor,
            excedente = if (parcial) despesa.valor - limite else null,
            notaExigida = notaExigida(despesa),
        )
    }

    // §4.2: consolidado do lote.
    // As contagens por classificação refletem apenas as classificações
    // que o motor produz hoje. Completas na T-014, quando a etapa 1 da
    // §8 existir e houver recusa possível.
    private fun consolidar(despesas: List<ResultadoDespesa>): Consolidado {
        val aceitas = despesas.filterIsInstance<ResultadoDespesa.Aceita>()
        return Consolidado(
            totalReembolsado = aceitas.fold(Valor.ZERO) { total, d -> total + d.reembolsado },
            integral = aceitas.count { it.excedente == null },
            parcial = aceitas.count { it.excedente != null },
            recusada = despesas.count { it is ResultadoDespesa.Recusada },
            erro = despesas.count { it is ResultadoDespesa.ComErro },
        )
    }
}
