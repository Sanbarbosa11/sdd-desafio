package reembolso.dominio

import kotlin.test.Test
import kotlin.test.assertEquals

class EspacosUnicodeTest {

    private fun texto(vararg codePoints: Int) = String(codePoints, 0, codePoints.size)

    @Test
    fun `§4-1 aparar remove os 25 code points White_Space nas extremidades`() {
        // Oráculo independente da lista do domínio: a propriedade White_Space do regex do JDK.
        val whiteSpace = Regex("\\p{IsWhite_Space}")
        val esperados = (0..0x10FFFF)
            .filter { it !in 0xD800..0xDFFF && whiteSpace.matches(texto(it)) }
        assertEquals(25, esperados.size)

        for (cp in 0..0x10FFFF) {
            if (cp in 0xD800..0xDFFF) continue
            val c = texto(cp)
            val esperado = if (cp in esperados) "a b" else "${c}a b$c"
            assertEquals(esperado, "${c}a b$c".aparar(), "U+%04X".format(cp))
        }

        val todos = texto(*esperados.toIntArray())
        assertEquals("d-006", "${todos}d-006$todos".aparar())
    }

    @Test
    fun `§4-1 aparar remove U+0085 e não remove U+001C a U+001F`() {
        assertEquals("x", "\u0085x\u0085".aparar())
        for (cp in 0x1C..0x1F) {
            val c = cp.toChar()
            assertEquals("${c}x$c", "${c}x$c".aparar(), "U+%04X".format(cp))
        }
    }

    @Test
    fun `§4-1 aparar não remove espaços do meio do texto`() {
        val nbsp = texto(0x00A0)
        val figureSpace = texto(0x2007)
        val narrowNbsp = texto(0x202F)
        assertEquals("Bistro \t$nbsp Central", "  Bistro \t$nbsp Central$figureSpace ".aparar())
        assertEquals("", " \t$nbsp$narrowNbsp ".aparar())
    }
}
