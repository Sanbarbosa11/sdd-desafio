package reembolso.regras

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import reembolso.dominio.CausaLote
import reembolso.dominio.Lido
import reembolso.dominio.ResultadoLote
import reembolso.dominio.validarLote

class RN012IdentificadorUnicoTest {

    @Test
    fun `RN-012 AMB-019 duas despesas com identificador d-006 → lote rejeitado sem avaliar nenhuma`() {
        val resultado = validarLote(lote(periodo(), despesa("d-005"), despesa("d-006"), despesa("d-006")))
        // Rejeitado não carrega resultado de nenhuma despesa, nem da d-005, de identificador único.
        assertEquals(ResultadoLote.Rejeitado(listOf(CausaLote.IdentificadorRepetido(listOf("d-006")))), resultado)
    }

    @Test
    fun `RN-012 AMB-020 d-006 com espaço à esquerda repete d-006 → lote rejeitado`() {
        assertEquals(
            listOf(CausaLote.IdentificadorRepetido(listOf("d-006"))),
            validarLote(lote(periodo(), despesa(" d-006"), despesa("d-006\t")))?.causas,
        )
    }

    @Test
    fun `RN-012 d-006 e D-006 são identificadores distintos`() {
        assertNull(validarLote(lote(periodo(), despesa("d-006"), despesa("D-006"))))
    }

    @Test
    fun `RN-012 identificadores d-001 a d-014 distintos → sem erro de lote`() {
        val despesas = (1..14).map { despesa("d-%03d".format(it)) }
        assertNull(validarLote(lote(periodo(), *despesas.toTypedArray())))
    }

    // AMB-020: ausência não é valor, então não há repetição (aceite da RN-012).
    @Test
    fun `RN-012 AMB-020 duas despesas sem identificador ou com identificador vazio não constituem repetição`() {
        assertNull(
            validarLote(
                lote(
                    periodo(),
                    despesa(id = Lido.Ausente),
                    despesa(id = Lido.Ausente),
                    despesa(id = Lido.TipoIncompativel),
                    despesa(id = Lido.TipoIncompativel),
                    despesa("  "),
                    despesa("\t"),
                ),
            ),
        )
    }
}
