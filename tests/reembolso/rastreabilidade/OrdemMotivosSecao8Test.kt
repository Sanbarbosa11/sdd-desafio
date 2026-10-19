package reembolso.rastreabilidade

import kotlin.test.Test
import kotlin.test.assertEquals
import reembolso.dominio.Motivo

class OrdemMotivosSecao8Test {

    @Test
    fun `§8 AMB-018 ordem de Motivo é a ordem da lista de elegibilidade da spec`() {
        val motivosDaSpec = Spec.secao("## 8.")
            .flatMap { linha -> Regex("→ ([A-Z][A-Z_]+)").findAll(linha).map { it.groupValues[1] } }

        // Guarda contra uma extração vazia ou parcial passar por coincidência.
        assertEquals(4, motivosDaSpec.size, "motivos extraídos da §8: $motivosDaSpec")
        assertEquals(motivosDaSpec, Motivo.entries.map { it.name })
    }
}
