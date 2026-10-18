# Log de Decisões e Mudanças de Spec

> Uma entrada **toda vez** que a spec mudar. Este arquivo é a prova de que a spec
> foi tratada como artefato vivo e não como cerimônia de abertura.
>
> Spec que não muda em dois dias é spec que ninguém consultou. Mudança não é
> demérito — mudança não registrada é.

Ordem cronológica inversa: a mais recente primeiro.

---

## D-006 — Referências à normalização monetária e ordem da etapa 0 após AMB-014 e 015 · `2026-10-18`

**Gatilho:** a decisão de precisão monetária (AMB-014) resolveu uma
pendência citada em regras já salvas e fixou uma ordem dentro da etapa de
validação; a AMB-015 acrescentou um item à seção de fora de escopo.

**O que mudou na spec:**
- RN-003, Regra: de "já normalizado conforme a AMB-014" para "conforme a
  RN-011". Depende de: de "AMB-014 (precisão monetária, …), pendente" para
  "RN-011 (normalização monetária, …)".
- RN-009, Regra: de "valor normalizado (AMB-014)" para "(RN-011)". Depende
  de: de "AMB-014 (valor normalizado), pendente" para "RN-011 (valor
  normalizado)".
- RN-010, Regra: de "valor menor ou igual a zero" para "valor normalizado
  (RN-011) menor ou igual a zero"; acrescentado que a faixa é avaliada sobre
  o valor já normalizado, e não sobre o valor escrito na entrada. Origem:
  acrescentada a AMB-014. Depende de: acrescentada a RN-011. Aceite:
  acrescentado o caso de 0,004.
- §8, etapa 0: de "Validação (RN-010)" para "Normalização e validação" —
  primeiro a normalização monetária (RN-011), depois a validação, com o
  motivo da ordem declarado.
- §3: acrescentado que o sistema não atribui efeito a campos da entrada que
  nenhuma regra utilize (AMB-015).
- §7: acrescentado d-011.
- RN-003, Aceite: acrescentados os casos de 100,004 (nota não exigida) e
  100,005 (nota exigida), para que as fronteiras da regra de nota fiscal
  estejam na própria regra.
- AMB-017, Decisão: de "valor menor ou igual a zero" e "um valor igual ou
  inferior a zero" para "valor normalizado (AMB-014)…", para que quem lesse
  apenas a AMB-017 não validasse o valor bruto.
- §10, "Contrato de entrada e representação de erros": registrado como já
  decidido que os campos sem regra associada são opcionais e o fornecedor é
  obrigatório (AMB-015), reduzindo a pendência ao restante do contrato.

**Por quê:** a spec descreve o estado atual do sistema. Regras que citavam a
AMB-014 como pendente e uma validação de faixa sem indicar sobre qual valor
opera permitiriam implementar a validação antes da normalização, aceitando
0,004 como valor positivo.

**O que isso invalidou:** nenhum critério de aceite. As referências da
AMB-004, da AMB-006 e da AMB-011 ao "valor normalizado (AMB-014)" permanecem
corretas, por apontarem para a decisão que agora existe.

**Tasks afetadas:** nenhuma — tasks.md ainda não foi escrito.

**Custo:** 1 arquivo, correção pontual.

---

## D-005 — Registros atualizados após AMB-011, 013 e 017 · `2026-10-18`

**Gatilho:** as decisões de duplicidade (AMB-011), categoria (AMB-013) e
distinção entre recusa e erro (AMB-017) resolveram pendências registradas em
textos já salvos e criaram uma restrição sobre a AMB-009.

**O que mudou na spec:**
- AMB-009, Decisão: acrescentado que "cada lançamento de diária traz a data
  da noite a que se refere, de modo que diárias consecutivas tenham datas
  distintas e não constituam duplicata (AMB-011)". Sem isso, duas noites
  consecutivas no mesmo hotel lançadas na mesma data satisfariam o critério
  de duplicidade.
- AMB-002, Consequências (d-014): de "60,00, parcial (condicionado à
  AMB-013)" para "60,00, parcial; a categoria é normalizada para alimentacao
  (RN-008)".
- RN-007, Regra: de "todas as regras de elegibilidade são avaliadas para toda
  despesa" para "para toda despesa sem erro de item (RN-010)"; acrescentado
  que a classificação assume quatro valores — integral, parcial, recusada ou
  erro — e que uma despesa com erro tem a lista de motivos de recusa vazia.
  Origem: acrescentada a AMB-017. Aceite: acrescentado d-009.
- §8: de duas etapas com dois passos de elegibilidade para três etapas —
  validação, elegibilidade (período, categoria, nota fiscal, duplicidade) e
  valor —, com o motivo de a duplicidade vir por último declarado. Removida a
  ressalva de ordem parcial.
- §7: a linha de d-014 passa a declarar o resultado completo (60,00,
  parcial); acrescentadas d-005, d-006, d-007 e d-009.
- §10: removidos "Diárias e duplicatas", resolvido pela alteração da AMB-009,
  e "Despesa fora do período e duplicidade", confirmado pela RN-009;
  acrescentado "Contrato de entrada e representação de erros", pendente.
- RN-005, Aceite, e AMB-008, Consequências: de "d-001 a d-007 e d-009 a
  d-013 → dentro do período" e "as despesas entre 03/07 e 22/07 estão dentro
  do período" para versões que excluem d-009, erro de item não avaliado pela
  regra de período (RN-010).
- AMB-001, Consequências (d-006 e d-007): de "o tratamento passa a depender
  apenas da regra de duplicata (AMB-011)" para o resultado afirmado — d-006
  54,90, integral; d-007 0,00, motivos [DUPLICATA] (RN-009).
- AMB-005 e AMB-010, Justificativa: de "quarto estado" para "estado
  adicional", já que a classificação passou a ter quatro valores (RN-007).
- AMB-016, Justificativa e Custo aceito: de "criaria um caminho de erro de
  entrada" e "exigiria um caminho de erro que a política não prevê" para
  formulações que remetem ao erro de lote da RN-010, que passou a existir.
- AMB-009, Custo aceito: de "as duas diárias, lançadas separadamente" para
  "lançadas separadamente, cada uma com a data da sua noite".
- AMB-013, Decisão: estendida a normalização ao fornecedor, com o mesmo
  alcance da categoria. RN-008 passa a normalizar categoria e fornecedor;
  RN-009 passa a usar o fornecedor normalizado. AMB-011, Decisão: de
  "fornecedor" para "fornecedor normalizado (AMB-013)", para que a decisão
  descreva o mesmo critério que a RN-009 implementa.
- RN-009, Regra: declarada a ordem lexicográfica dos identificadores, com o
  caso "d-10" precede "d-9" como aceite.
- AMB-011, Consequências: a consequência sobre par com e sem nota fiscal
  passa a valer apenas para valor até R$ 100,00 — acima disso a despesa sem
  nota é recusada pela RN-004 e não integra o grupo.
- AMB-012: registrada como dissolvida pela AMB-017.

**Por quê:** a spec descreve o estado atual do sistema. Uma consequência
condicionada a ambiguidade já decidida, uma regra que avalia "toda despesa"
quando parte delas passou a não ser avaliada, e pendências da §10 já
resolvidas fariam a spec afirmar coisas que deixaram de ser verdade.

**O que isso invalidou:** nenhum critério de aceite. A regra de lançamento da
AMB-009 ficou mais restritiva: diárias de uma mesma estadia lançadas na mesma
data passam a ser tratadas como duplicata.

**Tasks afetadas:** nenhuma — tasks.md ainda não foi escrito.

**Custo:** 1 arquivo, correção pontual.

---

## D-004 — Ordem de aplicação e motivo de recusa reescritos após AMB-008, 010, 016 e 018 · `2026-10-18`

**Gatilho:** a decisão de motivos múltiplos (AMB-018), tornada necessária pelo
segundo motivo de recusa criado na AMB-010, alterou registros já salvos: a
§8, que tratava a recusa por nota fiscal como interrupção da avaliação, e a
RN-004, que declarava um motivo único.

**O que mudou na spec:**
- §8: de uma sequência em que "se a nota é exigida e ausente, o item é
  recusado (RN-004) e não segue para o passo 2" para duas etapas —
  elegibilidade, em que todas as regras são avaliadas e seus motivos
  coletados na ordem período → nota fiscal, e valor, aplicada apenas a
  despesas sem motivo de recusa. A ordem dentro da elegibilidade passa a
  definir a ordenação da lista de motivos.
- RN-004, Regra: de "classificada como recusada, com motivo SEM_NOTA_FISCAL"
  para "com o motivo SEM_NOTA_FISCAL na sua lista de motivos (RN-007)";
  acrescentado que, quando mais de uma causa se aplica, os motivos coexistem
  na lista. Origem: acrescentada a AMB-018. Depende de: acrescentada a
  RN-007.
- Aceites, consequências verificáveis e linhas da §7 (RN-004, AMB-004,
  AMB-005, AMB-006, AMB-009 e §7, além dos novos RN-006, AMB-008 e AMB-010):
  de "motivo SEM_NOTA_FISCAL" e "motivo FORA_DA_COMPETENCIA" para
  "motivos [SEM_NOTA_FISCAL]" e "motivos [FORA_DA_COMPETENCIA]". O texto das
  regras e das decisões mantém o singular, por não ser critério verificável.

**Por quê:** a spec descreve o estado atual do sistema. Com dois motivos de
recusa independentes e a decisão de reportar todos, uma §8 que interrompe a
avaliação na primeira recusa, uma RN-004 com motivo único e aceites que
descrevem o motivo como valor escalar contradiriam a RN-007 — quem
implementasse a partir de um aceite no singular poderia produzir um campo
escalar.

**O que isso invalidou:** nenhum critério de aceite. O valor reembolsado de
todos os itens permanece o mesmo; muda apenas a representação dos motivos e
a garantia de que todos são reportados.

**Tasks afetadas:** nenhuma — tasks.md ainda não foi escrito.

**Custo:** 1 arquivo, correção pontual.

---

## D-003 — Limite de hospedagem fixado em uma diária por lançamento após a AMB-009 · `2026-10-18`

**Gatilho:** a decisão da AMB-009 (leitura a: cada lançamento de hospedagem
vale uma diária) tornou falsa uma afirmação da decisão da AMB-001 e
obsoletos registros que tratavam o número de diárias como pendente ou
variável: a RN-001, a RN-002, a AMB-006 e a AMB-007.

**O que mudou na spec:**
- AMB-001, Decisão: de "um lançamento pode cobrir múltiplas diárias numa
  única data; quantas é objeto da AMB-009, e o limite aplicável a um
  lançamento de hospedagem fica condicionado àquela decisão" para "cada
  lançamento de hospedagem corresponde a uma diária (AMB-009), e seu limite
  é R$ 250,00". O restante da decisão foi preservado.
- RN-001, Regra: de "o limite de um lançamento de hospedagem é R$ 250,00
  multiplicado pelo número de diárias que ele cobre, número definido pela
  AMB-009 (pendente)" para "cada lançamento vale uma diária: R$ 250,00 por
  lançamento (AMB-009)". Origem: acrescentada a AMB-009. Depende de: AMB-009
  removida. Aceite: acrescentado d-010 → 250,00, parcial.
- RN-002, Depende de: de "AMB-009 (…), pendente" para "RN-001 (limite da
  categoria)".
- AMB-006, Justificativa e Consequências (d-013): de "o limite de
  hospedagem nunca é inferior a R$ 250" para "o limite de hospedagem é
  sempre R$ 250,00 (AMB-009)".
- AMB-007, Custo aceito: removida a condicional "caso o lançamento conte uma
  diária"; o resultado de d-010 (250,00 em vez de 375,00) passa a ser
  afirmado.
- AMB-007, Consequências (d-010): de "limite 250,00 por diária, sem
  ampliação; resultado final condicionado à AMB-009" para "limite 250,00,
  sem ampliação; reembolso 250,00, parcial (AMB-009)".
- AMB-004, Consequências (d-010): de "segue para o limite" para "segue para
  o limite de 250,00 → reembolso 250,00, parcial".
- RN-003 e RN-004 (aceites de d-013), AMB-005 (Consequências de d-013) e §7
  (caso de d-013): removida a expressão "qualquer que seja o número de
  diárias". A recusa por ausência de nota precede a avaliação do limite, de
  modo que nem diárias nem descrição entram na conta.
- §5 e §7 (premissa global) e AMB-007 (Consequências): de "R$ 250,00 por
  diária" para "R$ 250,00 por lançamento (uma diária)", para que quem lê sem
  conhecer a AMB-009 não multiplique pelo número de noites.
- RN-001, lista de limites da Regra: de "hospedagem R$ 250,00 por diária"
  para "hospedagem R$ 250,00"; a explicação de que cada lançamento vale uma
  diária fica na frase seguinte da regra, com a origem (AMB-009).
- §7: novo caso de borda para d-010.
- §10: novo item "Diárias e duplicatas", dependência criada por esta decisão
  e levada à AMB-011.

**Por quê:** a spec descreve o estado atual do sistema. A decisão da AMB-001
afirmava algo que a AMB-009 negou, e a RN-001 descrevia uma multiplicação
por um número que o motor não determina.

**O que isso invalidou:** a frase da decisão da AMB-001 sobre múltiplas
diárias por lançamento. Nenhum critério de aceite caiu: os aceites de d-013
e da hospedagem de 200,00 permanecem válidos. A decisão criou uma
dependência nova: com uma diária por lançamento, diárias legítimas no mesmo
hotel e na mesma data ficam indistinguíveis de duplicatas, o que restringe a
AMB-011 (registrado na §10).

**Tasks afetadas:** nenhuma — tasks.md ainda não foi escrito.

**Custo:** 1 arquivo, correção pontual.

---

## D-002 — Premissa de viagem tornada permanente após a AMB-007 · `2026-10-18`

**Gatilho:** a decisão da AMB-007 (leitura a: caracterização de viagem fora
de escopo) tornou obsoletos registros já salvos que tratavam a viagem como
pendência: a premissa global das seções 5 e 7 e os campos "Depende de" das
RN-001, RN-002 e RN-003.

**O que mudou na spec:**
- §5 e §7, premissa global: de "assumem despesas fora de viagem… quando
  ela for decidida, os casos afetados serão revistos" para regra permanente
  de limites nominais, sem ampliação por viagem e sem previsão de revisão.
- RN-001, RN-002 e RN-003, "Depende de": a AMB-007 foi removida.
- §3: novo item — o sistema não determina se o colaborador está em viagem e
  não aplica a ampliação de 50% do item 6. Os marcadores de modelo da seção
  foram substituídos por esse item.
- §10: o item "Hospedagem recusada como indício de viagem" deixou de existir
  e foi substituído por "Ampliação de limites em viagem".
- RN-001, Regra: removida a frase "A eventual ampliação de limite por viagem
  é tratada na AMB-007 e opera sobre o valor do limite, não sobre a soma das
  despesas." Origem: de "decisão AMB-001" para "decisões AMB-001 e AMB-007".
- RN-002, Regra: de "O limite aplicável é o da categoria conforme a RN-001,
  com a eventual ampliação por viagem da AMB-007" para "O limite aplicável é
  o limite nominal da categoria conforme a RN-001; não há ampliação por
  viagem (AMB-007)". Origem: de "decisão AMB-002" para "decisões AMB-002 e
  AMB-007".
- AMB-005, Justificativa: de "esvaziando a regra em alimentação e em
  transporte fora de viagem" para "esvaziando a regra em alimentação e em
  transporte urbano".
- AMB-006, Justificativa: de "o limite de alimentação é no máximo R$ 90 e o
  de transporte fora de viagem é R$ 80… Produziria ainda o efeito invertido
  de exigir mais documentação de quem viaja" para "os limites são sempre os
  nominais (AMB-007) — alimentação R$ 60 e transporte urbano R$ 80, ambos
  abaixo de R$ 100… O item 5 da política passaria a valer apenas para
  hospedagem", sem o argumento do efeito invertido.

**Por quê:** a spec descreve o estado atual do sistema. Uma premissa
declarada como provisória, dependências apontando para uma ambiguidade já
decidida e regras que preveem ampliação por viagem fariam o leitor — ou quem
implementa a partir das RN-001 e RN-002 — supor que os limites ainda podem
mudar por viagem.

**O que isso invalidou:** nenhuma regra ou critério de aceite — todos já
assumiam despesas fora de viagem e permanecem válidos. A pendência da §10
sobre hospedagem recusada como indício de viagem deixou de existir. Na
justificativa da AMB-006, o argumento do efeito invertido sobre quem viaja
deixou de se aplicar e foi removido; a decisão da AMB-006 se sustenta pela
razão restante, reforçada.

**Tasks afetadas:** nenhuma — tasks.md ainda não foi escrito.

**Custo:** 1 arquivo, correção pontual.

---

## D-001 — Registros das AMB-001, 002, 004 e 006 atualizados após a AMB-005 · `2026-10-18`

**Gatilho:** a decisão da AMB-005 tornou obsoletas afirmações registradas nas
AMB-002 e AMB-004, que referenciavam a AMB-005 como pendente. A varredura que
se seguiu encontrou o mesmo problema nas AMB-001 e AMB-006.

**O que mudou na spec:** os registros das AMB-001, 002, 004 e 006 passam a
declarar o resultado final em vez de apontar para
ambiguidades já decididas, e a AMB-001 passa a usar a numeração AMB-003 em
vez da numeração da lista de trabalho.
- AMB-001, Consequências: de "(assumindo AMB-002 = corta o excedente)" para
  "(com o reembolso parcial definido na AMB-002: o excedente é cortado)";
  de "a ambiguidade #3" para "a AMB-003".
- AMB-002, d-003: de "80,00, parcial (condicionado à AMB-004/005)" para
  "80,00, parcial; a nota fiscal não é exigida (RN-003)".
- AMB-004, d-004 e d-013: de "resultado definido pela AMB-005" para
  "reembolso 0,00, recusado, motivo SEM_NOTA_FISCAL (RN-004)".
- AMB-006, Custo aceito: de "a severidade dessa diferença depende da
  AMB-005: se… se…" para "foi fixada pela AMB-005: a ausência de nota recusa
  o item integralmente, de modo que d-004 perde R$ 80,00 por conta do
  centavo".
- AMB-006, d-004: de "efeito definido pela AMB-005" para "reembolso 0,00,
  recusado, motivo SEM_NOTA_FISCAL (RN-004)"; d-013 e alimentação de 500,00
  passam a declarar o mesmo resultado.

**Por quê:** a spec descreve o estado atual do sistema. Afirmações
divergentes sobre o mesmo item tornam a spec não verificável.

**O que isso invalidou:** nenhuma regra ou critério de aceite. Apenas texto de
registro que havia se tornado desatualizado.

**Tasks afetadas:** nenhuma — tasks.md ainda não foi escrito.

**Custo:** 1 arquivo, correção pontual.
