package reembolso.regras

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import reembolso.dominio.ResultadoDespesa
import reembolso.dominio.Valor

class RN001LimitePorDespesaTest {

    // Despesas do lote de exemplo usadas nos aceites.
    private val d001 = despesa("d-001", "2026-07-03", "alimentacao", "Restaurante Tavola", "72.50", nf = true)
    private val d002 = despesa("d-002", "2026-07-03", "alimentacao", "Cantina do Porto", "38.00", nf = true)
    private val d010 = despesa("d-010", "2026-07-14", "hospedagem", "Hotel Copa Sul", "480.00", nf = true)
    private val d011 = despesa("d-011", "2026-07-15", "alimentacao", "Hotel Copa Sul", "33.333", nf = true)

    @Test
    fun `RN-001 AMB-001 d-001 alimentacao 72,50 → reembolso 60,00 parcial`() {
        val d = avaliar(d001, d002).aceita("d-001")
        assertEquals(Valor.de("60.00"), d.reembolsado)
        assertEquals(Valor.de("12.50"), d.excedente)
        assertFalse(d.notaExigida)
    }

    @Test
    fun `RN-001 AMB-001 d-002 alimentacao 38,00 → reembolso 38,00 integral`() {
        val d = avaliar(d001, d002).aceita("d-002")
        assertEquals(Valor.de("38.00"), d.reembolsado)
        assertNull(d.excedente)
    }

    @Test
    fun `RN-001 AMB-001 dia 03-07 totaliza 98,00 em alimentacao`() {
        val noDia = avaliar(d001, d002).filterIsInstance<ResultadoDespesa.Aceita>()
            .filter { it.categoria == "alimentacao" && it.id in setOf("d-001", "d-002") }
        assertEquals(Valor.de("98.00"), noDia.fold(Valor.ZERO) { total, d -> total + d.reembolsado })
    }

    @Test
    fun `RN-001 AMB-003 inverter a ordem de d-001 e d-002 não altera os valores`() {
        assertEquals(avaliar(d001, d002).toSet(), avaliar(d002, d001).toSet())
    }

    @Test
    fun `RN-001 AMB-001 AMB-003 três alimentações de 59,00 no mesmo dia → 59,00 cada e 177,00 no total`() {
        val tres = (1..3).map { despesa("a-$it", "2026-07-20", "alimentacao", "Fornecedor $it", "59.00", nf = true) }
        val lote = avaliado(lote(periodo(), *tres.toTypedArray()))

        lote.despesas.forEach {
            it as ResultadoDespesa.Aceita
            assertEquals(Valor.de("59.00"), it.reembolsado)
            assertNull(it.excedente)
        }
        assertEquals(Valor.de("177.00"), lote.consolidado.totalReembolsado)
    }

    @Test
    fun `RN-001 AMB-007 AMB-009 d-010 hospedagem 480,00 → limite 250,00, reembolso 250,00 parcial, excedente 230,00`() {
        val d = avaliar(d010).aceita("d-010")
        assertEquals(Valor.de("250.00"), d.limite)
        assertEquals(Valor.de("250.00"), d.reembolsado)
        assertEquals(Valor.de("230.00"), d.excedente)
        assertTrue(d.notaExigida)
    }

    @Test
    fun `AMB-007 d-011 no mesmo fornecedor de d-010 mantém limite 60,00`() {
        val d = avaliar(d010, d011).aceita("d-011")
        assertEquals(Valor.de("60.00"), d.limite)
        assertEquals(Valor.de("33.33"), d.reembolsado)
    }

    @Test
    fun `AMB-009 hospedagem 200,00 com NF → reembolso 200,00 integral`() {
        val d = avaliar(despesa("h-1", "2026-07-14", "hospedagem", "Hotel Copa Sul", "200.00", nf = true)).aceita("h-1")
        assertEquals(Valor.de("250.00"), d.limite)
        assertEquals(Valor.de("200.00"), d.reembolsado)
        assertNull(d.excedente)
    }
}
