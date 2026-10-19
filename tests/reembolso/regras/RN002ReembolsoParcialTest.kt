package reembolso.regras

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import reembolso.dominio.ResultadoDespesa
import reembolso.dominio.Valor

class RN002ReembolsoParcialTest {

    private fun alimentacao(valor: String) =
        avaliar(despesa("x", "2026-07-10", "alimentacao", "Bistro Central", valor, nf = true)).aceita("x")

    private fun assertIntegral(d: ResultadoDespesa.Aceita, reembolso: String) {
        assertEquals(Valor.de(reembolso), d.reembolsado)
        assertNull(d.excedente)
    }

    private fun assertParcial(d: ResultadoDespesa.Aceita, reembolso: String, excedente: String) {
        assertEquals(Valor.de(reembolso), d.reembolsado)
        assertEquals(Valor.de(excedente), d.excedente)
    }

    @Test
    fun `RN-002 AMB-002 d-001 72,50 → excedente 12,50`() {
        assertParcial(alimentacao("72.50"), reembolso = "60.00", excedente = "12.50")
    }

    @Test
    fun `RN-002 AMB-002 alimentacao de 60,00 → reembolso 60,00 integral`() {
        assertIntegral(alimentacao("60.00"), reembolso = "60.00")
    }

    @Test
    fun `RN-002 AMB-002 alimentacao de 60,01 → reembolso 60,00 parcial e excedente 0,01`() {
        assertParcial(alimentacao("60.01"), reembolso = "60.00", excedente = "0.01")
    }

    @Test
    fun `RN-002 transporte urbano de 90,00 → reembolso 80,00 parcial e excedente 10,00`() {
        val d = avaliar(despesa("t", "2026-07-10", "transporte_urbano", "TaxiApp", "90.00", nf = false)).aceita("t")
        assertEquals(Valor.de("80.00"), d.limite)
        assertParcial(d, reembolso = "80.00", excedente = "10.00")
    }
}
