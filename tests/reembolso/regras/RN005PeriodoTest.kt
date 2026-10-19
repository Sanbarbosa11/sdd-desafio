package reembolso.regras

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import reembolso.dominio.Periodo

class RN005PeriodoTest {

    // Período do lote de exemplo.
    private val julho = Periodo(LocalDate.parse("2026-07-01"), LocalDate.parse("2026-07-31"))

    private fun dentro(data: String) = LocalDate.parse(data) in julho

    @Test
    fun `RN-005 AMB-016 período do lote de exemplo é de 2026-07-01 a 2026-07-31 inclusive`() {
        val lote = avaliado(lote(periodo("2026-07-01", "2026-07-31"), despesa("d-001")))
        assertEquals(julho, lote.periodo)
        assertTrue(dentro("2026-07-01"))
        assertTrue(dentro("2026-07-31"))
        assertFalse(dentro("2026-06-30"))
    }

    @Test
    fun `RN-005 AMB-016 d-014 em 2026-07-31 igual à data de fim → dentro do período`() {
        assertTrue(dentro("2026-07-31"))
    }

    @Test
    fun `RN-005 AMB-016 despesa em 2026-07-01 igual à data de início → dentro do período`() {
        assertTrue(dentro("2026-07-01"))
    }

    @Test
    fun `RN-005 AMB-016 despesa em 2026-08-01 → fora do período`() {
        assertFalse(dentro("2026-08-01"))
    }

    @Test
    fun `RN-005 AMB-008 d-008 em 2026-04-15 → fora do período`() {
        assertFalse(dentro("2026-04-15"))
    }

    @Test
    fun `RN-005 d-001 a d-014 exceto d-008 e d-009 → dentro do período`() {
        // Datas do lote de exemplo; d-008 (15/04) fica fora e d-009 é erro de item (RN-010).
        val datas = mapOf(
            "d-001" to "2026-07-03", "d-002" to "2026-07-03", "d-003" to "2026-07-06",
            "d-004" to "2026-07-06", "d-005" to "2026-07-07", "d-006" to "2026-07-09",
            "d-007" to "2026-07-09", "d-010" to "2026-07-14", "d-011" to "2026-07-15",
            "d-012" to "2026-07-18", "d-013" to "2026-07-22", "d-014" to "2026-07-31",
        )
        datas.forEach { (id, data) -> assertTrue(dentro(data), "$id em $data") }
    }
}
