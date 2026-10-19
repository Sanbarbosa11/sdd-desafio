package reembolso.regras

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import reembolso.dominio.Motivo
import reembolso.dominio.Valor

class RN004SemNotaFiscalTest {

    @Test
    fun `RN-004 AMB-005 d-004 → reembolso 0,00 recusada com SEM_NOTA_FISCAL`() {
        val d = avaliar(despesa("d-004", "2026-07-06", "transporte_urbano", "TaxiApp", "100.01", nf = false))
            .recusada("d-004")
        assertEquals(listOf(Motivo.SEM_NOTA_FISCAL), d.motivos)
        assertNull(d.mantida)
    }

    @Test
    fun `RN-004 AMB-005 d-013 → recusada com SEM_NOTA_FISCAL`() {
        val d = avaliar(despesa("d-013", "2026-07-22", "hospedagem", "Airbnb", "690.00", nf = false)).recusada("d-013")
        assertEquals(listOf(Motivo.SEM_NOTA_FISCAL), d.motivos)
    }

    @Test
    fun `RN-004 AMB-005 alimentacao 500,00 sem NF → recusada com SEM_NOTA_FISCAL`() {
        val d = avaliar(despesa("x", "2026-07-10", "alimentacao", "Bistro Central", "500.00", nf = false)).recusada("x")
        assertEquals(listOf(Motivo.SEM_NOTA_FISCAL), d.motivos)
    }

    @Test
    fun `RN-004 AMB-005 hospedagem 200,00 sem NF → recusada com SEM_NOTA_FISCAL apesar de estar abaixo do limite`() {
        val semNf = avaliar(despesa("h", "2026-07-14", "hospedagem", "Hotel Copa Sul", "200.00", nf = false)).recusada("h")
        assertEquals(listOf(Motivo.SEM_NOTA_FISCAL), semNf.motivos)

        // Com NF, a mesma despesa seria integral.
        val comNf = avaliar(despesa("h", "2026-07-14", "hospedagem", "Hotel Copa Sul", "200.00", nf = true)).aceita("h")
        assertEquals(Valor.de("200.00"), comNf.reembolsado)
        assertNull(comNf.excedente)
    }

    @Test
    fun `RN-004 d-003 e transporte 100,01 com NF → regra não se aplica, reembolso 80,00 parcial`() {
        val resultados = avaliar(
            despesa("d-003", "2026-07-06", "transporte_urbano", "TaxiApp", "100.00", nf = false),
            despesa("t", "2026-07-06", "transporte_urbano", "Outro Taxi", "100.01", nf = true),
        )
        listOf("d-003", "t").forEach { assertEquals(Valor.de("80.00"), resultados.aceita(it).reembolsado) }
    }

    @Test
    fun `RN-004 AMB-018 fora do período, categoria inelegível e sem NF exigida → três motivos na ordem da §8`() {
        val d = avaliar(despesa("x", "2026-08-01", "coworking", "HubOffice", "150.00", nf = false)).recusada("x")
        assertEquals(
            listOf(Motivo.FORA_DA_COMPETENCIA, Motivo.CATEGORIA_INELEGIVEL, Motivo.SEM_NOTA_FISCAL),
            d.motivos,
        )
    }
}
