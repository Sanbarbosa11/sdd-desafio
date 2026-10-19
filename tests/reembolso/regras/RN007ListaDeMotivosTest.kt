package reembolso.regras

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import reembolso.dominio.Lido
import reembolso.dominio.Motivo
import reembolso.dominio.ResultadoDespesa
import reembolso.dominio.Valor

class RN007ListaDeMotivosTest {

    private fun campos(classe: Class<*>) = classe.declaredFields.map { it.name }.toSet()

    @Test
    fun `RN-007 AMB-018 150,00 sem NF fora do período → motivos FORA_DA_COMPETENCIA e SEM_NOTA_FISCAL nessa ordem`() {
        val d = avaliar(despesa("x", "2026-08-01", "alimentacao", "Bistro Central", "150.00", nf = false)).recusada("x")
        assertEquals(listOf(Motivo.FORA_DA_COMPETENCIA, Motivo.SEM_NOTA_FISCAL), d.motivos)
    }

    @Test
    fun `RN-007 AMB-018 d-008 → motivos FORA_DA_COMPETENCIA`() {
        val d = avaliar(despesa("d-008", "2026-04-15", "alimentacao", "Restaurante Tavola", "41.00", nf = true))
            .recusada("d-008")
        assertEquals(listOf(Motivo.FORA_DA_COMPETENCIA), d.motivos)
    }

    @Test
    fun `RN-007 AMB-018 d-004 → motivos SEM_NOTA_FISCAL`() {
        val d = avaliar(despesa("d-004", "2026-07-06", "transporte_urbano", "TaxiApp", "100.01", nf = false))
            .recusada("d-004")
        assertEquals(listOf(Motivo.SEM_NOTA_FISCAL), d.motivos)
    }

    // Aceita não tem lista de motivos: sem violação, o tipo é a lista vazia.
    @Test
    fun `RN-007 d-002 sem violação → lista de motivos vazia`() {
        val d = avaliar(despesa("d-002", "2026-07-03", "alimentacao", "Cantina do Porto", "38.00", nf = true)).single()
        assertIs<ResultadoDespesa.Aceita>(d)
        assertFalse("motivos" in campos(ResultadoDespesa.Aceita::class.java))
    }

    @Test
    fun `RN-007 d-001 parcial → lista de motivos vazia`() {
        val d = avaliar(despesa("d-001", "2026-07-03", "alimentacao", "Restaurante Tavola", "72.50", nf = true)).single()
        // O reembolso parcial não é motivo de recusa.
        assertEquals(Valor.de("12.50"), assertIs<ResultadoDespesa.Aceita>(d).excedente)
    }

    @Test
    fun `RN-007 AMB-017 d-009 erro → lista de motivos vazia`() {
        val d009 = despesa(
            Lido.Presente("d-009"), data("2026-07-11"), Lido.Presente("transporte_urbano"),
            Lido.Presente("TaxiApp"), valor("-45.00"), Lido.Presente(false),
        )
        assertIs<ResultadoDespesa.ComErro>(avaliar(d009).single())
        // Erro não é recusa: o resultado de erro não tem lista de motivos.
        assertFalse("motivos" in campos(ResultadoDespesa.ComErro::class.java))
    }

    @Test
    fun `§8 despesa recusada não tem limite aplicado`() {
        val camposRecusada = campos(ResultadoDespesa.Recusada::class.java)
        assertFalse("limite" in camposRecusada)
        assertFalse("excedente" in camposRecusada)
        assertTrue("limite" in campos(ResultadoDespesa.Aceita::class.java)) // guarda: o nome do campo é este
    }
}
