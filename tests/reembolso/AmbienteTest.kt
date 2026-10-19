package reembolso

import java.io.DataInputStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AmbienteTest {

    @Test
    fun `build compila src e executa testes de tests`() {
        val classe = Class.forName("reembolso.cli.MainKt")
        assertNotNull(classe.getMethod("main", Array<String>::class.java))
    }

    @Test
    fun `DT-001 bytecode de src é Java 17`() {
        val recurso = assertNotNull(javaClass.classLoader.getResourceAsStream("reembolso/cli/MainKt.class"))
        val versaoMajor = DataInputStream(recurso).use { entrada ->
            entrada.readInt() // magic 0xCAFEBABE
            entrada.readUnsignedShort() // minor
            entrada.readUnsignedShort()
        }
        assertEquals(61, versaoMajor, "class file major 61 = Java 17")
    }
}
