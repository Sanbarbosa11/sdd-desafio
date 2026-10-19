package reembolso.regras

import kotlin.test.Test
import kotlin.test.assertEquals
import reembolso.dominio.Motivo
import reembolso.dominio.ResultadoDespesa
import reembolso.dominio.Valor

class RN006ForaDoPeriodoTest {

    // Recusada não tem campo de limite nem de reembolso: o tipo garante "sem limite aplicado" e 0,00.
    private fun List<ResultadoDespesa>.recusada(id: String) =
        filterIsInstance<ResultadoDespesa.Recusada>().single { it.id == id }

    @Test
    fun `RN-006 AMB-010 d-008 alimentacao 41,00 com NF → recusada com FORA_DA_COMPETENCIA sem limite aplicado`() {
        val d008 = despesa("d-008", "2026-04-15", "alimentacao", "Restaurante Tavola", "41.00", nf = true)
        val resultados = avaliar(d008)

        val d = resultados.recusada("d-008")
        assertEquals(listOf(Motivo.FORA_DA_COMPETENCIA), d.motivos)
        assertEquals(Valor.de("41.00"), d.valor)
        // AMB-010: o item aparece na saída, como qualquer outro.
        assertEquals(1, resultados.size)
    }

    @Test
    fun `RN-006 despesa em 2026-08-01 com NF → recusada com FORA_DA_COMPETENCIA`() {
        val d = avaliar(despesa("x", "2026-08-01", "alimentacao", "Bistro Central", "30.00", nf = true)).recusada("x")
        assertEquals(listOf(Motivo.FORA_DA_COMPETENCIA), d.motivos)
    }

    @Test
    fun `RN-006 despesa em 2026-07-31 igual à data de fim → regra não se aplica`() {
        avaliar(despesa("x", "2026-07-31", "alimentacao", "Restaurante Tavola", "61.00", nf = true)).aceita("x")
    }
}
