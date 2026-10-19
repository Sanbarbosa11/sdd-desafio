package reembolso.regras

import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import reembolso.dominio.ItemValidado
import reembolso.dominio.Lido
import reembolso.dominio.Motivo
import reembolso.dominio.ResultadoDespesa
import reembolso.dominio.Valor
import reembolso.dominio.validarDespesa

class RN008CategoriaTest {

    private fun cp(vararg codePoints: Int) = String(codePoints, 0, codePoints.size)

    private fun comCategoria(categoria: String, data: String = "2026-07-10", valor: String = "50.00") =
        avaliar(despesa("x", data, categoria, "Bistro Central", valor, nf = true)).single()

    private fun recusada(r: ResultadoDespesa) = assertIs<ResultadoDespesa.Recusada>(r)

    private fun fornecedorNormalizado(fornecedor: String): String =
        assertIs<ItemValidado.Valido>(validarDespesa(despesa(fornecedor = Lido.Presente(fornecedor)), 1)).despesa.fornecedor

    @Test
    fun `RN-008 AMB-013 d-014 ALIMENTACAO 61,00 → categoria alimentacao, reembolso 60,00 parcial`() {
        val d = avaliar(despesa("d-014", "2026-07-31", "ALIMENTACAO", "Restaurante Tavola", "61.00", nf = true))
            .aceita("d-014")
        assertEquals("alimentacao", d.categoria)
        assertEquals(Valor.de("60.00"), d.reembolsado)
        assertEquals(Valor.de("1.00"), d.excedente)
    }

    @Test
    fun `RN-008 AMB-013 AMB-017 d-005 coworking → recusada com CATEGORIA_INELEGIVEL e não erro`() {
        val d = recusada(
            avaliar(despesa("d-005", "2026-07-07", "coworking", "HubOffice", "89.00", nf = true)).single(),
        )
        assertEquals(listOf(Motivo.CATEGORIA_INELEGIVEL), d.motivos)
        assertEquals("coworking", d.categoria)
    }

    @Test
    fun `RN-008 AMB-013 Alimentacao, ALIMENTACAO e alimentacao com espaços → a mesma categoria`() {
        for (grafia in listOf("Alimentacao", "ALIMENTACAO", " alimentacao ", "\talimentacao" + cp(0x00A0))) {
            val d = assertIs<ResultadoDespesa.Aceita>(comCategoria(grafia), "categoria '$grafia'")
            assertEquals("alimentacao", d.categoria)
            assertEquals(Valor.de("60.00"), d.limite)
        }
    }

    @Test
    fun `RN-008 AMB-013 alimentação com cedilha e til → recusada com CATEGORIA_INELEGIVEL`() {
        val d = recusada(comCategoria("alimentação"))
        assertEquals(listOf(Motivo.CATEGORIA_INELEGIVEL), d.motivos)
        assertEquals("alimentação", d.categoria)
    }

    @Test
    fun `RN-008 AMB-013 transporte-urbano → recusada com CATEGORIA_INELEGIVEL`() {
        assertEquals(listOf(Motivo.CATEGORIA_INELEGIVEL), recusada(comCategoria("transporte-urbano")).motivos)
    }

    @Test
    fun `RN-008 AMB-013 Bistro Central, BISTRO CENTRAL e Bistro Central com espaços → o mesmo fornecedor e Bistrô Central é distinto`() {
        val chaves = listOf("Bistro Central", "BISTRO CENTRAL", " Bistro Central ").map(::fornecedorNormalizado)
        assertEquals(setOf("bistro central"), chaves.toSet())
        assertNotEquals("bistro central", fornecedorNormalizado("Bistrô Central"))
    }

    @Test
    fun `RN-008 AMB-013 ALIMENTACAO em localidade turca ou azerbaijana → alimentacao`() {
        val original = Locale.getDefault()
        try {
            for (localidade in listOf("tr-TR", "az-AZ")) {
                Locale.setDefault(Locale.forLanguageTag(localidade))
                // Guarda do próprio teste: nessa localidade, a conversão dependente dela daria "alımentacao".
                assertNotEquals("alimentacao", "ALIMENTACAO".lowercase(Locale.getDefault()))

                val d = assertIs<ResultadoDespesa.Aceita>(comCategoria("ALIMENTACAO"), "localidade $localidade")
                assertEquals("alimentacao", d.categoria)
            }
        } finally {
            Locale.setDefault(original)
        }
    }

    @Test
    fun `RN-008 AMB-013 fornecedores ΟΔΟΣ e Οδος → a mesma chave οδος`() {
        val odos = cp(0x03BF, 0x03B4, 0x03BF, 0x03C2) // οδος, com sigma final
        assertEquals(odos, fornecedorNormalizado(cp(0x039F, 0x0394, 0x039F, 0x03A3))) // ΟΔΟΣ
        assertEquals(odos, fornecedorNormalizado(cp(0x039F, 0x03B4, 0x03BF, 0x03C2))) // Οδος
    }

    @Test
    fun `RN-008 AMB-013 ALİMENTACAO com U+0130 → recusada com CATEGORIA_INELEGIVEL`() {
        val d = recusada(comCategoria("AL" + cp(0x0130) + "MENTACAO"))
        assertEquals(listOf(Motivo.CATEGORIA_INELEGIVEL), d.motivos)
        // i seguido do ponto combinante U+0307: não é nenhuma das três categorias.
        assertEquals("al" + cp(0x0069, 0x0307) + "mentacao", d.categoria)
    }

    @Test
    fun `RN-008 AMB-018 fora do período e categoria inelegível → FORA_DA_COMPETENCIA antes de CATEGORIA_INELEGIVEL`() {
        val d = recusada(comCategoria("coworking", data = "2026-08-01"))
        assertEquals(listOf(Motivo.FORA_DA_COMPETENCIA, Motivo.CATEGORIA_INELEGIVEL), d.motivos)
        assertTrue(d.motivos == d.motivos.sortedBy { it.ordinal })
    }
}
