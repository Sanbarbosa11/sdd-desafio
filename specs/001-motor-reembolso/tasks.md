# Tasks — Motor de Cálculo de Reembolso

**Baseado em:** spec 2.0 · plan 1.1 (plan 2.0 na T-021)

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
  - **Commit:** `3c18414`; `.gitattributes` do `gradlew` em `c5a61d0`

- [x] **T-002** — `value class Valor` com `normalizar` (`setScale(2, HALF_UP)`)
  como único construtor, e os testes de guarda da aritmética (DT-003).
  - **Atende:** RN-011, AMB-014
  - **Aceite:** `RN011NormalizacaoMonetariaTest` passa:
    `RN-011 AMB-014 33,333 normaliza para 33,33`,
    `RN-011 AMB-014 100,005 normaliza para 100,01 e 100,004 para 100,00`,
    `RN-011 AMB-014 −45,005 normaliza para −45,01 afastando-se do zero`,
    `RN-011 AMB-014 54,90 e 54,900 são o mesmo valor`
  - **Commit:** `9abc75c`

- [x] **T-003** — Função `aparar()` no domínio com o conjunto explícito dos 25
  code points da propriedade Unicode `White_Space` (DT-005).
  - **Atende:** §4.1 (espaços nas extremidades); base de RN-008, RN-010, RN-012
  - **Aceite:** `EspacosUnicodeTest` passa:
    `§4-1 aparar remove os 25 code points White_Space nas extremidades`,
    `§4-1 aparar remove U+0085 e não remove U+001C a U+001F`,
    `§4-1 aparar não remove espaços do meio do texto`
  - **Commit:** `15ce265`

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
  - **Commit:** `b6734a8`

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
  - **Commit:** `bcfc828`

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
  - **Commit:** `e375e73`

- [x] **T-007** — Validação de item, identidade: identificador aparado,
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
  - **Commit:** `6868228`

- [x] **T-008** — `Motor.avaliar` com a etapa 2 (valor) da §8: limite nominal
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
  - **Commit:** `5af7f0b`

- [x] **T-009** — Etapa 1.1 da §8: pertinência ao período, bordas inclusivas,
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
  - **Commit:** `41f8d4f`

- [x] **T-010** — Normalização de categoria e fornecedor (`aparar()` +
  `lowercase()`) na construção de `Despesa`; etapa 1.2 da §8: elegibilidade
  da categoria.
  - **Atende:** RN-008, AMB-013, AMB-017
  - **Aceite:** `RN008CategoriaTest` passa:
    `RN-008 AMB-013 d-014 ALIMENTACAO 61,00 → categoria alimentacao, reembolso 60,00 parcial`,
    `RN-008 AMB-013 AMB-017 d-005 coworking → recusada com CATEGORIA_INELEGIVEL e não erro`,
    `RN-008 AMB-013 Alimentacao, ALIMENTACAO e alimentacao com espaços → a mesma categoria`,
    `RN-008 AMB-013 alimentação com cedilha e til → recusada com CATEGORIA_INELEGIVEL`,
    `RN-008 AMB-013 transporte-urbano → recusada com CATEGORIA_INELEGIVEL`,
    `RN-008 AMB-013 Bistro Central, BISTRO CENTRAL e Bistro Central com espaços → o mesmo fornecedor e Bistrô Central é distinto`,
    `RN-008 AMB-013 ALIMENTACAO em localidade turca ou azerbaijana → alimentacao`,
    `RN-008 AMB-013 fornecedores ΟΔΟΣ e Οδος → a mesma chave οδος`,
    `RN-008 AMB-013 ALİMENTACAO com U+0130 → recusada com CATEGORIA_INELEGIVEL`
  - **Commit:** `97504e0`

- [x] **T-011** — Etapa 1.3 da §8: exigência de nota fiscal sobre o valor
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
  - **Commit:** `dcc4601`

- [x] **T-012** — Lista de motivos: todas as regras de elegibilidade avaliadas
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
  - **Commit:** `5db55cc`

- [x] **T-013** — Etapa 1.4 da §8: duplicidade em duas passadas, só entre
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
    `RN-009 AMB-017 despesa com erro de item não integra grupo`,
    `RN-009 AMB-013 fornecedores ΟΔΟΣ e Οδος → grupo de duplicatas`
  - **Commit:** `8d6c8b6`

- [ ] **T-014** — Consolidado do lote: período avaliado, total reembolsado,
  quantidades por classificação e total; lote sem despesas.
  - **Revisão D-012:** O consolidado ganha a tabela aplicada (RN-013), no domínio pela T-024 e na saída pela T-029.
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
  - **Revisão D-012:** A §7 passou a ter 26 linhas; os seis casos novos estão na T-032.
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
  - **Revisão D-012:** O adaptador passa a ler o centro de custo e a moeda (T-028). O aceite `AMB-015 lotes que diferem só no centro de custo → resultados idênticos` deixou de valer e é substituído por `AMB-015 lotes que diferem só no identificador ou no nome do colaborador → resultados idênticos`.
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
  - **Revisão D-012:** Campos novos da saída na T-029: valor e moeda originais, cotação usada, tabela aplicada, causa moeda sem cotação.
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
  - **Revisão D-012:** Política e cotações como entradas da CLI na T-030, conforme a DT da T-021.
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
  - **Revisão D-012:** Os aceites desta task (total 623,43) deixaram de valer; o ponta a ponta é feito pela T-031, que a substitui, e as duas são fechadas juntas.
  - **Atende:** §9, RN-001, RN-009
  - **Aceite:** `Secao9CriteriosDeAceiteTest` passa:
    `§9 lote de exemplo produz o resultado da tabela da §9 despesa a despesa`,
    `§9 lote de exemplo produz total 623,43 com 4 integrais, 4 parciais, 5 recusadas e 1 erro`,
    `§9 cada despesa aparece exatamente uma vez na ordem da entrada`,
    `§9 a mesma entrada produz sempre a mesma saída`,
    `§9 RN-001 RN-009 inverter a ordem do lote de exemplo não altera nenhum resultado`
  - **Commit:** —

---

## Fase 5 — Envelope: política v4 (spec 2.0, D-012)

Tasks da mudança de requisito do Dia 2. Retrabalham as T-004, T-006, T-008,
T-010, T-011 e T-013, já concluídas, e complementam as T-014, T-015 e T-017 a
T-020, ainda pendentes (ver as notas "Revisão D-012" em cada uma). A ordem
abaixo é a de execução: o plan primeiro, depois o modelo, as regras e a
fronteira. Até a T-023, o `OrdemCamposSecao41Test` falha por desenho (a §4.1
já tem a moeda; `CampoDespesa` ainda não).

- [ ] **T-021** — Plan 2.0. Substituir o plan §4 ("constante, não arquivo de
  configuração") por uma DT de política como dado, lida por um adaptador
  `politica`; DT do adaptador `cambio`; decidir como a CLI recebe a política
  e as cotações sem quebrar a interface fixa `calcular --input X --output Y`
  (opções a avaliar: flags opcionais com caminho padrão, caminho convencional
  ao lado da entrada, variável de ambiente); atualizar o modelo (§3.1, §3.2)
  e o esquema de saída (§3.3) com moeda, cotação e tabela aplicada; exit code
  para política ou cotações ilegíveis (DT-008); declarar `specs/` como
  entrada dos testes (T-022).
  - **Atende:** RN-013, RN-014, AMB-022, AMB-037; plan §2, §3, §4, DT-008, DT-009
  - **Aceite:** plan.md versão 2.0, baseado na spec 2.0; cada DT nova cita a RN
    ou a AMB que implementa, e nenhuma decide regra de negócio; o autor revisa
    o diff antes do commit `docs(plan):`.
  - **Commit:** —

- [ ] **T-022** — Declarar `specs/` como entrada da tarefa de teste do Gradle,
  para que os testes que leem a spec rodem quando só a spec muda (achado de
  processo da D-012).
  - **Atende:** plan §6 (testes de rastreabilidade); D-012
  - **Aceite:** `AmbienteTest` passa:
    `T-022 a tarefa de teste declara specs como entrada`;
    e verificação manual registrada na mensagem do commit: com o build verde,
    alterar uma linha da §4.1 e rodar `./gradlew test`, sem `--rerun-tasks`,
    faz o `OrdemCamposSecao41Test` rodar e falhar.
  - **Commit:** —

- [ ] **T-023** — Modelo v2 (plan §3, versão 2.0): `CampoDespesa.MOEDA` entre
  `VALOR` e `NOTA_FISCAL`; `Falha.MOEDA_SEM_COTACAO`; causas de lote
  `PoliticaInvalida` e `CotacoesInvalidas`; `LoteLido` com o centro de custo
  e `DespesaLida` com a moeda, como `Lido<String>`.
  - **Atende:** AMB-021, AMB-029, AMB-033, AMB-037; RN-010
  - **Aceite:** passam
    `OrdemCamposSecao41Test` › `§4-1 AMB-021 ordem de CampoDespesa é a ordem da tabela de despesa da spec`,
    com "Moeda" → `MOEDA` na correspondência, e
    `FronteiraDominioTest` › `DT-009 nenhum arquivo do dominio importa kotlinx ou java io`
  - **Commit:** —

- [ ] **T-024** — RN-013: política como dado. Substituir o `enum Categoria` com
  limites e o `object Politica` por um tipo de domínio com tabela padrão,
  tabelas por centro de custo e limiar; resolver a tabela do lote (código
  exato, padrão como alternativa, categoria ausente ou com limite 0,00
  inelegível); `limiteAplicavel` passa a receber a tabela; `ResultadoLote.Avaliado`
  traz a tabela aplicada. Uma política de teste com a tabela padrão 60,00,
  80,00 e 250,00 mantém verdes os testes das T-001 a T-013, que constroem
  lotes sem centro de custo.
  - **Atende:** RN-013, RN-001, RN-002, RN-008, AMB-022, AMB-023, AMB-024,
    AMB-025, AMB-026, AMB-034, AMB-035
  - **Aceite:** `RN013TabelaAplicavelTest` passa, e os testes anteriores continuam passando:
    `RN-013 AMB-022 tabela padrão vem da política e alterar o seu limite altera o resultado`,
    `RN-013 c-0912 no CC-COMERCIAL usa a tabela do CC-COMERCIAL`,
    `RN-013 AMB-023 e-001 representacao 340,00 no CC-COMERCIAL → limite 300,00, reembolso 300,00 parcial`,
    `RN-013 AMB-023 f-003 representacao sob a tabela padrão → recusada com CATEGORIA_INELEGIVEL`,
    `RN-013 AMB-023 hospedagem no CC-ADM → recusada com CATEGORIA_INELEGIVEL sem consultar a tabela padrão`,
    `RN-013 AMB-024 lote sem centro de custo ou com centro de tipo incompatível → tabela padrão`,
    `RN-013 AMB-025 cc-comercial em minúsculas → tabela padrão e alimentacao 85,00 → 60,00 parcial`,
    `RN-013 AMB-026 d-010 hospedagem com limite 0,00 no CC-ENG-PLATAFORMA → recusada com CATEGORIA_INELEGIVEL`,
    `RN-013 AMB-026 d-013 no CC-ENG-PLATAFORMA → motivos CATEGORIA_INELEGIVEL e SEM_NOTA_FISCAL`,
    `RN-013 resultado do lote informa a tabela aplicada`,
    `RN-001 AMB-035 d-001 e d-002 sob CC-ENG-PLATAFORMA → 72,50 e 38,00, total 110,50 no dia`,
    `RN-001 AMB-034 e-007 hospedagem 1200,00 no CC-COMERCIAL → limite 400,00 sem acréscimo em viagem`,
    `RN-002 e-008 alimentacao 95,00 no CC-COMERCIAL → 90,00 parcial e excedente 5,00`,
    `RN-008 d-014 ALIMENTACAO 61,00 sob CC-ENG-PLATAFORMA → 61,00 integral`
  - **Commit:** —

- [ ] **T-025** — RN-014: conversão para reais na etapa 0, antes da RN-011 (§8):
  valor lançado × cotação da data ou da anterior mais recente, normalizado uma
  vez; moeda sem cotação aplicável e moeda inválida como erro de item.
  - **Atende:** RN-014, RN-011, RN-010, AMB-027, AMB-028, AMB-029, AMB-030,
    AMB-033, AMB-034
  - **Aceite:** `RN014ConversaoTest` passa:
    `RN-014 e-002 22,00 EUR em 14-07 → 130,46 e 90,00 parcial`,
    `RN-014 e-003 14,50 EUR em 15-07 → 85,26 integral`,
    `RN-014 AMB-027 e-004 sábado 18-07 → cotação de 17-07, 178,80 e excedente 88,80`,
    `RN-014 AMB-028 USD em 31-07 → cotação de 28-07`,
    `RN-014 AMB-028 EUR em 02-07 sem cotação anterior → erro de item moeda sem cotação`,
    `RN-014 AMB-029 e-006 55,00 GBP → erro de item moeda sem cotação e as demais processadas`,
    `RN-014 e-010 sem moeda → BRL sem conversão`,
    `RN-014 f-004 12,00 USD em 21-07 → 65,76 integral`,
    `RN-014 AMB-034 e-002 em EUR não amplia o limite de 90,00`;
    mais, nos arquivos das RN correspondentes:
    `RN-011 AMB-030 33,333 EUR a 5,93 → 197,66 e não 197,65`,
    `RN-010 AMB-033 moeda eur, EURO ou numérica → erro de item tipo incompatível`,
    `RN-010 AMB-033 moeda informada sem valor → erro de item e não BRL`,
    `RN-010 AMB-021 moeda sem cotação e valor ausente → as duas causas na ordem da §4-1`
  - **Commit:** —

- [ ] **T-026** — RN-003 com o limiar lido da política e comparado em reais;
  RN-009 com a chave em reais.
  - **Atende:** RN-003, RN-004, RN-009, AMB-022, AMB-031, AMB-032
  - **Aceite:** passam, nos arquivos das RN:
    `RN-003 AMB-031 e-005 40,00 USD = 220,00 sem NF → nota exigida e ausente`,
    `RN-003 AMB-031 e-003 14,50 EUR = 85,26 sem NF → nota não exigida`,
    `RN-003 AMB-022 limiar lido da política e com limiar de 150,00, transporte de 120,00 sem NF não exige nota`,
    `RN-004 e-005 → recusada com SEM_NOTA_FISCAL apesar do limite de 150,00`,
    `RN-009 AMB-032 22,00 EUR e 130,46 BRL no mesmo dia → grupo de duplicatas`,
    `RN-009 AMB-032 22,00 EUR e 22,00 BRL → não são duplicatas`
  - **Commit:** —

- [ ] **T-027** — RN-010, erro de lote: política e cotações inválidas, com as
  demais causas detectáveis.
  - **Atende:** RN-010, AMB-037
  - **Aceite:** `RN010ValidacaoDeLoteTest` passa, além dos testes da T-005:
    `RN-010 AMB-037 política sem tabela padrão, sem limiar ou com limite negativo → lote rejeitado`,
    `RN-010 AMB-037 cotação com taxa 0,00 → lote rejeitado mesmo só com despesas em BRL`,
    `RN-010 AMB-037 política inválida e início posterior ao fim → as duas causas`
  - **Commit:** —

- [ ] **T-028** — Adaptadores de entrada v2: `entrada` lê o centro de custo e a
  moeda; adaptadores novos `politica` e `cambio`, de JSON para o domínio;
  arquivo ilegível vira causa de lote. Complementa a T-017.
  - **Atende:** RN-010, RN-013, RN-014, AMB-015, AMB-024, AMB-026, AMB-033,
    AMB-035, AMB-037
  - **Aceite:** passam:
    `EntradaJsonTest` › `AMB-024 centro_custo ausente, null ou numérico → tabela padrão`,
    `EntradaJsonTest` › `AMB-033 moeda null, vazia ou numérica → erro de item tipo incompatível`,
    `EntradaJsonTest` › `AMB-033 moeda ausente → BRL`,
    `PoliticaJsonTest` › `RN-013 politica-v4 lida com tabela padrão, quatro centros de custo e limiar 100,00`,
    `PoliticaJsonTest` › `AMB-037 política ausente ou com JSON inválido → lote rejeitado`,
    `PoliticaJsonTest` › `AMB-026 AMB-035 periodicidade, observacao e acrescimo_em_viagem não chegam ao domínio`,
    `CambioJsonTest` › `RN-014 cambio lido com 12 datas e duas moedas`,
    `CambioJsonTest` › `AMB-037 cotações ausentes ou com taxa não numérica → lote rejeitado`
  - **Commit:** —

- [ ] **T-029** — Saída v2: valor e moeda originais e cotação usada em cada
  despesa convertida; tabela aplicada no consolidado; causa moeda sem
  cotação. Complementa a T-018.
  - **Atende:** §4.2, RN-013, RN-014, AMB-027, AMB-029
  - **Aceite:** `SaidaJsonTest` passa, além dos testes da T-018:
    `§4-2 RN-014 despesa em moeda estrangeira traz valor e moeda originais e a cotação usada`,
    `§4-2 AMB-027 e-004 traz a cotação de 17-07`,
    `§4-2 RN-014 despesa em BRL não traz cotação`,
    `§4-2 AMB-029 e-006 traz a causa moeda sem cotação e não traz valor considerado`,
    `§4-2 RN-013 consolidado traz a tabela aplicada`
  - **Commit:** —

- [ ] **T-030** — CLI v2: recebe a política e as cotações conforme a DT da
  T-021, mantendo `calcular --input X --output Y`. Complementa a T-019.
  - **Atende:** RN-010, AMB-037; plan DT-008
  - **Aceite:** `CliTest` passa, além dos testes da T-019:
    `DT-008 calcular --input --output continua aceito`,
    `DT-008 calcular com a política e as cotações do envelope → exit 0`,
    `DT-008 AMB-037 política ausente → lote rejeitado e exit 1`
  - **Commit:** —

- [ ] **T-031** — Ponta a ponta da §9 v2: o lote de exemplo e os dois lotes do
  envelope, com `politica-v4.json` e `cambio.json`. Substitui os aceites da
  T-020.
  - **Atende:** §9, RN-001, RN-009, RN-013, RN-014, AMB-036, AMB-038
  - **Aceite:** `Secao9CriteriosDeAceiteTest` passa:
    `§9 AMB-036 lote de exemplo sob CC-ENG-PLATAFORMA → total 386,93 com 6 integrais, 1 parcial, 6 recusadas e 1 erro`,
    `§9 lote de exemplo produz o resultado da tabela da §9 despesa a despesa`,
    `§9 despesas-envelope no CC-COMERCIAL → total 1143,26 com 2 integrais, 5 parciais, 2 recusadas e 1 erro`,
    `§9 despesas-envelope-cc-desconhecido sob a tabela padrão → total 373,76 com 2 integrais, 1 parcial e 1 recusada`,
    `§9 cada despesa aparece exatamente uma vez na ordem da entrada`,
    `§9 a mesma entrada produz sempre a mesma saída`,
    `§9 RN-001 RN-009 inverter a ordem dos três lotes não altera nenhum resultado`,
    `§9 AMB-038 nenhum resultado dos três lotes tem estado de pendência`
  - **Commit:** —

- [ ] **T-032** — Os seis casos novos da §7, que passa a ter 26 linhas.
  Complementa a T-015.
  - **Atende:** §7; RN-001 a RN-004, RN-008, RN-010, RN-013, RN-014
  - **Aceite:** `Secao7CasosDeBordaTest` passa, com os 20 testes da T-015 e mais:
    `§7 RN-008 RN-013 e-001 representacao no CC-COMERCIAL → 300,00 parcial e f-003 sob a padrão → recusada`,
    `§7 RN-008 RN-013 d-010 sob CC-ENG-PLATAFORMA com limite 0,00 → recusada com CATEGORIA_INELEGIVEL`,
    `§7 RN-001 RN-002 RN-014 e-002 22,00 EUR → 90,00 parcial e excedente 40,46`,
    `§7 RN-014 e-004 no sábado → cotação de 17-07 e 90,00 parcial`,
    `§7 RN-003 RN-004 RN-014 e-005 40,00 USD sem NF → recusada com SEM_NOTA_FISCAL`,
    `§7 RN-010 RN-014 e-006 em GBP → erro de item moeda sem cotação`
  - **Commit:** —

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
| RN-001 | T-008, T-024 | `RN-001 AMB-001 d-001 alimentacao 72,50 → reembolso 60,00 parcial`; `RN-001 AMB-001 AMB-003 três alimentações de 59,00 no mesmo dia → 59,00 cada e 177,00 no total`; `§9 RN-001 RN-009 inverter a ordem do lote de exemplo não altera nenhum resultado` (T-020) |
| RN-002 | T-008, T-024 | `RN-002 AMB-002 alimentacao de 60,01 → reembolso 60,00 parcial e excedente 0,01`; `RN-002 transporte urbano de 90,00 → reembolso 80,00 parcial e excedente 10,00` |
| RN-003 | T-011, T-026 | `RN-003 AMB-004 d-003 transporte 100,00 sem NF → nota não exigida, reembolso 80,00 parcial`; `RN-003 RN-011 AMB-014 100,004 → nota não exigida e 100,005 → nota exigida` |
| RN-004 | T-011, T-026 | `RN-004 AMB-005 d-004 → reembolso 0,00 recusada com SEM_NOTA_FISCAL`; `RN-004 AMB-005 hospedagem 200,00 sem NF → recusada com SEM_NOTA_FISCAL apesar de estar abaixo do limite` |
| RN-005 | T-009 | `RN-005 AMB-016 d-014 em 2026-07-31 igual à data de fim → dentro do período`; `RN-005 AMB-016 despesa em 2026-07-01 igual à data de início → dentro do período` |
| RN-006 | T-009 | `RN-006 AMB-010 d-008 alimentacao 41,00 com NF → recusada com FORA_DA_COMPETENCIA sem limite aplicado` |
| RN-007 | T-012 | `RN-007 AMB-018 150,00 sem NF fora do período → motivos FORA_DA_COMPETENCIA e SEM_NOTA_FISCAL nessa ordem`; `RN-007 AMB-017 d-009 erro → lista de motivos vazia` |
| RN-008 | T-010, T-024 | `RN-008 AMB-013 AMB-017 d-005 coworking → recusada com CATEGORIA_INELEGIVEL e não erro`; `RN-008 AMB-013 d-014 ALIMENTACAO 61,00 → categoria alimentacao, reembolso 60,00 parcial` |
| RN-009 | T-013, T-017, T-026 | `RN-009 AMB-011 d-006 e d-007 → d-006 integral e d-007 recusada com DUPLICATA mantida d-006`; `RN-009 identificador fora do BMP é comparado por code point`; `RN-009 AMB-011 iguais no critério com descrições diferentes → grupo de duplicatas` (T-017) |
| RN-010 | T-005, T-006, T-007, T-014, T-017, T-019, T-025, T-027, T-028, T-030 | `RN-010 AMB-017 início posterior ao fim → lote rejeitado sem despesa processada` (T-005); `RN-010 AMB-021 sem fornecedor e valor −5,00 → causas fornecedor ausente e valor não positivo nessa ordem` (T-006); `RN-010 AMB-020 despesa sem identificador na terceira posição → erro identificado pela posição 3` (T-007); `RN-010 lote sem despesas → total 0,00 e zero em todas as classificações` (T-014); `RN-010 raiz JSON que é uma lista ou um número → rejeitado com causa única ENTRADA_ILEGIVEL` (T-017); `RN-010 DT-008 arquivo de entrada inexistente → rejeitado com ENTRADA_ILEGIVEL e exit 1` (T-019) |
| RN-011 | T-002, T-006, T-008, T-011, T-013, T-017, T-025 | `RN-011 AMB-014 100,005 normaliza para 100,01 e 100,004 para 100,00` (T-002); `RN-011 AMB-014 0,004 normaliza para 0,00 e é erro de item` (T-006); `RN-011 d-011 33,333 → reembolso 33,33 integral` (T-008); `RN-011 AMB-014 54,90 e 54,904 → grupo de duplicatas` (T-013); `RN-011 33,333 é lido do texto cru sem passar por Double` (T-017) |
| RN-012 | T-005, T-007, T-018 | `RN-012 AMB-019 duas despesas com identificador d-006 → lote rejeitado sem avaliar nenhuma`; `RN-012 AMB-020 d-006 com espaço à esquerda repete d-006 → lote rejeitado` (T-005); `RN-012 AMB-020 duas despesas sem identificador → ambas erro de item e lote não rejeitado` (T-007); `RN-012 AMB-020 identificador com espaços nas extremidades sai aparado` (T-018) |
| RN-013 | T-024, T-026, T-028, T-029, T-031 | `RN-013 AMB-023 e-001 representacao 340,00 no CC-COMERCIAL → limite 300,00, reembolso 300,00 parcial`; `RN-013 AMB-026 d-010 hospedagem com limite 0,00 no CC-ENG-PLATAFORMA → recusada com CATEGORIA_INELEGIVEL` (T-024); `§4-2 RN-013 consolidado traz a tabela aplicada` (T-029) |
| RN-014 | T-025, T-028, T-029, T-031 | `RN-014 AMB-027 e-004 sábado 18-07 → cotação de 17-07, 178,80 e excedente 88,80`; `RN-014 AMB-029 e-006 55,00 GBP → erro de item moeda sem cotação e as demais processadas` (T-025); `§4-2 RN-014 despesa em moeda estrangeira traz valor e moeda originais e a cotação usada` (T-029) |

### Ambiguidades

| Regra da spec | Task | Teste |
|---|---|---|
| AMB-001 | T-008 | `RN-001 AMB-001 AMB-003 três alimentações de 59,00 no mesmo dia → 59,00 cada e 177,00 no total`; `RN-001 AMB-001 dia 03-07 totaliza 98,00 em alimentacao` |
| AMB-002 | T-008 | `RN-002 AMB-002 alimentacao de 60,00 → reembolso 60,00 integral`; `RN-002 AMB-002 alimentacao de 60,01 → reembolso 60,00 parcial e excedente 0,01` |
| AMB-003 — **dissolvida** pela AMB-001; sem decisão própria, isenta no `RastreabilidadeTest` | T-008 (teste que demonstra a dissolução) | `RN-001 AMB-003 inverter a ordem de d-001 e d-002 não altera os valores`; `RN-001 AMB-001 AMB-003 três alimentações de 59,00 no mesmo dia → 59,00 cada e 177,00 no total` |
| AMB-004 | T-011 | `RN-003 AMB-004 d-003 transporte 100,00 sem NF → nota não exigida, reembolso 80,00 parcial`; `RN-003 AMB-004 transporte 100,01 com NF → nota exigida e presente, reembolso 80,00 parcial` |
| AMB-005 | T-011 | `RN-004 AMB-005 hospedagem 200,00 sem NF → recusada com SEM_NOTA_FISCAL apesar de estar abaixo do limite`; `RN-004 AMB-005 alimentacao 500,00 sem NF → recusada com SEM_NOTA_FISCAL` |
| AMB-006 | T-011 | `RN-003 AMB-006 alimentacao 500,00 sem NF → nota exigida e ausente apesar do teto de 60,00`; `RN-003 AMB-004 AMB-006 d-004 transporte 100,01 sem NF → nota exigida e ausente` |
| AMB-007 | T-008, T-024 (mantida pela AMB-034) | `RN-001 AMB-007 AMB-009 d-010 hospedagem 480,00 → limite 250,00, reembolso 250,00 parcial, excedente 230,00`; `AMB-007 d-011 no mesmo fornecedor de d-010 mantém limite 60,00` |
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
| AMB-021 | T-004, T-006, T-007, T-023, T-025 | `§4-1 AMB-021 ordem de CampoDespesa é a ordem da tabela de despesa da spec` (T-004); `RN-010 AMB-021 sem fornecedor e valor −5,00 → causas fornecedor ausente e valor não positivo nessa ordem` (T-006); `AMB-021 sem identificador e data 2026-02-30 → causas identificador ausente e data inexistente nessa ordem` (T-007) |
| AMB-022 | T-024, T-026 | `RN-013 AMB-022 tabela padrão vem da política e alterar o seu limite altera o resultado`; `RN-003 AMB-022 limiar lido da política e com limiar de 150,00, transporte de 120,00 sem NF não exige nota` (T-026) |
| AMB-023 | T-024 | `RN-013 AMB-023 f-003 representacao sob a tabela padrão → recusada com CATEGORIA_INELEGIVEL`; `RN-013 AMB-023 hospedagem no CC-ADM → recusada com CATEGORIA_INELEGIVEL sem consultar a tabela padrão` |
| AMB-024 | T-024, T-028 | `RN-013 AMB-024 lote sem centro de custo ou com centro de tipo incompatível → tabela padrão`; `AMB-024 centro_custo ausente, null ou numérico → tabela padrão` (T-028) |
| AMB-025 | T-024 | `RN-013 AMB-025 cc-comercial em minúsculas → tabela padrão e alimentacao 85,00 → 60,00 parcial` |
| AMB-026 | T-024, T-028 | `RN-013 AMB-026 d-010 hospedagem com limite 0,00 no CC-ENG-PLATAFORMA → recusada com CATEGORIA_INELEGIVEL`; `RN-013 AMB-026 d-013 no CC-ENG-PLATAFORMA → motivos CATEGORIA_INELEGIVEL e SEM_NOTA_FISCAL` |
| AMB-027 | T-025, T-029 | `RN-014 AMB-027 e-004 sábado 18-07 → cotação de 17-07, 178,80 e excedente 88,80`; `§4-2 AMB-027 e-004 traz a cotação de 17-07` (T-029) |
| AMB-028 | T-025 | `RN-014 AMB-028 USD em 31-07 → cotação de 28-07`; `RN-014 AMB-028 EUR em 02-07 sem cotação anterior → erro de item moeda sem cotação` |
| AMB-029 | T-023, T-025, T-029 | `RN-014 AMB-029 e-006 55,00 GBP → erro de item moeda sem cotação e as demais processadas` (T-025); `§4-2 AMB-029 e-006 traz a causa moeda sem cotação e não traz valor considerado` (T-029) |
| AMB-030 | T-025 | `RN-011 AMB-030 33,333 EUR a 5,93 → 197,66 e não 197,65` |
| AMB-031 | T-026 | `RN-003 AMB-031 e-005 40,00 USD = 220,00 sem NF → nota exigida e ausente`; `RN-003 AMB-031 e-003 14,50 EUR = 85,26 sem NF → nota não exigida` |
| AMB-032 | T-026 | `RN-009 AMB-032 22,00 EUR e 130,46 BRL no mesmo dia → grupo de duplicatas`; `RN-009 AMB-032 22,00 EUR e 22,00 BRL → não são duplicatas` |
| AMB-033 | T-023, T-025, T-028 | `RN-010 AMB-033 moeda eur, EURO ou numérica → erro de item tipo incompatível` (T-025); `AMB-033 moeda ausente → BRL` (T-028) |
| AMB-034 | T-024, T-025 | `RN-001 AMB-034 e-007 hospedagem 1200,00 no CC-COMERCIAL → limite 400,00 sem acréscimo em viagem` (T-024); `RN-014 AMB-034 e-002 em EUR não amplia o limite de 90,00` (T-025) |
| AMB-035 | T-024, T-028 | `RN-001 AMB-035 d-001 e d-002 sob CC-ENG-PLATAFORMA → 72,50 e 38,00, total 110,50 no dia` (T-024); `AMB-026 AMB-035 periodicidade, observacao e acrescimo_em_viagem não chegam ao domínio` (T-028) |
| AMB-036 | T-031 | `§9 AMB-036 lote de exemplo sob CC-ENG-PLATAFORMA → total 386,93 com 6 integrais, 1 parcial, 6 recusadas e 1 erro` |
| AMB-037 | T-023, T-027, T-028, T-030 | `RN-010 AMB-037 cotação com taxa 0,00 → lote rejeitado mesmo só com despesas em BRL` (T-027); `AMB-037 política ausente ou com JSON inválido → lote rejeitado` (T-028); `DT-008 AMB-037 política ausente → lote rejeitado e exit 1` (T-030) |
| AMB-038 | T-031 | `§9 AMB-038 nenhum resultado dos três lotes tem estado de pendência` |

### Seções da spec

| Seção | Task | Teste |
|---|---|---|
| §4.1 — espaços nas extremidades | T-003 | `§4-1 aparar remove U+0085 e não remove U+001C a U+001F` |
| §4.2 — saída por despesa e consolidado | T-014, T-018, T-029 | `§4-2 AMB-014 total reembolsado é a soma dos reembolsos dos itens`; `§4-2 motivos aparece sempre e campos não aplicáveis são omitidos` |
| §7 — casos de borda | T-015, T-032 | `Secao7CasosDeBordaTest` (20 testes `§7`) |
| §8 — ordem de aplicação | T-004, T-012, T-025 | `§8 AMB-018 ordem de Motivo é a ordem da lista de elegibilidade da spec`; `§8 despesa recusada não tem limite aplicado` |
| §9 — critérios de aceite | T-020, T-031 | `§9 lote de exemplo produz total 623,43 com 4 integrais, 4 parciais, 5 recusadas e 1 erro` |
