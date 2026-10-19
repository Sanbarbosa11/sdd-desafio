# Tasks — Motor de Cálculo de Reembolso

**Baseado em:** spec 1.2 · plan 1.1

> Cada task é pequena o bastante para virar **um commit**. Se você não consegue
> descrever o critério de aceite como "o teste X passa", a task está grande demais.
>
> Marque `[x]` conforme conclui — ao longo do caminho, não tudo no fim. O histórico
> de quando cada task foi marcada é lido na correção.

**Formato do commit:** `feat(T-003): <descrição>` · `test(T-003): <descrição>`

### Convenções desta lista

- **Aceites que dependem de mais de uma regra.** O plan (§6) prevê um arquivo
  de teste por RN, com um teste por item da lista "Aceite". Muitos aceites só
  são verificáveis depois de outra regra existir — "100,005 → nota exigida" é
  aceite da RN-011, mas depende da RN-003. Regra: **cada task acrescenta ao
  arquivo da RN os aceites que ela torna verificáveis.** O arquivo de uma RN
  pode, portanto, receber testes em mais de uma task; a matriz de Cobertura
  registra em qual task cada teste nasce.
- **IDs no nome do teste.** O nome começa pelo ID da RN e, quando o teste
  verifica a consequência de uma AMB, leva também o ID da AMB (plan §6). O
  `RastreabilidadeTest` (T-016) exige um teste por RN e por AMB não
  dissolvida a partir desses nomes.
- **Seções com ponto.** `.` não é permitido em nome de método JVM; nos nomes
  de teste, §4.1 e §4.2 são escritos `§4-1` e `§4-2`.
- **Checklist de revisão de toda task de código:** nenhum `.trim()` em `src/`
  (R-05); nenhum `Double` em caminho de dinheiro (R-04).

---

## Fase 1 — Fundação

- [x] **T-001** — Configurar o ambiente (R-01): `JAVA_HOME` apontando para o
  JDK 21 do Android Studio; Gradle Wrapper 8.11.1 gerado a partir da
  distribuição do cache e versionado; `build.gradle.kts` com Kotlin 2.0.21,
  plugin `application`, alvo Java 17 (`jvmTarget`, `-Xjdk-release=17`,
  `options.release = 17`), source sets `src/` e `tests/` sem recursos, e as
  dependências `kotlinx-serialization-json` 1.7.3, `junit-bom` 5.11 e
  `kotlin-test-junit5` (R-02). Preencher "Stack e comandos" no CLAUDE.md.
  - **Atende:** nenhuma RN ou AMB (infraestrutura); plan R-01, R-02, DT-001, DT-002
  - **Aceite:** `./gradlew --version` mostra Gradle 8.11.1 rodando no JDK 21,
    e `./gradlew build` termina com sucesso executando
    `AmbienteTest` › `build compila src e executa testes de tests`
  - **Commit:** —

- [x] **T-002** — `value class Valor` com `normalizar` (`setScale(2, HALF_UP)`)
  como único construtor, e os testes de guarda da aritmética (DT-003).
  - **Atende:** RN-011, AMB-014
  - **Aceite:** `RN011NormalizacaoMonetariaTest` passa:
    `RN-011 AMB-014 33,333 normaliza para 33,33`,
    `RN-011 AMB-014 100,005 normaliza para 100,01 e 100,004 para 100,00`,
    `RN-011 AMB-014 −45,005 normaliza para −45,01 afastando-se do zero`,
    `RN-011 AMB-014 54,90 e 54,900 são o mesmo valor`
  - **Commit:** —

- [x] **T-003** — Função `aparar()` no domínio com o conjunto explícito dos 25
  code points da propriedade Unicode `White_Space` (DT-005).
  - **Atende:** §4.1 (espaços nas extremidades); base de RN-008, RN-010, RN-012
  - **Aceite:** `EspacosUnicodeTest` passa:
    `§4-1 aparar remove os 25 code points White_Space nas extremidades`,
    `§4-1 aparar remove U+0085 e não remove U+001C a U+001F`,
    `§4-1 aparar não remove espaços do meio do texto`
  - **Commit:** —

- [x] **T-004** — Modelo do domínio (plan §3.1, §3.2, §4): `Lido`, `LoteLido`,
  `Categoria` e `Politica` em `Politica.kt`, `Motivo`, `CampoDespesa`, `Falha`,
  `CausaErro`, `CausaLote`, `Despesa`, `Referencia`, `ResultadoDespesa`,
  `ResultadoLote`. Testes que amarram a ordem dos enums à spec e a fronteira
  do domínio.
  - **Atende:** §8 e AMB-018 (ordem dos motivos); §4.1 e AMB-021 (ordem dos campos); plan DT-006, DT-009
  - **Aceite:** passam
    `OrdemMotivosSecao8Test` › `§8 AMB-018 ordem de Motivo é a ordem da lista de elegibilidade da spec`,
    `OrdemCamposSecao41Test` › `§4-1 AMB-021 ordem de CampoDespesa é a ordem da tabela de despesa da spec`,
    `FronteiraDominioTest` › `DT-009 nenhum arquivo do dominio importa kotlinx ou java io`
  - **Commit:** —

## Fase 2 — Regras de negócio

Todos os testes desta fase constroem `LoteLido` direto, sem JSON (plan §6).

- [x] **T-005** — Validação de lote: período ausente ou de tipo incompatível,
  início ou fim ausente ou inválido, início posterior ao fim, despesas não
  informadas ou de tipo incompatível, identificador repetido após `aparar()`.
  Todas as causas detectáveis são reportadas.
  - **Atende:** RN-010 (erro de lote), RN-012, AMB-017, AMB-019, AMB-020 (ii)
  - **Aceite:** `RN010ValidacaoDeLoteTest` e `RN012IdentificadorUnicoTest` passam:
    `RN-010 AMB-017 início posterior ao fim → lote rejeitado sem despesa processada`,
    `RN-010 período ausente → lote rejeitado`,
    `RN-010 AMB-020 período com tipo incompatível → lote rejeitado`,
    `RN-010 AMB-020 relação de despesas com tipo incompatível → lote rejeitado`,
    `RN-010 data de início inválida → lote rejeitado`,
    `RN-010 período com início igual ao fim → válido`,
    `RN-012 AMB-019 duas despesas com identificador d-006 → lote rejeitado sem avaliar nenhuma`,
    `RN-010 AMB-019 início posterior ao fim e identificador repetido → as duas causas`,
    `RN-012 AMB-020 d-006 com espaço à esquerda repete d-006 → lote rejeitado`,
    `RN-012 d-006 e D-006 são identificadores distintos`,
    `RN-012 identificadores d-001 a d-014 distintos → sem erro de lote`
  - **Commit:** —

- [x] **T-006** — Validação de item, campos: informação obrigatória ausente,
  tipo incompatível, data inexistente, texto obrigatório vazio após `aparar()`
  tratado como ausente, valor normalizado menor ou igual a zero. Todas as
  causas, na ordem de `CampoDespesa`.
  - **Atende:** RN-010 (erro de item), RN-011, AMB-012 (dissolvida), AMB-014, AMB-015 (fornecedor obrigatório), AMB-017, AMB-021
  - **Aceite:** `RN010ValidacaoDeItemTest` passa:
    `RN-010 AMB-012 d-009 −45,00 → erro de item com causa valor não positivo`,
    `RN-010 AMB-017 despesa sem o campo de valor → erro de item e as demais processadas`,
    `RN-010 despesa de valor 0,00 → erro de item`,
    `RN-011 AMB-014 0,004 normaliza para 0,00 e é erro de item`,
    `RN-011 −45,005 é erro de item com valor considerado −45,01`,
    `RN-010 despesa sem indicação de nota fiscal → erro de item e não SEM_NOTA_FISCAL`,
    `RN-010 fornecedor só com espaços, tabulação ou espaço não separável → erro de item ausente`,
    `RN-010 AMB-015 fornecedor com tipo incompatível → erro de item`,
    `RN-010 data 2026-02-30 → erro de item data inexistente`,
    `RN-010 AMB-021 sem fornecedor e valor −5,00 → causas fornecedor ausente e valor não positivo nessa ordem`,
    `RN-010 erro de item em uma despesa não altera o resultado das demais`
  - **Commit:** —

- [ ] **T-007** — Validação de item, identidade: identificador aparado,
  `Referencia.Posicao` (contada a partir de 1) quando o identificador falta ou
  tem tipo incompatível, elemento que não é despesa, ausências que não contam
  como repetição.
  - **Atende:** RN-010, RN-012, AMB-020 (i, iii), AMB-021
  - **Aceite:** `RN010IdentidadeDoItemTest` passa:
    `RN-010 AMB-020 despesa sem identificador na terceira posição → erro identificado pela posição 3`,
    `RN-012 AMB-020 duas despesas sem identificador → ambas erro de item e lote não rejeitado`,
    `RN-010 AMB-020 elemento que não é despesa → erro de item na sua posição com causa única`,
    `AMB-021 sem identificador e data 2026-02-30 → causas identificador ausente e data inexistente nessa ordem`,
    `RN-012 AMB-020 identificador com espaços nas extremidades é considerado aparado`
  - **Commit:** —

- [ ] **T-008** — `Motor.avaliar` com a etapa 2 (valor) da §8: limite nominal
  por despesa via `limiteAplicavel`, reembolso parcial com excedente.
  - **Atende:** RN-001, RN-002, RN-011, AMB-001, AMB-002, AMB-003 (dissolvida), AMB-007, AMB-009
  - **Aceite:** `RN001LimitePorDespesaTest` e `RN002ReembolsoParcialTest` passam,
    mais os aceites da RN-011 que passam a ser verificáveis:
    `RN-001 AMB-001 d-001 alimentacao 72,50 → reembolso 60,00 parcial`,
    `RN-001 AMB-001 d-002 alimentacao 38,00 → reembolso 38,00 integral`,
    `RN-001 AMB-001 dia 03-07 totaliza 98,00 em alimentacao`,
    `RN-001 AMB-003 inverter a ordem de d-001 e d-002 não altera os valores`,
    `RN-001 AMB-001 AMB-003 três alimentações de 59,00 no mesmo dia → 59,00 cada e 177,00 no total`,
    `RN-001 AMB-007 AMB-009 d-010 hospedagem 480,00 → limite 250,00, reembolso 250,00 parcial, excedente 230,00`,
    `AMB-007 d-011 no mesmo fornecedor de d-010 mantém limite 60,00`,
    `AMB-009 hospedagem 200,00 com NF → reembolso 200,00 integral`,
    `RN-002 AMB-002 d-001 72,50 → excedente 12,50`,
    `RN-002 AMB-002 alimentacao de 60,00 → reembolso 60,00 integral`,
    `RN-002 AMB-002 alimentacao de 60,01 → reembolso 60,00 parcial e excedente 0,01`,
    `RN-002 transporte urbano de 90,00 → reembolso 80,00 parcial e excedente 10,00`,
    `RN-011 d-011 33,333 → reembolso 33,33 integral`,
    `RN-011 alimentacao de 60,004 → reembolso 60,00 integral`
  - **Commit:** —

- [ ] **T-009** — Etapa 1.1 da §8: pertinência ao período, bordas inclusivas,
  e recusa com `FORA_DA_COMPETENCIA` sem avaliação contra o limite.
  - **Atende:** RN-005, RN-006, AMB-008, AMB-010, AMB-016
  - **Aceite:** `RN005PeriodoTest` e `RN006ForaDoPeriodoTest` passam:
    `RN-005 AMB-016 período do lote de exemplo é de 2026-07-01 a 2026-07-31 inclusive`,
    `RN-005 AMB-016 d-014 em 2026-07-31 igual à data de fim → dentro do período`,
    `RN-005 AMB-016 despesa em 2026-07-01 igual à data de início → dentro do período`,
    `RN-005 AMB-016 despesa em 2026-08-01 → fora do período`,
    `RN-005 AMB-008 d-008 em 2026-04-15 → fora do período`,
    `RN-005 d-001 a d-014 exceto d-008 e d-009 → dentro do período`,
    `RN-006 AMB-010 d-008 alimentacao 41,00 com NF → recusada com FORA_DA_COMPETENCIA sem limite aplicado`,
    `RN-006 despesa em 2026-08-01 com NF → recusada com FORA_DA_COMPETENCIA`
  - **Commit:** —

- [ ] **T-010** — Normalização de categoria e fornecedor (`aparar()` +
  `lowercase()`) na construção de `Despesa`; etapa 1.2 da §8: elegibilidade
  da categoria.
  - **Atende:** RN-008, AMB-013, AMB-017
  - **Aceite:** `RN008CategoriaTest` passa:
    `RN-008 AMB-013 d-014 ALIMENTACAO 61,00 → categoria alimentacao, reembolso 60,00 parcial`,
    `RN-008 AMB-013 AMB-017 d-005 coworking → recusada com CATEGORIA_INELEGIVEL e não erro`,
    `RN-008 AMB-013 Alimentacao, ALIMENTACAO e alimentacao com espaços → a mesma categoria`,
    `RN-008 AMB-013 alimentação com cedilha e til → recusada com CATEGORIA_INELEGIVEL`,
    `RN-008 AMB-013 transporte-urbano → recusada com CATEGORIA_INELEGIVEL`,
    `RN-008 AMB-013 Bistro Central, BISTRO CENTRAL e Bistro Central com espaços → o mesmo fornecedor e Bistrô Central é distinto`
  - **Commit:** —

- [ ] **T-011** — Etapa 1.3 da §8: exigência de nota fiscal sobre o valor
  normalizado (`Politica.LIMIAR_NOTA_FISCAL`) e recusa com `SEM_NOTA_FISCAL`.
  - **Atende:** RN-003, RN-004, RN-011, AMB-004, AMB-005, AMB-006, AMB-014
  - **Aceite:** `RN003NotaFiscalTest` e `RN004SemNotaFiscalTest` passam:
    `RN-003 AMB-004 d-003 transporte 100,00 sem NF → nota não exigida, reembolso 80,00 parcial`,
    `RN-003 AMB-004 AMB-006 d-004 transporte 100,01 sem NF → nota exigida e ausente`,
    `RN-003 AMB-004 transporte 100,01 com NF → nota exigida e presente, reembolso 80,00 parcial`,
    `RN-003 RN-011 AMB-014 100,004 → nota não exigida e 100,005 → nota exigida`,
    `RN-003 alimentacao 50,00 sem NF → nota não exigida, reembolso 50,00 integral`,
    `RN-003 AMB-006 alimentacao 500,00 sem NF → nota exigida e ausente apesar do teto de 60,00`,
    `RN-003 AMB-006 d-013 hospedagem 690,00 sem NF → nota exigida e ausente`,
    `RN-004 AMB-005 d-004 → reembolso 0,00 recusada com SEM_NOTA_FISCAL`,
    `RN-004 AMB-005 d-013 → recusada com SEM_NOTA_FISCAL`,
    `RN-004 AMB-005 alimentacao 500,00 sem NF → recusada com SEM_NOTA_FISCAL`,
    `RN-004 AMB-005 hospedagem 200,00 sem NF → recusada com SEM_NOTA_FISCAL apesar de estar abaixo do limite`,
    `RN-004 d-003 e transporte 100,01 com NF → regra não se aplica, reembolso 80,00 parcial`
  - **Commit:** —

- [ ] **T-012** — Lista de motivos: todas as regras de elegibilidade avaliadas
  sem interrupção, lista ordenada por `Motivo.ordinal`, recusa sem limite,
  erro com lista vazia.
  - **Atende:** RN-007, AMB-017, AMB-018; §8 (recusada não é avaliada contra o limite)
  - **Aceite:** `RN007ListaDeMotivosTest` passa:
    `RN-007 AMB-018 150,00 sem NF fora do período → motivos FORA_DA_COMPETENCIA e SEM_NOTA_FISCAL nessa ordem`,
    `RN-007 AMB-018 d-008 → motivos FORA_DA_COMPETENCIA`,
    `RN-007 AMB-018 d-004 → motivos SEM_NOTA_FISCAL`,
    `RN-007 d-002 sem violação → lista de motivos vazia`,
    `RN-007 d-001 parcial → lista de motivos vazia`,
    `RN-007 AMB-017 d-009 erro → lista de motivos vazia`,
    `§8 despesa recusada não tem limite aplicado`
  - **Commit:** —

- [ ] **T-013** — Etapa 1.4 da §8: duplicidade em duas passadas, só entre
  despesas sem motivo, com comparador de identificadores por code point
  (DT-007, R-07).
  - **Atende:** RN-009, RN-011, AMB-009, AMB-010, AMB-011, AMB-013 (fornecedor), AMB-014, AMB-017
  - **Aceite:** `RN009DuplicidadeTest` passa:
    `RN-009 AMB-011 d-006 e d-007 → d-006 integral e d-007 recusada com DUPLICATA mantida d-006`,
    `RN-009 AMB-011 inverter a ordem de d-006 e d-007 → os mesmos resultados`,
    `RN-009 AMB-011 iguais até 100,00 uma com NF e outra sem → mantém a de menor identificador`,
    `RN-009 hospedagens de 200,00 com a menor sem NF → a sem NF recusada SEM_NOTA_FISCAL e a outra integral sem DUPLICATA`,
    `RN-009 AMB-010 duas iguais fora do período → ambas FORA_DA_COMPETENCIA e nenhuma DUPLICATA`,
    `RN-009 AMB-009 duas diárias de 240,00 em 14-07 e 15-07 → não são duplicatas`,
    `RN-009 AMB-009 as mesmas duas diárias ambas em 14-07 → grupo de duplicatas`,
    `RN-009 AMB-011 d-9 e d-10 → d-10 mantida pela ordem lexicográfica`,
    `RN-009 AMB-013 Bistro Central e BISTRO CENTRAL → grupo de duplicatas`,
    `RN-009 AMB-011 d-006 e D-006 → mantém D-006`,
    `RN-009 identificador fora do BMP é comparado por code point`,
    `RN-011 AMB-014 54,90 e 54,904 → grupo de duplicatas`,
    `RN-009 AMB-017 despesa com erro de item não integra grupo`
  - **Commit:** —

- [ ] **T-014** — Consolidado do lote: período avaliado, total reembolsado,
  quantidades por classificação e total; lote sem despesas.
  - **Atende:** §4.2 (consolidado), RN-010, AMB-010, AMB-014
  - **Aceite:** `ConsolidadoTest` passa:
    `§4-2 AMB-014 total reembolsado é a soma dos reembolsos dos itens`,
    `§4-2 AMB-010 quantidades por classificação somam o número de despesas da entrada`,
    `§4-2 consolidado traz o período avaliado`,
    `RN-010 lote sem despesas → total 0,00 e zero em todas as classificações`
  - **Commit:** —

## Fase 3 — Casos de borda

- [ ] **T-015** — Um teste por linha da tabela da §7 (20 linhas), nomeado com
  as RNs da coluna "Regra".
  - **Atende:** §7
  - **Aceite:** `Secao7CasosDeBordaTest` passa, com os 20 testes:
    `§7 RN-001 RN-002 d-001 alimentacao 72,50 → 60,00 parcial, excedente 12,50`,
    `§7 RN-001 d-002 38,00 → integral e d-001 no mesmo dia não reduz o valor`,
    `§7 RN-002 alimentacao 61,00 → 60,00 parcial, excedente 1,00`,
    `§7 RN-002 alimentacao 60,00 → 60,00 integral`,
    `§7 RN-002 RN-003 d-003 transporte 100,00 sem NF → nota não exigida, 80,00 parcial`,
    `§7 RN-002 RN-003 transporte 100,01 com NF → nota exigida e presente, 80,00 parcial`,
    `§7 RN-003 RN-004 d-004 transporte 100,01 sem NF → recusada SEM_NOTA_FISCAL`,
    `§7 RN-003 RN-004 alimentacao 500,00 sem NF → recusada SEM_NOTA_FISCAL`,
    `§7 RN-003 RN-004 d-013 hospedagem 690,00 sem NF → recusada SEM_NOTA_FISCAL`,
    `§7 RN-001 RN-002 RN-003 d-010 hospedagem 480,00 com NF → limite 250,00, parcial`,
    `§7 RN-004 hospedagem 200,00 sem NF → recusada SEM_NOTA_FISCAL`,
    `§7 RN-005 RN-006 d-008 em 2026-04-15 → recusada FORA_DA_COMPETENCIA`,
    `§7 RN-004 RN-006 RN-007 150,00 sem NF fora do período → FORA_DA_COMPETENCIA e SEM_NOTA_FISCAL`,
    `§7 RN-001 RN-002 RN-005 RN-008 d-014 ALIMENTACAO em 2026-07-31 → 60,00 parcial`,
    `§7 RN-009 d-006 → mantida, 54,90 integral`,
    `§7 RN-009 d-007 → recusada DUPLICATA independentemente da ordem`,
    `§7 RN-010 d-009 −45,00 → erro`,
    `§7 RN-011 RN-001 d-011 33,333 → 33,33 integral`,
    `§7 RN-008 d-005 coworking → recusada CATEGORIA_INELEGIVEL e não erro`,
    `§7 RN-001 RN-002 d-012 sábado 47,20 → integral`
  - **Commit:** —

- [ ] **T-016** — `RastreabilidadeTest` (plan §6, versão 1.1): lê `spec.md` e
  os nomes de teste em `tests/` (fora do pacote `rastreabilidade`, para que os
  IDs citados no próprio teste não contem) e falha o build quando falta
  cobertura. A isenção das AMB dissolvidas é uma lista explícita no teste
  (`AMB-003`, `AMB-012`), verificada contra a spec, de modo que uma AMB nova
  nunca entre na isenção por descuido.
  - **Atende:** RN-001 a RN-012, AMB-001 a AMB-021, §7 (guarda do processo)
  - **Aceite:** `RastreabilidadeTest` passa:
    `toda RN da spec tem ao menos um teste com o seu ID`,
    `toda AMB não dissolvida da spec tem ao menos um teste com o seu ID`,
    `cada AMB da lista de isenção existe na spec e está marcada como dissolvida`,
    `a quantidade de testes §7 é igual à de linhas da tabela da §7`
  - **Commit:** —

## Fase 4 — Saída e CLI

> **Requisitos observáveis só na fronteira (T-017 e T-018).** A AMB-008, a
> AMB-009 e a AMB-015 determinam que a descrição da despesa e os campos do
> colaborador não alteram nenhum resultado. No domínio, isso se cumpre por
> construção: o adaptador `entrada` não lê esses campos (plan §2), e eles não
> existem em `LoteLido`. Um teste de domínio sobre eles seria vazio. Por isso
> os testes que verificam essas AMB estão no adaptador de entrada (T-017) e na
> saída (T-018), onde os campos existem. Pelo mesmo motivo ficam no T-017 o
> teste do rótulo de competência divergente (AMB-016) e o aceite da RN-009
> sobre despesas com descrições diferentes (AMB-011): os dois dependem de
> campos que não chegam ao domínio.

- [ ] **T-017** — Adaptador `entrada`: JSON → `LoteLido` (DT-004).
  `ENTRADA_ILEGIVEL` como causa única para JSON inválido ou raiz que não é
  lote; `BigDecimal` a partir do texto cru; data com `ResolverStyle.STRICT`;
  `null` como `Ausente`; campos sem regra não lidos.
  - **Atende:** RN-009, RN-010, RN-011, AMB-008, AMB-009, AMB-011, AMB-015, AMB-016, AMB-020
  - **Aceite:** `EntradaJsonTest` passa:
    `RN-010 JSON sintaticamente inválido → rejeitado com causa única ENTRADA_ILEGIVEL`,
    `RN-010 raiz JSON que é uma lista ou um número → rejeitado com causa única ENTRADA_ILEGIVEL`,
    `RN-011 33,333 é lido do texto cru sem passar por Double`,
    `RN-011 100,005 é lido do texto cru e normaliza para 100,01`,
    `RN-010 valor 72,50 escrito como string → tipo incompatível`,
    `RN-010 data 2026-02-30 → data inexistente`,
    `RN-010 campo null → ausente`,
    `RN-010 AMB-020 período e relação de despesas com tipo incompatível → lote rejeitado`,
    `RN-010 AMB-020 elemento da relação que não é objeto → erro de item`,
    `RN-010 AMB-015 fornecedor informado como número → erro de item tipo incompatível`,
    `RN-010 AMB-015 descrição informada como número → processada normalmente`,
    `RN-010 AMB-015 centro de custo e rótulo com tipo incompatível → processado normalmente`,
    `RN-010 campo não previsto é ignorado`,
    `AMB-015 lotes que diferem só no centro de custo → resultados idênticos`,
    `AMB-015 lote sem centro de custo e sem rótulo com despesa sem descrição → processado normalmente`,
    `AMB-016 rótulo de competência divergente do par de datas não impede o processamento`,
    `AMB-008 descrição de d-008 não altera o resultado FORA_DA_COMPETENCIA`,
    `AMB-009 hospedagem 400,00 descrita como 1 diaria ou 2 diarias → mesmo resultado 250,00 parcial`,
    `RN-009 AMB-011 iguais no critério com descrições diferentes → grupo de duplicatas`
  - **Commit:** —

- [ ] **T-018** — Adaptador `saida`: `ResultadoLote` → JSON (plan §3.3).
  Campos não aplicáveis omitidos, `motivos` sempre presente, dinheiro com
  duas casas via `toPlainString()`, `posicao` no lugar de `id`, frase de
  justificativa montada só a partir dos campos do resultado.
  - **Atende:** §4.2, RN-012, AMB-015, AMB-020
  - **Aceite:** `SaidaJsonTest` passa:
    `§4-2 parcial traz limite_aplicado e excedente e recusada não traz limite_aplicado`,
    `§4-2 dinheiro sai como número com duas casas sem passar por Double`,
    `§4-2 motivos aparece sempre e campos não aplicáveis são omitidos`,
    `§4-2 duplicata traz despesa_mantida`,
    `§4-2 erro traz causas_erro na ordem da §4-1`,
    `§4-2 lote rejeitado traz todas as causas sem resultado por despesa nem consolidado`,
    `§4-2 toda despesa traz justificativa não vazia`,
    `RN-012 AMB-020 identificador com espaços nas extremidades sai aparado`,
    `AMB-020 despesa sem identificador sai com posicao no lugar de id`,
    `AMB-015 saída não contém colaborador, rótulo de competência nem descrição`
  - **Commit:** —

- [ ] **T-019** — CLI `calcular --input X --output Y` com parsing manual e
  exit codes (DT-008).
  - **Atende:** RN-010 (entrada que não pode ser lida); plan DT-008
  - **Aceite:** `CliTest` passa:
    `DT-008 lote avaliado sai com exit 0`,
    `DT-008 lote rejeitado sai com exit 1 e JSON escrito`,
    `DT-008 subcomando ou flag ausente sai com exit 2 sem escrever nada`,
    `RN-010 DT-008 arquivo de entrada inexistente → rejeitado com ENTRADA_ILEGIVEL e exit 1`,
    `DT-008 falha ao escrever a saída → exit 3 com mensagem no stderr`
  - **Commit:** —

- [ ] **T-020** — Ponta a ponta: `main` com `exemplos/despesas-exemplo.json`,
  comparado com a tabela da §9; determinismo com a ordem das despesas
  invertida.
  - **Atende:** §9, RN-001, RN-009
  - **Aceite:** `Secao9CriteriosDeAceiteTest` passa:
    `§9 lote de exemplo produz o resultado da tabela da §9 despesa a despesa`,
    `§9 lote de exemplo produz total 623,43 com 4 integrais, 4 parciais, 5 recusadas e 1 erro`,
    `§9 cada despesa aparece exatamente uma vez na ordem da entrada`,
    `§9 a mesma entrada produz sempre a mesma saída`,
    `§9 RN-001 RN-009 inverter a ordem do lote de exemplo não altera nenhum resultado`
  - **Commit:** —

---

## Fase 5 — Envelope (criar no Dia 2)

<Novas tasks a partir da mudança de requisito. Numeração continua de onde parou —
não reinicie e não renumere as antigas: a numeração é o eixo da rastreabilidade.>

---

## Cobertura

Preencha ao fechar cada fase. É a sua própria checagem de rastreabilidade — e é
exatamente a matriz que a correção vai montar.

Quando uma linha tem mais de uma task, a primeira é a que implementa a decisão,
e as seguintes acrescentam aceites que dependem de outras regras (ver
Convenções). O `RastreabilidadeTest` (T-016) verifica automaticamente que todo
ID das duas tabelas abaixo, exceto as AMB dissolvidas, aparece no nome de ao
menos um teste.

### Regras de negócio

| Regra da spec | Task | Teste |
|---|---|---|
| RN-001 | T-008 | `RN-001 AMB-001 d-001 alimentacao 72,50 → reembolso 60,00 parcial`; `RN-001 AMB-001 AMB-003 três alimentações de 59,00 no mesmo dia → 59,00 cada e 177,00 no total`; `§9 RN-001 RN-009 inverter a ordem do lote de exemplo não altera nenhum resultado` (T-020) |
| RN-002 | T-008 | `RN-002 AMB-002 alimentacao de 60,01 → reembolso 60,00 parcial e excedente 0,01`; `RN-002 transporte urbano de 90,00 → reembolso 80,00 parcial e excedente 10,00` |
| RN-003 | T-011 | `RN-003 AMB-004 d-003 transporte 100,00 sem NF → nota não exigida, reembolso 80,00 parcial`; `RN-003 RN-011 AMB-014 100,004 → nota não exigida e 100,005 → nota exigida` |
| RN-004 | T-011 | `RN-004 AMB-005 d-004 → reembolso 0,00 recusada com SEM_NOTA_FISCAL`; `RN-004 AMB-005 hospedagem 200,00 sem NF → recusada com SEM_NOTA_FISCAL apesar de estar abaixo do limite` |
| RN-005 | T-009 | `RN-005 AMB-016 d-014 em 2026-07-31 igual à data de fim → dentro do período`; `RN-005 AMB-016 despesa em 2026-07-01 igual à data de início → dentro do período` |
| RN-006 | T-009 | `RN-006 AMB-010 d-008 alimentacao 41,00 com NF → recusada com FORA_DA_COMPETENCIA sem limite aplicado` |
| RN-007 | T-012 | `RN-007 AMB-018 150,00 sem NF fora do período → motivos FORA_DA_COMPETENCIA e SEM_NOTA_FISCAL nessa ordem`; `RN-007 AMB-017 d-009 erro → lista de motivos vazia` |
| RN-008 | T-010 | `RN-008 AMB-013 AMB-017 d-005 coworking → recusada com CATEGORIA_INELEGIVEL e não erro`; `RN-008 AMB-013 d-014 ALIMENTACAO 61,00 → categoria alimentacao, reembolso 60,00 parcial` |
| RN-009 | T-013, T-017 | `RN-009 AMB-011 d-006 e d-007 → d-006 integral e d-007 recusada com DUPLICATA mantida d-006`; `RN-009 identificador fora do BMP é comparado por code point`; `RN-009 AMB-011 iguais no critério com descrições diferentes → grupo de duplicatas` (T-017) |
| RN-010 | T-005, T-006, T-007, T-014, T-017, T-019 | `RN-010 AMB-017 início posterior ao fim → lote rejeitado sem despesa processada` (T-005); `RN-010 AMB-021 sem fornecedor e valor −5,00 → causas fornecedor ausente e valor não positivo nessa ordem` (T-006); `RN-010 AMB-020 despesa sem identificador na terceira posição → erro identificado pela posição 3` (T-007); `RN-010 lote sem despesas → total 0,00 e zero em todas as classificações` (T-014); `RN-010 raiz JSON que é uma lista ou um número → rejeitado com causa única ENTRADA_ILEGIVEL` (T-017); `RN-010 DT-008 arquivo de entrada inexistente → rejeitado com ENTRADA_ILEGIVEL e exit 1` (T-019) |
| RN-011 | T-002, T-006, T-008, T-011, T-013, T-017 | `RN-011 AMB-014 100,005 normaliza para 100,01 e 100,004 para 100,00` (T-002); `RN-011 AMB-014 0,004 normaliza para 0,00 e é erro de item` (T-006); `RN-011 d-011 33,333 → reembolso 33,33 integral` (T-008); `RN-011 AMB-014 54,90 e 54,904 → grupo de duplicatas` (T-013); `RN-011 33,333 é lido do texto cru sem passar por Double` (T-017) |
| RN-012 | T-005, T-007, T-018 | `RN-012 AMB-019 duas despesas com identificador d-006 → lote rejeitado sem avaliar nenhuma`; `RN-012 AMB-020 d-006 com espaço à esquerda repete d-006 → lote rejeitado` (T-005); `RN-012 AMB-020 duas despesas sem identificador → ambas erro de item e lote não rejeitado` (T-007); `RN-012 AMB-020 identificador com espaços nas extremidades sai aparado` (T-018) |

### Ambiguidades

| Regra da spec | Task | Teste |
|---|---|---|
| AMB-001 | T-008 | `RN-001 AMB-001 AMB-003 três alimentações de 59,00 no mesmo dia → 59,00 cada e 177,00 no total`; `RN-001 AMB-001 dia 03-07 totaliza 98,00 em alimentacao` |
| AMB-002 | T-008 | `RN-002 AMB-002 alimentacao de 60,00 → reembolso 60,00 integral`; `RN-002 AMB-002 alimentacao de 60,01 → reembolso 60,00 parcial e excedente 0,01` |
| AMB-003 — **dissolvida** pela AMB-001; sem decisão própria, isenta no `RastreabilidadeTest` | T-008 (teste que demonstra a dissolução) | `RN-001 AMB-003 inverter a ordem de d-001 e d-002 não altera os valores`; `RN-001 AMB-001 AMB-003 três alimentações de 59,00 no mesmo dia → 59,00 cada e 177,00 no total` |
| AMB-004 | T-011 | `RN-003 AMB-004 d-003 transporte 100,00 sem NF → nota não exigida, reembolso 80,00 parcial`; `RN-003 AMB-004 transporte 100,01 com NF → nota exigida e presente, reembolso 80,00 parcial` |
| AMB-005 | T-011 | `RN-004 AMB-005 hospedagem 200,00 sem NF → recusada com SEM_NOTA_FISCAL apesar de estar abaixo do limite`; `RN-004 AMB-005 alimentacao 500,00 sem NF → recusada com SEM_NOTA_FISCAL` |
| AMB-006 | T-011 | `RN-003 AMB-006 alimentacao 500,00 sem NF → nota exigida e ausente apesar do teto de 60,00`; `RN-003 AMB-004 AMB-006 d-004 transporte 100,01 sem NF → nota exigida e ausente` |
| AMB-007 | T-008 | `RN-001 AMB-007 AMB-009 d-010 hospedagem 480,00 → limite 250,00, reembolso 250,00 parcial, excedente 230,00`; `AMB-007 d-011 no mesmo fornecedor de d-010 mantém limite 60,00` |
| AMB-008 | T-009, T-017 (fronteira) | `RN-005 AMB-008 d-008 em 2026-04-15 → fora do período`; `AMB-008 descrição de d-008 não altera o resultado FORA_DA_COMPETENCIA` (T-017) |
| AMB-009 | T-008, T-013, T-017 (fronteira) | `RN-001 AMB-007 AMB-009 d-010 hospedagem 480,00 → limite 250,00, …`; `RN-009 AMB-009 duas diárias de 240,00 em 14-07 e 15-07 → não são duplicatas` (T-013); `AMB-009 hospedagem 400,00 descrita como 1 diaria ou 2 diarias → mesmo resultado 250,00 parcial` (T-017) |
| AMB-010 | T-009, T-013, T-014 | `RN-006 AMB-010 d-008 alimentacao 41,00 com NF → recusada com FORA_DA_COMPETENCIA sem limite aplicado`; `RN-009 AMB-010 duas iguais fora do período → ambas FORA_DA_COMPETENCIA e nenhuma DUPLICATA` (T-013); `§4-2 AMB-010 quantidades por classificação somam o número de despesas da entrada` (T-014) |
| AMB-011 | T-013, T-017 (fronteira) | `RN-009 AMB-011 inverter a ordem de d-006 e d-007 → os mesmos resultados`; `RN-009 AMB-011 d-9 e d-10 → d-10 mantida pela ordem lexicográfica`; `RN-009 AMB-011 iguais no critério com descrições diferentes → grupo de duplicatas` (T-017) |
| AMB-012 — **dissolvida** pela AMB-017; sem decisão própria, isenta no `RastreabilidadeTest` | T-006 (teste que demonstra a dissolução) | `RN-010 AMB-012 d-009 −45,00 → erro de item com causa valor não positivo` |
| AMB-013 | T-010, T-013 | `RN-008 AMB-013 alimentação com cedilha e til → recusada com CATEGORIA_INELEGIVEL`; `RN-008 AMB-013 Bistro Central, BISTRO CENTRAL e Bistro Central com espaços → o mesmo fornecedor e Bistrô Central é distinto`; `RN-009 AMB-013 Bistro Central e BISTRO CENTRAL → grupo de duplicatas` (T-013) |
| AMB-014 | T-002, T-006, T-011, T-013, T-014 | `RN-011 AMB-014 33,333 normaliza para 33,33` (T-002); `RN-011 AMB-014 0,004 normaliza para 0,00 e é erro de item` (T-006); `RN-003 RN-011 AMB-014 100,004 → nota não exigida e 100,005 → nota exigida` (T-011); `§4-2 AMB-014 total reembolsado é a soma dos reembolsos dos itens` (T-014) |
| AMB-015 | T-006, T-017 (fronteira), T-018 (fronteira) | `RN-010 AMB-015 fornecedor com tipo incompatível → erro de item` (T-006); `AMB-015 lotes que diferem só no centro de custo → resultados idênticos`; `RN-010 AMB-015 descrição informada como número → processada normalmente` (T-017); `AMB-015 saída não contém colaborador, rótulo de competência nem descrição` (T-018) |
| AMB-016 | T-009, T-017 (fronteira) | `RN-005 AMB-016 período do lote de exemplo é de 2026-07-01 a 2026-07-31 inclusive`; `RN-005 AMB-016 despesa em 2026-08-01 → fora do período`; `AMB-016 rótulo de competência divergente do par de datas não impede o processamento` (T-017) |
| AMB-017 | T-005, T-006, T-010, T-012, T-013 | `RN-010 AMB-017 início posterior ao fim → lote rejeitado sem despesa processada` (T-005); `RN-010 AMB-017 despesa sem o campo de valor → erro de item e as demais processadas` (T-006); `RN-008 AMB-013 AMB-017 d-005 coworking → recusada com CATEGORIA_INELEGIVEL e não erro` (T-010); `RN-007 AMB-017 d-009 erro → lista de motivos vazia` (T-012); `RN-009 AMB-017 despesa com erro de item não integra grupo` (T-013) |
| AMB-018 | T-004, T-012 | `§8 AMB-018 ordem de Motivo é a ordem da lista de elegibilidade da spec` (T-004); `RN-007 AMB-018 150,00 sem NF fora do período → motivos FORA_DA_COMPETENCIA e SEM_NOTA_FISCAL nessa ordem` (T-012) |
| AMB-019 | T-005 | `RN-012 AMB-019 duas despesas com identificador d-006 → lote rejeitado sem avaliar nenhuma`; `RN-010 AMB-019 início posterior ao fim e identificador repetido → as duas causas` |
| AMB-020 | T-005, T-007, T-017, T-018 | `RN-010 AMB-020 período com tipo incompatível → lote rejeitado` (T-005); `RN-010 AMB-020 despesa sem identificador na terceira posição → erro identificado pela posição 3`; `RN-010 AMB-020 elemento que não é despesa → erro de item na sua posição com causa única` (T-007); `AMB-020 despesa sem identificador sai com posicao no lugar de id` (T-018) |
| AMB-021 | T-004, T-006, T-007 | `§4-1 AMB-021 ordem de CampoDespesa é a ordem da tabela de despesa da spec` (T-004); `RN-010 AMB-021 sem fornecedor e valor −5,00 → causas fornecedor ausente e valor não positivo nessa ordem` (T-006); `AMB-021 sem identificador e data 2026-02-30 → causas identificador ausente e data inexistente nessa ordem` (T-007) |

### Seções da spec

| Seção | Task | Teste |
|---|---|---|
| §4.1 — espaços nas extremidades | T-003 | `§4-1 aparar remove U+0085 e não remove U+001C a U+001F` |
| §4.2 — saída por despesa e consolidado | T-014, T-018 | `§4-2 AMB-014 total reembolsado é a soma dos reembolsos dos itens`; `§4-2 motivos aparece sempre e campos não aplicáveis são omitidos` |
| §7 — casos de borda | T-015 | `Secao7CasosDeBordaTest` (20 testes `§7`) |
| §8 — ordem de aplicação | T-004, T-012 | `§8 AMB-018 ordem de Motivo é a ordem da lista de elegibilidade da spec`; `§8 despesa recusada não tem limite aplicado` |
| §9 — critérios de aceite | T-020 | `§9 lote de exemplo produz total 623,43 com 4 integrais, 4 parciais, 5 recusadas e 1 erro` |
