package reembolso.dominio

import java.time.LocalDate

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

        // Etapa 0: validação de item (RN-010). Etapa 1, passada 1: regras 1.1 a 1.3 de cada despesa válida.
        val avaliacoes = validarItens(elementos).map { item ->
            when (item) {
                is ItemValidado.Invalido -> Avaliacao.Erro(item.erro)
                is ItemValidado.Valido -> elegibilidade(item.despesa, periodo)
            }
        }
        // Etapa 1, passada 2: 1.4 duplicidade, só entre as despesas sem motivo (RN-009, DT-007).
        marcarDuplicatas(avaliacoes.filterIsInstance<Avaliacao.Elegibilidade>().filter { it.motivos.isEmpty() })

        // Etapa 2 só para as despesas sem motivo; a ordem da saída é a da entrada (§4.2).
        val despesas = avaliacoes.map(::concluir)
        return ResultadoLote.Avaliado(periodo, despesas, consolidar(despesas))
    }

    private sealed interface Avaliacao {
        class Erro(val erro: ResultadoDespesa.ComErro) : Avaliacao

        class Elegibilidade(val despesa: Despesa, val categoria: Categoria?) : Avaliacao {
            val motivos = mutableListOf<Motivo>()
            var mantida: String? = null
        }
    }

    // Etapa 1 (elegibilidade): todas as regras são avaliadas, sem interrupção, na ordem da §8.
    private fun elegibilidade(despesa: Despesa, periodo: Periodo): Avaliacao.Elegibilidade {
        // Categoria da política, ou null se inelegível; usada na 1.2 e na etapa 2.
        val categoria = Categoria.porCodigo(despesa.categoria)
        return Avaliacao.Elegibilidade(despesa, categoria).apply {
            // 1.1 Pertinência ao período (RN-005, RN-006).
            if (despesa.data !in periodo) motivos += Motivo.FORA_DA_COMPETENCIA
            // 1.2 Elegibilidade da categoria, já normalizada (RN-008).
            if (categoria == null) motivos += Motivo.CATEGORIA_INELEGIVEL
            // 1.3 Exigência de nota fiscal (RN-003, RN-004).
            if (notaExigida(despesa) && !despesa.temNotaFiscal) motivos += Motivo.SEM_NOTA_FISCAL
        }
    }

    // RN-009: chave de comparação, com categoria, fornecedor e valor já normalizados.
    private data class ChaveDuplicidade(val data: LocalDate, val categoria: String, val fornecedor: String, val valor: Valor)

    // 1.4 Duplicidade (RN-009): em cada grupo, mantém a de menor identificador; as demais recebem DUPLICATA.
    private fun marcarDuplicatas(semMotivo: List<Avaliacao.Elegibilidade>) {
        semMotivo
            .groupBy { ChaveDuplicidade(it.despesa.data, it.despesa.categoria, it.despesa.fornecedor, it.despesa.valor) }
            .values
            .filter { it.size > 1 }
            .forEach { grupo ->
                val mantida = grupo.minWith(compareBy(porPontoDeCodigo) { it.despesa.id })
                grupo.filter { it !== mantida }.forEach {
                    it.motivos += Motivo.DUPLICATA
                    it.mantida = mantida.despesa.id
                }
            }
    }

    // RN-009: ordem lexicográfica pelo ponto de código Unicode. String.compareTo compara unidades
    // UTF-16 e diverge fora do BMP (DT-007, R-07); um Collator seguiria a ordem de uma localidade.
    private val porPontoDeCodigo = Comparator<String> { a, b ->
        val pa = a.codePoints().toArray()
        val pb = b.codePoints().toArray()
        for (i in 0 until minOf(pa.size, pb.size)) {
            if (pa[i] != pb[i]) return@Comparator pa[i].compareTo(pb[i])
        }
        pa.size.compareTo(pb.size)
    }

    private fun concluir(avaliacao: Avaliacao): ResultadoDespesa = when (avaliacao) {
        is Avaliacao.Erro -> avaliacao.erro
        // Com qualquer motivo, reembolso 0,00 e nenhuma avaliação contra o limite (§8, etapa 2).
        is Avaliacao.Elegibilidade ->
            if (avaliacao.motivos.isNotEmpty() || avaliacao.categoria == null) recusar(avaliacao)
            // Etapa 2 (valor).
            else avaliarValor(avaliacao.despesa, avaliacao.categoria)
    }

    private fun recusar(avaliacao: Avaliacao.Elegibilidade) = ResultadoDespesa.Recusada(
        id = avaliacao.despesa.id,
        valor = avaliacao.despesa.valor,
        categoria = avaliacao.despesa.categoria,
        notaExigida = notaExigida(avaliacao.despesa),
        // RN-007: ordem da §8, que é a ordem de declaração de Motivo (DT-006, OrdemMotivosSecao8Test).
        // A ordenação por Motivo.ordinal torna a ordem da saída independente
        // da ordem de coleta. Os testes de ordem da T-010 e da T-011 passam a
        // verificar a saída, não a coleta. A ordem da §8 continua amarrada pelo
        // OrdemMotivosSecao8Test, que lê a spec.
        motivos = avaliacao.motivos.sortedBy { it.ordinal },
        mantida = avaliacao.mantida,
    )

    // RN-003: sobre o valor normalizado, antes de qualquer limite. Único ponto que lê o limiar:
    // alimenta o campo notaExigida da saída e a recusa SEM_NOTA_FISCAL da etapa 1.3.
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
