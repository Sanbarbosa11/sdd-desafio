
> /clear

> Leia specs/001-motor-reembolso/spec.md, specs/001-motor-reembolso/plan.md e
  CLAUDE.md. A spec está na versão 1.1 com 21 ambiguidades e RN-001 a RN-012.
  O plan.md está escrito.

  Vamos escrever o tasks.md, seguindo o template em
  template/specs/001-motor-reembolso/tasks.md.

  Requisitos:

  - Cada task pequena o bastante para virar um commit. Mire entre 12 e 20
    tasks no total — o template avisa que, se o critério de aceite não puder
    ser expresso como "o teste X passa", a task está grande demais.
  - Cada task declara: o que faz, quais RN e AMB da spec atende, e o critério
    de aceite nomeando o teste que precisa passar.
  - A T-001 é a configuração do ambiente (R-01 do plan): JAVA_HOME, Gradle
    Wrapper, build.gradle.kts com os source sets de src/ e tests/. Critério de
    aceite: o build roda.
  - A matriz de Cobertura no fim do arquivo precisa estar completa: cada RN e
    cada AMB com decisão operacional mapeada para a task que a implementa e
    para o nome do teste que a verifica. O template diz que é exatamente a
    matriz que a correção vai montar.
  - Organize em fases, como o template sugere, e deixe a Fase 5 (Envelope)
    reservada e vazia.

  Me apresente a proposta antes de escrever, e diga se alguma RN ou AMB ficou
  sem task ou sem teste — isso seria buraco de rastreabilidade.

  Searched for 1 pattern, listed 1 directory (ctrl+o to expand)

● Bash(cd /c/Users/dev/SDD-Desafio/sdd-desafio && cat CLAUDE.md && echo "=====
      SPEC" && cat specs/001-motor-reembolso/spec.md && echo "===== PLAN" && cat
      specs/001-mot…)
  ⎿  (No output)

  Read 1 file (ctrl+o to expand)

● Li a spec 1.1, o plan.md, o CLAUDE.md e o template. O
  specs/001-motor-reembolso/tasks.md atual é uma cópia do template sem nada
  preenchido, então vou sobrescrevê-lo quando você aprovar. Segue a proposta.

  Proposta: 20 tasks em 5 fases

  Uma convenção antes das tasks. O plan (§6) prevê um arquivo de teste por RN,
  com um teste por item de aceite. Vários aceites dependem de mais de uma regra;
  por exemplo, "100,005 → nota exigida" é aceite da RN-011, mas só dá para
  verificar depois da RN-003. Por isso cada task acrescenta ao arquivo da RN os
  aceites que ela passa a tornar verificáveis. A matriz de Cobertura indica a
  task em que cada teste nasce.

  Fase 1 — Fundação

  Task: T-001
  O que faz: Ambiente (R-01): JAVA_HOME apontando para o JDK 21 do Android
  Studio, Gradle Wrapper 8.11.1 gerado a partir do cache, build.gradle.kts com
  source sets src/ e tests/ (DT-002), alvo Java 17 (DT-001) e dependências
  (R-02). Preenche também "Stack e comandos" no CLAUDE.md, que ainda tem <...>
  Atende: — (infra; R-01, R-02, DT-001, DT-002)
  Aceite (teste que passa): ./gradlew --version mostra 8.11.1, e ./gradlew build

  termina com sucesso executando AmbienteTest › build compila src e executa
  testes de tests
  ────────────────────────────────────────
  Task: T-002
  O que faz: Valor com normalizar (HALF_UP, escala 2) e testes de guarda da
  aritmética
  Atende: RN-011, AMB-014
  Aceite (teste que passa): RN011NormalizacaoMonetariaTest › RN-011 100,005
  normaliza para 100,01, RN-011 −45,005 normaliza para −45,01, RN-011 54,90 e
  54,900 são o mesmo valor
  ────────────────────────────────────────
  Task: T-003
  O que faz: aparar() com os 25 code points de White_Space (DT-005)
  Atende: §4.1 (espaços); base de RN-008, RN-012, AMB-020
  Aceite (teste que passa): EspacosUnicodeTest › §4.1 aparar remove os 25 code
  points White_Space, §4.1 aparar remove U+0085 e não remove U+001C a U+001F
  ────────────────────────────────────────
  Task: T-004
  O que faz: Modelo do domínio: Lido, LoteLido, Categoria/Politica, Motivo,
  CampoDespesa, Falha, ResultadoDespesa, ResultadoLote. Mais os testes de ordem
   e de fronteira
  Atende: §8 (AMB-018), AMB-021 (ordem dos campos), DT-009
  Aceite (teste que passa): OrdemMotivosSecao8Test, OrdemCamposSecao41Test,
  FronteiraDominioTest

  Fase 2 — Regras de negócio

  Task: T-005
  O que faz: Validação de lote: período e despesas ausentes ou de tipo
  incompatível, datas, início posterior ao fim, identificador repetido. Reporta
   todas as causas detectáveis
  Atende: RN-010 (lote), RN-012, AMB-017, AMB-019, AMB-020 (ii)
  Aceite: RN-010 início posterior ao fim → lote rejeitado, RN-010 início
  posterior ao fim e identificador repetido → as duas causas, RN-012 duas
  despesas com identificador d-006 → lote rejeitado, RN-010 período com tipo
  incompatível → lote rejeitado
  ────────────────────────────────────────
  Task: T-006
  O que faz: Validação de item: campos obrigatórios, tipo incompatível, data
  inexistente, texto vazio após aparar, valor normalizado ≤ 0. Reporta todas as
   causas, na ordem da §4.1
  Atende: RN-010 (item), RN-011, AMB-012, AMB-015 (fornecedor obrigatório),
  AMB-017, AMB-021
  Aceite: RN-010 d-009 −45,00 → erro de item com causa valor não positivo,
  RN-010
  despesa sem fornecedor e valor −5,00 → causas fornecedor ausente e valor não
  positivo nessa ordem, RN-011 0,004 normaliza para 0,00 e é erro de item
  ────────────────────────────────────────
  Task: T-007
  O que faz: Identidade do item: id aparado, Referencia.Posicao quando o id
  falta, elemento que não é despesa, ausências que não contam como repetição
  Atende: RN-010, RN-012, AMB-020 (i, iii), AMB-021
  Aceite: RN-010 despesa sem identificador na terceira posição → erro
  identificado pela posição 3, RN-012 duas despesas sem identificador → não é
  erro de lote, RN-010 elemento que não é despesa → erro de item na sua
  posição, AMB-021 sem identificador e data 2026-02-30 → causas identificador
  ausente e data inexistente nessa ordem
  ────────────────────────────────────────
  Task: T-008
  O que faz: Motor.avaliar com a etapa de valor: limite por despesa e reembolso
  parcial
  Atende: RN-001, RN-002, AMB-001, AMB-002, AMB-003, AMB-007, AMB-009
  Aceite: RN001LimitePorDespesaTest, RN002ReembolsoParcialTest (todos os aceites

  das duas RNs)
  ────────────────────────────────────────
  Task: T-009
  O que faz: Pertinência ao período (passo 1.1)
  Atende: RN-005, RN-006, AMB-008, AMB-010, AMB-016
  Aceite: RN005PeriodoTest, RN006ForaDoPeriodoTest
  ────────────────────────────────────────
  Task: T-010
  O que faz: Normalização de categoria e fornecedor; elegibilidade da categoria
  (passo 1.2)
  Atende: RN-008, AMB-013, AMB-017 (d-005 é recusa, não erro)
  Aceite: RN008CategoriaTest
  ────────────────────────────────────────
  Task: T-011
  O que faz: Exigência de nota fiscal e recusa (passo 1.3)
  Atende: RN-003, RN-004, RN-011, AMB-004, AMB-005, AMB-006
  Aceite: RN003NotaFiscalTest, RN004SemNotaFiscalTest
  ────────────────────────────────────────
  Task: T-012
  O que faz: Lista de motivos sem interrupção na primeira regra, ordenada pela
  §8; erro com lista vazia
  Atende: RN-007, AMB-018
  Aceite: RN007ListaDeMotivosTest
  ────────────────────────────────────────
  Task: T-013
  O que faz: Duplicidade em duas passadas, com comparador por code point
  (DT-007,
  R-07)
  Atende: RN-009, AMB-011, AMB-013 (fornecedor), AMB-010 (fora do período não
  forma grupo)
  Aceite: RN009DuplicidadeTest, incluindo RN-009 identificador fora do BMP é
  comparado por code point
  ────────────────────────────────────────
  Task: T-014
  O que faz: Consolidado: total, quantidades por classificação, lote vazio
  Atende: §4.2, RN-010, AMB-014
  Aceite: ConsolidadoTest › §4.2 total reembolsado é a soma dos itens e as
  quantidades somam o total, RN-010 lote sem despesas → total 0,00 e zero em
  todas as classificações

  Fase 3 — Casos de borda

  ┌───────┬───────────────────────────┬─────────┬──────────────────────────┐
  │ Task  │         O que faz         │ Atende  │          Aceite          │
  ├───────┼───────────────────────────┼─────────┼──────────────────────────┤
  │       │ Um teste por linha da     │         │ Secao7CasosDeBordaTest   │
  │ T-015 │ tabela da §7 (20 linhas)  │ §7      │ (20 testes com prefixo   │
  │       │                           │         │ §7)                      │
  ├───────┼───────────────────────────┼─────────┼──────────────────────────┤
  │       │ RastreabilidadeTest: toda │ RN-001  │                          │
  │ T-016 │  RN com teste, e número   │ a       │ RastreabilidadeTest      │
  │       │ de testes §7 igual ao de  │ RN-012, │                          │
  │       │ linhas da tabela          │  §7     │                          │
  └───────┴───────────────────────────┴─────────┴──────────────────────────┘

  Fase 4 — Saída e CLI

  Task: T-017
  O que faz: Adaptador JSON → LoteLido (DT-004): ENTRADA_ILEGIVEL, BigDecimal
  lido do texto cru, data STRICT, campos sem regra não lidos
  Atende: RN-010, RN-011, AMB-008, AMB-009, AMB-015, AMB-020
  Aceite: EntradaJsonTest › RN-010 raiz JSON que é uma lista → rejeitado com
  causa única ENTRADA_ILEGIVEL, RN-011 33.333 é lido sem passar por Double,
  AMB-015 lotes que diferem só no centro de custo → resultados idênticos,
  AMB-009 hospedagem 400,00 descrita como 1 diaria ou 2 diarias → mesmo
  resultado 250,00 parcial
  ────────────────────────────────────────
  Task: T-018
  O que faz: ResultadoLote → JSON (§3.3 do plan): campos omitidos conforme a
  classificação, dinheiro com duas casas, frase de justificativa
  Atende: §4.2, AMB-015, AMB-020
  Aceite: SaidaJsonTest › §4.2 recusada não traz limite_aplicado, RN-012
  identificador " d-001 " sai como d-001, AMB-015 saída não contém colaborador,
   rótulo nem descrição
  ────────────────────────────────────────
  Task: T-019
  O que faz: CLI calcular --input --output e exit codes 0/1/2/3 (DT-008)
  Atende: RN-010 (arquivo inexistente vira entrada ilegível), DT-008
  Aceite: CliTest › DT-008 lote rejeitado sai com exit 1 e JSON escrito, DT-008
  flag ausente sai com exit 2
  ────────────────────────────────────────
  Task: T-020
  O que faz: Ponta a ponta com exemplos/despesas-exemplo.json e determinismo com

  a ordem invertida
  Atende: §9, RN-001, RN-009
  Aceite: Secao9CriteriosDeAceiteTest › §9 lote de exemplo produz total 623,43
  com 4 integrais, 4 parciais, 5 recusadas e 1 erro, §9 RN-001 RN-009 inverter
  a ordem do lote de exemplo não altera nenhum resultado

  Fase 5 — Envelope

  Reservada e vazia. A numeração continua em T-021.

  Matriz de Cobertura (resumida; no arquivo vai uma linha por teste-chave)

  Regra: RN-001
  Task: T-008
  Teste: RN-001 d-001 alimentacao 72,50 → reembolso 60,00 parcial
  ────────────────────────────────────────
  Regra: RN-002
  Task: T-008
  Teste: RN-002 alimentacao de 60,01 → reembolso 60,00 parcial e excedente 0,01
  ────────────────────────────────────────
  Regra: RN-003
  Task: T-011
  Teste: RN-003 d-003 transporte 100,00 sem NF → nota não exigida e reembolso
  80,00 parcial
  ────────────────────────────────────────
  Regra: RN-004
  Task: T-011
  Teste: RN-004 d-004 transporte 100,01 sem NF → recusada com SEM_NOTA_FISCAL
  ────────────────────────────────────────
  Regra: RN-005
  Task: T-009
  Teste: RN-005 d-014 em 2026-07-31 igual à data de fim → dentro do período
  ────────────────────────────────────────
  Regra: RN-006
  Task: T-009
  Teste: RN-006 d-008 em 2026-04-15 → recusada com FORA_DA_COMPETENCIA
  ────────────────────────────────────────
  Regra: RN-007
  Task: T-012
  Teste: RN-007 150,00 sem NF fora do período → motivos FORA_DA_COMPETENCIA e
  SEM_NOTA_FISCAL nessa ordem
  ────────────────────────────────────────
  Regra: RN-008
  Task: T-010
  Teste: RN-008 d-005 coworking → recusada com CATEGORIA_INELEGIVEL
  ────────────────────────────────────────
  Regra: RN-009
  Task: T-013
  Teste: RN-009 d-006 e d-007 → d-006 integral e d-007 recusada com DUPLICATA
  ────────────────────────────────────────
  Regra: RN-010
  Task: T-005, T-006, T-007, T-014, T-017
  Teste: um teste por aspecto (lote, item, posição, lote vazio, entrada
  ilegível), listados acima
  ────────────────────────────────────────
  Regra: RN-011
  Task: T-002 (+ T-006, T-008, T-011, T-013)
  Teste: RN-011 100,005 normaliza para 100,01
  ────────────────────────────────────────
  Regra: RN-012
  Task: T-005, T-007
  Teste: RN-012 d-006 com espaço à esquerda repete d-006 → lote rejeitado
  ────────────────────────────────────────
  Regra: AMB-001
  Task: T-008
  Teste: RN-001 três alimentações de 59,00 no mesmo dia → 59,00 cada e 177,00 no

  total
  ────────────────────────────────────────
  Regra: AMB-002
  Task: T-008
  Teste: RN-002 alimentacao de 60,00 → reembolso 60,00 integral
  ────────────────────────────────────────
  Regra: AMB-003 (dissolvida)
  Task: T-008
  Teste: RN-001 inverter a ordem de d-001 e d-002 não altera os valores
  ────────────────────────────────────────
  Regra: AMB-004
  Task: T-011
  Teste: RN-003 transporte 100,01 com NF → nota exigida e presente, reembolso
  80,00 parcial
  ────────────────────────────────────────
  Regra: AMB-005
  Task: T-011
  Teste: RN-004 hospedagem 200,00 sem NF → recusada com SEM_NOTA_FISCAL apesar
  de
  estar abaixo do limite
  ────────────────────────────────────────
  Regra: AMB-006
  Task: T-011
  Teste: RN-003 alimentacao 500,00 sem NF → nota exigida e ausente apesar do
  teto
  de 60,00
  ────────────────────────────────────────
  Regra: AMB-007
  Task: T-008
  Teste: RN-001 d-010 hospedagem 480,00 → limite 250,00 sem ampliação, excedente

  230,00 + AMB-007 d-011 no mesmo fornecedor de d-010 mantém limite 60,00
  ────────────────────────────────────────
  Regra: AMB-008
  Task: T-009
  Teste: RN-005 d-008 em 2026-04-15 → fora do período
  ────────────────────────────────────────
  Regra: AMB-009
  Task: T-008, T-017
  Teste: RN-001 d-010 … + AMB-009 hospedagem 400,00 descrita como 1 diaria ou 2
  diarias → mesmo resultado
  ────────────────────────────────────────
  Regra: AMB-010
  Task: T-009, T-013
  Teste: RN-006 d-008 … + RN-009 duas iguais fora do período → nenhuma recebe
  DUPLICATA
  ────────────────────────────────────────
  Regra: AMB-011
  Task: T-013
  Teste: RN-009 inverter a ordem de d-006 e d-007 → os mesmos resultados, RN-009

  d-9 e d-10 → d-10 mantida
  ────────────────────────────────────────
  Regra: AMB-012 (dissolvida)
  Task: T-006
  Teste: RN-010 d-009 −45,00 → erro de item com causa valor não positivo
  ────────────────────────────────────────
  Regra: AMB-013
  Task: T-010, T-013
  Teste: RN-008 alimentação com cedilha e til → recusada com
  CATEGORIA_INELEGIVEL
  + RN-009 Bistro Central e BISTRO CENTRAL → grupo de duplicatas
  ────────────────────────────────────────
  Regra: AMB-014
  Task: T-002, T-006
  Teste: RN-011 … + RN-011 0,004 normaliza para 0,00 e é erro de item
  ────────────────────────────────────────
  Regra: AMB-015
  Task: T-006, T-017, T-018
  Teste: RN-010 fornecedor informado como número → erro tipo incompatível,
  AMB-015 centro de custo …, AMB-015 saída não contém …
  ────────────────────────────────────────
  Regra: AMB-016
  Task: T-009
  Teste: RN-005 despesa em 2026-07-01 igual à data de início → dentro do período
  ────────────────────────────────────────
  Regra: AMB-017
  Task: T-006, T-010
  Teste: RN-010 despesa sem o campo de valor → erro de item e as demais
  processadas + RN-010 d-005 coworking é recusada e não erro
  ────────────────────────────────────────
  Regra: AMB-018
  Task: T-012
  Teste: o mesmo teste da RN-007
  ────────────────────────────────────────
  Regra: AMB-019
  Task: T-005
  Teste: RN-012 duas despesas com identificador d-006 → lote rejeitado
  ────────────────────────────────────────
  Regra: AMB-020
  Task: T-005, T-007
  Teste: RN-010 período com tipo incompatível → lote rejeitado, RN-010 despesa
  sem identificador na terceira posição …
  ────────────────────────────────────────
  Regra: AMB-021
  Task: T-006, T-007
  Teste: RN-010 sem fornecedor e valor −5,00 → causas … nessa ordem + AMB-021
  sem
  identificador e data 2026-02-30 …
  ────────────────────────────────────────
  Regra: §8 / §4.1 / §4.2 / §7 / §9
  Task: T-004 / T-003 / T-014, T-018 / T-015 / T-020
  Teste: listados nas tasks

  Rastreabilidade: o que encontrei

  Toda RN e toda AMB tem task e teste. Há quatro pontos que você deveria
  conhecer:

  1. AMB-003 e AMB-012 são dissolvidas. Não têm decisão operacional própria,
     então não ganham task. Na matriz elas aparecem marcadas como "dissolvida" e
     ligadas ao teste que mostra que a dissolução vale: a avaliação independe
     da ordem, e o valor negativo é erro de item. Para mim isso não é buraco,
     desde que fique explícito.
  2. Três consequências só podem ser testadas pelo JSON. As da AMB-008, AMB-009
     e AMB-015 dizem que a descrição e os campos do colaborador não alteram o
     resultado. Por desenho do plan, o domínio nunca vê esses campos, então os
     testes ficam no adaptador (T-017) e na saída (T-018). No domínio, a regra
     se cumpre porque esses campos nem existem lá.
  3. Esta é a lacuna real: o RastreabilidadeTest do plan (§6) só verifica RN.
     Uma AMB sem teste não quebra o build, e a cobertura das AMBs passa a
     depender só da matriz preenchida à mão. Minha recomendação é fazer o T-016
     exigir também, para cada AMB não dissolvida, ao menos um teste cujo nome
     contenha o ID. Para isso os testes de AMB precisam levar o ID no nome, por
     exemplo RN-001 AMB-001 três alimentações …. Como isso muda a §6 do plan,
     entraria antes, num commit docs(plan):.
  4. Ficamos em 20 tasks, no teto do intervalo. Separei a validação de item em
     T-006 (campos) e T-007 (identidade e posição) porque juntas não caberiam
     num commit. Se preferir 19, dá para juntar T-015 e T-016.

  Preciso de duas respostas para escrever o arquivo: posso estender o
  RastreabilidadeTest às AMBs (ponto 3)? E fico com as 20 tasks ou junto T-015 e
  T-016?

> Duas respostas.

  Ponto 3: sim, estenda o RastreabilidadeTest às ambiguidades. Você encontrou
  uma lacuna real — sem isso, a cobertura das AMB depende de uma matriz
  mantida à mão, e matriz mantida à mão desatualiza silenciosamente. As
  ambiguidades são requisitos tanto quanto as regras, e o que o teste garante
  é justamente que nenhum requisito fique sem verificação.

  Faça o T-016 exigir, para cada AMB não dissolvida, ao menos um teste cujo
  nome contenha o identificador. As dissolvidas (AMB-003 e AMB-012) ficam
  isentas, e o teste deve declarar essa isenção explicitamente, listando-as,
  para que uma AMB nova nunca entre na isenção por descuido.

  Como isso altera a §6 do plan, faça essa mudança primeiro, num commit
  docs(plan): próprio, antes de escrever o tasks.md.

  Tasks: mantenha as 20. T-015 e T-016 fazem coisas diferentes — uma cobre os
  casos de borda, a outra guarda o processo — e juntas não caberiam num
  commit, que é o critério do template.

  Sobre o ponto 1: concordo que as dissolvidas não são buraco, desde que a
  matriz as marque como tal e as ligue ao teste que demonstra a dissolução.
  Mantenha como você propôs.

  Sobre o ponto 2: correto. A regra de que a descrição e os campos do
  colaborador não alteram o resultado se cumpre no domínio por construção —
  esses campos não chegam lá — e só é observável na fronteira. Registre essa
  observação no próprio tasks.md, junto às tasks T-017 e T-018, para que quem
  leia a matriz entenda por que o teste dessas AMB está no adaptador e não no
  domínio.

  Escreva o tasks.md com a proposta aprovada, incluindo a convenção de que
  cada task acrescenta ao arquivo da RN os aceites que ela torna verificáveis,
  e a nota sobre as três AMB testadas na fronteira.

● Bash(git status --short 2>&1 | head; git log --oneline -8 2>&1; echo ===; head
      -40 specs/001-motor-reembolso/DECISIONS.md)
  ⎿  (No output)

● Repositório limpo. Primeiro a mudança na §6 do plan.

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 13 lines, removed 3 lines
      410  - **Testes de rastreabilidade** (`tests/reembolso/rastreabilid
           ade/`). Leem
      411    `spec.md` e falham o build quando código e spec divergem:
      412    - `RastreabilidadeTest`: extrai todo `### RN-\d{3}` da spec 
           e exige pelo
      413 -    menos um teste cujo nome contenha o ID. Conta as linhas da
          - tabela da §7 e                                               
      414 -    exige o mesmo número de testes com prefixo `§7`. Uma RN no
          -va sem teste,                                                 
      415 -    ou um caso de borda novo sem teste, quebra o build.       
      413 +    menos um teste cujo nome contenha o ID. Faz o mesmo com to
          +do                                                            
      414 +    `### AMB-\d{3}`: cada ambiguidade é requisito tanto quanto
          + uma regra, e                                                 
      415 +    exige pelo menos um teste cujo nome contenha o seu ID. Fic
          +am isentas                                                    
      416 +    apenas as ambiguidades dissolvidas, que não têm decisão op
          +eracional                                                     
      417 +    própria; o teste as declara numa lista explícita (`AMB-003
          +`, `AMB-012`),                                                
      418 +    de modo que uma AMB nova nunca entre na isenção por descui
          +do, e falha se                                                
      419 +    um ID da lista não existir na spec ou se o título correspo
          +ndente não                                                    
      420 +    contiver "(dissolvida)". Conta as linhas da tabela da §7 e
          + exige o mesmo                                                
      421 +    número de testes com prefixo `§7`. Uma RN ou AMB nova sem 
          +teste, ou um                                                  
      422 +    caso de borda novo sem teste, quebra o build.             
      423 +  - Consequência para a nomenclatura: o teste que verifica a c
          +onsequência                                                   
      424 +    de uma AMB leva o seu ID no nome, depois do ID da RN quand
          +o houver                                                      
      425 +    (`RN-001 AMB-001 três alimentações de 59,00 …`).          
      426    - `OrdemMotivosSecao8Test`: extrai, na ordem, os códigos de 
           motivo da lista
      427      de elegibilidade da §8 (`→ FORA_DA_COMPETENCIA`, …) e comp
           ara com
      428      `Motivo.entries.map { it.name }`. Uma reordenação acidenta
           l do enum, ou da

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      1  # Plano Técnico — Motor de Cálculo de Reembolso
      2  
      3 -**Versão:** 1.0 · **Baseado na spec:** 1.1                      
      3 +**Versão:** 1.1 · **Baseado na spec:** 1.1                      
      4  
      5  > Aqui mora o COMO. Este arquivo pode e deve falar de linguagem,
          biblioteca e
      6  > arquitetura. O que ele **não** pode é introduzir regra de negó
         cio nova — se

● Bash(git add specs/001-motor-reembolso/plan.md && git commit -q -F - <<'EOF'
      docs(plan): RastreabilidadeTest exige teste para cada AMB nao dissolvida…)
  ⎿  (No output)

● Plan commitado (db8b1b6). Agora o tasks.md.

● Write(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 397 lines, removed 17 lines
      1  # Tasks — Motor de Cálculo de Reembolso
      2  
      3 +**Baseado em:** spec 1.1 · plan 1.1                             
      4 +                                                                
      5  > Cada task é pequena o bastante para virar **um commit**. Se vo
         cê não consegue
      6  > descrever o critério de aceite como "o teste X passa", a task 
         está grande demais.
      7  >
     ...
       10  
       11  **Formato do commit:** `feat(T-003): <descrição>` · `test(T-00
           3): <descrição>`
       12  
       13 +### Convenções desta lista                                    
       14 +                                                              
       15 +- **Aceites que dependem de mais de uma regra.** O plan (§6) p
          +revê um arquivo                                               
       16 +  de teste por RN, com um teste por item da lista "Aceite". Mu
          +itos aceites só                                               
       17 +  são verificáveis depois de outra regra existir — "100,005 → 
          +nota exigida" é                                               
       18 +  aceite da RN-011, mas depende da RN-003. Regra: **cada task 
          +acrescenta ao                                                 
       19 +  arquivo da RN os aceites que ela torna verificáveis.** O arq
          +uivo de uma RN                                                
       20 +  pode, portanto, receber testes em mais de uma task; a matriz
          + de Cobertura                                                 
       21 +  registra em qual task cada teste nasce.                     
       22 +- **IDs no nome do teste.** O nome começa pelo ID da RN e, qua
          +ndo o teste                                                   
       23 +  verifica a consequência de uma AMB, leva também o ID da AMB 
          +(plan §6). O                                                  
       24 +  `RastreabilidadeTest` (T-016) exige um teste por RN e por AM
          +B não                                                         
       25 +  dissolvida a partir desses nomes.                           
       26 +- **Seções com ponto.** `.` não é permitido em nome de método 
          +JVM; nos nomes                                                
       27 +  de teste, §4.1 e §4.2 são escritos `§4-1` e `§4-2`.         
       28 +- **Checklist de revisão de toda task de código:** nenhum `.tr
          +im()` em `src/`                                               
       29 +  (R-05); nenhum `Double` em caminho de dinheiro (R-04).      
       30 +                                                              
       31  ---
       32  
       33  ## Fase 1 — Fundação
       34  
       15 -- [ ] **T-001** — <o que faz>                                 
       16 -  - **Atende:** RN-001                                        
       17 -  - **Aceite:** <o teste que precisa passar>                  
       18 -  - **Commit:** `<hash preenchido depois>`                    
       35 +- [ ] **T-001** — Configurar o ambiente (R-01): `JAVA_HOME` ap
          +ontando para o                                                
       36 +  JDK 21 do Android Studio; Gradle Wrapper 8.11.1 gerado a par
          +tir da                                                        
       37 +  distribuição do cache e versionado; `build.gradle.kts` com K
          +otlin 2.0.21,                                                 
       38 +  plugin `application`, alvo Java 17 (`jvmTarget`, `-Xjdk-rele
          +ase=17`,                                                      
       39 +  `options.release = 17`), source sets `src/` e `tests/` sem r
          +ecursos, e as                                                 
       40 +  dependências `kotlinx-serialization-json` 1.7.3, `junit-bom`
          + 5.11 e                                                       
       41 +  `kotlin-test-junit5` (R-02). Preencher "Stack e comandos" no
          + CLAUDE.md.                                                   
       42 +  - **Atende:** nenhuma RN ou AMB (infraestrutura); plan R-01,
          + R-02, DT-001, DT-002                                         
       43 +  - **Aceite:** `./gradlew --version` mostra Gradle 8.11.1 rod
          +ando no JDK 21,                                               
       44 +    e `./gradlew build` termina com sucesso executando        
       45 +    `AmbienteTest` › `build compila src e executa testes de te
          +sts`                                                          
       46 +  - **Commit:** —                                             
       47  
       20 -- [ ] **T-002** — <...>                                       
       21 -  - **Atende:**                                               
       22 -  - **Aceite:**                                               
       23 -  - **Commit:**                                               
       48 +- [ ] **T-002** — `value class Valor` com `normalizar` (`setSc
          +ale(2, HALF_UP)`)                                             
       49 +  como único construtor, e os testes de guarda da aritmética (
          +DT-003).                                                      
       50 +  - **Atende:** RN-011, AMB-014                               
       51 +  - **Aceite:** `RN011NormalizacaoMonetariaTest` passa:       
       52 +    `RN-011 AMB-014 33,333 normaliza para 33,33`,             
       53 +    `RN-011 AMB-014 100,005 normaliza para 100,01 e 100,004 pa
          +ra 100,00`,                                                   
       54 +    `RN-011 AMB-014 −45,005 normaliza para −45,01 afastando-se
          + do zero`,                                                    
       55 +    `RN-011 AMB-014 54,90 e 54,900 são o mesmo valor`         
       56 +  - **Commit:** —                                             
       57  
       58 +- [ ] **T-003** — Função `aparar()` no domínio com o conjunto 
          +explícito dos 25                                              
       59 +  code points da propriedade Unicode `White_Space` (DT-005).  
       60 +  - **Atende:** §4.1 (espaços nas extremidades); base de RN-00
          +8, RN-010, RN-012                                             
       61 +  - **Aceite:** `EspacosUnicodeTest` passa:                   
       62 +    `§4-1 aparar remove os 25 code points White_Space nas extr
          +emidades`,                                                    
       63 +    `§4-1 aparar remove U+0085 e não remove U+001C a U+001F`, 
       64 +    `§4-1 aparar não remove espaços do meio do texto`         
       65 +  - **Commit:** —                                             
       66 +                                                              
       67 +- [ ] **T-004** — Modelo do domínio (plan §3.1, §3.2, §4): `Li
          +do`, `LoteLido`,                                              
       68 +  `Categoria` e `Politica` em `Politica.kt`, `Motivo`, `CampoD
          +espesa`, `Falha`,                                             
       69 +  `CausaErro`, `CausaLote`, `Despesa`, `Referencia`, `Resultad
          +oDespesa`,                                                    
       70 +  `ResultadoLote`. Testes que amarram a ordem dos enums à spec
          + e a fronteira                                                
       71 +  do domínio.                                                 
       72 +  - **Atende:** §8 e AMB-018 (ordem dos motivos); §4.1 e AMB-0
          +21 (ordem dos campos); plan DT-006, DT-009                    
       73 +  - **Aceite:** passam                                        
       74 +    `OrdemMotivosSecao8Test` › `§8 AMB-018 ordem de Motivo é a
          + ordem da lista de elegibilidade da spec`,                    
       75 +    `OrdemCamposSecao41Test` › `§4-1 AMB-021 ordem de CampoDes
          +pesa é a ordem da tabela de despesa da spec`,                 
       76 +    `FronteiraDominioTest` › `DT-009 nenhum arquivo do dominio
          + importa kotlinx ou java io`                                  
       77 +  - **Commit:** —                                             
       78 +                                                              
       79  ## Fase 2 — Regras de negócio
       80  
       27 -- [ ] **T-00N** — <...>                                       
       28 -  - **Atende:** RN-00X, AMB-00Y                               
       29 -  - **Aceite:**                                               
       30 -  - **Commit:**                                               
       81 +Todos os testes desta fase constroem `LoteLido` direto, sem JS
          +ON (plan §6).                                                 
       82  
       83 +- [ ] **T-005** — Validação de lote: período ausente ou de tip
          +o incompatível,                                               
       84 +  início ou fim ausente ou inválido, início posterior ao fim, 
          +despesas não                                                  
       85 +  informadas ou de tipo incompatível, identificador repetido a
          +pós `aparar()`.                                               
       86 +  Todas as causas detectáveis são reportadas.                 
       87 +  - **Atende:** RN-010 (erro de lote), RN-012, AMB-017, AMB-01
          +9, AMB-020 (ii)                                               
       88 +  - **Aceite:** `RN010ValidacaoDeLoteTest` e `RN012Identificad
          +orUnicoTest` passam:                                          
       89 +    `RN-010 AMB-017 início posterior ao fim → lote rejeitado s
          +em despesa processada`,                                       
       90 +    `RN-010 período ausente → lote rejeitado`,                
       91 +    `RN-010 AMB-020 período com tipo incompatível → lote rejei
          +tado`,                                                        
       92 +    `RN-010 AMB-020 relação de despesas com tipo incompatível 
          +→ lote rejeitado`,                                            
       93 +    `RN-010 data de início inválida → lote rejeitado`,        
       94 +    `RN-010 período com início igual ao fim → válido`,        
       95 +    `RN-012 AMB-019 duas despesas com identificador d-006 → lo
          +te rejeitado sem avaliar nenhuma`,                            
       96 +    `RN-010 AMB-019 início posterior ao fim e identificador re
          +petido → as duas causas`,                                     
       97 +    `RN-012 AMB-020 d-006 com espaço à esquerda repete d-006 →
          + lote rejeitado`,                                             
       98 +    `RN-012 d-006 e D-006 são identificadores distintos`,     
       99 +    `RN-012 identificadores d-001 a d-014 distintos → sem erro
          + de lote`                                                     
      100 +  - **Commit:** —                                             
      101 +                                                              
      102 +- [ ] **T-006** — Validação de item, campos: informação obriga
          +tória ausente,                                                
      103 +  tipo incompatível, data inexistente, texto obrigatório vazio
          + após `aparar()`                                              
      104 +  tratado como ausente, valor normalizado menor ou igual a zer
          +o. Todas as                                                   
      105 +  causas, na ordem de `CampoDespesa`.                         
      106 +  - **Atende:** RN-010 (erro de item), RN-011, AMB-012 (dissol
          +vida), AMB-014, AMB-015 (fornecedor obrigatório), AMB-017, AMB
          +-021                                                          
      107 +  - **Aceite:** `RN010ValidacaoDeItemTest` passa:             
      108 +    `RN-010 AMB-012 d-009 −45,00 → erro de item com causa valo
          +r não positivo`,                                              
      109 +    `RN-010 AMB-017 despesa sem o campo de valor → erro de ite
          +m e as demais processadas`,                                   
      110 +    `RN-010 despesa de valor 0,00 → erro de item`,            
      111 +    `RN-011 AMB-014 0,004 normaliza para 0,00 e é erro de item
          +`,                                                            
      112 +    `RN-011 −45,005 é erro de item com valor considerado −45,0
          +1`,                                                           
      113 +    `RN-010 despesa sem indicação de nota fiscal → erro de ite
          +m e não SEM_NOTA_FISCAL`,                                     
      114 +    `RN-010 fornecedor só com espaços, tabulação ou espaço não
          + separável → erro de item ausente`,                           
      115 +    `RN-010 AMB-015 fornecedor com tipo incompatível → erro de
          + item`,                                                       
      116 +    `RN-010 data 2026-02-30 → erro de item data inexistente`, 
      117 +    `RN-010 AMB-021 sem fornecedor e valor −5,00 → causas forn
          +ecedor ausente e valor não positivo nessa ordem`,             
      118 +    `RN-010 erro de item em uma despesa não altera o resultado
          + das demais`                                                  
      119 +  - **Commit:** —                                             
      120 +                                                              
      121 +- [ ] **T-007** — Validação de item, identidade: identificador
          + aparado,                                                     
      122 +  `Referencia.Posicao` (contada a partir de 1) quando o identi
          +ficador falta ou                                              
      123 +  tem tipo incompatível, elemento que não é despesa, ausências
          + que não contam                                               
      124 +  como repetição.                                             
      125 +  - **Atende:** RN-010, RN-012, AMB-020 (i, iii), AMB-021     
      126 +  - **Aceite:** `RN010IdentidadeDoItemTest` passa:            
      127 +    `RN-010 AMB-020 despesa sem identificador na terceira posi
          +ção → erro identificado pela posição 3`,                      
      128 +    `RN-012 AMB-020 duas despesas sem identificador → ambas er
          +ro de item e lote não rejeitado`,                             
      129 +    `RN-010 AMB-020 elemento que não é despesa → erro de item 
          +na sua posição com causa única`,                              
      130 +    `AMB-021 sem identificador e data 2026-02-30 → causas iden
          +tificador ausente e data inexistente nessa ordem`,            
      131 +    `RN-012 AMB-020 identificador com espaços nas extremidades
          + é considerado aparado`                                       
      132 +  - **Commit:** —                                             
      133 +                                                              
      134 +- [ ] **T-008** — `Motor.avaliar` com a etapa 2 (valor) da §8:
          + limite nominal                                               
      135 +  por despesa via `limiteAplicavel`, reembolso parcial com exc
          +edente.                                                       
      136 +  - **Atende:** RN-001, RN-002, RN-011, AMB-001, AMB-002, AMB-
          +003 (dissolvida), AMB-007, AMB-009                            
      137 +  - **Aceite:** `RN001LimitePorDespesaTest` e `RN002ReembolsoP
          +arcialTest` passam,                                           
      138 +    mais os aceites da RN-011 que passam a ser verificáveis:  
      139 +    `RN-001 AMB-001 d-001 alimentacao 72,50 → reembolso 60,00 
          +parcial`,                                                     
      140 +    `RN-001 AMB-001 d-002 alimentacao 38,00 → reembolso 38,00 
          +integral`,                                                    
      141 +    `RN-001 AMB-001 dia 03-07 totaliza 98,00 em alimentacao`, 
      142 +    `RN-001 AMB-003 inverter a ordem de d-001 e d-002 não alte
          +ra os valores`,                                               
      143 +    `RN-001 AMB-001 AMB-003 três alimentações de 59,00 no mesm
          +o dia → 59,00 cada e 177,00 no total`,                        
      144 +    `RN-001 AMB-007 AMB-009 d-010 hospedagem 480,00 → limite 2
          +50,00, reembolso 250,00 parcial, excedente 230,00`,           
      145 +    `AMB-007 d-011 no mesmo fornecedor de d-010 mantém limite 
          +60,00`,                                                       
      146 +    `AMB-009 hospedagem 200,00 com NF → reembolso 200,00 integ
          +ral`,                                                         
      147 +    `RN-002 AMB-002 d-001 72,50 → excedente 12,50`,           
      148 +    `RN-002 AMB-002 alimentacao de 60,00 → reembolso 60,00 int
          +egral`,                                                       
      149 +    `RN-002 AMB-002 alimentacao de 60,01 → reembolso 60,00 par
          +cial e excedente 0,01`,                                       
      150 +    `RN-002 transporte urbano de 90,00 → reembolso 80,00 parci
          +al e excedente 10,00`,                                        
      151 +    `RN-011 d-011 33,333 → reembolso 33,33 integral`,         
      152 +    `RN-011 alimentacao de 60,004 → reembolso 60,00 integral` 
      153 +  - **Commit:** —                                             
      154 +                                                              
      155 +- [ ] **T-009** — Etapa 1.1 da §8: pertinência ao período, bor
          +das inclusivas,                                               
      156 +  e recusa com `FORA_DA_COMPETENCIA` sem avaliação contra o li
          +mite.                                                         
      157 +  - **Atende:** RN-005, RN-006, AMB-008, AMB-010, AMB-016     
      158 +  - **Aceite:** `RN005PeriodoTest` e `RN006ForaDoPeriodoTest` 
          +passam:                                                       
      159 +    `RN-005 AMB-016 período do lote de exemplo é de 2026-07-01
          + a 2026-07-31 inclusive`,                                     
      160 +    `RN-005 AMB-016 d-014 em 2026-07-31 igual à data de fim → 
          +dentro do período`,                                           
      161 +    `RN-005 AMB-016 despesa em 2026-07-01 igual à data de iníc
          +io → dentro do período`,                                      
      162 +    `RN-005 AMB-016 despesa em 2026-08-01 → fora do período`, 
      163 +    `RN-005 AMB-008 d-008 em 2026-04-15 → fora do período`,   
      164 +    `RN-005 d-001 a d-014 exceto d-008 e d-009 → dentro do per
          +íodo`,                                                        
      165 +    `RN-006 AMB-010 d-008 alimentacao 41,00 com NF → recusada 
          +com FORA_DA_COMPETENCIA sem limite aplicado`,                 
      166 +    `RN-006 despesa em 2026-08-01 com NF → recusada com FORA_D
          +A_COMPETENCIA`                                                
      167 +  - **Commit:** —                                             
      168 +                                                              
      169 +- [ ] **T-010** — Normalização de categoria e fornecedor (`apa
          +rar()` +                                                      
      170 +  `lowercase()`) na construção de `Despesa`; etapa 1.2 da §8: 
          +elegibilidade                                                 
      171 +  da categoria.                                               
      172 +  - **Atende:** RN-008, AMB-013, AMB-017                      
      173 +  - **Aceite:** `RN008CategoriaTest` passa:                   
      174 +    `RN-008 AMB-013 d-014 ALIMENTACAO 61,00 → categoria alimen
          +tacao, reembolso 60,00 parcial`,                              
      175 +    `RN-008 AMB-013 AMB-017 d-005 coworking → recusada com CAT
          +EGORIA_INELEGIVEL e não erro`,                                
      176 +    `RN-008 AMB-013 Alimentacao, ALIMENTACAO e alimentacao com
          + espaços → a mesma categoria`,                                
      177 +    `RN-008 AMB-013 alimentação com cedilha e til → recusada c
          +om CATEGORIA_INELEGIVEL`,                                     
      178 +    `RN-008 AMB-013 transporte-urbano → recusada com CATEGORIA
          +_INELEGIVEL`,                                                 
      179 +    `RN-008 AMB-013 Bistro Central, BISTRO CENTRAL e Bistro Ce
          +ntral com espaços → o mesmo fornecedor e Bistrô Central é dist
          +into`                                                         
      180 +  - **Commit:** —                                             
      181 +                                                              
      182 +- [ ] **T-011** — Etapa 1.3 da §8: exigência de nota fiscal so
          +bre o valor                                                   
      183 +  normalizado (`Politica.LIMIAR_NOTA_FISCAL`) e recusa com `SE
          +M_NOTA_FISCAL`.                                               
      184 +  - **Atende:** RN-003, RN-004, RN-011, AMB-004, AMB-005, AMB-
          +006, AMB-014                                                  
      185 +  - **Aceite:** `RN003NotaFiscalTest` e `RN004SemNotaFiscalTes
          +t` passam:                                                    
      186 +    `RN-003 AMB-004 d-003 transporte 100,00 sem NF → nota não 
          +exigida, reembolso 80,00 parcial`,                            
      187 +    `RN-003 AMB-004 AMB-006 d-004 transporte 100,01 sem NF → n
          +ota exigida e ausente`,                                       
      188 +    `RN-003 AMB-004 transporte 100,01 com NF → nota exigida e 
          +presente, reembolso 80,00 parcial`,                           
      189 +    `RN-003 RN-011 AMB-014 100,004 → nota não exigida e 100,00
          +5 → nota exigida`,                                            
      190 +    `RN-003 alimentacao 50,00 sem NF → nota não exigida, reemb
          +olso 50,00 integral`,                                         
      191 +    `RN-003 AMB-006 alimentacao 500,00 sem NF → nota exigida e
          + ausente apesar do teto de 60,00`,                            
      192 +    `RN-003 AMB-006 d-013 hospedagem 690,00 sem NF → nota exig
          +ida e ausente`,                                               
      193 +    `RN-004 AMB-005 d-004 → reembolso 0,00 recusada com SEM_NO
          +TA_FISCAL`,                                                   
      194 +    `RN-004 AMB-005 d-013 → recusada com SEM_NOTA_FISCAL`,    
      195 +    `RN-004 AMB-005 alimentacao 500,00 sem NF → recusada com S
          +EM_NOTA_FISCAL`,                                              
      196 +    `RN-004 AMB-005 hospedagem 200,00 sem NF → recusada com SE
          +M_NOTA_FISCAL apesar de estar abaixo do limite`,              
      197 +    `RN-004 d-003 e transporte 100,01 com NF → regra não se ap
          +lica, reembolso 80,00 parcial`                                
      198 +  - **Commit:** —                                             
      199 +                                                              
      200 +- [ ] **T-012** — Lista de motivos: todas as regras de elegibi
          +lidade avaliadas                                              
      201 +  sem interrupção, lista ordenada por `Motivo.ordinal`, recusa
          + sem limite,                                                  
      202 +  erro com lista vazia.                                       
      203 +  - **Atende:** RN-007, AMB-017, AMB-018; §8 (recusada não é a
          +valiada contra o limite)                                      
      204 +  - **Aceite:** `RN007ListaDeMotivosTest` passa:              
      205 +    `RN-007 AMB-018 150,00 sem NF fora do período → motivos FO
          +RA_DA_COMPETENCIA e SEM_NOTA_FISCAL nessa ordem`,             
      206 +    `RN-007 AMB-018 d-008 → motivos FORA_DA_COMPETENCIA`,     
      207 +    `RN-007 AMB-018 d-004 → motivos SEM_NOTA_FISCAL`,         
      208 +    `RN-007 d-002 sem violação → lista de motivos vazia`,     
      209 +    `RN-007 d-001 parcial → lista de motivos vazia`,          
      210 +    `RN-007 AMB-017 d-009 erro → lista de motivos vazia`,     
      211 +    `§8 despesa recusada não tem limite aplicado`             
      212 +  - **Commit:** —                                             
      213 +                                                              
      214 +- [ ] **T-013** — Etapa 1.4 da §8: duplicidade em duas passada
          +s, só entre                                                   
      215 +  despesas sem motivo, com comparador de identificadores por c
          +ode point                                                     
      216 +  (DT-007, R-07).                                             
      217 +  - **Atende:** RN-009, RN-011, AMB-009, AMB-010, AMB-011, AMB
          +-013 (fornecedor), AMB-014, AMB-017                           
      218 +  - **Aceite:** `RN009DuplicidadeTest` passa:                 
      219 +    `RN-009 AMB-011 d-006 e d-007 → d-006 integral e d-007 rec
          +usada com DUPLICATA mantida d-006`,                           
      220 +    `RN-009 AMB-011 inverter a ordem de d-006 e d-007 → os mes
          +mos resultados`,                                              
      221 +    `RN-009 AMB-011 iguais até 100,00 uma com NF e outra sem →
          + mantém a de menor identificador`,                            
      222 +    `RN-009 hospedagens de 200,00 com a menor sem NF → a sem N
          +F recusada SEM_NOTA_FISCAL e a outra integral sem DUPLICATA`, 
      223 +    `RN-009 AMB-010 duas iguais fora do período → ambas FORA_D
          +A_COMPETENCIA e nenhuma DUPLICATA`,                           
      224 +    `RN-009 AMB-009 duas diárias de 240,00 em 14-07 e 15-07 → 
          +não são duplicatas`,                                          
      225 +    `RN-009 AMB-009 as mesmas duas diárias ambas em 14-07 → gr
          +upo de duplicatas`,                                           
      226 +    `RN-009 AMB-011 d-9 e d-10 → d-10 mantida pela ordem lexic
          +ográfica`,                                                    
      227 +    `RN-009 AMB-013 Bistro Central e BISTRO CENTRAL → grupo de
          + duplicatas`,                                                 
      228 +    `RN-009 AMB-011 d-006 e D-006 → mantém D-006`,            
      229 +    `RN-009 identificador fora do BMP é comparado por code poi
          +nt`,                                                          
      230 +    `RN-011 AMB-014 54,90 e 54,904 → grupo de duplicatas`,    
      231 +    `RN-009 AMB-017 despesa com erro de item não integra grupo
          +`                                                             
      232 +  - **Commit:** —                                             
      233 +                                                              
      234 +- [ ] **T-014** — Consolidado do lote: período avaliado, total
          + reembolsado,                                                 
      235 +  quantidades por classificação e total; lote sem despesas.   
      236 +  - **Atende:** §4.2 (consolidado), RN-010, AMB-010, AMB-014  
      237 +  - **Aceite:** `ConsolidadoTest` passa:                      
      238 +    `§4-2 AMB-014 total reembolsado é a soma dos reembolsos do
          +s itens`,                                                     
      239 +    `§4-2 AMB-010 quantidades por classificação somam o número
          + de despesas da entrada`,                                     
      240 +    `§4-2 consolidado traz o período avaliado`,               
      241 +    `RN-010 lote sem despesas → total 0,00 e zero em todas as 
          +classificações`                                               
      242 +  - **Commit:** —                                             
      243 +                                                              
      244  ## Fase 3 — Casos de borda
      245  
       34 -- [ ] **T-00N** — <...>                                       
      246 +- [ ] **T-015** — Um teste por linha da tabela da §7 (20 linha
          +s), nomeado com                                               
      247 +  as RNs da coluna "Regra".                                   
      248 +  - **Atende:** §7                                            
      249 +  - **Aceite:** `Secao7CasosDeBordaTest` passa, com os 20 test
          +es:                                                           
      250 +    `§7 RN-001 RN-002 d-001 alimentacao 72,50 → 60,00 parcial,
          + excedente 12,50`,                                            
      251 +    `§7 RN-001 d-002 38,00 → integral e d-001 no mesmo dia não
          + reduz o valor`,                                              
      252 +    `§7 RN-002 alimentacao 61,00 → 60,00 parcial, excedente 1,
          +00`,                                                          
      253 +    `§7 RN-002 alimentacao 60,00 → 60,00 integral`,           
      254 +    `§7 RN-002 RN-003 d-003 transporte 100,00 sem NF → nota nã
          +o exigida, 80,00 parcial`,                                    
      255 +    `§7 RN-002 RN-003 transporte 100,01 com NF → nota exigida 
          +e presente, 80,00 parcial`,                                   
      256 +    `§7 RN-003 RN-004 d-004 transporte 100,01 sem NF → recusad
          +a SEM_NOTA_FISCAL`,                                           
      257 +    `§7 RN-003 RN-004 alimentacao 500,00 sem NF → recusada SEM
          +_NOTA_FISCAL`,                                                
      258 +    `§7 RN-003 RN-004 d-013 hospedagem 690,00 sem NF → recusad
          +a SEM_NOTA_FISCAL`,                                           
      259 +    `§7 RN-001 RN-002 RN-003 d-010 hospedagem 480,00 com NF → 
          +limite 250,00, parcial`,                                      
      260 +    `§7 RN-004 hospedagem 200,00 sem NF → recusada SEM_NOTA_FI
          +SCAL`,                                                        
      261 +    `§7 RN-005 RN-006 d-008 em 2026-04-15 → recusada FORA_DA_C
          +OMPETENCIA`,                                                  
      262 +    `§7 RN-004 RN-006 RN-007 150,00 sem NF fora do período → F
          +ORA_DA_COMPETENCIA e SEM_NOTA_FISCAL`,                        
      263 +    `§7 RN-001 RN-002 RN-005 RN-008 d-014 ALIMENTACAO em 2026-
          +07-31 → 60,00 parcial`,                                       
      264 +    `§7 RN-009 d-006 → mantida, 54,90 integral`,              
      265 +    `§7 RN-009 d-007 → recusada DUPLICATA independentemente da
          + ordem`,                                                      
      266 +    `§7 RN-010 d-009 −45,00 → erro`,                          
      267 +    `§7 RN-011 RN-001 d-011 33,333 → 33,33 integral`,         
      268 +    `§7 RN-008 d-005 coworking → recusada CATEGORIA_INELEGIVEL
          + e não erro`,                                                 
      269 +    `§7 RN-001 RN-002 d-012 sábado 47,20 → integral`          
      270 +  - **Commit:** —                                             
      271  
      272 +- [ ] **T-016** — `RastreabilidadeTest` (plan §6, versão 1.1):
          + lê `spec.md` e                                               
      273 +  os nomes de teste em `tests/` (fora do pacote `rastreabilida
          +de`, para que os                                              
      274 +  IDs citados no próprio teste não contem) e falha o build qua
          +ndo falta                                                     
      275 +  cobertura. A isenção das AMB dissolvidas é uma lista explíci
          +ta no teste                                                   
      276 +  (`AMB-003`, `AMB-012`), verificada contra a spec, de modo qu
          +e uma AMB nova                                                
      277 +  nunca entre na isenção por descuido.                        
      278 +  - **Atende:** RN-001 a RN-012, AMB-001 a AMB-021, §7 (guarda
          + do processo)                                                 
      279 +  - **Aceite:** `RastreabilidadeTest` passa:                  
      280 +    `toda RN da spec tem ao menos um teste com o seu ID`,     
      281 +    `toda AMB não dissolvida da spec tem ao menos um teste com
          + o seu ID`,                                                   
      282 +    `cada AMB da lista de isenção existe na spec e está marcad
          +a como dissolvida`,                                           
      283 +    `a quantidade de testes §7 é igual à de linhas da tabela d
          +a §7`                                                         
      284 +  - **Commit:** —                                             
      285 +                                                              
      286  ## Fase 4 — Saída e CLI
      287  
       38 -- [ ] **T-00N** — <...>                                       
      288 +> **Requisitos observáveis só na fronteira (T-017 e T-018).** 
          +A AMB-008, a                                                  
      289 +> AMB-009 e a AMB-015 determinam que a descrição da despesa e 
          +os campos do                                                  
      290 +> colaborador não alteram nenhum resultado. No domínio, isso s
          +e cumpre por                                                  
      291 +> construção: o adaptador `entrada` não lê esses campos (plan 
          +§2), e eles não                                               
      292 +> existem em `LoteLido`. Um teste de domínio sobre eles seria 
          +vazio. Por isso                                               
      293 +> os testes que verificam essas AMB estão no adaptador de entr
          +ada (T-017) e na                                              
      294 +> saída (T-018), onde os campos existem. Pelo mesmo motivo fic
          +am no T-017 o                                                 
      295 +> teste do rótulo de competência divergente (AMB-016) e o acei
          +te da RN-009                                                  
      296 +> sobre despesas com descrições diferentes (AMB-011): os dois 
          +dependem de                                                   
      297 +> campos que não chegam ao domínio.                           
      298  
      299 +- [ ] **T-017** — Adaptador `entrada`: JSON → `LoteLido` (DT-0
          +04).                                                          
      300 +  `ENTRADA_ILEGIVEL` como causa única para JSON inválido ou ra
          +iz que não é                                                  
      301 +  lote; `BigDecimal` a partir do texto cru; data com `Resolver
          +Style.STRICT`;                                                
      302 +  `null` como `Ausente`; campos sem regra não lidos.          
      303 +  - **Atende:** RN-009, RN-010, RN-011, AMB-008, AMB-009, AMB-
          +011, AMB-015, AMB-016, AMB-020                                
      304 +  - **Aceite:** `EntradaJsonTest` passa:                      
      305 +    `RN-010 JSON sintaticamente inválido → rejeitado com causa
          + única ENTRADA_ILEGIVEL`,                                     
      306 +    `RN-010 raiz JSON que é uma lista ou um número → rejeitado
          + com causa única ENTRADA_ILEGIVEL`,                           
      307 +    `RN-011 33,333 é lido do texto cru sem passar por Double`,
      308 +    `RN-011 100,005 é lido do texto cru e normaliza para 100,0
          +1`,                                                           
      309 +    `RN-010 valor 72,50 escrito como string → tipo incompatíve
          +l`,                                                           
      310 +    `RN-010 data 2026-02-30 → data inexistente`,              
      311 +    `RN-010 campo null → ausente`,                            
      312 +    `RN-010 AMB-020 período e relação de despesas com tipo inc
          +ompatível → lote rejeitado`,                                  
      313 +    `RN-010 AMB-020 elemento da relação que não é objeto → err
          +o de item`,                                                   
      314 +    `RN-010 AMB-015 fornecedor informado como número → erro de
          + item tipo incompatível`,                                     
      315 +    `RN-010 AMB-015 descrição informada como número → processa
          +da normalmente`,                                              
      316 +    `RN-010 AMB-015 centro de custo e rótulo com tipo incompat
          +ível → processado normalmente`,                               
      317 +    `RN-010 campo não previsto é ignorado`,                   
      318 +    `AMB-015 lotes que diferem só no centro de custo → resulta
          +dos idênticos`,                                               
      319 +    `AMB-015 lote sem centro de custo e sem rótulo com despesa
          + sem descrição → processado normalmente`,                     
      320 +    `AMB-016 rótulo de competência divergente do par de datas 
          +não impede o processamento`,                                  
      321 +    `AMB-008 descrição de d-008 não altera o resultado FORA_DA
          +_COMPETENCIA`,                                                
      322 +    `AMB-009 hospedagem 400,00 descrita como 1 diaria ou 2 dia
          +rias → mesmo resultado 250,00 parcial`,                       
      323 +    `RN-009 AMB-011 iguais no critério com descrições diferent
          +es → grupo de duplicatas`                                     
      324 +  - **Commit:** —                                             
      325 +                                                              
      326 +- [ ] **T-018** — Adaptador `saida`: `ResultadoLote` → JSON (p
          +lan §3.3).                                                    
      327 +  Campos não aplicáveis omitidos, `motivos` sempre presente, d
          +inheiro com                                                   
      328 +  duas casas via `toPlainString()`, `posicao` no lugar de `id`
          +, frase de                                                    
      329 +  justificativa montada só a partir dos campos do resultado.  
      330 +  - **Atende:** §4.2, RN-012, AMB-015, AMB-020                
      331 +  - **Aceite:** `SaidaJsonTest` passa:                        
      332 +    `§4-2 parcial traz limite_aplicado e excedente e recusada 
          +não traz limite_aplicado`,                                    
      333 +    `§4-2 dinheiro sai como número com duas casas sem passar p
          +or Double`,                                                   
      334 +    `§4-2 motivos aparece sempre e campos não aplicáveis são o
          +mitidos`,                                                     
      335 +    `§4-2 duplicata traz despesa_mantida`,                    
      336 +    `§4-2 erro traz causas_erro na ordem da §4-1`,            
      337 +    `§4-2 lote rejeitado traz todas as causas sem resultado po
          +r despesa nem consolidado`,                                   
      338 +    `§4-2 toda despesa traz justificativa não vazia`,         
      339 +    `RN-012 AMB-020 identificador com espaços nas extremidades
          + sai aparado`,                                                
      340 +    `AMB-020 despesa sem identificador sai com posicao no luga
          +r de id`,                                                     
      341 +    `AMB-015 saída não contém colaborador, rótulo de competênc
          +ia nem descrição`                                             
      342 +  - **Commit:** —                                             
      343 +                                                              
      344 +- [ ] **T-019** — CLI `calcular --input X --output Y` com pars
          +ing manual e                                                  
      345 +  exit codes (DT-008).                                        
      346 +  - **Atende:** RN-010 (entrada que não pode ser lida); plan D
          +T-008                                                         
      347 +  - **Aceite:** `CliTest` passa:                              
      348 +    `DT-008 lote avaliado sai com exit 0`,                    
      349 +    `DT-008 lote rejeitado sai com exit 1 e JSON escrito`,    
      350 +    `DT-008 subcomando ou flag ausente sai com exit 2 sem escr
          +ever nada`,                                                   
      351 +    `RN-010 DT-008 arquivo de entrada inexistente → rejeitado 
          +com ENTRADA_ILEGIVEL e exit 1`,                               
      352 +    `DT-008 falha ao escrever a saída → exit 3 com mensagem no
          + stderr`                                                      
      353 +  - **Commit:** —                                             
      354 +                                                              
      355 +- [ ] **T-020** — Ponta a ponta: `main` com `exemplos/despesas
          +-exemplo.json`,                                               
      356 +  comparado com a tabela da §9; determinismo com a ordem das d
          +espesas                                                       
      357 +  invertida.                                                  
      358 +  - **Atende:** §9, RN-001, RN-009                            
      359 +  - **Aceite:** `Secao9CriteriosDeAceiteTest` passa:          
      360 +    `§9 lote de exemplo produz o resultado da tabela da §9 des
          +pesa a despesa`,                                              
      361 +    `§9 lote de exemplo produz total 623,43 com 4 integrais, 4
          + parciais, 5 recusadas e 1 erro`,                             
      362 +    `§9 cada despesa aparece exatamente uma vez na ordem da en
          +trada`,                                                       
      363 +    `§9 a mesma entrada produz sempre a mesma saída`,         
      364 +    `§9 RN-001 RN-009 inverter a ordem do lote de exemplo não 
          +altera nenhum resultado`                                      
      365 +  - **Commit:** —                                             
      366 +                                                              
      367  ---
      368  
      369  ## Fase 5 — Envelope (criar no Dia 2)
     ...
      378  Preencha ao fechar cada fase. É a sua própria checagem de rast
           reabilidade — e é
      379  exatamente a matriz que a correção vai montar.
      380  
      381 +Quando uma linha tem mais de uma task, a primeira é a que impl
          +ementa a decisão,                                             
      382 +e as seguintes acrescentam aceites que dependem de outras regr
          +as (ver                                                       
      383 +Convenções). O `RastreabilidadeTest` (T-016) verifica automati
          +camente que todo                                              
      384 +ID das duas tabelas abaixo, exceto as AMB dissolvidas, aparece
          + no nome de ao                                                
      385 +menos um teste.                                               
      386 +                                                              
      387 +### Regras de negócio                                         
      388 +                                                              
      389  | Regra da spec | Task | Teste |
      390  |---|---|---|
       56 -| RN-001 | T-00X | `<nome do teste>` |                        
       57 -| RN-002 | | |                                                
       58 -| AMB-001 | | |                                               
      391 +| RN-001 | T-008 | `RN-001 AMB-001 d-001 alimentacao 72,50 → r
          +eembolso 60,00 parcial`; `RN-001 AMB-001 AMB-003 três alimenta
          +ções de 59,00 no mesmo dia → 59,00 cada e 177,00 no total`; `§
          +9 RN-001 RN-009 inverter a ordem do lote de exemplo não altera
          + nenhum resultado` (T-020) |                                  
      392 +| RN-002 | T-008 | `RN-002 AMB-002 alimentacao de 60,01 → reem
          +bolso 60,00 parcial e excedente 0,01`; `RN-002 transporte urba
          +no de 90,00 → reembolso 80,00 parcial e excedente 10,00` |    
      393 +| RN-003 | T-011 | `RN-003 AMB-004 d-003 transporte 100,00 sem
          + NF → nota não exigida, reembolso 80,00 parcial`; `RN-003 RN-0
          +11 AMB-014 100,004 → nota não exigida e 100,005 → nota exigida
          +` |                                                           
      394 +| RN-004 | T-011 | `RN-004 AMB-005 d-004 → reembolso 0,00 recu
          +sada com SEM_NOTA_FISCAL`; `RN-004 AMB-005 hospedagem 200,00 s
          +em NF → recusada com SEM_NOTA_FISCAL apesar de estar abaixo do
          + limite` |                                                    
      395 +| RN-005 | T-009 | `RN-005 AMB-016 d-014 em 2026-07-31 igual à
          + data de fim → dentro do período`; `RN-005 AMB-016 despesa em 
          +2026-07-01 igual à data de início → dentro do período` |      
      396 +| RN-006 | T-009 | `RN-006 AMB-010 d-008 alimentacao 41,00 com
          + NF → recusada com FORA_DA_COMPETENCIA sem limite aplicado` | 
      397 +| RN-007 | T-012 | `RN-007 AMB-018 150,00 sem NF fora do perío
          +do → motivos FORA_DA_COMPETENCIA e SEM_NOTA_FISCAL nessa ordem
          +`; `RN-007 AMB-017 d-009 erro → lista de motivos vazia` |     
      398 +| RN-008 | T-010 | `RN-008 AMB-013 AMB-017 d-005 coworking → r
          +ecusada com CATEGORIA_INELEGIVEL e não erro`; `RN-008 AMB-013 
          +d-014 ALIMENTACAO 61,00 → categoria alimentacao, reembolso 60,
          +00 parcial` |                                                 
      399 +| RN-009 | T-013, T-017 | `RN-009 AMB-011 d-006 e d-007 → d-00
          +6 integral e d-007 recusada com DUPLICATA mantida d-006`; `RN-
          +009 identificador fora do BMP é comparado por code point`; `RN
          +-009 AMB-011 iguais no critério com descrições diferentes → gr
          +upo de duplicatas` (T-017) |                                  
      400 +| RN-010 | T-005, T-006, T-007, T-014, T-017, T-019 | `RN-010 
          +AMB-017 início posterior ao fim → lote rejeitado sem despesa p
          +rocessada` (T-005); `RN-010 AMB-021 sem fornecedor e valor −5,
          +00 → causas fornecedor ausente e valor não positivo nessa orde
          +m` (T-006); `RN-010 AMB-020 despesa sem identificador na terce
          +ira posição → erro identificado pela posição 3` (T-007); `RN-0
          +10 lote sem despesas → total 0,00 e zero em todas as classific
          +ações` (T-014); `RN-010 raiz JSON que é uma lista ou um número
          + → rejeitado com causa única ENTRADA_ILEGIVEL` (T-017); `RN-01
          +0 DT-008 arquivo de entrada inexistente → rejeitado com ENTRAD
          +A_ILEGIVEL e exit 1` (T-019) |                                
      401 +| RN-011 | T-002, T-006, T-008, T-011, T-013, T-017 | `RN-011 
          +AMB-014 100,005 normaliza para 100,01 e 100,004 para 100,00` (
          +T-002); `RN-011 AMB-014 0,004 normaliza para 0,00 e é erro de 
          +item` (T-006); `RN-011 d-011 33,333 → reembolso 33,33 integral
          +` (T-008); `RN-011 AMB-014 54,90 e 54,904 → grupo de duplicata
          +s` (T-013); `RN-011 33,333 é lido do texto cru sem passar por 
          +Double` (T-017) |                                             
      402 +| RN-012 | T-005, T-007, T-018 | `RN-012 AMB-019 duas despesas
          + com identificador d-006 → lote rejeitado sem avaliar nenhuma`
          +; `RN-012 AMB-020 d-006 com espaço à esquerda repete d-006 → l
          +ote rejeitado` (T-005); `RN-012 AMB-020 duas despesas sem iden
          +tificador → ambas erro de item e lote não rejeitado` (T-007); 
          +`RN-012 AMB-020 identificador com espaços nas extremidades sai
          + aparado` (T-018) |                                           
      403 +                                                              
      404 +### Ambiguidades                                              
      405 +                                                              
      406 +| Regra da spec | Task | Teste |                              
      407 +|---|---|---|                                                 
      408 +| AMB-001 | T-008 | `RN-001 AMB-001 AMB-003 três alimentações 
          +de 59,00 no mesmo dia → 59,00 cada e 177,00 no total`; `RN-001
          + AMB-001 dia 03-07 totaliza 98,00 em alimentacao` |           
      409 +| AMB-002 | T-008 | `RN-002 AMB-002 alimentacao de 60,00 → ree
          +mbolso 60,00 integral`; `RN-002 AMB-002 alimentacao de 60,01 →
          + reembolso 60,00 parcial e excedente 0,01` |                  
      410 +| AMB-003 — **dissolvida** pela AMB-001; sem decisão própria, 
          +isenta no `RastreabilidadeTest` | T-008 (teste que demonstra a
          + dissolução) | `RN-001 AMB-003 inverter a ordem de d-001 e d-0
          +02 não altera os valores`; `RN-001 AMB-001 AMB-003 três alimen
          +tações de 59,00 no mesmo dia → 59,00 cada e 177,00 no total` |
      411 +| AMB-004 | T-011 | `RN-003 AMB-004 d-003 transporte 100,00 se
          +m NF → nota não exigida, reembolso 80,00 parcial`; `RN-003 AMB
          +-004 transporte 100,01 com NF → nota exigida e presente, reemb
          +olso 80,00 parcial` |                                         
      412 +| AMB-005 | T-011 | `RN-004 AMB-005 hospedagem 200,00 sem NF →
          + recusada com SEM_NOTA_FISCAL apesar de estar abaixo do limite
          +`; `RN-004 AMB-005 alimentacao 500,00 sem NF → recusada com SE
          +M_NOTA_FISCAL` |                                              
      413 +| AMB-006 | T-011 | `RN-003 AMB-006 alimentacao 500,00 sem NF 
          +→ nota exigida e ausente apesar do teto de 60,00`; `RN-003 AMB
          +-004 AMB-006 d-004 transporte 100,01 sem NF → nota exigida e a
          +usente` |                                                     
      414 +| AMB-007 | T-008 | `RN-001 AMB-007 AMB-009 d-010 hospedagem 4
          +80,00 → limite 250,00, reembolso 250,00 parcial, excedente 230
          +,00`; `AMB-007 d-011 no mesmo fornecedor de d-010 mantém limit
          +e 60,00` |                                                    
      415 +| AMB-008 | T-009, T-017 (fronteira) | `RN-005 AMB-008 d-008 e
          +m 2026-04-15 → fora do período`; `AMB-008 descrição de d-008 n
          +ão altera o resultado FORA_DA_COMPETENCIA` (T-017) |          
      416 +| AMB-009 | T-008, T-013, T-017 (fronteira) | `RN-001 AMB-007 
          +AMB-009 d-010 hospedagem 480,00 → limite 250,00, …`; `RN-009 A
          +MB-009 duas diárias de 240,00 em 14-07 e 15-07 → não são dupli
          +catas` (T-013); `AMB-009 hospedagem 400,00 descrita como 1 dia
          +ria ou 2 diarias → mesmo resultado 250,00 parcial` (T-017) |  
      417 +| AMB-010 | T-009, T-013, T-014 | `RN-006 AMB-010 d-008 alimen
          +tacao 41,00 com NF → recusada com FORA_DA_COMPETENCIA sem limi
          +te aplicado`; `RN-009 AMB-010 duas iguais fora do período → am
          +bas FORA_DA_COMPETENCIA e nenhuma DUPLICATA` (T-013); `§4-2 AM
          +B-010 quantidades por classificação somam o número de despesas
          + da entrada` (T-014) |                                        
      418 +| AMB-011 | T-013, T-017 (fronteira) | `RN-009 AMB-011 inverte
          +r a ordem de d-006 e d-007 → os mesmos resultados`; `RN-009 AM
          +B-011 d-9 e d-10 → d-10 mantida pela ordem lexicográfica`; `RN
          +-009 AMB-011 iguais no critério com descrições diferentes → gr
          +upo de duplicatas` (T-017) |                                  
      419 +| AMB-012 — **dissolvida** pela AMB-017; sem decisão própria, 
          +isenta no `RastreabilidadeTest` | T-006 (teste que demonstra a
          + dissolução) | `RN-010 AMB-012 d-009 −45,00 → erro de item com
          + causa valor não positivo` |                                  
      420 +| AMB-013 | T-010, T-013 | `RN-008 AMB-013 alimentação com ced
          +ilha e til → recusada com CATEGORIA_INELEGIVEL`; `RN-008 AMB-0
          +13 Bistro Central, BISTRO CENTRAL e Bistro Central com espaços
          + → o mesmo fornecedor e Bistrô Central é distinto`; `RN-009 AM
          +B-013 Bistro Central e BISTRO CENTRAL → grupo de duplicatas` (
          +T-013) |                                                      
      421 +| AMB-014 | T-002, T-006, T-011, T-013, T-014 | `RN-011 AMB-01
          +4 33,333 normaliza para 33,33` (T-002); `RN-011 AMB-014 0,004 
          +normaliza para 0,00 e é erro de item` (T-006); `RN-003 RN-011 
          +AMB-014 100,004 → nota não exigida e 100,005 → nota exigida` (
          +T-011); `§4-2 AMB-014 total reembolsado é a soma dos reembolso
          +s dos itens` (T-014) |                                        
      422 +| AMB-015 | T-006, T-017 (fronteira), T-018 (fronteira) | `RN-
          +010 AMB-015 fornecedor com tipo incompatível → erro de item` (
          +T-006); `AMB-015 lotes que diferem só no centro de custo → res
          +ultados idênticos`; `RN-010 AMB-015 descrição informada como n
          +úmero → processada normalmente` (T-017); `AMB-015 saída não co
          +ntém colaborador, rótulo de competência nem descrição` (T-018)
          + |                                                            
      423 +| AMB-016 | T-009, T-017 (fronteira) | `RN-005 AMB-016 período
          + do lote de exemplo é de 2026-07-01 a 2026-07-31 inclusive`; `
          +RN-005 AMB-016 despesa em 2026-08-01 → fora do período`; `AMB-
          +016 rótulo de competência divergente do par de datas não imped
          +e o processamento` (T-017) |                                  
      424 +| AMB-017 | T-005, T-006, T-010, T-012, T-013 | `RN-010 AMB-01
          +7 início posterior ao fim → lote rejeitado sem despesa process
          +ada` (T-005); `RN-010 AMB-017 despesa sem o campo de valor → e
          +rro de item e as demais processadas` (T-006); `RN-008 AMB-013 
          +AMB-017 d-005 coworking → recusada com CATEGORIA_INELEGIVEL e 
          +não erro` (T-010); `RN-007 AMB-017 d-009 erro → lista de motiv
          +os vazia` (T-012); `RN-009 AMB-017 despesa com erro de item nã
          +o integra grupo` (T-013) |                                    
      425 +| AMB-018 | T-004, T-012 | `§8 AMB-018 ordem de Motivo é a ord
          +em da lista de elegibilidade da spec` (T-004); `RN-007 AMB-018
          + 150,00 sem NF fora do período → motivos FORA_DA_COMPETENCIA e
          + SEM_NOTA_FISCAL nessa ordem` (T-012) |                       
      426 +| AMB-019 | T-005 | `RN-012 AMB-019 duas despesas com identifi
          +cador d-006 → lote rejeitado sem avaliar nenhuma`; `RN-010 AMB
          +-019 início posterior ao fim e identificador repetido → as dua
          +s causas` |                                                   
      427 +| AMB-020 | T-005, T-007, T-017, T-018 | `RN-010 AMB-020 perío
          +do com tipo incompatível → lote rejeitado` (T-005); `RN-010 AM
          +B-020 despesa sem identificador na terceira posição → erro ide
          +ntificado pela posição 3`; `RN-010 AMB-020 elemento que não é 
          +despesa → erro de item na sua posição com causa única` (T-007)
          +; `AMB-020 despesa sem identificador sai com posicao no lugar 
          +de id` (T-018) |                                              
      428 +| AMB-021 | T-004, T-006, T-007 | `§4-1 AMB-021 ordem de Campo
          +Despesa é a ordem da tabela de despesa da spec` (T-004); `RN-0
          +10 AMB-021 sem fornecedor e valor −5,00 → causas fornecedor au
          +sente e valor não positivo nessa ordem` (T-006); `AMB-021 sem 
          +identificador e data 2026-02-30 → causas identificador ausente
          + e data inexistente nessa ordem` (T-007) |                    
      429 +                                                              
      430 +### Seções da spec                                            
      431 +                                                              
      432 +| Seção | Task | Teste |                                      
      433 +|---|---|---|                                                 
      434 +| §4.1 — espaços nas extremidades | T-003 | `§4-1 aparar remov
          +e U+0085 e não remove U+001C a U+001F` |                      
      435 +| §4.2 — saída por despesa e consolidado | T-014, T-018 | `§4-
          +2 AMB-014 total reembolsado é a soma dos reembolsos dos itens`
          +; `§4-2 motivos aparece sempre e campos não aplicáveis são omi
          +tidos` |                                                      
      436 +| §7 — casos de borda | T-015 | `Secao7CasosDeBordaTest` (20 t
          +estes `§7`) |                                                 
      437 +| §8 — ordem de aplicação | T-004, T-012 | `§8 AMB-018 ordem d
          +e Motivo é a ordem da lista de elegibilidade da spec`; `§8 des
          +pesa recusada não tem limite aplicado` |                      
      438 +| §9 — critérios de aceite | T-020 | `§9 lote de exemplo produ
          +z total 623,43 com 4 integrais, 4 parciais, 5 recusadas e 1 er
          +ro` |                                                         

● Uma verificação rápida: todo RN e toda AMB não dissolvida aparece no nome de
  pelo menos um teste?

● Bash(f=specs/001-motor-reembolso/tasks.md; names=$(sed -n '/^## Fase 1/,/^##
      Fase 5/p' $f | grep -o '`[^`]*`' ); for i in $(seq -w 1 12); do echo
      "$names" | grep -q…)
  ⎿  (No output)

● Escrevi o tasks.md e fiz antes o commit do plan, na ordem que você pediu.

  1. Plan. Commit db8b1b6, docs(plan): RastreabilidadeTest exige teste para cada
  AMB nao dissolvida. O plan passou para a versão 1.1, e a §6 agora diz três
  coisas:
  - Toda ### AMB-\d{3} da spec precisa de pelo menos um teste com o ID no nome.
  - A isenção é uma lista explícita (AMB-003, AMB-012). O teste falha se um ID
    da lista não existir na spec ou se o título dele não tiver "(dissolvida)".
  - Quando um teste verifica uma AMB, o nome leva o ID dela depois do ID da RN
    (RN-001 AMB-001 …).

  2. tasks.md (specs/001-motor-reembolso/tasks.md) — 20 tasks com a estrutura
  aprovada:
  - Convenções no topo: cada task acrescenta ao arquivo da RN os aceites que
    passa a tornar verificáveis; IDs de RN e AMB entram no nome do teste; está
    ali o checklist de revisão de R-04 e R-05.
  - T-016 traz os quatro testes do RastreabilidadeTest. Um deles verifica a
    lista de isenção. Ele ignora os nomes do próprio pacote rastreabilidade,
    para que os IDs que o teste cita não contem como cobertura.
  - Nota sobre a fronteira, no início da Fase 4, junto a T-017 e T-018.
  - Matriz de Cobertura em três tabelas: RNs, AMBs e seções §4.1, §4.2, §7, §8 e
    §9. As duas dissolvidas aparecem marcadas e ligadas ao teste que mostra a
    dissolução.
  - Fase 5 reservada e vazia.

  Verificação. Um script percorreu os nomes de teste das Fases 1 a 4. Os 12 RN e
  as 21 AMB aparecem em pelo menos um nome, inclusive as duas dissolvidas. O
  T-015 tem 20 testes §7, o mesmo número de linhas da tabela. Nenhum nome usa
  caracteres proibidos em método JVM. Por isso §4.1 e §4.2 aparecem como §4-1 e
  §4-2 nos nomes, regra registrada nas convenções.

  Duas coisas que você pode querer revisar:
  - A nota sobre a fronteira cobre cinco AMB, não três. Ao escrever os testes, a
    AMB-016 e a AMB-011 caíram na mesma situação. A divergência do rótulo de
    competência e o aceite da RN-009 com descrições diferentes também dependem
    de campos que não chegam ao domínio. Coloquei as duas no T-017, e a nota
    explica isso.
  - Não fiz commit do tasks.md, porque você pediu commit só do plan. Se quiser,
    faço agora como docs(tasks): tasks.md com 20 tasks e matriz de cobertura.

> Faça o commit do tasks.md. Depois disso, pare — a janela está acabando e a
  T-001 fica para a próxima sessão.

● Bash(git add specs/001-motor-reembolso/tasks.md && git commit -q -F - <<'EOF'
      docs(tasks): tasks.md com 20 tasks e matriz de cobertura…)
  ⎿  (No output)

● Commit do tasks.md feito: 6edb655 docs(tasks): tasks.md com 20 tasks e matriz
  de cobertura. Não há mais nenhuma alteração pendente no repositório.

  Parei aqui. A próxima sessão começa pela T-001, a configuração do ambiente.