package reembolso.rastreabilidade

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class FronteiraDominioTest {

    @Test
    fun `DT-009 nenhum arquivo do dominio importa kotlinx ou java io`() {
        val arquivos = File("src/reembolso/dominio").walkTopDown().filter { it.extension == "kt" }.toList()
        assertTrue(arquivos.isNotEmpty(), "nenhum arquivo encontrado em src/reembolso/dominio")

        // Pega import e também nome qualificado no meio do código; ignora comentários de linha.
        val proibido = Regex("""\b(kotlinx|java\.io)\.""")
        val violacoes = arquivos.flatMap { arquivo ->
            arquivo.readLines().withIndex()
                .filter { (_, linha) -> proibido.containsMatchIn(linha.substringBefore("//")) }
                .map { (i, linha) -> "${arquivo.path}:${i + 1}: ${linha.trim()}" }
        }
        assertTrue(violacoes.isEmpty(), "o domínio não pode depender de kotlinx nem java.io:\n" + violacoes.joinToString("\n"))
    }
}
