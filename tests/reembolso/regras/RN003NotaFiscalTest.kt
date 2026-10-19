package reembolso.regras

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import reembolso.dominio.Motivo
import reembolso.dominio.Valor

class RN003NotaFiscalTest {

    private val d003 = despesa("d-003", "2026-07-06", "transporte_urbano", "TaxiApp", "100.00", nf = false)
    private val d004 = despesa("d-004", "2026-07-06", "transporte_urbano", "TaxiApp", "100.01", nf = false)
    private val d013 = despesa("d-013", "2026-07-22", "hospedagem", "Airbnb", "690.00", nf = false)

    @Test
    fun `RN-003 AMB-004 d-003 transporte 100,00 sem NF → nota não exigida, reembolso 80,00 parcial`() {
        val d = avaliar(d003).aceita("d-003")
        assertFalse(d.notaExigida)
        assertEquals(Valor.de("80.00"), d.reembolsado)
        assertEquals(Valor.de("20.00"), d.excedente)
    }

    @Test
    fun `RN-003 AMB-004 AMB-006 d-004 transporte 100,01 sem NF → nota exigida e ausente`() {
        val d = avaliar(d004).recusada("d-004")
        assertTrue(d.notaExigida)
        assertEquals(listOf(Motivo.SEM_NOTA_FISCAL), d.motivos)
    }

    @Test
    fun `RN-003 AMB-004 transporte 100,01 com NF → nota exigida e presente, reembolso 80,00 parcial`() {
        val d = avaliar(despesa("t", "2026-07-06", "transporte_urbano", "TaxiApp", "100.01", nf = true)).aceita("t")
        assertTrue(d.notaExigida)
        assertEquals(Valor.de("80.00"), d.reembolsado)
        assertEquals(Valor.de("20.01"), d.excedente)
    }

    @Test
    fun `RN-003 RN-011 AMB-014 100,004 → nota não exigida e 100,005 → nota exigida`() {
        // Sem NF: a fronteira decide entre aceita e recusada, além do campo notaExigida.
        val abaixo = avaliar(despesa("a", "2026-07-06", "hospedagem", "Hotel Copa Sul", "100.004", nf = false)).aceita("a")
        assertFalse(abaixo.notaExigida)
        assertEquals(Valor.de("100.00"), abaixo.valor)

        val acima = avaliar(despesa("b", "2026-07-06", "hospedagem", "Hotel Copa Sul", "100.005", nf = false)).recusada("b")
        assertTrue(acima.notaExigida)
        assertEquals(Valor.de("100.01"), acima.valor)
        assertEquals(listOf(Motivo.SEM_NOTA_FISCAL), acima.motivos)
    }

    @Test
    fun `RN-003 alimentacao 50,00 sem NF → nota não exigida, reembolso 50,00 integral`() {
        val d = avaliar(despesa("x", "2026-07-10", "alimentacao", "Bistro Central", "50.00", nf = false)).aceita("x")
        assertFalse(d.notaExigida)
        assertEquals(Valor.de("50.00"), d.reembolsado)
        assertNull(d.excedente)
    }

    @Test
    fun `RN-003 AMB-006 alimentacao 500,00 sem NF → nota exigida e ausente apesar do teto de 60,00`() {
        val d = avaliar(despesa("x", "2026-07-10", "alimentacao", "Bistro Central", "500.00", nf = false)).recusada("x")
        assertTrue(d.notaExigida)
        assertEquals(listOf(Motivo.SEM_NOTA_FISCAL), d.motivos)
    }

    @Test
    fun `RN-003 AMB-006 d-013 hospedagem 690,00 sem NF → nota exigida e ausente`() {
        val d = avaliar(d013).recusada("d-013")
        assertTrue(d.notaExigida)
        assertEquals(listOf(Motivo.SEM_NOTA_FISCAL), d.motivos)
    }
}
