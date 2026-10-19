package reembolso.rastreabilidade

import kotlin.test.Test
import kotlin.test.assertEquals
import reembolso.dominio.CampoDespesa

class OrdemCamposSecao41Test {

    // Informação da tabela de despesa da §4.1 → campo do domínio; null = sem regra, não lida (AMB-015).
    private val correspondencia = mapOf(
        "Identificador" to CampoDespesa.IDENTIFICADOR,
        "Data" to CampoDespesa.DATA,
        "Categoria" to CampoDespesa.CATEGORIA,
        "Descrição" to null,
        "Fornecedor" to CampoDespesa.FORNECEDOR,
        "Valor" to CampoDespesa.VALOR,
        "Nota fiscal apresentada" to CampoDespesa.NOTA_FISCAL,
    )

    @Test
    fun `§4-1 AMB-021 ordem de CampoDespesa é a ordem da tabela de despesa da spec`() {
        val secao = Spec.secao("### 4.1")
        val inicioTabela = secao.indexOfFirst { it.startsWith("**Informações de cada despesa**") }
        val informacoes = secao.drop(inicioTabela + 1)
            .dropWhile { !it.startsWith("|") }
            .takeWhile { it.startsWith("|") }
            .drop(2) // cabeçalho e separador
            .map { it.split("|")[1].trim() }

        // Uma informação nova na spec quebra aqui até ganhar lugar na correspondência.
        assertEquals(correspondencia.keys.toList(), informacoes)

        val ordemDaSpec = informacoes.mapNotNull { correspondencia.getValue(it) }
        assertEquals(ordemDaSpec, CampoDespesa.entries - CampoDespesa.DESPESA)
    }
}
