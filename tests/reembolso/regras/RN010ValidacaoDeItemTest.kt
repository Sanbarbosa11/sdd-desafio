package reembolso.regras

import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import reembolso.dominio.CampoDespesa
import reembolso.dominio.CausaErro
import reembolso.dominio.ElementoLido
import reembolso.dominio.Falha
import reembolso.dominio.ItemValidado
import reembolso.dominio.Lido
import reembolso.dominio.Referencia
import reembolso.dominio.ResultadoDespesa
import reembolso.dominio.Valor
import reembolso.dominio.validarDespesa

class RN010ValidacaoDeItemTest {

    private fun valor(texto: String) = Lido.Presente(BigDecimal(texto))
    private fun texto(t: String) = Lido.Presente(t)

    private fun erro(lida: ElementoLido.DespesaLida, posicao: Int = 1): ResultadoDespesa.ComErro =
        assertIs<ItemValidado.Invalido>(validarDespesa(lida, posicao)).erro

    private fun causa(campo: CampoDespesa, falha: Falha) = CausaErro(campo, falha)

    private val d009 = despesa(
        id = texto("d-009"),
        data = data("2026-07-11"),
        categoria = texto("transporte_urbano"),
        fornecedor = texto("TaxiApp"),
        valor = valor("-45.00"),
        temNotaFiscal = Lido.Presente(false),
    )

    @Test
    fun `RN-010 AMB-012 d-009 −45,00 → erro de item com causa valor não positivo`() {
        assertEquals(
            ResultadoDespesa.ComErro(
                ref = Referencia.Id("d-009"),
                valorConsiderado = Valor.de("-45.00"),
                categoriaConsiderada = "transporte_urbano",
                causas = listOf(causa(CampoDespesa.VALOR, Falha.VALOR_NAO_POSITIVO)),
            ),
            erro(d009),
        )
    }

    @Test
    fun `RN-010 AMB-017 despesa sem o campo de valor → erro de item e as demais processadas`() {
        val resultados = listOf(despesa("d-001"), despesa(id = texto("d-002"), valor = Lido.Ausente), despesa("d-003"))
            .mapIndexed { i, d -> validarDespesa(d, i + 1) }

        val semValor = assertIs<ItemValidado.Invalido>(resultados[1]).erro
        assertEquals(listOf(causa(CampoDespesa.VALOR, Falha.AUSENTE)), semValor.causas)
        assertEquals(null, semValor.valorConsiderado)
        assertIs<ItemValidado.Valido>(resultados[0])
        assertIs<ItemValidado.Valido>(resultados[2])
    }

    @Test
    fun `RN-010 despesa de valor 0,00 → erro de item`() {
        assertEquals(
            listOf(causa(CampoDespesa.VALOR, Falha.VALOR_NAO_POSITIVO)),
            erro(despesa(valor = valor("0.00"))).causas,
        )
    }

    @Test
    fun `RN-011 AMB-014 0,004 normaliza para 0,00 e é erro de item`() {
        val e = erro(despesa(valor = valor("0.004")))
        assertEquals(Valor.de("0.00"), e.valorConsiderado)
        assertEquals("0.00", e.valorConsiderado?.decimal?.toPlainString())
        assertEquals(listOf(causa(CampoDespesa.VALOR, Falha.VALOR_NAO_POSITIVO)), e.causas)
    }

    @Test
    fun `RN-011 −45,005 é erro de item com valor considerado −45,01`() {
        val e = erro(despesa(valor = valor("-45.005")))
        assertEquals("-45.01", e.valorConsiderado?.decimal?.toPlainString())
        assertEquals(listOf(causa(CampoDespesa.VALOR, Falha.VALOR_NAO_POSITIVO)), e.causas)
    }

    @Test
    fun `RN-010 despesa sem indicação de nota fiscal → erro de item e não SEM_NOTA_FISCAL`() {
        // ComErro não tem lista de motivos: o tipo impede SEM_NOTA_FISCAL aqui.
        assertEquals(
            listOf(causa(CampoDespesa.NOTA_FISCAL, Falha.AUSENTE)),
            erro(despesa(valor = valor("150.00"), temNotaFiscal = Lido.Ausente)).causas,
        )
    }

    @Test
    fun `RN-010 fornecedor só com espaços, tabulação ou espaço não separável → erro de item ausente`() {
        val nbsp = String(Character.toChars(0x00A0))
        for (fornecedor in listOf("   ", "\t", nbsp, " \t$nbsp ")) {
            assertEquals(
                listOf(causa(CampoDespesa.FORNECEDOR, Falha.AUSENTE)),
                erro(despesa(fornecedor = texto(fornecedor))).causas,
                "fornecedor com code points ${fornecedor.map { "U+%04X".format(it.code) }}",
            )
        }
    }

    @Test
    fun `RN-010 AMB-015 fornecedor com tipo incompatível → erro de item`() {
        assertEquals(
            listOf(causa(CampoDespesa.FORNECEDOR, Falha.TIPO_INCOMPATIVEL)),
            erro(despesa(fornecedor = Lido.TipoIncompativel)).causas,
        )
    }

    @Test
    fun `RN-010 data 2026-02-30 → erro de item data inexistente`() {
        assertEquals(
            listOf(causa(CampoDespesa.DATA, Falha.DATA_INEXISTENTE)),
            erro(despesa(data = Lido.DataInexistente)).causas,
        )
    }

    @Test
    fun `RN-010 AMB-021 sem fornecedor e valor −5,00 → causas fornecedor ausente e valor não positivo nessa ordem`() {
        assertEquals(
            listOf(
                causa(CampoDespesa.FORNECEDOR, Falha.AUSENTE),
                causa(CampoDespesa.VALOR, Falha.VALOR_NAO_POSITIVO),
            ),
            erro(despesa(fornecedor = Lido.Ausente, valor = valor("-5.00"))).causas,
        )
    }

    @Test
    fun `RN-010 AMB-021 todas as informações inválidas → uma causa por informação na ordem da §4-1`() {
        val e = erro(
            despesa(
                id = texto(" "),
                data = Lido.DataInexistente,
                categoria = Lido.TipoIncompativel,
                fornecedor = Lido.Ausente,
                valor = valor("-1"),
                temNotaFiscal = Lido.TipoIncompativel,
            ),
            posicao = 4,
        )
        assertEquals(
            listOf(
                causa(CampoDespesa.IDENTIFICADOR, Falha.AUSENTE),
                causa(CampoDespesa.DATA, Falha.DATA_INEXISTENTE),
                causa(CampoDespesa.CATEGORIA, Falha.TIPO_INCOMPATIVEL),
                causa(CampoDespesa.FORNECEDOR, Falha.AUSENTE),
                causa(CampoDespesa.VALOR, Falha.VALOR_NAO_POSITIVO),
                causa(CampoDespesa.NOTA_FISCAL, Falha.TIPO_INCOMPATIVEL),
            ),
            e.causas,
        )
        assertEquals(Referencia.Posicao(4), e.ref)
        assertEquals(null, e.categoriaConsiderada)
    }

    @Test
    fun `RN-010 erro de item em uma despesa não altera o resultado das demais`() {
        val validas = listOf(despesa("d-001"), despesa(id = texto("d-002"), valor = valor("72.50")))
        val sozinhas = validas.mapIndexed { i, d -> validarDespesa(d, i + 1) }

        val comErroNoMeio = listOf(validas[0], d009, validas[1]).mapIndexed { i, d -> validarDespesa(d, i + 1) }

        assertEquals(sozinhas, listOf(comErroNoMeio[0], comErroNoMeio[2]))
        assertIs<ItemValidado.Invalido>(comErroNoMeio[1])
    }
}
