package reembolso.regras

import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertIs
import reembolso.dominio.DespesasLidas
import reembolso.dominio.ElementoLido
import reembolso.dominio.Lido
import reembolso.dominio.LoteLido
import reembolso.dominio.Motor
import reembolso.dominio.PeriodoLido
import reembolso.dominio.ResultadoDespesa
import reembolso.dominio.ResultadoLote

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

fun avaliado(lote: LoteLido): ResultadoLote.Avaliado = assertIs<ResultadoLote.Avaliado>(Motor.avaliar(lote))

// Resultados por despesa de um lote de julho/2026 com as despesas dadas.
fun avaliar(vararg despesas: ElementoLido): List<ResultadoDespesa> = avaliado(lote(periodo(), *despesas)).despesas

fun List<ResultadoDespesa>.aceita(id: String): ResultadoDespesa.Aceita =
    filterIsInstance<ResultadoDespesa.Aceita>().single { it.id == id }

// Recusada não tem campo de limite nem de reembolso: o tipo garante "sem limite aplicado" e 0,00.
fun List<ResultadoDespesa>.recusada(id: String): ResultadoDespesa.Recusada =
    filterIsInstance<ResultadoDespesa.Recusada>().single { it.id == id }

fun valor(texto: String): Lido<BigDecimal> = Lido.Presente(BigDecimal(texto))

fun despesa(id: String, data: String, categoria: String, fornecedor: String, valor: String, nf: Boolean) =
    despesa(Lido.Presente(id), data(data), Lido.Presente(categoria), Lido.Presente(fornecedor), valor(valor), Lido.Presente(nf))
