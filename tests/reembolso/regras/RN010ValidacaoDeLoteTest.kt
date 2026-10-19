package reembolso.regras

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import reembolso.dominio.CausaLote
import reembolso.dominio.DespesasLidas
import reembolso.dominio.Lido
import reembolso.dominio.LoteLido
import reembolso.dominio.PeriodoLido
import reembolso.dominio.ResultadoLote
import reembolso.dominio.validarLote

class RN010ValidacaoDeLoteTest {

    private fun causas(lote: LoteLido) = validarLote(lote)?.causas

    @Test
    fun `RN-010 AMB-017 início posterior ao fim → lote rejeitado sem despesa processada`() {
        val resultado = validarLote(lote(periodo("2026-07-31", "2026-07-01"), despesa("d-001"), despesa("d-002")))
        // Rejeitado não tem resultado por despesa nem consolidado: nenhuma despesa é processada.
        assertEquals(ResultadoLote.Rejeitado(listOf(CausaLote.InicioPosteriorAoFim)), resultado)
    }

    @Test
    fun `RN-010 período ausente → lote rejeitado`() {
        assertEquals(listOf(CausaLote.PeriodoAusente), causas(lote(PeriodoLido.Ausente, despesa("d-001"))))
    }

    @Test
    fun `RN-010 AMB-020 período com tipo incompatível → lote rejeitado`() {
        assertEquals(
            listOf(CausaLote.PeriodoTipoIncompativel),
            causas(lote(PeriodoLido.TipoIncompativel, despesa("d-001"))),
        )
    }

    @Test
    fun `RN-010 AMB-020 relação de despesas com tipo incompatível → lote rejeitado`() {
        assertEquals(
            listOf(CausaLote.DespesasTipoIncompativel),
            causas(lote(despesas = DespesasLidas.TipoIncompativel)),
        )
    }

    @Test
    fun `RN-010 despesas não informadas → lote rejeitado`() {
        assertEquals(listOf(CausaLote.DespesasAusentes), causas(lote(despesas = DespesasLidas.Ausente)))
    }

    @Test
    fun `RN-010 data de início inválida → lote rejeitado`() {
        assertEquals(
            listOf(CausaLote.InicioInvalido),
            causas(lote(PeriodoLido.Presente(Lido.DataInexistente, data("2026-07-31")))),
        )
        assertEquals(
            listOf(CausaLote.InicioInvalido),
            causas(lote(PeriodoLido.Presente(Lido.TipoIncompativel, data("2026-07-31")))),
        )
        assertEquals(
            listOf(CausaLote.InicioAusente, CausaLote.FimInvalido),
            causas(lote(PeriodoLido.Presente(Lido.Ausente, Lido.DataInexistente))),
        )
    }

    @Test
    fun `RN-010 período com início igual ao fim → válido`() {
        assertNull(validarLote(lote(periodo("2026-07-15", "2026-07-15"), despesa("d-001"))))
        assertNull(validarLote(lote(periodo(), despesas = DespesasLidas.Presente(emptyList()))))
    }

    @Test
    fun `RN-010 AMB-019 início posterior ao fim e identificador repetido → as duas causas`() {
        assertEquals(
            listOf(CausaLote.InicioPosteriorAoFim, CausaLote.IdentificadorRepetido(listOf("d-006"))),
            causas(lote(periodo("2026-07-31", "2026-07-01"), despesa("d-006"), despesa("d-006"))),
        )
    }
}
