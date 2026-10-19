package reembolso.rastreabilidade

import java.io.File

// O diretório de trabalho dos testes é a raiz do projeto (plan DT-002).
internal object Spec {
    val linhas: List<String> by lazy { File("specs/001-motor-reembolso/spec.md").readLines() }

    // Linhas entre o título que começa com `titulo` e o próximo título de nível igual ou superior.
    fun secao(titulo: String): List<String> {
        val inicio = linhas.indexOfFirst { it.startsWith(titulo) }
        require(inicio >= 0) { "seção não encontrada na spec: $titulo" }
        val nivel = titulo.takeWhile { it == '#' }.length
        val fim = (inicio + 1 until linhas.size).firstOrNull { i ->
            val hashes = linhas[i].takeWhile { it == '#' }.length
            hashes in 1..nivel && linhas[i].getOrNull(hashes) == ' '
        } ?: linhas.size
        return linhas.subList(inicio + 1, fim)
    }
}
