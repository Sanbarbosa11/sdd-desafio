# CLAUDE.md

## O projeto

Motor de cálculo de reembolso de despesas corporativas. CLI que lê um JSON de
despesas e emite um JSON com o valor reembolsável e a justificativa de cada item.

## Fonte da verdade

`specs/001-motor-reembolso/spec.md` define **o que** o sistema faz.
`specs/001-motor-reembolso/plan.md` define **como**.
`specs/001-motor-reembolso/tasks.md` define **em que ordem**.

Quando o código e a spec discordarem, a spec está certa e o código é o bug —
a menos que a spec esteja errada, e nesse caso corrigimos a spec primeiro e
registramos em `DECISIONS.md`.

**Antes de implementar qualquer coisa, leia a task correspondente em `tasks.md`.**
Se o que eu pedi não está coberto por nenhuma task, me avise em vez de implementar.

## Regras de trabalho

- Toda regra de negócio vive na spec, não no chat e não em comentário de código.
- Se eu te explicar uma regra que não está na spec, **pare e me diga isso** antes
  de escrever código. Isso é um bug de spec.
- Todo commit referencia uma task: `feat(T-003): <descrição>`.
  Mudanças de documentação: `docs(spec):`, `docs(plan):`, `docs(tasks):`.
- Nenhuma regra de negócio entra sem teste.

## Stack e comandos

- Linguagem: Kotlin 2.0.21, bytecode e API do Java 17 (DT-001). Gradle 8.11.1
  via wrapper; exige `JAVA_HOME` com JDK 17+ (nesta máquina, o JDK 21 do
  Android Studio).
- Rodar: **ainda não implementado** — a CLI vem na T-019 (e recebe política e
  câmbio na T-030). Contrato previsto:
  `./gradlew run --args="calcular --input exemplos/despesas-exemplo.json --output saida.json"`.
  Hoje `main` está vazio e o `run` não faz nada.
- Testes: `./gradlew test` (um só: `./gradlew test --tests 'reembolso.regras.RN002*'`)
- Lint/format: nenhum; `./gradlew build` é o check.

## Convenções de código

- Layout (DT-002): `src/` e `tests/` na raiz, pacotes direto abaixo delas
  (`src/reembolso/dominio/…`). Sem pasta de recursos.
- Fronteira (plan §2): `reembolso.dominio` contém toda regra e só usa
  `java.time` e `java.math` — nunca `kotlinx.*` nem `java.io.*` (DT-009, teste
  de arquitetura). `entrada`, `saida` e `cli` traduzem e não decidem nada.
- Entrada: árvore JSON traduzida para `Lido<T>` por campo (DT-004); campos sem
  regra não são lidos.
- Texto: `aparar()` do domínio, nunca `.trim()`/`.strip()` (DT-005, R-05);
  minúsculas com `lowercase()`.
- Valores monetários: `BigDecimal` encapsulado em `value class Valor`, sempre
  escala 2 com `HALF_UP`, criado do texto cru do JSON e escrito com
  `toPlainString()`. Nunca `Double` nem `Long` em centavos (DT-003, R-04).
- Política (limites, limiar de NF): constantes em `src/reembolso/dominio/Politica.kt`,
  não em arquivo de configuração.
- Erros de entrada são resultado (`ResultadoLote.Rejeitado`, `ResultadoDespesa.ComErro`),
  não exceção. Exit codes da CLI na DT-008.
- Nomes de teste entre crases, começando pelo ID do requisito (`RN-002`,
  `§7`, `§4-1`…) e, quando verificam uma ambiguidade, também o `AMB-xxx`. Um
  arquivo por RN em `tests/reembolso/regras/`. O `RastreabilidadeTest` usa
  esses nomes para exigir cobertura.

## Fora de escopo

- Determinar se o colaborador está em viagem e aplicar a ampliação de 50% da
  política (spec §3, AMB-007).
- Dar efeito a campos sem regra: dados do colaborador, rótulo de competência,
  descrição (spec §3, AMB-015).
- Teto diário agregado, comprovação alternativa à nota fiscal, tratamento de
  dia não útil (spec §10).
- Tudo que não é a CLI `calcular`: sem API, banco, interface ou arquivo de
  configuração da política.
