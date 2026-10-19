
> /clear

> Leia specs/001-motor-reembolso/spec.md e CLAUDE.md. A spec está completa:
  19 ambiguidades decididas, RN-001 a RN-012, todas as seções preenchidas.

  Vamos escrever o plan.md. Ele responde o COMO e pode falar de linguagem,
  biblioteca e arquitetura — o que não pode é introduzir regra de negócio nova.
  Se aparecer uma, me avise: é bug de spec.

  Stack: Kotlin (JVM 17), Gradle Kotlin DSL. O resto das escolhas é para
  decidirmos.

  Me apresente uma proposta antes de escrever no arquivo, seguindo as seções do
  template em template/specs/001-motor-reembolso/plan.md: stack, arquitetura,
  modelo de dados, como a política é representada, decisões técnicas com
  alternativa descartada, estratégia de testes e riscos.

  Quatro pontos que o plan precisa resolver:

  1. Onde os limites vivem. A spec os fixa na RN-001 (60,00, 80,00, 250,00),
     mas a representação é decisão sua: constantes, arquivo de configuração,
     tabela. O template diz que esta é a decisão que mais barato ou mais caro
  vai
     custar se a política mudar.
  2. Aritmética monetária. Precisa suportar a RN-011: normalização para
     centavos com metade para cima, antes de qualquer regra, e o valor
  normalizado
     aparece na saída. Também precisa ler um valor como 33.333 sem perda de
     precisão.
  3. Estrutura de pastas. O enunciado exige src/ e tests/ na raiz, enquanto o
     Gradle espera src/main/kotlin e src/test/kotlin. Proponha como resolver e
     registre como decisão técnica.
  4. Fronteira entre núcleo puro e entrada/saída. O template diz que onde essa
     linha está desenhada determina o quanto o sistema resiste a mudança de
     requisito.

  Na estratégia de testes, diga como garante que cada RN tenha teste e como a
  nomenclatura dos testes remete aos requisitos — isso é o que fecha a
  rastreabilidade.

  Evite over-engineering: nada de motor de regras genérico, sistema de plugins
  ou camada de abstração para política futura. Simples e bem fatiado.

● Bash(cd "c:/Users/dev/SDD-Desafio" && cat CLAUDE.md && echo ===== && cat
      specs/001-motor-reembolso/spec.md && echo ===== && cat
      template/specs/001-motor-reembolso/pl…)
  ⎿  Error: Exit code 1
     cat: CLAUDE.md: No such file or directory

● Bash(cd "c:/Users/dev/SDD-Desafio" && find . -type f -not -path '*/.git/*' |
      head -80)
  ⎿  (No output)

● Bash(cd "c:/Users/dev/SDD-Desafio/sdd-desafio" && cat CLAUDE.md && echo =====
      && cat specs/001-motor-reembolso/spec.md && echo =====TEMPLATE && cat
      template/specs/00…)
  ⎿  (No output)

  Read 4 files (ctrl+o to expand)

● Bash(cd "c:/Users/dev/SDD-Desafio/sdd-desafio" && cat
      exemplos/despesas-exemplo.json | head -60 && grep -n -i -E
      "src|tests|gradle|estrutura|pasta" FAQ.md RUBRICA.md…)
  ⎿  (No output)

● Encontrei tudo em sdd-desafio/. A seguir, a proposta, ainda sem escrever no
  arquivo. Antes de ela entrar no plan, há 4 possíveis bugs de spec para você
  decidir (seção 8, no fim).

  ---

  Proposta de plan.md

  1. Stack

  Escolha: Linguagem
  O quê: Kotlin 2.1, JVM 17 via jvmToolchain(17); Gradle Kotlin DSL + plugin
  application
  Por quê: Stack definida; o plugin application gera o executável da CLI
  (installDist)
  O que descartei e por quê: —
  ────────────────────────────────────────
  Escolha: Testes
  O quê: JUnit 5 + kotlin-test
  Por quê: Nomes de teste entre crases permitem frases com o ID da RN; não
  precisa de biblioteca de asserções
  O que descartei e por quê: Kotest: DSL mais bonita, mas é mais uma dependência

  e um estilo a aprender, e não ganha nada em rastreabilidade
  ────────────────────────────────────────
  Escolha: Parsing/validação
  O quê: kotlinx-serialization-json usado só como árvore (parseToJsonElement),
  sem DTO @Serializable na entrada
  Por quê: Um JsonPrimitive guarda o texto cru do número ("33.333") e diz se ele

  veio como string ou como número. É isso que torna possível o "tipo
  incompatível" campo a campo e a coleta de todas as causas
  O que descartei e por quê: Desserializar direto para DTO: falha na primeira
  inconsistência e converte para tipo antes de a RN-010 opinar. Jackson:
  precisa de configuração (USE_BIG_DECIMAL_FOR_FLOATS) para não perder
  precisão, e o default perigoso é justamente o que alguém vai esquecer
  ────────────────────────────────────────
  Escolha: Aritmética monetária
  O quê: BigDecimal dentro de uma value class Valor, sempre com escala 2
  Por quê: Ver DT-002
  O que descartei e por quê: Double; Long em centavos (ver DT-002)
  ────────────────────────────────────────
  Escolha: CLI
  O quê: Parsing manual de calcular --input X --output Y
  Por quê: Um subcomando e duas flags
  O que descartei e por quê: Clikt: dependência para ~15 linhas
  ────────────────────────────────────────
  Escolha: Lint
  O quê: Nenhum por enquanto; ./gradlew build é o check
  Por quê: Evitar cerimônia
  O que descartei e por quê: ktlint: entra depois se fizer falta

  2. Arquitetura

  arquivo JSON
    → [cli] lê bytes, chama o adaptador, escreve o arquivo, define o exit code
    → [entrada] JSON → LoteLido   (só sabe JSON: nome de campo, tipo JSON,
  formato ISO de data)
    → [dominio] Motor.avaliar(LoteLido): ResultadoLote      ← núcleo puro
          0. normalização (RN-011, RN-008) + validação (RN-010, RN-012)
          1. elegibilidade (RN-005/006, RN-008, RN-003/004, RN-009) nesta ordem
          2. valor (RN-001, RN-002)
          3. consolidação
    → [saida] ResultadoLote → JSON (+ frase de justificativa)
  arquivo JSON

  Fronteira (ponto 4). A linha passa entre "o que o JSON diz" e "o que isso
  significa":

  - entrada só traduz tipos JSON em Lido<T> = Presente(v) | Ausente |
    TipoIncompativel | DataInexistente. Não sabe o que é obrigatório e não faz
    trim nem normalização. null em JSON vira Ausente. Uma data que não está no
    formato uuuu-MM-dd vira TipoIncompativel; uma que está no formato mas não
    existe (2026-02-30) vira DataInexistente (ResolverStyle.STRICT).
  - dominio decide tudo que é regra: obrigatoriedade, texto vazio após trim
    tratado como ausente, valor ≤ 0, id repetido, início > fim, motivos,
    limites. Não importa kotlinx.* nem java.io. Usa só java.time.LocalDate e
    BigDecimal.
  - Consequência: toda a RN-010 se testa sem JSON. Se o formato de entrada mudar
    (renomear campo, acrescentar um campo de viagem), mexe-se no adaptador e
    num campo de LoteLido, e as regras não se movem.
  - Para garantir a fronteira: módulo Gradle único com convenção de pacote, mais
    um teste que falha se algum arquivo em src/reembolso/dominio/ importar
    kotlinx ou java.io. Descartei o multi-módulo Gradle: a garantia em tempo de
    compilação não paga a configuração extra num projeto deste tamanho.

  3. Modelo de dados (núcleo)

  @JvmInline value class Valor private constructor(val decimal: BigDecimal)  //
  escala 2 sempre
    // Valor.normalizar(bruto: BigDecimal) = setScale(2, HALF_UP)  ← único
  construtor

  enum class Categoria(val codigo: String, val limite: Valor)               //
  ver §4
  enum class Motivo { FORA_DA_COMPETENCIA, CATEGORIA_INELEGIVEL,
  SEM_NOTA_FISCAL, DUPLICATA }
    // a ordem de declaração É a ordem da §8; a lista sai ordenada por ordinal

  data class Despesa(                      // despesa já validada e normalizada
    val id: String, val data: LocalDate,
    val categoria: String,                 // normalizada; String porque
  "coworking" é entrada válida
    val fornecedor: String, val valor: Valor, val temNotaFiscal: Boolean)

  sealed interface ResultadoDespesa { val id: String }
    Aceita(id, valor, categoria, limite, reembolsado, excedente?, notaFiscal)
     // integral | parcial
    Recusada(id, valor, categoria, notaFiscal, motivos, mantida?)
     // reembolsado = 0,00
    ComErro(id, valorConsiderado?, categoriaConsiderada?, causas:
  List<CausaErro>)  // motivos = []

  data class CausaErro(val campo: CampoDespesa, val falha: Falha)   // Falha:
  AUSENTE, TIPO_INCOMPATIVEL, DATA_INEXISTENTE, VALOR_NAO_POSITIVO
  sealed interface ResultadoLote
    Avaliado(periodo, despesas: List<ResultadoDespesa>, consolidado)
    Rejeitado(causas: List<CausaLote>)

  O sealed reproduz a coluna "Quando aparece" da §4.2: um Recusada não tem campo
  limite para alguém preencher por engano. A classificação é derivada do tipo,
  sem flag paralela que possa divergir dele. A justificativa é o conjunto de
  campos, como a spec define. A frase legível é montada em saida a partir deles
  e não carrega decisão nenhuma.

  Esquema de saída (representação, decidida no plan):

  { "status": "avaliado",
    "periodo": { "inicio": "2026-07-01", "fim": "2026-07-31" },
    "despesas": [
      { "id": "d-001", "classificacao": "parcial", "valor_considerado": 72.50,
        "categoria_considerada": "alimentacao", "valor_reembolsado": 60.00,
        "limite_aplicado": 60.00, "excedente": 12.50, "nota_fiscal":
  "nao_exigida",
        "motivos": [], "justificativa": "..." } ],
    "consolidado": { "total_reembolsado": 623.43,
      "quantidades": { "integral": 4, "parcial": 4, "recusada": 5, "erro": 1,
  "total": 14 } } }

  - Lote rejeitado: { "status": "rejeitado", "causas": [...] }.
  - Campos que não se aplicam são omitidos, não escritos como null. motivos
    sempre aparece.
  - Dinheiro sai como número JSON com exatamente 2 casas, escrito a partir de
    toPlainString().

  4. Como a política é representada (ponto 1)

  Ficam em constantes Kotlin, num único arquivo dominio/Politica.kt:

  enum class Categoria(val codigo: String, val limite: Valor) {
    ALIMENTACAO("alimentacao", Valor.de("60.00")),
    TRANSPORTE_URBANO("transporte_urbano", Valor.de("80.00")),
    HOSPEDAGEM("hospedagem", Valor.de("250.00"));
    companion object { fun porCodigo(c: String): Categoria? }
  }
  object Politica { val LIMIAR_NOTA_FISCAL = Valor.de("100.00") }

  - Por que constante e não arquivo de configuração: aqui, mudar a política
    significa mudar a spec. Toda mudança passa por spec → DECISIONS → teste →
    commit. Um YAML deixaria o limite mudar sem o teste nem a spec mudarem, que
    é exatamente a quebra de rastreabilidade que o desafio pune. Ele também
    traria modos de falha novos (arquivo ausente, malformado, limite negativo)
    que a spec não define.
  - Tabela/banco: fora de proporção.
  - Custo aceito: mudar um valor exige recompilar. É uma linha mais o ajuste dos
    testes.
  - Barateamento: a enum tem lista elegível e limite no mesmo lugar, então uma
    categoria nova é uma linha. O limite é consultado por uma única função
    (limiteAplicavel(despesa)). Se o envelope trouxer a ampliação por viagem, só
    essa função muda. Não crio abstração para isso antes da hora.

  5. Decisões técnicas

  DT-001 — Layout src/ e tests/ na raiz (ponto 3)
  - Contexto: o enunciado exige src/ e tests/, e o Gradle espera src/main/kotlin
    e src/test/kotlin.
  - Decisão: reconfigurar os source sets para usar src/ e tests/ como raízes
    Kotlin. Fica src/reembolso/dominio/… e tests/reembolso/…:
  sourceSets {
    main { kotlin.setSrcDirs(listOf("src"));
  resources.setSrcDirs(emptyList<String>()) }
    test { kotlin.setSrcDirs(listOf("tests"));
  resources.setSrcDirs(emptyList<String>()) }
  }
    Fixtures de JSON não precisam de pasta de recursos. O teste ponta a ponta lê
    exemplos/despesas-exemplo.json pelo caminho (o diretório de trabalho do
    teste é o do projeto), e as entradas pequenas são strings dentro do próprio
    teste.
  - Alternativa descartada: manter src/main/kotlin e mover só os testes. O
    resultado é assimétrico e src/ fica com um nível main/ que confunde o
    leitor. Também descartei manter o padrão Gradle e alegar que src/test "é"
    tests/, porque viola o enunciado.
  - Consequência: a estrutura fica exatamente como o enunciado pede. O preço é
    um bloco de configuração não convencional, que alguém acostumado com Gradle
    precisa ler uma vez.

  DT-002 — Dinheiro como BigDecimal de escala 2, encapsulado (ponto 2)
  - Contexto: pela RN-011, o valor é normalizado antes de qualquer regra e esse
    valor normalizado aparece na saída. É preciso ler 33.333 sem perda.
  - Decisão:
    a. O adaptador faz BigDecimal(jsonPrimitive.content) direto do texto cru,
       sem passar por Double.
    b. Valor.normalizar aplica setScale(2, RoundingMode.HALF_UP).
    c. Valor não tem construtor público fora disso. Comparações usam compareTo,
       e a escala fixa torna 54.90 == 54.900 verdadeiro também no equals, o que
       importa para a chave de duplicidade.
  - Alternativas descartadas:
    - Double: 100.005 em Double é 100.00499…, que arredonda para 100,00 e
      inverte o aceite da RN-003.
    - Long em centavos: valores absurdos estouram o Long, e isso criaria um caso
      de entrada que a spec não define.
  - Consequência: a normalização fica em um lugar só e é impossível operar sobre
    valor não normalizado. O custo é a verbosidade do BigDecimal, contida
    dentro de Valor.

  DT-003 — A ordem da §8 vive em dois lugares só
  - Decisão: a ordem das etapas fica numa função sequencial em Motor. A ordem da
    lista de motivos é a ordem de declaração de Motivo.
  - Alternativa descartada: uma lista de objetos-regra iteráveis, ou seja, um
    mini motor de regras. É mais indireção, e a duplicidade não cabe nesse
    formato porque compara despesas entre si.

  DT-004 — Duplicidade em duas passadas
  - Decisão: a passada 1 calcula os motivos 1.1 a 1.3 por despesa. A passada 2
    faz groupBy(data, categoria, fornecedor, valor) só sobre quem ficou sem
    motivo, mantém o menor id e marca o resto com DUPLICATA e mantida. O "menor"
    é comparado por code point Unicode, não pelo String.compareTo do Kotlin,
    que compara unidades UTF-16. "Códigos dos caracteres" na RN-009 é code
    point, e as duas ordens só divergem com caracteres fora do BMP.
  - Alternativa descartada: comparar com cada despesa anterior conforme a ordem
    da entrada. Isso depende da ordem e viola a RN-001/RN-009.

  DT-005 — Entrada como árvore + Lido<T> (seção 2). Alternativa descartada: DTO
  desserializado.

  DT-006 — Exit codes
  - 0: lote avaliado.
  - 1: lote rejeitado. O JSON de saída é escrito do mesmo jeito.
  - 2: uso incorreto da CLI. Nada é escrito.

  Arquivo de entrada inexistente ou ilegível vira lote rejeitado com causa
  "entrada ilegível", conforme a RN-010.

  6. Estratégia de testes

  - Nível:
    - ~75% unitários no núcleo: constroem LoteLido direto, sem JSON.
    - ~15% no adaptador: precisão de 33.333 e 100.005, "72.50" como string vira
      tipo incompatível, 2026-02-30, null, JSON inválido.
    - ~10% ponta a ponta: chamam o main com exemplos/despesas-exemplo.json e
      comparam com a tabela da §9; cobrem também o lote rejeitado e os exit
      codes.
  - Cada RN tem teste: um arquivo por RN,
    tests/reembolso/regras/RN002ReembolsoParcialTest.kt, com um teste por item
    da lista "Aceite" daquela RN. Além disso, um RastreabilidadeTest lê spec.md,
    extrai todo ### RN-\d{3} e falha se algum ID não aparecer no nome de pelo
    menos um teste em tests/. O mesmo teste conta as linhas da tabela da §7 e
    confere que existem tantos testes com prefixo §7 quantas linhas houver. Uma
    RN nova na spec sem teste quebra o build.
  - Casos de borda da §7: Secao7CasosDeBordaTest, um teste por linha da tabela,
    nomeado com as RNs da coluna "Regra".
  - Nomenclatura: o nome entre crases começa pelo ID da RN e reproduz o aceite:
  @Test fun `RN-002 alimentacao de 60,01 é parcial com reembolso 60,00 e
  excedente 0,01`()
  @Test fun `§7 RN-003 RN-004 d-004 transporte 100,01 sem NF é recusada
  SEM_NOTA_FISCAL`()
  @Test fun `§9 lote de exemplo produz total 623,43 e 4-4-5-1`()
    (., :, < e > não são permitidos em nome JVM, por isso vírgula decimal e →
    não aparecem.)
  - Testes de guarda: 100.005 → 100,01 e 54.90 ≡ 54.900 detectam vazamento de
    Double e uso de equals em BigDecimal fora de Valor.

  7. Riscos

  Risco: JDK e Gradle ausentes nesta máquina (java não está no PATH)
  Prob.: alta
  O que faço: Instalar um JDK 17 (Temurin) uma vez e gerar e commitar o Gradle
  Wrapper. O plugin foojay-resolver deixa o toolchain baixar o JDK sozinho nos
  builds seguintes
  ────────────────────────────────────────
  Risco: Double vazar (por exemplo jsonPrimitive.double)
  Prob.: média
  O que faço: Valor só nasce de BigDecimal, e os testes de guarda acima pegam o
  vazamento
  ────────────────────────────────────────
  Risco: O envelope mudar a política (viagem, limite novo, categoria nova)
  Prob.: alta
  O que faço: Limites num arquivo só, uma função de limite e regras isoladas por

  RN: o impacto fica em spec, Politica.kt, o adaptador (se houver campo novo) e
   os testes daquela RN
  ────────────────────────────────────────
  Risco: Número com expoente gigante (1e999999999) estourar memória no setScale
  Prob.: baixa
  O que faço: Registro o risco e não trato: tratar seria regra nova. Se
  acontecer, vira AMB nova
  ────────────────────────────────────────
  Risco: Interpretação de "espaços nas extremidades"
  Prob.: baixa
  O que faço: Uso trim() do Kotlin, que remove todo whitespace Unicode (tab,
  NBSP). Ver gap 5

  8. Possíveis bugs de spec, para você decidir

  Encontrei estes ao desenhar o modelo. Nenhum entra no plan sem a sua decisão.

  1. Despesa sem identificador, ou com identificador que não é texto. A §4.2 diz
     que o identificador aparece "sempre", mas a RN-010 torna a despesa um erro
     de item, e aí não há id para mostrar. A RN-012 também não diz se duas
     despesas sem id são "repetidas". Opções:
     - (a) id ausente ou inválido é erro de lote;
     - (b) erro de item, e a saída mostra o id ausente e se apoia na posição da
       despesa na lista.

     Ligado a isso: o id na saída é o informado ou o informado após trim?
  2. Erro de item com várias causas. A §4.2 fala em "a causa do erro", no
     singular. Uma despesa sem fornecedor e com valor −5 recebe uma causa ou
     todas? O princípio da AMB-018 sugere todas, mas a spec não diz.
  3. "Metade para cima" em valor negativo. −45.005 vira −45.01 com HALF_UP
     (afasta do zero) ou −45.00 com HALF_CEILING? A classificação não muda (é
     erro de qualquer forma), mas o valor considerado que aparece na saída muda.
  4. despesas ou periodo presentes com o tipo JSON errado, e um elemento de
     despesas que não é objeto. A spec cobre "não informadas" e "data inválida",
     mas não "informadas com o tipo errado". Minha leitura seria:
     despesas/periodo com tipo errado é erro de lote; elemento que não é objeto
     é erro de item. Só que isso decide entre lote e item, então é regra.
  5. (menor) "Espaços" na RN-008/RN-010/RN-012 inclui tab e NBSP?

  Quer que eu leve a proposta para o plan.md como está, depois que você decidir
  os gaps (eles vão para a spec e para o DECISIONS.md primeiro), ou prefere
  ajustar algum ponto antes?

> A proposta está aprovada no conteúdo. Mas os cinco gaps vão para a spec
  primeiro — são regra de negócio, e a Regra 2 do CLAUDE.md se aplica.

  Decisões:

  GAP 1 — Identificador ausente ou inválido: erro de item, não erro de lote.
  Uma despesa sem identificador não deve derrubar o lote inteiro, do mesmo
  modo que uma despesa sem valor não derruba. Na saída, uma despesa com erro
  cujo identificador esteja ausente é identificada pela sua posição na relação
  de despesas da entrada, contada a partir de um. O identificador exibido é o
  normalizado — após a remoção de espaços nas extremidades (RN-012) —, de modo
  que a saída mostre o mesmo valor que a comparação usa. Duas despesas sem
  identificador não constituem repetição: a ausência não é um valor, e a
  RN-012 compara identificadores informados.

  GAP 2 — Todas as causas, não apenas uma. Mesma razão da AMB-018: as causas
  são independentes, e reportar uma só esconderia as demais, obrigando a
  descobrir um problema por submissão. A §4.2 passa a dizer "as causas do
  erro", e a ordem é a dos campos tal como declarados na tabela de entrada
  da §4.1, para que o resultado seja determinístico.

  GAP 3 — HALF_UP, afastando do zero. A RN-011 estabelece "metade para cima",
  e aplicar um critério diferente a valores negativos criaria duas regras onde
  a spec declara uma. Como valor menor ou igual a zero é sempre erro de item,
  a escolha afeta apenas o valor exibido na saída, não a classificação.

  GAP 4 — Sua leitura está certa, e o critério é a abrangência do dano: se a
  relação de despesas ou o período vêm com tipo incompatível, a estrutura do
  lote está quebrada e nenhuma despesa pode ser localizada — erro de lote. Se
  um elemento isolado da relação não é uma despesa bem formada, apenas aquela
  despesa é inavaliável — erro de item, identificado pela posição conforme o
  GAP 1.

  GAP 5 — Espaços nas extremidades são todos os caracteres de espaço em branco
  reconhecidos como tal pelo padrão Unicode, incluindo tabulação e espaços não
  separáveis. Declarar o alcance evita que a regra seja lida como
  interpretação, e mantém a coerência com o alcance estreito já fixado na
  AMB-013.

  === O que fazer agora ===

  1. Registre as cinco decisões na spec, nos lugares que couberem: a RN-010,
     a RN-012, a §4.1 e a §4.2. Avalie se alguma merece registro como
  ambiguidade
     própria (AMB-020 em diante) ou se são esclarecimentos de regras já
     existentes — proponha e justifique a escolha.
  2. Crie a entrada D-009 no DECISIONS.md, registrando que estes pontos foram
     descobertos ao projetar a implementação, e não ao escrever a spec.
  3. Só depois escreva o plan.md com a proposta aprovada, acrescentando:

  - um teste que verifique que a ordem de declaração do enum de motivos
    corresponde à ordem declarada na §8, para que uma reordenação acidental
    quebre o build em vez de alterar a saída silenciosamente;
  - o risco do ambiente (JDK e Gradle ausentes) como primeiro item a resolver
    na execução, não depois.

  4. Sobre o ambiente: verifique agora se há JDK instalado e qual a versão. Se
     não houver, me diga antes de prosseguir — isso bloqueia a implementação
     inteira e precisa ser resolvido primeiro.

  Me mostre o diff da spec antes de salvar, e a proposta final do plan depois.

● PowerShell("JAVA_HOME=$env:JAVA_HOME"; Get-Command java,javac,gradle
            -ErrorAction SilentlyContinue | Select-Object Name,Source; foreach
            ($d in 'C:\Program Files\Java','C:\…)
  ⎿  Running in the background (↓ to manage)

  Read 2 files (ctrl+o to expand)

● Bash(ls ~/.gradle/wrapper/dists 2>/dev/null; ls ~/.gradle/daemon 2>/dev/null;
      ls ~/.gradle/jdks 2>/dev/null; for d in "/c/Program Files/Android/Android
      Studio/jbr" "…)
  ⎿  (No output)
  ⎿  (timeout 1m 30s)

● Bash(cat "/c/Program Files/Android/Android Studio/jbr/release" | head -5;
      "/c/Program Files/Android/Android Studio/jbr/bin/java.exe" -version 2>&1;
      ls "/c/Program Fi…)
  ⎿  (No output)

● Encontrei um JDK 21.0.5 (JBR do Android Studio) fora do PATH, sem JDK 17, e
  Gradle 8.11.1 no cache. Vou preparar o rascunho da spec numa cópia para
  mostrar o diff antes de salvar.

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/e40ecf
      b1-d5e2-49b7-b285-8ff63e8b1094/scratchpad"; mkdir -p "$S" && cp
      "c:/Users/dev/SDD-Desaf…)
  ⎿  (No output)

● Agora vou aplicar as cinco decisões na cópia de rascunho.

  Made 17 scratchpad edits +203 -69, read 1 file (ctrl+o to expand)

● Background command "Search for installed JDK and Gradle" completed (exit code
0)

● Bash(cd "C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/e40ec
      fb1-d5e2-49b7-b285-8ff63e8b1094/scratchpad" && git diff --no-index --stat
      spec.orig.md…)
  ⎿  (No output)

● Bash(cat "C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/e40e
      cfb1-d5e2-49b7-b285-8ff63e8b1094/scratchpad/spec.diff" | sed -n '1,140p')
  ⎿  (No output)

  Made 1 scratchpad edit +2 -2 (ctrl+o to expand)

● Bash(cd "C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/e40ec
      fb1-d5e2-49b7-b285-8ff63e8b1094/scratchpad" && git diff --no-index -U1
      spec.orig.md spe…)
  ⎿  (No output)

● Ainda não salvei nada: nem a spec, nem o DECISIONS.md, nem o plan.md. Seguem o
  ambiente, a proposta de classificação e o diff para revisão.

  1. Ambiente: há JDK, mas não o 17

  ┌────────┬─────────────────────────────────────────────────────────────────┐
  │  Item  │                            Situação                             │
  ├────────┼─────────────────────────────────────────────────────────────────┤
  │        │ 21.0.5 (JetBrains Runtime que veio com o Android Studio, em     │
  │ JDK    │ C:\Program Files\Android\Android Studio\jbr, com javac). Não    │
  │        │ está no PATH, e o JAVA_HOME está vazio.                         │
  ├────────┼─────────────────────────────────────────────────────────────────┤
  │ JDK 17 │ Não existe na máquina.                                          │
  ├────────┼─────────────────────────────────────────────────────────────────┤
  │ Gradle │ A distribuição 8.11.1 está no cache (~/.gradle/wrapper/dists).  │
  │        │ Não há Gradle no PATH, mas o wrapper resolve isso.              │
  └────────┴─────────────────────────────────────────────────────────────────┘

  Isso não impede a implementação, mas a escolha é sua:
  - (a) Recomendada: usar o JDK 21 como JAVA_HOME para rodar o Gradle e compilar
    com jvmTarget = 17 e -Xjdk-release=17. O bytecode e a API ficam limitados
    ao Java 17, sem instalar nada.
  - (b) Instalar o Temurin 17 (via winget) e usar jvmToolchain(17), que é o que
    está na proposta.
  - (c) Usar jvmToolchain(17) com o plugin foojay, que baixa o JDK 17 no
    primeiro build. Depende de rede.

  Em qualquer caso, o primeiro passo continua sendo apontar o JAVA_HOME.

  2. AMB nova ou esclarecimento?

  O critério que usei: vira AMB quando a decisão precisa de justificativa
  própria, porque nenhuma decisão existente cobre o caso. Vira esclarecimento
  quando a regra existente já alcança o caso e só faltava declarar o alcance de
  um termo.

  Gap: 1 + 4
  Registro: AMB-020, "Abrangência de um defeito de estrutura: lote ou despesa"
  Por quê: Os dois se resolvem pelo mesmo critério (abrangência do dano), e o
  GAP
  4 usa a identificação por posição do GAP 1. Separados, repetiriam a
  justificativa
  ────────────────────────────────────────
  Gap: 2
  Registro: AMB-021, "Causas múltiplas de erro de item"
  Por quê: A AMB-018 cobre só motivos de recusa, e erro não é recusa (RN-007). O

  texto dizia "a causa", no singular, então a decisão contraria a redação
  anterior
  ────────────────────────────────────────
  Gap: 3
  Registro: Esclarecimento na RN-011
  Por quê: A regra já declara um critério só. Falta dizer que "para cima"
  significa afastando-se do zero
  ────────────────────────────────────────
  Gap: 5
  Registro: Esclarecimento: definição única na §4.1, referenciada pela RN-008,
  RN-010 e RN-012
  Por quê: Declara o alcance de um termo já usado em três regras. Defini uma vez

  para não haver três definições

  Com isso, a §1 passa de "dezenove" para "vinte e uma" ambiguidades, e a spec
  sobe para a versão 1.1.

  3. Diff da spec

  O diff completo (174 linhas acrescentadas, 40 removidas) está em
  C:\Users\dev\AppData\Local\Temp\claude\c--Users-dev-SDD-Desafio\e40ecfb1-d5e2-
  49b7-b285-8ff63e8b1094\scratchpad\spec.diff, e a spec rascunhada em
  C:\Users\dev\AppData\Local\Temp\claude\c--Users-dev-SDD-Desafio\e40ecfb1-d5e2-
  49b7-b285-8ff63e8b1094\scratchpad\spec.md. Os trechos alterados em texto que
  já existia:

   §4.1 tabela
  -| Identificador | texto | Identifica a despesa; único no lote; desempata
  duplicatas em ordem lexicográfica | **sim** | RN-009, RN-012 |
  +| Identificador | texto | …; comparado e apresentado após a remoção de
  espaços nas extremidades | **sim** | RN-009, RN-012, AMB-020 |

   §4.1 (novo parágrafo)
  +**Espaços nas extremidades.** Em toda esta spec — na regra acima, na
  +normalização de categoria e fornecedor (RN-008) e na comparação de
  +identificadores (RN-012) —, espaços nas extremidades são todos os caracteres
  +que o padrão Unicode reconhece como espaço em branco, incluindo a tabulação e
  +os espaços não separáveis. Nenhum outro caractere é removido.

   §4.1 Lote inválido
  -…o período está ausente; … as despesas do lote não são informadas; …
  +…o período está ausente ou tem tipo incompatível; … as despesas do lote não
  +são informadas ou a relação de despesas tem tipo incompatível; …

   §4.1 Despesa inválida
  -erro quando lhe falta informação obrigatória, …, ou quando o valor
  -normalizado é menor ou igual a zero.
  +erro quando lhe falta informação obrigatória — inclusive o identificador —,
  +…, quando o valor normalizado é menor ou igual a zero, ou quando o
  +elemento da relação de despesas não é uma despesa (AMB-020). Todas as causas
  +aplicáveis são reportadas (AMB-021).

   §4.2 tabela
  -| Identificador | sempre | …
  +| Identificador, após a remoção de espaços nas extremidades | sempre; em
  despesa com erro cujo identificador
  +  falta ou tem tipo incompatível, é substituído pela posição da despesa na
  relação de despesas da entrada,
  +  contada a partir de um | … apresentado normalizado para mostrar o mesmo
  valor que a comparação usa (AMB-020) |
  -| Causa do erro: qual informação falhou e de que forma — … | erro | RN-010,
  AMB-017; …
  +| Causas do erro: para cada informação que falhou, qual e de que forma — … —,
  todas as aplicáveis,
  +  na ordem em que as informações da despesa são declaradas na §4.1 | erro |
  RN-010, AMB-017, AMB-021; … |
   exemplo d-009
  -… | causa: valor não positivo |
  +… | causas: valor não positivo |

   RN-008
  -espaços nas extremidades são removidos.
  +espaços nas extremidades, no alcance declarado na §4.1, são removidos.

   RN-010, item 1: mesmas duas causas novas de erro de lote da §4.1
   RN-010, item 2
  +…inclusive o identificador e a indicação de nota fiscal…, ou porque o
  elemento
  +da relação de despesas não é uma despesa (AMB-020). … (§4.1) …
  +A saída traz todas as causas do erro, na ordem em que as informações da
  despesa
  +são declaradas na §4.1 (AMB-021); a despesa cujo identificador falta ou tem
  tipo
  +incompatível é identificada pela sua posição na relação de despesas, contada
  a
  +partir de um (AMB-020).
  -**Origem:** decisões AMB-014, AMB-017 e AMB-019; §4.1
  +**Origem:** decisões AMB-014, AMB-017, AMB-019, AMB-020 e AMB-021; §4.1
   RN-010, Aceite (+7)
  +- Despesa sem identificador, na terceira posição → erro de item, posição 3;
  demais processadas.
  +- Duas despesas sem identificador → ambas erro de item; não é repetição; lote
  não rejeitado.
  +- Elemento da relação que não é uma despesa → erro de item, pela posição.
  +- Relação de despesas com tipo incompatível → lote rejeitado.
  +- Período com tipo incompatível → lote rejeitado.
  +- Sem fornecedor e valor −5,00 → erro; causas: fornecedor ausente e valor não
  positivo, nessa ordem.
  +- Fornecedor só com tabulação ou espaço não separável → erro de item
  (ausente).

   RN-012
  -remoção de espaços nas extremidades.
  -**Origem:** decisão AMB-019
  +remoção de espaços nas extremidades (§4.1), e são apresentados na saída
  +nessa mesma forma. A regra compara apenas identificadores informados: uma
  +despesa sem identificador, ou com identificador de tipo incompatível, é erro
  +de item (RN-010) e não constitui repetição com nenhuma outra (AMB-020).
  +**Origem:** decisões AMB-019 e AMB-020
   RN-012, Aceite (+3)
  +- " d-006" e "d-006" → o mesmo identificador; lote rejeitado; causa:
  identificador repetido.
  +- Identificador " d-001 " → apresentado como "d-001".
  +- Duas despesas sem identificador → esta regra não produz erro de lote; ambas
  erro de item.

   RN-011
  +"Para cima" significa afastando-se do zero, com o mesmo critério para valores
  +positivos e negativos; como valor normalizado menor ou igual a zero é sempre
  +erro de item (RN-010), em valor negativo o critério afeta apenas o valor
  +apresentado na saída, não a classificação.
   RN-011, Aceite
  +- Despesa de −45,005 → valor normalizado −45,01; erro de item; a saída
  apresenta −45,01.

   §9: os dois critérios de validação passam a incluir tipo incompatível do
  período e da relação,
   identificador ausente, elemento que não é despesa, todas as causas na ordem
  da §4.1 e
   identificação por posição. Novo critério: espaços conforme o padrão Unicode e
  identificador
   apresentado normalizado.

  As duas AMBs novas entram depois da AMB-019, no formato das demais: texto do
  RH ("não há; surgiu ao projetar a implementação"), o que não está claro,
  decisão, justificativa, custo aceito, consequências verificáveis e regra
  afetada. As decisões e justificativas são as suas, com redação ajustada à da
  spec.

  4. Três pontos que acrescentei e que precisam do seu aval

  São extensões das suas decisões, mas são regra:

  1. Identificador de tipo incompatível (por exemplo, um número) recebe o mesmo
     tratamento do ausente: é identificado pela posição e não conta como
     repetição. Você decidiu o caso "ausente" e chamou o caso geral de "ausente
     ou inválido".
  2. Elemento da relação que não é despesa tem uma única causa: "a própria
     despesa, com tipo incompatível". Sem essa decisão, não haveria campo a que
     atribuir a causa.
  3. Novo gap, não decidido: a §4.1 diz que é erro "quando uma informação tem
     tipo incompatível", sem limitar às informações obrigatórias. Uma descrição
     informada como número seria erro de item? A AMB-015 diz que os campos sem
     regra "são aceitos" e "não alteram nenhum resultado", o que sugere que não.
     Mas o texto atual permite a outra leitura.

  Para seguir, preciso de três respostas: se aprova o diff (com os pontos 1 e
  2), como resolve o ponto 3 e qual opção de JDK prefere. Depois disso salvo a
  spec, escrevo a D-009 e mostro a proposta final do plan.

> Três respostas.

  === JDK: opção (a) ===

  Usar o JDK 21 já instalado como JAVA_HOME, compilando com jvmTarget 17 e
  -Xjdk-release=17. Motivo: não adiciona instalação nem dependência de rede
  num momento em que o tempo é restrito, e o bytecode resultante fica limitado
  à API do Java 17.

  Registre isso como decisão técnica no plan.md, com a alternativa descartada
  (instalar o Temurin 17 ou usar o resolvedor de toolchain) e a consequência:
  o projeto compila e executa em qualquer JDK 17 ou superior, e o README deve
  declarar esse requisito. Acrescente ao plan que a configuração do JAVA_HOME
  é pré-requisito de execução, não parte do código.

  === Ponto 3: campo opcional com tipo incompatível ===

  Você está certo que o texto permite as duas leituras, e a AMB-015 resolve:
  um campo que nenhuma regra utiliza não pode alterar resultado algum, e um
  tipo incompatível nele é apenas mais uma forma de o campo não trazer
  informação aproveitável.

  Decisão: tipo incompatível em informação opcional e sem regra associada —
  descrição, rótulo de competência, identificador, nome e centro de custo do
  colaborador — é tratado como informação ausente, e não constitui erro.
  Apenas as informações obrigatórias produzem erro quando têm tipo
  incompatível.

  Atenção ao fornecedor: ele é obrigatório (RN-009 o usa como chave), de modo
  que tipo incompatível nele é erro de item. A AMB-015 já distingue "campo não
  utilizado" de "campo usado apenas como chave" — confirme que esta decisão
  preserva essa distinção, e ajuste a redação se não preservar.

  Registre como esclarecimento da AMB-015 e da RN-010, não como ambiguidade
  nova: a decisão deriva de um critério que a AMB-015 já estabeleceu, e só
  faltava declarar que ele alcança também o tipo do dado, não apenas a sua
  ausência.

  === Pontos 1 e 2: aprovados ===

  Os dois são extensões corretas. No gap 1 eu escrevi "ausente ou inválido",
  de modo que o tratamento do tipo incompatível no identificador segue o
  mesmo. E atribuir a causa à própria despesa é a única atribuição possível
  quando não há campo a que atribuí-la.

  === Classificação dos registros: aprovada ===

  Seu critério está correto. AMB-020 reunindo os gaps 1 e 4 pelo mesmo
  critério de abrangência do dano, AMB-021 para causas múltiplas porque a
  AMB-018 trata apenas de motivos de recusa e erro não é recusa, e os gaps 3 e
  5 como esclarecimentos de regras existentes. A definição única de "espaços
  nas extremidades" na §4.1, referenciada pelas três regras, evita três
  definições divergentes.

  === Pode salvar ===

  Salve a spec com o diff aprovado mais o ponto 3, crie a D-009 registrando
  que estes pontos foram descobertos ao projetar a implementação e não ao
  escrever a spec, e me mostre a proposta final do plan.md depois.

● Vou aplicar o ponto 3 no rascunho, salvar a spec, escrever a D-009 e verificar
  quais versões de dependência já estão no cache do Gradle para fixar as do
  plan.

● Bash(C=~/.gradle/caches/modules-2/files-2.1; ls $C 2>/dev/null | grep -E
      "jetbrains.kotlin$|kotlinx$|junit" ; ls
      $C/org.jetbrains.kotlin/kotlin-gradle-plugin 2>/dev/…)  ⎿  (No output)

● Bash(C=~/.gradle/caches/modules-2/files-2.1; ls $C/org.jetbrains.kotlin | head
      -40; ls $C/org.jetbrains.kotlinx; ls $C/org.junit* -d; ls
      $C/org.junit.jupiter 2>/dev/…)
  ⎿  Error: Exit code 2
     compose-compiler-gradle-plugin
     kotlin-build-common
     kotlin-build-statistics
     kotlin-build-tools-api
     kotlin-build-tools-impl
     kotlin-compiler-embeddable
     kotlin-compiler-runner
     kotlin-compose-compiler-plugin-embeddable
     kotlin-daemon-client
     … +30 lines (ctrl+o to expand)

  Made 6 scratchpad edits +40 -19 (ctrl+o to expand)

● Bash(cd "C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/e40ec
      fb1-d5e2-49b7-b285-8ff63e8b1094/scratchpad" && grep -n "^\*\*Regra
      afetada:\*\* nenhuma…)
  ⎿  (No output)

  Made 2 scratchpad edits +5 -3 (ctrl+o to expand)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/e40ecf
      b1-d5e2-49b7-b285-8ff63e8b1094/scratchpad";
      R="c:/Users/dev/SDD-Desafio/sdd-desafio/spe…)
  ⎿  Updated specs\001-motor-reembolso\spec.md (+206 -49)
      1  # Spec — Motor de Cálculo de Reembolso
      2  
      3 -**Versão:** 1.0 · **Status:** completa · **Última alteração:** 2
        -026-10-18                                                       
      3 +**Versão:** 1.1 · **Status:** completa · **Última alteração:** 2
        +026-10-18                                                       
      4  
      5  ---
      6  
     ...
      10  reembolso é decidido à mão: alguém do financeiro confere cada d
          espesa contra a
      11  política do RH e devolve uma relação de aprovações e recusas. O
           processo é
      12  lento, e a política contém lacunas que cada conferente preenche
           à sua maneira
      13 -— esta especificação identifica dezenove delas na §6 —, de modo
         - que a mesma                                                   
      13 +— esta especificação identifica vinte e uma delas na §6 —, de m
         +odo que a mesma                                                
      14  despesa pode receber decisões diferentes e nenhuma delas vem ac
          ompanhada de
      15  uma justificativa que o colaborador ou o auditor consigam verif
          icar. O custo
      16  aparece em pagamentos indevidos, recusas injustas e retrabalho 
          para contestar
     ...
      58  
      59  | Informação | Tipo | Significado | Obrigatória | Origem |
      60  |---|---|---|---|---|
      61 -| Identificador | texto | Identifica a despesa; único no lote; 
         -desempata duplicatas em ordem lexicográfica | **sim** | RN-009,
         - RN-012 |                                                      
      61 +| Identificador | texto | Identifica a despesa; único no lote; 
         +desempata duplicatas em ordem lexicográfica; comparado e aprese
         +ntado após a remoção de espaços nas extremidades | **sim** | RN
         +-009, RN-012, AMB-020 |                                        
      62  | Data | data (dia, sem hora) | Dia em que a despesa ocorreu; d
          ecide a pertinência ao período | **sim** | RN-005 |
      63  | Categoria | texto | Comparada após normalização; qualquer tex
          to é aceito, e categoria fora das três elegíveis é recusa, não 
          erro | **sim** | RN-008, AMB-017 |
      64  | Descrição | texto | Não é interpretada | não | AMB-015 |
     ...
      67  | Nota fiscal apresentada | sim ou não | Se a despesa tem compr
          ovante fiscal | **sim** | RN-003, RN-010 |
      68  
      69  Um texto obrigatório que fique vazio após a remoção de espaços 
          nas
      70 -extremidades é tratado como ausente. Informações não previstas 
         -nesta seção                                                    
      71 -são aceitas e ignoradas (AMB-015).                             
      70 +extremidades é tratado como ausente. Uma informação não obrigat
         +ória                                                           
      71 +informada com tipo incompatível é tratada como ausente e não co
         +nstitui erro                                                   
      72 +(AMB-015). Informações não previstas nesta seção são aceitas e 
         +ignoradas                                                      
      73 +(AMB-015).                                                     
      74 +                                                               
      75 +**Espaços nas extremidades.** Em toda esta spec — na regra acim
         +a, na                                                          
      76 +normalização de categoria e fornecedor (RN-008) e na comparação
         + de                                                            
      77 +identificadores (RN-012) —, espaços nas extremidades são todos 
         +os caracteres                                                  
      78 +que o padrão Unicode reconhece como espaço em branco, incluindo
         + a tabulação e                                                 
      79 +os espaços não separáveis. Nenhum outro caractere é removido.  
      80  
      81  **Lote inválido (erro de lote, RN-010 e RN-012).** O lote intei
          ro é
     … 337 more lines (ctrl+o to expand)

● Spec salva. Agora vou escrever a D-009 no DECISIONS.md.

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 79 lines
      10  
      11  ---
      12  
      13 +## D-009 — Lacunas de validação descobertas ao projetar a imple
         +mentação; AMB-020 e AMB-021 · `2026-10-18`                     
      14 +                                                               
      15 +**Gatilho:** a redação do plan.md. Ao desenhar o modelo de dado
         +s e o                                                          
      16 +adaptador de entrada, seis situações apareceram para as quais a
         + spec 1.0,                                                     
      17 +já declarada completa, não determinava o resultado. Nenhuma foi
         + encontrada                                                    
      18 +ao escrever a spec: todas surgiram ao tentar representar o cont
         +rato da §4                                                     
      19 +em estruturas concretas, quando cada campo exigiu uma resposta 
         +para                                                           
      20 +"ausente", "de outro tipo" e "presente". O plan.md não introduz
         +iu regra                                                       
      21 +nenhuma; as decisões foram trazidas para a spec antes de o plan
         + ser escrito.                                                  
      22 +                                                               
      23 +**O que mudou na spec (versão 1.0 → 1.1):**                    
      24 +- §6: novas AMB-020 (abrangência de um defeito de estrutura: lo
         +te ou                                                          
      25 +  despesa — identificador ausente ou de tipo incompatível, perí
         +odo ou                                                         
      26 +  relação de despesas de tipo incompatível, elemento da relação
         + que não é                                                     
      27 +  despesa) e AMB-021 (causas múltiplas de erro de item). §1: de
         + "dezenove"                                                    
      28 +  para "vinte e uma" ambiguidades.                             
      29 +- §4.1: novo parágrafo "Espaços nas extremidades", com a defini
         +ção única —                                                    
      30 +  todo caractere de espaço em branco do padrão Unicode, incluin
         +do tabulação e                                                 
      31 +  espaços não separáveis —, referenciada pela RN-008, pela RN-0
         +10 e pela                                                      
      32 +  RN-012. Lote inválido: acrescentados período e relação de des
         +pesas com tipo                                                 
      33 +  incompatível. Despesa inválida: acrescentados identificador a
         +usente e                                                       
      34 +  elemento que não é despesa; "uma informação tem tipo incompat
         +ível" passa a                                                  
      35 +  "uma informação obrigatória tem tipo incompatível"; todas as 
         +causas são                                                     
      36 +  reportadas. Acrescentado que informação não obrigatória com t
         +ipo                                                            
      37 +  incompatível é tratada como ausente. Tabela: identificador co
         +mparado e                                                      
      38 +  apresentado após a remoção de espaços.                       
      39 +- §4.2: identificador apresentado normalizado e, em erro sem id
         +entificador,                                                   
      40 +  substituído pela posição na relação, contada a partir de um; 
         +de "Causa do                                                   
      41 +  erro" para "Causas do erro", todas, na ordem da §4.1. Exemplo
         + de d-009: de                                                  
      42 +  "causa" para "causas".                                       
      43 +- RN-008: remissão ao alcance de "espaços" declarado na §4.1.  
      44 +- RN-010: as mesmas inclusões da §4.1 nos itens 1 e 2; tipo inc
         +ompatível só                                                   
      45 +  produz erro em informação obrigatória. Origem: acrescentadas 
         +AMB-015,                                                       
      46 +  AMB-020 e AMB-021. Aceite: dez casos acrescentados.          
      47 +- RN-011: "para cima" declarado como "afastando-se do zero", co
         +m o mesmo                                                      
      48 +  critério para valores negativos. Aceite: −45,005 → −45,01, er
         +ro de item.                                                    
      49 +- RN-012: identificador apresentado na forma comparada; despesa
         + sem                                                           
      50 +  identificador ou com identificador de tipo incompatível não c
         +onstitui                                                       
      51 +  repetição. Origem: acrescentada a AMB-020. Aceite: três casos
      52 +  acrescentados.                                               
      53 +- AMB-015, Decisão: o critério passa a alcançar o tipo do dado 
         +— campo sem                                                    
      54 +  regra com tipo incompatível é tratado como ausente; o fornece
         +dor, por ser                                                   
      55 +  chave da RN-009, permanece obrigatório, e o seu tipo incompat
         +ível é erro de                                                 
      56 +  item. Consequências: dois casos acrescentados. Regra afetada:
         + de "nenhuma                                                   
      57 +  RN" para "RN-010".                                           
      58 +- §9: critérios de validação atualizados; novo critério sobre e
         +spaços e                                                       
      59 +  identificador normalizado.                                   
      60 +                                                               
      61 +**Classificação:** AMB nova quando a decisão exige justificativ
         +a própria,                                                     
      62 +por não estar coberta por decisão existente; esclarecimento qua
         +ndo a regra                                                    
      63 +existente já alcança o caso e faltava declarar o alcance de um 
         +termo.                                                         
      64 +- AMB-020 reúne identificador ausente e tipo incompatível na es
         +trutura,                                                       
      65 +  resolvidos pelo mesmo critério (abrangência do dano).        
      66 +- AMB-021 é própria porque a AMB-018 trata de motivos de recusa
         +, e erro não                                                   
      67 +  é recusa (RN-007).                                           
      68 +- Esclarecimentos: o arredondamento de negativos (RN-011 já dec
         +larava um                                                      
      69 +  único critério), o alcance de "espaços" (termo já usado em tr
         +ês regras) e                                                   
      70 +  o tipo incompatível em campo opcional (critério já estabeleci
         +do pela                                                        
      71 +  AMB-015 para a ausência).                                    
      72 +                                                               
      73 +**Por quê:** cada uma das seis situações admitia leituras com r
         +esultados                                                      
      74 +verificáveis diferentes — lote rejeitado ou despesa em erro, um
         +a causa ou                                                     
      75 +várias, −45,01 ou −45,00 na saída, fornecedor de tabulação acei
         +to ou em                                                       
      76 +erro, descrição numérica aceita ou em erro. Decidi-las no códig
         +o seria                                                        
      77 +introduzir regra de negócio fora da spec.                      
      78 +                                                               
      79 +**O que isso invalidou:** nenhum critério de aceite. O resultad
         +o do lote de                                                   
      80 +exemplo é o mesmo. Muda a representação da causa de erro, de um
         +a para uma                                                     
      81 +lista: d-009 passa a ter "causas: valor não positivo".         
      82 +                                                               
      83 +**Tasks afetadas:** nenhuma — tasks.md ainda não foi escrito.  
      84 +                                                               
      85 +**Custo:** 2 arquivos (spec.md, DECISIONS.md). Achado de projet
         +o, não de                                                      
      86 +especificação: a spec foi declarada completa na D-008, e esta e
         +ntrada é a                                                     
      87 +evidência de que o contrato só se mostrou incompleto quando foi
      88 +representado.                                                  
      89 +                                                               
      90 +---                                                            
      91 +                                                               
      92  ## D-008 — Fechamento: §1, §2 e §9 escritas; vazamentos e andai
          me do template removidos · `2026-10-18`
      93  
      94  **Gatilho:** o fechamento da spec — redação das seções que perm
          aneciam no

● Agora escrevo o rascunho do plan na pasta temporária, para você revisar antes
  de ele ir para o repositório.

  Made 2 scratchpad edits +441 -2 (ctrl+o to expand)

● A spec e o DECISIONS.md estão salvos. O plan.md ainda não foi escrito: o
  rascunho completo está em C:\Users\dev\AppData\Local\Temp\claude\c--Users-dev-
  SDD-Desafio\e40ecfb1-d5e2-49b7-b285-8ff63e8b1094\scratchpad\plan.md.

  O que foi salvo

  spec.md (sdd-desafio/specs/001-motor-reembolso/spec.md), agora versão 1.1, com
  o diff aprovado mais o ponto 3. Este foi registrado como esclarecimento, não
  como AMB:
  - AMB-015, Decisão: o critério passa a valer também para o tipo do dado. Um
    campo sem regra com tipo incompatível é tratado como ausente.
  - Distinção do fornecedor preservada: o mesmo parágrafo diz que o fornecedor
    fica fora da lista por ser chave da RN-009. Por isso, se ele estiver ausente
    ou tiver tipo incompatível, é erro de item. A AMB-015 também ganhou duas
    consequências: descrição numérica é processada normalmente; fornecedor
    numérico é erro. O campo "Regra afetada" mudou de "nenhuma RN" para
    "RN-010".
  - §4.1 e RN-010: "tipo incompatível" passa a valer só para informação
    obrigatória (o fornecedor entrou como exemplo). A RN-010 ganhou três
    aceites: descrição numérica, fornecedor numérico e campos do colaborador ou
    do rótulo de competência com tipo incompatível.

  DECISIONS.md (sdd-desafio/specs/001-motor-reembolso/DECISIONS.md), D-009. O
  gatilho registrado é a redação do plan.md: os pontos apareceram ao representar
  o contrato da §4 em estruturas concretas, depois de a spec ter sido declarada
  completa na D-008. A entrada também registra o critério de classificação (AMB
  nova ou esclarecimento) e que nenhum critério de aceite caiu. A única mudança
  visível no lote de exemplo é que d-009 passa a ter "causas", no plural.

  O que mudou no plan em relação à proposta aprovada

  - §0, Pré-requisitos de execução: JAVA_HOME e o Gradle Wrapper ficam no topo,
    declarados como ambiente e não como código, e o README declara "JDK 17 ou
    superior".
  - DT-001, JDK: a sua opção (a), com as duas alternativas descartadas e a
    consequência. Acrescentei options.release = 17 no JavaCompile; sem isso, a
    verificação de alvo JVM do plugin Kotlin falha com o JDK 21.
  - R-01 é o primeiro risco e vira a primeira task do tasks.md, antes de
    qualquer código.
  - OrdemMotivosSecao8Test: lê a lista da §8 e compara com Motivo.entries. Fiz o
    mesmo para a ordem das causas de erro: o OrdemCamposSecao41Test compara a
    tabela da §4.1 com o enum CampoDespesa, pelo mesmo motivo.
  - DT-005, novo: o trim() do Kotlin não corresponde à definição de espaço em
    branco do Unicode que a §4.1 agora usa. Ele apara U+001C a U+001F, que não
    são espaço em branco, e não apara U+0085, que é. Proponho uma função
    aparar() própria, com os 25 code points da definição, e um teste para ela.
  - Versões: Kotlin 2.0.21, que já está no cache, e não 2.1. Sem @Serializable,
    não é preciso o plugin de compilador de serialização.
  - Esquema de saída: posicao no lugar de id (AMB-020) e causas_erro como lista
    (AMB-021).

  Uma questão antes de salvar o plan

  Escrevi a DT-008 tratando arquivo inexistente e JSON inválido como
  ENTRADA_ILEGIVEL. Falta decidir um caso: um JSON válido cuja raiz não é um
  lote, como uma lista ou um número.
  - (a) É "entrada que não pode ser lida": a causa é só ENTRADA_ILEGIVEL, que
    exclui as demais.
  - (b) É uma entrada lida sem período nem despesas: as causas são
    PERIODO_AUSENTE e DESPESAS_AUSENTES.

  O lote é rejeitado nos dois casos, mas as causas listadas na saída mudam,
  então isso é regra e vai para a spec. Recomendo (a): sem a estrutura de lote
  não há onde procurar período nem despesas, e (b) diria "ausente" sobre algo
  que nem chegou a ser procurado.

  Se você decidir isso e aprovar o rascunho, eu salvo o plan.md e acrescento a
  frase na spec com uma linha na D-009.

> (a) — a causa é apenas "entrada ilegível", que exclui as demais.

  Justificativa a registrar: sem a estrutura de um lote não há onde procurar o
  período nem a relação de despesas, e afirmar que estão "ausentes" seria dizer
  algo sobre uma estrutura que nunca existiu. É o mesmo critério de abrangência
  do dano adotado na AMB-020: a falha está no nível do lote inteiro, não no de
  um campo.

  Acrescente à RN-010 que uma entrada sintaticamente válida cuja estrutura não
  seja a de um lote constitui causa única de rejeição, e um aceite
  correspondente. Registre como esclarecimento da RN-010, com uma linha na
  D-009.

  Sobre a DT-005 — aprovada, e a observação é importante. A §4.1 declarou o
  alcance como "os caracteres que o padrão Unicode reconhece como espaço em
  branco", e se a implementação usasse uma função que diverge dessa definição,
  o código contrariaria a spec num ponto que a própria spec declara. A função
  própria com os code points enumerados é a solução correta, e o teste dela
  deve verificar os dois casos que você identificou: U+001C a U+001F não são
  aparados, U+0085 é.

  Aprovado o rascunho do plan com as mudanças que você listou. O
  OrdemCamposSecao41Test, que você acrescentou por conta própria, é a extensão
  correta do mesmo princípio do OrdemMotivosSecao8Test — uma reordenação
  acidental quebra o build em vez de alterar a saída em silêncio.

  Pode salvar o plan.md e a frase na spec.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 7 lines, removed 1 line
      427     identificador de despesa repetido (RN-012). Nenhuma despesa
            é processada e
      428     o lote é rejeitado. A saída informa todas as causas de erro
            de lote
      429     detectáveis; a entrada que não pode ser lida é a exceção, p
           or impedir a
      430 -   detecção das demais.                                       
      430 +   detecção das demais. Uma entrada sintaticamente válida cuja
          + estrutura não                                                
      431 +   seja a de um lote também é entrada que não pode ser lida, e
          + constitui                                                    
      432 +   causa única de rejeição: sem a estrutura de um lote não há 
          +onde procurar o                                               
      433 +   período nem a relação de despesas, e afirmar que estão ause
          +ntes seria                                                    
      434 +   dizer algo sobre uma estrutura que nunca existiu — a falha 
          +está no nível                                                 
      435 +   do lote inteiro, não no de um campo, pelo mesmo critério de
          + abrangência                                                  
      436 +   da AMB-020.                                                
      437  2. **Erro de item** — uma despesa não pode ser avaliada por in
           formação
      438     obrigatória ausente — inclusive o identificador e a indicaç
           ão de nota
      439     fiscal —, tipo incompatível, data inexistente no calendário
            ou valor

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 4 lines
      496    identificado pela sua posição; as demais despesas são proces
           sadas.
      497  - Relação de despesas com tipo incompatível → lote rejeitado.
      498  - Período com tipo incompatível → lote rejeitado.
      499 +- Entrada sintaticamente válida cuja estrutura não é a de um l
          +ote — por                                                     
      500 +  exemplo, uma relação ou um número no lugar do lote → lote re
          +jeitado; causa                                                
      501 +  única: entrada que não pode ser lida; não se informam períod
          +o ausente nem                                                 
      502 +  despesas não informadas.                                    
      503  - Despesa sem fornecedor e com valor −5,00 → erro de item; cau
           sas: fornecedor
      504    ausente e valor não positivo, nessa ordem.
      505  - Despesa com fornecedor formado apenas por tabulação ou por e
           spaço não

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 1 line
      80  
      81  **Lote inválido (erro de lote, RN-010 e RN-012).** O lote intei
          ro é
      82  rejeitado, sem que nenhuma despesa seja avaliada, quando: a ent
          rada não pode
      83 -ser lida; o período está ausente ou tem tipo incompatível; a da
         -ta de início                                                   
      83 +ser lida, inclusive quando é sintaticamente válida mas sua estr
         +utura não é a                                                  
      84 +de um lote (RN-010); o período está ausente ou tem tipo incompa
         +tível; a data de início                                        
      85  ou a de fim está ausente ou não é data válida; a data de início
           é posterior
      86  à de fim; as despesas do lote não são informadas ou a relação d
          e despesas
      87  tem tipo incompatível; ou duas ou mais despesas têm o mesmo ide
          ntificador.

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 5 lines
      57    RN" para "RN-010".
      58  - §9: critérios de validação atualizados; novo critério sobre e
          spaços e
      59    identificador normalizado.
      60 +- RN-010, item 1, e §4.1, Lote inválido: entrada sintaticamente
         + válida cuja                                                   
      61 +  estrutura não é a de um lote passa a ser entrada que não pode
         + ser lida —                                                    
      62 +  causa única de rejeição, sem período ausente nem despesas não
         + informadas.                                                   
      63 +  Aceite acrescentado. Esclarecimento, pelo critério de abrangê
         +ncia da                                                        
      64 +  AMB-020: sem estrutura de lote não há onde procurar período n
         +em despesas.                                                   
      65  
      66  **Classificação:** AMB nova quando a decisão exige justificativ
          a própria,
      67  por não estar coberta por decisão existente; esclarecimento qua
          ndo a regra

  Made 3 scratchpad edits +8 -5 (ctrl+o to expand)

● O aviso de "modificado no disco" provavelmente vem da cópia que fiz via shell.
  Vou confirmar que não há outras mudanças além das minhas.

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/e40ecf
      b1-d5e2-49b7-b285-8ff63e8b1094/scratchpad";
      R="c:/Users/dev/SDD-Desafio/sdd-desafio"; g…)
  ⎿  1 file changed (binary, mode only or too large to show)

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      81  **Lote inválido (erro de lote, RN-010 e RN-012).** O lote intei
          ro é
      82  rejeitado, sem que nenhuma despesa seja avaliada, quando: a ent
          rada não pode
      83  ser lida, inclusive quando é sintaticamente válida mas sua estr
          utura não é a
      84 -de um lote (RN-010); o período está ausente ou tem tipo incompa
         -tível; a data de início                                        
      85 -ou a de fim está ausente ou não é data válida; a data de início
         - é posterior                                                   
      84 +de um lote (RN-010); o período está ausente ou tem tipo incompa
         +tível; a                                                       
      85 +data de início ou a de fim está ausente ou não é data válida; a
         + data de início é posterior                                    
      86  à de fim; as despesas do lote não são informadas ou a relação d
          e despesas
      87  tem tipo incompatível; ou duas ou mais despesas têm o mesmo ide
          ntificador.
      88  Uma data de início igual à de fim é válida: o período tem um di
          a.

● Salvei os três arquivos. Conferi o diff da spec: as únicas mudanças são as
  desta rodada.

  spec.md (sdd-desafio/specs/001-motor-reembolso/spec.md), como esclarecimento
  da RN-010:
  - RN-010, item 1: uma entrada sintaticamente válida cuja estrutura não é a de
    um lote conta como "entrada que não pode ser lida", e essa é a causa única
    de rejeição. A justificativa é a sua, com remissão ao critério de
    abrangência da AMB-020.
  - RN-010, Aceite: um caso novo. Uma lista ou um número no lugar do lote
    rejeita o lote só por entrada ilegível, sem "período ausente" nem "despesas
    não informadas".
  - §4.1, Lote inválido: uma frase curta que remete à RN-010. Você pediu a
    mudança só na RN-010, mas a §4.1 repete a lista de causas de rejeição, e sem
    a frase as duas ficariam divergentes.

  DECISIONS.md (sdd-desafio/specs/001-motor-reembolso/DECISIONS.md): uma linha
  nova na D-009, registrando o caso como esclarecimento pelo critério da
  AMB-020.

  plan.md (sdd-desafio/specs/001-motor-reembolso/plan.md): o rascunho aprovado,
  com três ajustes:
  - DT-008: arquivo inexistente, JSON sintaticamente inválido e JSON válido que
    não é um lote dão rejeição com a causa única ENTRADA_ILEGIVEL.
  - DT-005: o teste verifica que U+001C a U+001F não são aparados e que U+0085
    é.
  - Testes do adaptador (§6): incluem o caso de JSON válido que não é um lote.

  Sugestão de commit para os três arquivos: docs(spec): AMB-020, AMB-021 e
  esclarecimentos de validação (D-009), seguido de docs(plan): plano técnico
  v1.0. Não fiz commit nem criei repositório git: a pasta ainda não é um
  repositório.

  O próximo passo é o tasks.md, com a T-001 sendo a configuração do ambiente
  (R-01): JAVA_HOME e o Gradle Wrapper.