package reembolso.regras

import java.math.BigDecimal
import java.time.LocalDate
import reembolso.dominio.DespesasLidas
import reembolso.dominio.ElementoLido
import reembolso.dominio.Lido
import reembolso.dominio.LoteLido
import reembolso.dominio.PeriodoLido

// Construtores de LoteLido para os testes de domínio, sem JSON (plan §6).

fun data(texto: String): Lido<LocalDate> = Lido.Presente(LocalDate.parse(texto))

fun periodo(inicio: String = "2026-07-01", fim: String = "2026-07-31") =
    PeriodoLido.Presente(data(inicio), data(fim))

fun despesa(
    id: Lido<String> = Lido.Presente("d-001"),
    data: Lido<LocalDate> = data("2026-07-03"),
    categoria: Lido<String> = Lido.Presente("alimentacao"),
    fornecedor: Lido<String> = Lido.Presente("Bistro Central"),
    valor: Lido<BigDecimal> = Lido.Presente(BigDecimal("38.00")),
    temNotaFiscal: Lido<Boolean> = Lido.Presente(true),
) = ElementoLido.DespesaLida(id, data, categoria, fornecedor, valor, temNotaFiscal)

fun despesa(id: String) = despesa(id = Lido.Presente(id))

fun lote(periodo: PeriodoLido = periodo(), vararg despesas: ElementoLido) =
    LoteLido(periodo, DespesasLidas.Presente(despesas.toList()))

fun lote(periodo: PeriodoLido = periodo(), despesas: DespesasLidas) = LoteLido(periodo, despesas)
