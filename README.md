# Motor de cálculo de reembolso

Motor que decide quanto de cada despesa de trabalho de um colaborador é
reembolsável, pela política do RH, e justifica cada decisão. Ele recebe um
lote (as despesas de um colaborador num período) e devolve, para cada
despesa, a classificação (integral, parcial, recusada ou erro), o valor
reembolsado, o limite aplicado e os motivos. Para o lote, devolve o
consolidado.

O projeto foi feito em Spec-Driven Development: a regra de negócio vive na
spec, e o código é verificado contra ela. As instruções do desafio que
originou o projeto estão em [`docs/README-desafio.md`](docs/README-desafio.md).

## Estado atual

- **Implementado**, no domínio (`src/reembolso/dominio/`), sem entrada nem
  saída JSON:
  - validação de lote e de item (RN-010, RN-012);
  - a etapa 1 da §8 completa, a elegibilidade: período, categoria, nota
    fiscal e duplicidade (RN-003 a RN-009);
  - a etapa de valor: limite e reembolso parcial (RN-001, RN-002);
  - normalização monetária (RN-011).

  São as T-001 a T-013, mais a T-023 (o campo moeda na ordem da §4.1 da
  spec 2.0), com **112 testes**.
- **Em aberto:**
  - T-014 a T-020: consolidado completo, casos de borda da §7,
    `RastreabilidadeTest`, entrada e saída JSON, CLI e ponta a ponta;
  - T-021, T-022 e T-024 a T-032: o restante da política v4 do Dia 2
    (limites por centro de custo e conversão de moeda estrangeira).
- **A spec está à frente do código.** A spec 2.0 já absorveu a política v4
  (D-012); o código ainda implementa a política v3.
- **Ainda não há CLI.** `calcular --input X --output Y` é a T-019. O
  `./gradlew run` existe, mas `main` está vazio e não faz nada.

## Estado do build

`./gradlew build` passa: **112 testes, nenhuma falha**.

Cuidado com um build verde depois de mudar **só** a spec: o Gradle ainda não
declara `specs/` como entrada da tarefa de teste e pode considerar os testes
atualizados sem rodá-los. Para forçar, use `./gradlew build --rerun-tasks`. A
correção é a T-022.

## Como buildar e testar

Pré-requisito: JDK 17 ou superior, apontado por `JAVA_HOME`. O projeto compila
para Java 17 (DT-001). O Gradle Wrapper 8.11.1 está versionado, e o primeiro
build baixa a distribuição e as dependências.

```bash
./gradlew build                 # compila e roda todos os testes
./gradlew test                  # só os testes
./gradlew test --tests 'reembolso.regras.RN009*'   # um arquivo de teste
./gradlew build --rerun-tasks   # força a execução dos testes
```

No Windows, use `gradlew.bat` no lugar de `./gradlew`.

O relatório de testes fica em `build/reports/tests/test/index.html`.

## Estrutura

```
.
├── CLAUDE.md                        convenções do projeto para o agente
├── src/reembolso/dominio/           regras de negócio (sem I/O; DT-009)
├── tests/reembolso/
│   ├── regras/                      um arquivo de teste por RN
│   ├── dominio/                     aparar() e espaços Unicode
│   └── rastreabilidade/             testes que leem a spec (§4.1, §8, fronteira)
├── specs/001-motor-reembolso/
│   ├── spec.md                      o QUÊ e o PORQUÊ (versão 2.0)
│   ├── plan.md                      o COMO (versão 1.1; a 2.0 é a T-021)
│   ├── tasks.md                     T-001 a T-032, com aceite e cobertura
│   └── DECISIONS.md                 D-001 a D-012, toda mudança de spec
├── exemplos/
│   ├── despesas-exemplo.json        lote do Dia 1
│   └── envelope/                    política v4, câmbio e lotes do Dia 2
└── docs/
    ├── RELATORIO.md                 relatório final
    ├── analise-envelope.md          análise da v4, feita antes de mexer na spec
    ├── notas-trabalho.md            notas do autor durante o trabalho
    ├── sessions/                    exports das sessões com o Claude Code
    └── README-desafio.md            instruções originais do desafio
```

`src/` e `tests/` ficam na raiz por exigência do enunciado. O
`build.gradle.kts` aponta os source sets para eles (DT-002).

## Onde ler as decisões

- **A regra de negócio:** `spec.md`, com as RN-001 a RN-014 e as 38
  ambiguidades da política decididas na §6 (AMB-001 a AMB-038).
- **O porquê de cada mudança na spec:** `DECISIONS.md`.
- **Quais testes cobrem cada RN e AMB:** a matriz de cobertura no fim de
  `tasks.md`.
