package reembolso.regras

import java.math.BigDecimal
import java.text.Collator
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import reembolso.dominio.Lido
import reembolso.dominio.Motivo
import reembolso.dominio.ResultadoDespesa
import reembolso.dominio.Valor

class RN009DuplicidadeTest {

    private fun cp(vararg codePoints: Int) = String(codePoints, 0, codePoints.size)

    // Despesa igual a d-006 em tudo que integra o critério, salvo o que for passado.
    private fun igual(
        id: String,
        data: String = "2026-07-09",
        categoria: String = "alimentacao",
        fornecedor: String = "Bistro Central",
        valor: String = "54.90",
        nf: Boolean = true,
    ) = despesa(id, data, categoria, fornecedor, valor, nf)

    private fun assertDuplicata(resultados: List<ResultadoDespesa>, recusada: String, mantida: String) {
        val r = resultados.recusada(recusada)
        assertEquals(listOf(Motivo.DUPLICATA), r.motivos)
        assertEquals(mantida, r.mantida)
        resultados.aceita(mantida)
    }

    private fun semDuplicata(resultados: List<ResultadoDespesa>) =
        resultados.none { it is ResultadoDespesa.Recusada && Motivo.DUPLICATA in it.motivos }

    @Test
    fun `RN-009 AMB-011 d-006 e d-007 → d-006 integral e d-007 recusada com DUPLICATA mantida d-006`() {
        val resultados = avaliar(igual("d-006"), igual("d-007"))
        assertDuplicata(resultados, recusada = "d-007", mantida = "d-006")
        val d006 = resultados.aceita("d-006")
        assertEquals(Valor.de("54.90"), d006.reembolsado)
        assertNull(d006.excedente)
    }

    @Test
    fun `RN-009 AMB-011 inverter a ordem de d-006 e d-007 → os mesmos resultados`() {
        val direta = avaliar(igual("d-006"), igual("d-007"))
        val invertida = avaliar(igual("d-007"), igual("d-006"))
        assertEquals(direta.toSet(), invertida.toSet())
        // A saída segue a ordem da entrada (§4.2).
        assertEquals(listOf("d-007", "d-006"), invertida.map { (it as? ResultadoDespesa.Aceita)?.id ?: (it as ResultadoDespesa.Recusada).id })
    }

    @Test
    fun `RN-009 AMB-011 iguais até 100,00 uma com NF e outra sem → mantém a de menor identificador`() {
        // A de maior identificador é a que tem nota: a nota não decide quem fica.
        assertDuplicata(avaliar(igual("a-1", nf = false), igual("a-2", nf = true)), recusada = "a-2", mantida = "a-1")
        assertDuplicata(avaliar(igual("a-1", nf = true), igual("a-2", nf = false)), recusada = "a-2", mantida = "a-1")
    }

    @Test
    fun `RN-009 hospedagens de 200,00 com a menor sem NF → a sem NF recusada SEM_NOTA_FISCAL e a outra integral sem DUPLICATA`() {
        val resultados = avaliar(
            igual("h-1", categoria = "hospedagem", fornecedor = "Hotel Copa Sul", valor = "200.00", nf = false),
            igual("h-2", categoria = "hospedagem", fornecedor = "Hotel Copa Sul", valor = "200.00", nf = true),
        )
        assertEquals(listOf(Motivo.SEM_NOTA_FISCAL), resultados.recusada("h-1").motivos)
        assertEquals(Valor.de("200.00"), resultados.aceita("h-2").reembolsado)
        assertTrue(semDuplicata(resultados))
    }

    @Test
    fun `RN-009 AMB-010 duas iguais fora do período → ambas FORA_DA_COMPETENCIA e nenhuma DUPLICATA`() {
        val resultados = avaliar(igual("d-1", data = "2026-04-15"), igual("d-2", data = "2026-04-15"))
        listOf("d-1", "d-2").forEach { assertEquals(listOf(Motivo.FORA_DA_COMPETENCIA), resultados.recusada(it).motivos) }
        listOf("d-1", "d-2").forEach { assertNull(resultados.recusada(it).mantida) }
    }

    @Test
    fun `RN-009 duas iguais de categoria inelegível → ambas CATEGORIA_INELEGIVEL e nenhuma DUPLICATA`() {
        val resultados = avaliar(igual("c-1", categoria = "coworking"), igual("c-2", categoria = "coworking"))
        listOf("c-1", "c-2").forEach { assertEquals(listOf(Motivo.CATEGORIA_INELEGIVEL), resultados.recusada(it).motivos) }
        assertTrue(semDuplicata(resultados))
    }

    @Test
    fun `RN-009 AMB-009 duas diárias de 240,00 em 14-07 e 15-07 → não são duplicatas`() {
        val resultados = avaliar(
            igual("h-14", data = "2026-07-14", categoria = "hospedagem", fornecedor = "Hotel Copa Sul", valor = "240.00"),
            igual("h-15", data = "2026-07-15", categoria = "hospedagem", fornecedor = "Hotel Copa Sul", valor = "240.00"),
        )
        listOf("h-14", "h-15").forEach { assertEquals(Valor.de("240.00"), resultados.aceita(it).reembolsado) }
    }

    @Test
    fun `RN-009 AMB-009 as mesmas duas diárias ambas em 14-07 → grupo de duplicatas`() {
        val resultados = avaliar(
            igual("h-14", data = "2026-07-14", categoria = "hospedagem", fornecedor = "Hotel Copa Sul", valor = "240.00"),
            igual("h-15", data = "2026-07-14", categoria = "hospedagem", fornecedor = "Hotel Copa Sul", valor = "240.00"),
        )
        assertDuplicata(resultados, recusada = "h-15", mantida = "h-14")
        assertEquals(Valor.de("240.00"), resultados.aceita("h-14").reembolsado)
    }

    @Test
    fun `RN-009 AMB-011 d-9 e d-10 → d-10 mantida pela ordem lexicográfica`() {
        assertDuplicata(avaliar(igual("d-9"), igual("d-10")), recusada = "d-9", mantida = "d-10")
    }

    @Test
    fun `RN-009 AMB-013 Bistro Central e BISTRO CENTRAL → grupo de duplicatas`() {
        assertDuplicata(
            avaliar(igual("d-1", fornecedor = "Bistro Central"), igual("d-2", fornecedor = " BISTRO CENTRAL ")),
            recusada = "d-2",
            mantida = "d-1",
        )
    }

    @Test
    fun `RN-009 AMB-011 d-006 e D-006 → mantém D-006`() {
        assertDuplicata(avaliar(igual("d-006"), igual("D-006")), recusada = "d-006", mantida = "D-006")
    }

    @Test
    fun `RN-009 AMB-011 d-é e d-f → d-f mantida por code point e não pela ordem alfabética de uma localidade`() {
        // Guarda: pela ordem alfabética do português, d-é viria antes de d-f; por code point, f (U+0066) < é (U+00E9).
        assertTrue(Collator.getInstance(Locale.forLanguageTag("pt-BR")).compare("d-é", "d-f") < 0)
        assertDuplicata(avaliar(igual("d-é"), igual("d-f")), recusada = "d-é", mantida = "d-f")
        // A mesma guarda vale para a caixa: pela localidade, minúscula antes; por code point, D (U+0044) < d (U+0064).
        assertTrue(Collator.getInstance(Locale.forLanguageTag("pt-BR")).compare("d-006", "D-006") < 0)
    }

    @Test
    fun `RN-009 identificador fora do BMP é comparado por code point`() {
        val meiaLargura = "d-" + cp(0xFF71) // ｱ
        val foraDoBmp = "d-" + cp(0x1F600)  // emoji, par substituto D83D DE00 em UTF-16
        // Guarda: por unidade UTF-16, o de fora do BMP viria antes (D83D < FF71).
        assertTrue(foraDoBmp < meiaLargura)
        assertDuplicata(avaliar(igual(foraDoBmp), igual(meiaLargura)), recusada = foraDoBmp, mantida = meiaLargura)
    }

    @Test
    fun `RN-011 AMB-014 54,90 e 54,904 → grupo de duplicatas`() {
        assertDuplicata(avaliar(igual("d-1", valor = "54.90"), igual("d-2", valor = "54.904")), recusada = "d-2", mantida = "d-1")
    }

    @Test
    fun `RN-009 AMB-017 despesa com erro de item não integra grupo`() {
        // d-0 seria igual a d-1 no critério, mas não tem indicação de nota fiscal: erro de item.
        val comErro = despesa(
            Lido.Presente("d-0"), data("2026-07-09"), Lido.Presente("alimentacao"),
            Lido.Presente("Bistro Central"), Lido.Presente(BigDecimal("54.90")), Lido.Ausente,
        )
        val resultados = avaliar(comErro, igual("d-1"))
        // Se integrasse o grupo, d-0 seria a mantida e d-1 receberia DUPLICATA.
        assertEquals(Valor.de("54.90"), resultados.aceita("d-1").reembolsado)
        assertTrue(resultados.any { it is ResultadoDespesa.ComErro })
    }

    @Test
    fun `RN-009 AMB-013 fornecedores ΟΔΟΣ e Οδος → grupo de duplicatas`() {
        val maiusculas = cp(0x039F, 0x0394, 0x039F, 0x03A3) // ΟΔΟΣ
        val comFinal = cp(0x039F, 0x03B4, 0x03BF, 0x03C2)   // Οδος
        assertDuplicata(
            avaliar(igual("d-1", fornecedor = maiusculas), igual("d-2", fornecedor = comFinal)),
            recusada = "d-2",
            mantida = "d-1",
        )
    }
}
