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
                is ItemValidado.Valido -> avaliarDespesa(item.despesa)
            }
        }
        return ResultadoLote.Avaliado(periodo, despesas, consolidar(despesas))
    }

    private fun avaliarDespesa(despesa: Despesa): ResultadoDespesa {
        // Etapa 1 (elegibilidade): período, categoria, nota fiscal e duplicidade entram nas T-009 a T-013.
        val categoria = Categoria.porCodigo(despesa.categoria)
            ?: TODO("T-010: categoria inelegível é recusada com CATEGORIA_INELEGIVEL (RN-008)")

        // Etapa 2 (valor).
        return avaliarValor(despesa, categoria)
    }

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
            notaExigida = despesa.valor > Politica.LIMIAR_NOTA_FISCAL, // RN-003
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
