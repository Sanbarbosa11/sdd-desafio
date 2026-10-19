package reembolso.regras

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import reembolso.dominio.CampoDespesa
import reembolso.dominio.CausaErro
import reembolso.dominio.ElementoLido
import reembolso.dominio.Falha
import reembolso.dominio.ItemValidado
import reembolso.dominio.Lido
import reembolso.dominio.Referencia
import reembolso.dominio.ResultadoDespesa
import reembolso.dominio.validarItens
import reembolso.dominio.validarLote

class RN010IdentidadeDoItemTest {

    private fun erro(item: ItemValidado): ResultadoDespesa.ComErro = assertIs<ItemValidado.Invalido>(item).erro

    @Test
    fun `RN-010 AMB-020 despesa sem identificador na terceira posição → erro identificado pela posição 3`() {
        val itens = validarItens(listOf(despesa("d-001"), despesa("d-002"), despesa(id = Lido.Ausente), despesa("d-004")))

        val semId = erro(itens[2])
        assertEquals(Referencia.Posicao(3), semId.ref)
        assertEquals(listOf(CausaErro(CampoDespesa.IDENTIFICADOR, Falha.AUSENTE)), semId.causas)
        listOf(0, 1, 3).forEach { assertIs<ItemValidado.Valido>(itens[it]) }
    }

    @Test
    fun `RN-010 AMB-020 identificador com tipo incompatível → erro identificado pela posição`() {
        val itens = validarItens(listOf(despesa("d-001"), despesa(id = Lido.TipoIncompativel)))

        val e = erro(itens[1])
        assertEquals(Referencia.Posicao(2), e.ref)
        assertEquals(listOf(CausaErro(CampoDespesa.IDENTIFICADOR, Falha.TIPO_INCOMPATIVEL)), e.causas)
    }

    @Test
    fun `RN-012 AMB-020 duas despesas sem identificador → ambas erro de item e lote não rejeitado`() {
        val elementos = listOf(despesa(id = Lido.Ausente), despesa("d-002"), despesa(id = Lido.Ausente))

        assertNull(validarLote(lote(periodo(), *elementos.toTypedArray())))
        val itens = validarItens(elementos)
        assertEquals(Referencia.Posicao(1), erro(itens[0]).ref)
        assertEquals(Referencia.Posicao(3), erro(itens[2]).ref)
        assertIs<ItemValidado.Valido>(itens[1])
    }

    @Test
    fun `RN-010 AMB-020 elemento que não é despesa → erro de item na sua posição com causa única`() {
        val elementos = listOf(despesa("d-001"), ElementoLido.NaoEDespesa, despesa("d-003"))

        assertNull(validarLote(lote(periodo(), *elementos.toTypedArray())))
        val itens = validarItens(elementos)
        assertEquals(
            ResultadoDespesa.ComErro(
                ref = Referencia.Posicao(2),
                valorConsiderado = null,
                categoriaConsiderada = null,
                causas = listOf(CausaErro(CampoDespesa.DESPESA, Falha.TIPO_INCOMPATIVEL)),
            ),
            erro(itens[1]),
        )
        assertIs<ItemValidado.Valido>(itens[0])
        assertIs<ItemValidado.Valido>(itens[2])
    }

    @Test
    fun `AMB-021 sem identificador e data 2026-02-30 → causas identificador ausente e data inexistente nessa ordem`() {
        val e = erro(validarItens(listOf(despesa(id = Lido.Ausente, data = Lido.DataInexistente))).single())
        assertEquals(
            listOf(
                CausaErro(CampoDespesa.IDENTIFICADOR, Falha.AUSENTE),
                CausaErro(CampoDespesa.DATA, Falha.DATA_INEXISTENTE),
            ),
            e.causas,
        )
        assertEquals(Referencia.Posicao(1), e.ref)
    }

    @Test
    fun `RN-012 AMB-020 identificador com espaços nas extremidades é considerado aparado`() {
        val itens = validarItens(listOf(despesa(" d-001 "), despesa(id = Lido.Presente("\td-002 "), valor = Lido.Ausente)))

        assertEquals("d-001", assertIs<ItemValidado.Valido>(itens[0]).despesa.id)
        assertEquals(Referencia.Id("d-002"), erro(itens[1]).ref)
    }
}
