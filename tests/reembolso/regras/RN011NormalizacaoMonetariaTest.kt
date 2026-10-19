package reembolso.regras

import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import reembolso.dominio.Valor

class RN011NormalizacaoMonetariaTest {

    private fun normalizado(texto: String) = Valor.normalizar(BigDecimal(texto)).decimal.toPlainString()

    @Test
    fun `RN-011 AMB-014 33,333 normaliza para 33,33`() {
        assertEquals("33.33", normalizado("33.333"))
    }

    @Test
    fun `RN-011 AMB-014 100,005 normaliza para 100,01 e 100,004 para 100,00`() {
        assertEquals("100.01", normalizado("100.005"))
        assertEquals("100.00", normalizado("100.004"))
    }

    @Test
    fun `RN-011 AMB-014 −45,005 normaliza para −45,01 afastando-se do zero`() {
        assertEquals("-45.01", normalizado("-45.005"))
    }

    @Test
    fun `RN-011 AMB-014 54,90 e 54,900 são o mesmo valor`() {
        val a = Valor.normalizar(BigDecimal("54.90"))
        val b = Valor.normalizar(BigDecimal("54.900"))
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
        assertEquals("54.90", b.decimal.toPlainString())
    }

    // Aceites que passam a ser verificáveis com a etapa de valor (T-008).

    @Test
    fun `RN-011 d-011 33,333 → reembolso 33,33 integral`() {
        val d = avaliar(despesa("d-011", "2026-07-15", "alimentacao", "Hotel Copa Sul", "33.333", nf = true))
            .aceita("d-011")
        assertEquals("33.33", d.valor.decimal.toPlainString())
        assertEquals("33.33", d.reembolsado.decimal.toPlainString())
        assertNull(d.excedente)
    }

    @Test
    fun `RN-011 alimentacao de 60,004 → reembolso 60,00 integral`() {
        val d = avaliar(despesa("x", "2026-07-15", "alimentacao", "Bistro Central", "60.004", nf = true)).aceita("x")
        assertEquals("60.00", d.valor.decimal.toPlainString())
        assertEquals("60.00", d.reembolsado.decimal.toPlainString())
        assertNull(d.excedente)
    }
}
