# Plano Técnico — Motor de Cálculo de Reembolso

**Versão:** 1.1 · **Baseado na spec:** 1.3

> Aqui mora o COMO. Este arquivo pode e deve falar de linguagem, biblioteca e
> arquitetura. O que ele **não** pode é introduzir regra de negócio nova — se
> apareceu uma, ela pertence à `spec.md`.

---

## 0. Pré-requisitos de execução

Resolvidos **antes da primeira task de código** (ver R-01 na §7):

- JDK 17 ou superior acessível pela variável `JAVA_HOME`. Nesta máquina, o
  JDK 21.0.5 do Android Studio (`C:\Program Files\Android\Android Studio\jbr`),
  que hoje não está no `PATH` nem em `JAVA_HOME`.
- Gradle Wrapper 8.11.1 gerado e versionado (`gradlew`, `gradlew.bat`,
  `gradle/wrapper/`). A distribuição 8.11.1 já está no cache local do Gradle.

A configuração de `JAVA_HOME` é pré-requisito do ambiente, não parte do
código: nada no build depende do caminho do JDK. O README declara o
requisito "JDK 17 ou superior".

## 1. Stack

| Escolha | O quê | Por quê | O que descartei e por quê |
|---|---|---|---|
| Linguagem | Kotlin 2.0.21, bytecode e API do Java 17 (DT-001); Gradle 8.11.1 com Kotlin DSL e plugin `application` | Stack definida. A versão 2.0.21 do plugin Kotlin já está no cache local. O plugin `application` gera o executável da CLI (`installDist`) | — |
| Testes | JUnit 5.11 (`junit-bom`) + `kotlin-test-junit5` | Nomes de teste entre crases carregam o ID da RN como frase; `kotlin-test` dá as asserções sem outra biblioteca | Kotest: mais uma dependência e um estilo a aprender, sem ganho de rastreabilidade |
| Parsing/validação | `kotlinx-serialization-json` 1.7.3, só a API de árvore (`Json.parseToJsonElement`, `buildJsonObject`) | O `JsonPrimitive` guarda o texto cru do número (`"33.333"`) e diz se ele veio como string ou como número, o que torna o "tipo incompatível" verificável campo a campo. Sem `@Serializable`, não é preciso o plugin de compilador de serialização | DTO `@Serializable` na entrada: falha na primeira inconsistência e converte o tipo antes de a RN-010 opinar. Jackson: precisa de `USE_BIG_DECIMAL_FOR_FLOATS` para não perder precisão, e esquecer essa configuração é exatamente o erro previsível |
| Aritmética monetária | `BigDecimal` encapsulado em `value class Valor`, sempre com escala 2 (DT-003) | Exato, com o modo de arredondamento explícito | `Double` e `Long` em centavos (DT-003) |
| CLI | Parsing manual de `calcular --input X --output Y` | Um subcomando e duas flags | Clikt: dependência para cerca de 15 linhas |
| Lint | Nenhum; `./gradlew build` é o check | Evitar cerimônia | ktlint: entra se fizer falta |

## 2. Arquitetura

```
arquivo JSON
  → [cli]     lê o arquivo, chama entrada → dominio → saida, escreve o arquivo, define o exit code
  → [entrada] JSON → LoteLido            só conhece JSON: nomes de campo, tipos JSON, formato de data
  → [dominio] Motor.avaliar(LoteLido): ResultadoLote          núcleo puro
        0. normalização (RN-011, RN-008, §4.1) e validação (RN-010, RN-012)
        1. elegibilidade, na ordem da §8: período (RN-005/006) → categoria (RN-008)
           → nota fiscal (RN-003/004) → duplicidade (RN-009)
        2. valor (RN-001, RN-002)
        3. consolidação (§4.2)
  → [saida]   ResultadoLote → JSON, com a frase de justificativa
arquivo JSON
```

| Pacote | Responsabilidade | Não pode |
|---|---|---|
| `reembolso.cli` | `main`, argumentos, leitura e escrita de arquivo, exit code | conter regra |
| `reembolso.entrada` | JSON → `LoteLido` (`Lido<T>` por campo) | decidir obrigatoriedade, aparar texto, normalizar |
| `reembolso.dominio` | toda regra da spec | importar `kotlinx.*` ou `java.io.*` |
| `reembolso.saida` | `ResultadoLote` → JSON; frase legível montada a partir dos campos | decidir algo que não esteja no resultado |

**Fronteira.** A linha passa entre o que o JSON diz e o que isso significa.

- O adaptador `entrada` traduz cada campo em
  `Lido<T> = Presente(v) | Ausente | TipoIncompativel | DataInexistente`.
  - `null` em JSON vira `Ausente`.
  - Uma data fora do formato `uuuu-MM-dd` vira `TipoIncompativel`. Uma data no
    formato mas inexistente, como `2026-02-30`, vira `DataInexistente`
    (`ResolverStyle.STRICT`).
  - O adaptador **não lê** as informações sem regra associada (colaborador,
    rótulo de competência, descrição). Assim, ausência ou tipo incompatível
    nelas não tem como produzir efeito: a AMB-015 é cumprida por construção.
- O `dominio` decide tudo que é regra: obrigatoriedade, texto vazio após
  aparar tratado como ausente, valor menor ou igual a zero, identificador
  repetido, início posterior ao fim, causas e motivos, limites. Usa apenas
  `java.time.LocalDate` e `java.math.BigDecimal`.
- Consequência: a RN-010 inteira se testa sem JSON, e uma mudança no formato
  de entrada (campo renomeado ou acrescentado) fica no adaptador e num campo
  de `LoteLido`, sem tocar nas regras.

## 3. Modelo de dados

### 3.1 Entrada lida (fronteira)

```kotlin
sealed interface Lido<out T> {
  data class Presente<T>(val valor: T) : Lido<T>
  data object Ausente : Lido<Nothing>
  data object TipoIncompativel : Lido<Nothing>
  data object DataInexistente : Lido<Nothing>
}

data class LoteLido(val periodo: PeriodoLido, val despesas: DespesasLidas)
// PeriodoLido: Ausente | TipoIncompativel | Presente(inicio: Lido<LocalDate>, fim: Lido<LocalDate>)
// DespesasLidas: Ausente | TipoIncompativel | Presente(List<ElementoLido>)
// ElementoLido: NaoEDespesa | DespesaLida(id: Lido<String>, data: Lido<LocalDate>,
//               categoria: Lido<String>, fornecedor: Lido<String>,
//               valor: Lido<BigDecimal>, temNotaFiscal: Lido<Boolean>)
// Entrada ilegível não chega aqui: o adaptador devolve Rejeitado(ENTRADA_ILEGIVEL) direto.
```

### 3.2 Núcleo

```kotlin
@JvmInline value class Valor private constructor(val decimal: BigDecimal) {
  companion object { fun normalizar(bruto: BigDecimal) = Valor(bruto.setScale(2, RoundingMode.HALF_UP)) }
}

enum class Motivo { FORA_DA_COMPETENCIA, CATEGORIA_INELEGIVEL, SEM_NOTA_FISCAL, DUPLICATA }
  // nomes = códigos de negócio da spec; ordem de declaração = ordem da §8 (teste na §6)

enum class CampoDespesa { DESPESA, IDENTIFICADOR, DATA, CATEGORIA, FORNECEDOR, VALOR, NOTA_FISCAL }
  // IDENTIFICADOR..NOTA_FISCAL na ordem da tabela da §4.1 (AMB-021); DESPESA = elemento que não é despesa (AMB-020)
enum class Falha { AUSENTE, TIPO_INCOMPATIVEL, DATA_INEXISTENTE, VALOR_NAO_POSITIVO }
data class CausaErro(val campo: CampoDespesa, val falha: Falha)

data class Despesa(                  // validada e normalizada
  val id: String,                    // aparado (§4.1)
  val data: LocalDate,
  val categoria: String,             // normalizada; String porque "coworking" é entrada válida
  val fornecedor: String,            // normalizado
  val valor: Valor,
  val temNotaFiscal: Boolean)

sealed interface Referencia { data class Id(val id: String); data class Posicao(val posicao: Int) }  // AMB-020

sealed interface ResultadoDespesa {
  data class Aceita(id, valor, categoria, limite: Valor, reembolsado: Valor, excedente: Valor?, notaExigida: Boolean)
  data class Recusada(id, valor, categoria, notaExigida: Boolean, motivos: List<Motivo>, mantida: String?)
  data class ComErro(ref: Referencia, valorConsiderado: Valor?, categoriaConsiderada: String?, causas: List<CausaErro>)
}

sealed interface ResultadoLote {
  data class Avaliado(periodo, despesas: List<ResultadoDespesa>, consolidado: Consolidado)
  data class Rejeitado(causas: List<CausaLote>)
}
```

- O `sealed` reproduz a coluna "Quando aparece" da §4.2: um `Recusada` não tem
  `limite` para ser preenchido por engano.
- A classificação é derivada do tipo (`Aceita` com excedente é parcial, sem
  excedente é integral), sem flag paralela.
- A justificativa da spec é o conjunto desses campos. A frase legível é
  montada em `saida` e não carrega decisão nenhuma.
- `CausaLote`: `ENTRADA_ILEGIVEL`, `PERIODO_AUSENTE`,
  `PERIODO_TIPO_INCOMPATIVEL`, `INICIO_AUSENTE`, `INICIO_INVALIDO`,
  `FIM_AUSENTE`, `FIM_INVALIDO`, `INICIO_POSTERIOR_AO_FIM`,
  `DESPESAS_AUSENTES`, `DESPESAS_TIPO_INCOMPATIVEL` e
  `IDENTIFICADOR_REPETIDO(ids)`. Todas as detectáveis são reportadas, menos
  `ENTRADA_ILEGIVEL`, que é exclusiva (RN-010).

### 3.3 Esquema de saída

Lote avaliado:

```json
{
  "status": "avaliado",
  "periodo": { "inicio": "2026-07-01", "fim": "2026-07-31" },
  "despesas": [
    { "id": "d-001", "classificacao": "parcial", "valor_considerado": 72.50,
      "categoria_considerada": "alimentacao", "valor_reembolsado": 60.00,
      "limite_aplicado": 60.00, "excedente": 12.50, "nota_fiscal": "nao_exigida",
      "motivos": [], "justificativa": "Alimentação de 72,50 acima do limite de 60,00: reembolsado 60,00; excedente de 12,50 não reembolsado." },
    { "id": "d-007", "classificacao": "recusada", "valor_considerado": 54.90,
      "categoria_considerada": "alimentacao", "valor_reembolsado": 0.00,
      "nota_fiscal": "nao_exigida", "motivos": ["DUPLICATA"], "despesa_mantida": "d-006",
      "justificativa": "…" },
    { "id": "d-009", "classificacao": "erro", "valor_considerado": -45.00,
      "categoria_considerada": "transporte_urbano", "valor_reembolsado": 0.00,
      "motivos": [], "causas_erro": [ { "campo": "valor", "falha": "valor_nao_positivo" } ],
      "justificativa": "…" },
    { "posicao": 15, "classificacao": "erro", "valor_reembolsado": 0.00, "motivos": [],
      "causas_erro": [ { "campo": "identificador", "falha": "ausente" } ], "justificativa": "…" }
  ],
  "consolidado": {
    "total_reembolsado": 623.43,
    "quantidades": { "integral": 4, "parcial": 4, "recusada": 5, "erro": 1, "total": 14 }
  }
}
```

Lote rejeitado:

```json
{ "status": "rejeitado",
  "causas": [ { "codigo": "INICIO_POSTERIOR_AO_FIM" },
              { "codigo": "IDENTIFICADOR_REPETIDO", "identificadores": ["d-006"] } ] }
```

- Campos que não se aplicam à classificação são **omitidos**, não escritos
  como `null`. `motivos` aparece sempre.
- A despesa sem identificador traz `posicao` no lugar de `id` (AMB-020).
- Dinheiro sai como número JSON com exatamente duas casas, escrito com
  `toPlainString()` (`JsonUnquotedLiteral`), nunca a partir de `Double`.
- A ordem das despesas na saída é a ordem da entrada (§4.2).

## 4. Como a política é representada

Os limites ficam em constantes Kotlin, num único arquivo,
`src/reembolso/dominio/Politica.kt`:

```kotlin
enum class Categoria(val codigo: String, val limite: Valor) {
  ALIMENTACAO("alimentacao", Valor.de("60.00")),
  TRANSPORTE_URBANO("transporte_urbano", Valor.de("80.00")),
  HOSPEDAGEM("hospedagem", Valor.de("250.00"));
  companion object { fun porCodigo(codigo: String): Categoria? = entries.find { it.codigo == codigo } }
}
object Politica { val LIMIAR_NOTA_FISCAL = Valor.de("100.00") }
fun limiteAplicavel(categoria: Categoria): Valor = categoria.limite   // único ponto de consulta (RN-001, RN-002)
```

- **Por que constante e não arquivo de configuração.** Neste projeto, mudar a
  política é mudar a spec, e toda mudança passa por spec → DECISIONS → teste
  → commit. Um arquivo de configuração deixaria o limite mudar sem que a spec
  nem os testes mudassem, que é a quebra de rastreabilidade que o processo
  existe para impedir. Também traria modos de falha que a spec não define:
  arquivo ausente, malformado, limite negativo.
- **Tabela/banco:** descartado, fora de proporção.
- **Custo aceito:** mudar um valor exige recompilar. É uma linha mais o ajuste
  dos testes da RN afetada.
- **O que barateia mudança:** a lista elegível e o limite ficam na mesma enum,
  então categoria nova é uma linha. O limite é consultado por uma só função,
  de modo que uma ampliação condicional (por exemplo, por viagem) altera essa
  função e mais nada. Nenhuma abstração é criada para política futura.

## 5. Decisões técnicas

### DT-001 — JDK 21 instalado, compilando para Java 17

**Contexto:** a stack fixa JVM 17. A máquina tem apenas o JDK 21.0.5 do
Android Studio, fora do `PATH` e sem `JAVA_HOME`, e não tem JDK 17.
**Decisão:** o Gradle roda com o JDK 21 apontado por `JAVA_HOME`. O código é
compilado com `jvmTarget = JVM_17` e `-Xjdk-release=17` no Kotlin, e
`options.release = 17` no `JavaCompile`. O último é necessário para a
verificação de consistência de alvo JVM entre as tarefas Kotlin e Java.
**Alternativas descartadas:** instalar o Temurin 17 (uma instalação a mais
num momento em que o tempo é restrito); `jvmToolchain(17)` com o resolvedor
foojay (depende de rede no primeiro build e baixa um JDK inteiro).
**Consequência:** sem instalação nova nem dependência de rede para o JDK.
`-Xjdk-release` limita o bytecode **e a API** ao Java 17, então usar uma API
do 21 é erro de compilação, não surpresa em produção. O artefato roda em
qualquer JDK 17 ou superior, e o README declara esse requisito. O custo é que
o build não fixa a versão do JDK que o executa: um JDK 16 em `JAVA_HOME`
falharia com mensagem do Gradle, não do projeto.

### DT-002 — Layout `src/` e `tests/` na raiz

**Contexto:** o enunciado exige `src/` e `tests/` na raiz, e o Gradle espera
`src/main/kotlin` e `src/test/kotlin`.
**Decisão:** reconfigurar os source sets para usar `src/` e `tests/` como
raízes Kotlin, com pacotes direto abaixo delas (`src/reembolso/dominio/…`,
`tests/reembolso/…`):

```kotlin
sourceSets {
  main { kotlin.setSrcDirs(listOf("src")); resources.setSrcDirs(emptyList<String>()) }
  test { kotlin.setSrcDirs(listOf("tests")); resources.setSrcDirs(emptyList<String>()) }
}
```

Não há pasta de recursos. O teste ponta a ponta lê
`exemplos/despesas-exemplo.json` pelo caminho (o diretório de trabalho do
teste é o do projeto), e as entradas pequenas são strings dentro do teste.
Os testes de rastreabilidade leem `specs/001-motor-reembolso/spec.md` da
mesma forma.
**Alternativas descartadas:** manter `src/main/kotlin` e mover só os testes,
o que deixa o layout assimétrico e um nível `main/` sem razão; manter o padrão
Gradle e alegar que `src/test` "é" `tests/`, o que viola o enunciado.
**Consequência:** a estrutura é exatamente a do enunciado. O preço é um bloco
de configuração não convencional, que precisa ser lido uma vez.

### DT-003 — Dinheiro como `BigDecimal` de escala 2, encapsulado

**Contexto:** a RN-011 normaliza todo valor para centavos com a metade para
cima, afastando-se do zero, antes de qualquer regra, e esse é o valor da
saída. É preciso ler `33.333` sem perda.
**Decisão:**
1. O adaptador constrói `BigDecimal(jsonPrimitive.content)` a partir do texto
   cru, sem passar por `Double`.
2. `Valor.normalizar` aplica `setScale(2, RoundingMode.HALF_UP)`. `HALF_UP`
   do Java afasta do zero, que é exatamente a RN-011: `−45.005` vira
   `−45.01`.
3. `Valor` não tem outro construtor. Com a escala fixa, `54.90` e `54.900`
   são iguais também no `equals`, o que importa para a chave de duplicidade
   (RN-009).
4. A saída escreve com `toPlainString()`.

**Alternativas descartadas:** `Double`, porque `100.005` em `Double` é
`100.00499…`, que arredonda para 100,00 e inverte o aceite da RN-003; `Long`
em centavos, porque valores absurdos estouram o `Long` e criariam um caso de
entrada que a spec não define; `HALF_CEILING`, que viola a RN-011 em
negativos.
**Consequência:** a normalização acontece em um lugar só, e é impossível
operar sobre valor não normalizado. A verbosidade do `BigDecimal` fica
contida em `Valor`.

### DT-004 — Entrada como árvore JSON e `Lido<T>`

**Contexto:** a RN-010 distingue ausente, tipo incompatível e data
inexistente, e exige todas as causas (AMB-021), não apenas a primeira.
**Decisão:** `parseToJsonElement` seguido de tradução manual para
`LoteLido` (§3.1). As regras de período e relação de tipo incompatível
(erro de lote) e de elemento que não é despesa (erro de item) são decididas
no domínio a partir dos estados de `Lido`.
**Alternativa descartada:** desserializar direto para data classes, que
aborta na primeira falha e mistura tipo JSON com regra.
**Consequência:** cerca de 80 linhas de tradução escritas à mão, em troca de
uma RN-010 testável sem JSON.

### DT-005 — Espaços nas extremidades conforme o padrão Unicode, implementados explicitamente

**Contexto:** a §4.1 define espaço como todo caractere de espaço em branco
reconhecido pelo padrão Unicode (propriedade `White_Space`). O `trim()` do
Kotlin usa `Char.isWhitespace()`, que **não** coincide com ela: inclui
U+001C–U+001F, que não são `White_Space`, e omite U+0085 (NEL), que é.
**Decisão:** uma função `aparar()` no domínio, com o conjunto explícito dos
25 code points da propriedade `White_Space`. É usada em categoria,
fornecedor, identificador e na regra de texto vazio.
**Alternativa descartada:** `trim()` da biblioteca padrão, que diverge da
spec em cinco caracteres. `String.strip()` do Java, que usa só
`Character.isWhitespace`, diverge ainda mais: também deixa de aparar os
espaços não separáveis (U+00A0, U+2007, U+202F), que a §4.1 inclui
expressamente.
**Consequência:** um teste lista os 25 code points e verifica os casos em
que a biblioteca padrão diverge: U+001C a U+001F **não** são aparados, e
U+0085 **é**. A conversão para minúsculas usa
`lowercase()` do Kotlin, que é independente de localidade (`Locale.ROOT`) e
não sofre o problema do "i" turco. É o mapeamento completo de caixa do
padrão Unicode, que a RN-008 fixa (spec 1.3, D-011); esta DT implementa a
regra, não a decide.

### DT-006 — A ordem da §8 vive em dois lugares

**Decisão:** a ordem das etapas fica numa única função sequencial,
`Motor.avaliar`. A ordem da lista de motivos é a ordem de declaração de
`Motivo` (ordenação por `ordinal`). As duas são verificadas contra a spec
por teste (§6).
**Alternativa descartada:** uma lista de objetos-regra iterada por um
pequeno motor. Seria mais indireção, e a duplicidade não cabe no formato,
porque compara despesas entre si.
**Consequência:** ler `Motor.avaliar` é ler a §8. Uma regra nova exige editar
essa função, o que é desejável, porque a §8 também muda nesse caso.

### DT-007 — Duplicidade em duas passadas, com comparação por code point

**Decisão:** a passada 1 calcula os motivos dos passos 1.1 a 1.3 por despesa.
A passada 2 faz `groupBy(data, categoria, fornecedor, valor)` apenas sobre as
despesas sem motivo. Em cada grupo de tamanho maior que 1, mantém a de menor
identificador e marca as demais com `DUPLICATA` e `mantida`. "Menor" é
comparado por **code point Unicode**, com um comparador próprio, porque o
`String.compareTo` do Kotlin compara unidades UTF-16 e diverge fora do BMP.
A RN-009 fixa a comparação pelo ponto de código Unicode (spec 1.2, D-010);
esta DT implementa a regra, não a decide.
**Alternativa descartada:** comparar cada despesa com as anteriores na ordem
da entrada, o que torna o resultado dependente da ordem e viola a RN-001 e a
RN-009.
**Consequência:** o resultado é independente de ordem por construção, e um
teste inverte a entrada para provar isso.

### DT-008 — Exit codes da CLI

**Decisão:**
- `0`: lote avaliado.
- `1`: lote rejeitado. O JSON de saída é escrito do mesmo jeito.
- `2`: uso incorreto da CLI (subcomando ou flag ausente). Nada é escrito.

Arquivo de entrada inexistente, JSON sintaticamente inválido ou JSON válido
cuja raiz não é um objeto de lote resultam em lote rejeitado com
`ENTRADA_ILEGIVEL` como causa única (RN-010: "a entrada não pode ser lida"). A falha ao
escrever o arquivo de saída é a única situação de exceção não tratada:
mensagem no stderr e exit code `3`.
**Alternativa descartada:** exit `0` sempre, que impediria um script de
distinguir lote rejeitado de avaliado sem ler o JSON.
**Consequência:** a CLI é usável em pipeline. Os códigos são interface, não
regra, e por isso vivem aqui.

### DT-009 — Fronteira garantida por convenção de pacote e teste

**Decisão:** módulo Gradle único. Um teste de arquitetura falha se algum
arquivo em `src/reembolso/dominio/` importar `kotlinx.` ou `java.io.`.
**Alternativa descartada:** multi-módulo Gradle (`core` e `app`). A garantia
em tempo de compilação não paga a configuração extra num projeto deste
tamanho.
**Consequência:** a fronteira é verificada a cada build, com custo de cerca de
15 linhas de teste.

## 6. Estratégia de testes

- **Nível:**
  - ~75% unitários no domínio: constroem `LoteLido` direto, sem JSON.
  - ~15% no adaptador: precisão de `33.333` e `100.005`; `"72.50"` como string
    vira tipo incompatível; `2026-02-30`; `null`; JSON inválido; JSON válido
    cuja raiz não é lote (causa única `ENTRADA_ILEGIVEL`); período e
    relação de tipo incompatível; elemento que não é objeto; descrição
    numérica ignorada.
  - ~10% ponta a ponta: chamam `main` com `exemplos/despesas-exemplo.json` e
    comparam com a tabela da §9 da spec; cobrem também o lote rejeitado e os
    exit codes.
- **Cada RN tem teste:** um arquivo por RN em `tests/reembolso/regras/`, por
  exemplo `RN002ReembolsoParcialTest.kt`, com um teste por item da lista
  "Aceite" daquela RN.
- **Casos de borda da §7:** `Secao7CasosDeBordaTest`, um teste por linha da
  tabela, nomeado com as RNs da coluna "Regra".
- **Critérios da §9:** `Secao9CriteriosDeAceiteTest`. O lote de exemplo é
  verificado despesa a despesa, mais o consolidado.
- **Nomenclatura:** o nome entre crases começa pelo identificador do requisito
  e reproduz o aceite. Vírgula decimal e `→` porque `.`, `:`, `<` e `>` não são
  permitidos em nome JVM.
  ```kotlin
  @Test fun `RN-002 alimentacao de 60,01 é parcial com reembolso 60,00 e excedente 0,01`()
  @Test fun `§7 RN-003 RN-004 d-004 transporte 100,01 sem NF é recusada SEM_NOTA_FISCAL`()
  @Test fun `§9 lote de exemplo produz total 623,43 com 4 integrais, 4 parciais, 5 recusadas e 1 erro`()
  ```
- **Testes de rastreabilidade** (`tests/reembolso/rastreabilidade/`). Leem
  `spec.md` e falham o build quando código e spec divergem:
  - `RastreabilidadeTest`: extrai todo `### RN-\d{3}` da spec e exige pelo
    menos um teste cujo nome contenha o ID. Faz o mesmo com todo
    `### AMB-\d{3}`: cada ambiguidade é requisito tanto quanto uma regra, e
    exige pelo menos um teste cujo nome contenha o seu ID. Ficam isentas
    apenas as ambiguidades dissolvidas, que não têm decisão operacional
    própria; o teste as declara numa lista explícita (`AMB-003`, `AMB-012`),
    de modo que uma AMB nova nunca entre na isenção por descuido, e falha se
    um ID da lista não existir na spec ou se o título correspondente não
    contiver "(dissolvida)". Conta as linhas da tabela da §7 e exige o mesmo
    número de testes com prefixo `§7`. Uma RN ou AMB nova sem teste, ou um
    caso de borda novo sem teste, quebra o build.
  - Consequência para a nomenclatura: o teste que verifica a consequência
    de uma AMB leva o seu ID no nome, depois do ID da RN quando houver
    (`RN-001 AMB-001 três alimentações de 59,00 …`).
  - `OrdemMotivosSecao8Test`: extrai, na ordem, os códigos de motivo da lista
    de elegibilidade da §8 (`→ FORA_DA_COMPETENCIA`, …) e compara com
    `Motivo.entries.map { it.name }`. Uma reordenação acidental do enum, ou da
    §8, quebra o build em vez de alterar a ordem da saída em silêncio.
  - `OrdemCamposSecao41Test`: extrai a ordem das informações da tabela de
    despesa da §4.1 e compara, por uma tabela de correspondência no próprio
    teste, com a ordem de `CampoDespesa`, que define a ordem das causas de
    erro (AMB-021).
  - `FronteiraDominioTest`: DT-009.
- **Testes de guarda da aritmética:** `100.005 → 100,01`, `−45.005 → −45,01` e
  `54.90 ≡ 54.900` detectam vazamento de `Double`, modo de arredondamento
  errado e uso de `equals` em `BigDecimal` fora de `Valor`.
- **Determinismo:** um teste avalia o lote de exemplo com as despesas em
  ordem inversa e compara os resultados por identificador (RN-001, RN-009).

## 7. Riscos

| # | Risco | Probabilidade | O que faço se acontecer |
|---|---|---|---|
| R-01 | **Ambiente sem `JAVA_HOME` e sem Gradle Wrapper.** Bloqueia toda a implementação. **Primeiro item da execução, antes de qualquer task de código** | certa (estado atual) | Apontar `JAVA_HOME` para o JDK 21 do Android Studio; gerar o wrapper 8.11.1 com a distribuição do cache (`~/.gradle/wrapper/dists/gradle-8.11.1-bin/…/bin/gradle wrapper`); confirmar com `./gradlew --version` e um build vazio. Vira a primeira task do tasks.md |
| R-02 | Dependências (`kotlinx-serialization-json`, JUnit) não estão no cache e exigem rede no primeiro build | média | Um único download no primeiro `./gradlew build`. Sem rede, o bloqueio é o mesmo de R-01 e é tratado junto |
| R-03 | O envelope mudar a política: viagem, novo limite, nova categoria | alta | Limites num arquivo só, uma função de limite, uma regra por RN e um teste por RN. O impacto fica em spec, `Politica.kt`, o adaptador (se houver campo novo) e os testes da RN afetada |
| R-04 | `Double` vazar (por exemplo `jsonPrimitive.double`) | média | `Valor` só nasce de `BigDecimal`, e os testes de guarda da §6 pegam o vazamento |
| R-05 | `trim()` da biblioteca padrão usado por hábito no lugar de `aparar()` | média | O teste de `aparar()` cobre U+001C e U+0085. Uma busca por `.trim()` em `src/` entra no checklist de revisão de cada task |
| R-06 | Número com expoente gigante (`1e999999999`) estourar memória em `setScale` | baixa | Registro o risco e não trato: tratar seria regra nova. Se acontecer, vira AMB nova na spec |
| R-07 | Comparação de identificadores por `String.compareTo` (UTF-16) no lugar de code point | baixa | Comparador próprio (DT-007), com teste usando um caractere fora do BMP |
