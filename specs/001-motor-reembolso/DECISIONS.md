# Log de Decisões e Mudanças de Spec

> Uma entrada **toda vez** que a spec mudar. Este arquivo é a prova de que a spec
> foi tratada como artefato vivo e não como cerimônia de abertura.
>
> Spec que não muda em dois dias é spec que ninguém consultou. Mudança não é
> demérito — mudança não registrada é.

Ordem cronológica inversa: a mais recente primeiro.

---

## D-010 — Achados de revisão independente da spec antes das regras de negócio · `2026-10-18`

**Gatilho:** revisão independente da spec 1.1 por um subagente com contexto
limpo — sem ter participado de nenhuma decisão e sem acesso a plan.md,
tasks.md, DECISIONS.md ou código —, feita depois da fundação (T-001 a T-004)
e antes da implementação das regras de negócio (T-005 em diante). O
subagente procurou contradições, indeterminações, decisões não refletidas
nas regras e vazamentos técnicos, e relatou com citação, sem alterar nada.
Quatro achados foram corrigidos aqui; os demais são de redação e não alteram
nenhum resultado (ver Pendente).

**O que mudou na spec (versão 1.1 → 1.2):**
- **Ponto de código (achado D4, reclassificado).** RN-009, AMB-011 e §9: de
  "pelos códigos dos caracteres" para "pelo ponto de código Unicode de cada
  caractere". AMB-011, Justificativa: acrescentado o porquê. RN-009 e
  AMB-011: um caso acrescentado — identificadores que diferem apenas por
  U+FF71 e U+1F600 → o de U+FF71 é mantido.
- **Vocabulário de categoria (achados A1 e D5).** §4.2, Notação: alimentacao,
  transporte_urbano e hospedagem declarados como vocabulário de valores de
  categoria da entrada — identificadores de negócio definidos pela spec, como
  os códigos de motivo. AMB-013, Decisão: de "exatamente três — alimentação,
  transporte urbano e hospedagem" para "exatamente três — alimentacao,
  transporte_urbano e hospedagem, na grafia do vocabulário de categoria".
  AMB-013, Consequências: d-014 de "normalizada para alimentação" para
  "normalizada para alimentacao".
- **Causa única do elemento que não é despesa (achados C1 e B1).** §4.1
  (Despesa inválida), RN-010 (item 2 e aceite), §4.2 (Causas do erro, com
  AMB-020 acrescentada à origem) e §9: o elemento da relação que não é uma
  despesa tem uma única causa — a própria despesa, com tipo incompatível. A
  decisão já existia na AMB-020; não tinha chegado às regras que ela declara
  afetar.
- **Alcance da despesa fora do período (achado A5).** RN-006 e AMB-010,
  Consequências: de "não participa de nenhuma agregação ou comparação com
  outras despesas" para "não é avaliada por nenhuma regra de valor e não
  integra grupo de duplicatas; as demais regras de elegibilidade continuam a
  ser avaliadas e acrescentam seus motivos (RN-007)".

**Classificação:** esclarecimentos. Nenhuma AMB nova: cada correção declara
o alcance de um termo ou leva uma decisão existente às regras que ela já
afetava.
- O ponto de código é o achado mais sério. O subagente o classificou como
  possível vazamento técnico de gravidade baixa; ele é, na verdade, regra de
  negócio decidida fora da spec. "Códigos dos caracteres" não dizia se eram
  pontos de código ou unidades de codificação, e quem decidiu foi a DT-007
  do plan — o que o CLAUDE.md proíbe. A diferença muda qual despesa é
  mantida quando um identificador tem caractere fora do plano multilíngue
  básico. Justificativa da escolha: a comparação precisa ser independente da
  representação interna de texto adotada pela implementação.
- O vocabulário de categoria fecha o A1 pela raiz: a AMB-013 dizia que
  acentuação e separadores não são normalizados e, em seguida, listava como
  elegível justamente a grafia acentuada e com espaço.
- No A5, a redação "não participa de nenhuma regra de elegibilidade nem de
  valor" foi considerada e descartada: contradiria a RN-007 e o caso da §7
  em que a despesa de 150,00 sem NF fora do período recebe
  [FORA_DA_COMPETENCIA, SEM_NOTA_FISCAL]. A restrição adotada nomeia o que
  de fato não se aplica — regras de valor e duplicidade — e deixa explícito
  que a RN-012 (identificador) e o consolidado continuam a considerar a
  despesa.

**Por quê:** cada um dos quatro achados admitia leituras com resultados
diferentes, ou deixava uma decisão fora da spec. Corrigi-los antes da T-005
evita que o código fixe uma das leituras por conta própria.

**O que isso invalidou:** nenhum critério de aceite. O resultado do lote de
exemplo é o mesmo. A RN-009 ganhou um caso; a RN-010 explicita a causa de um
caso que já existia.

**Tasks afetadas:** nenhuma precisa ser reescrita. T-007 (causa única: o
modelo já tem `CampoDespesa.DESPESA`) e T-013 (o teste
`RN-009 identificador fora do BMP é comparado por code point` passa a
verificar um aceite da spec, e não só a DT-007) já seguiam as leituras
adotadas. A DT-007 do plan deixa de decidir e passa a implementar a RN-009.

**Pendente:** achados de redação da mesma revisão, sem efeito em resultado,
para uma passada final — A2 (Justificativa da AMB-018 sobre "rótulo"), A3
("três campos" na AMB-015, que abrange cinco), A4 (§1 descreve as 21 AMB
como lacunas preenchidas por conferentes), B2 (caso de 150,00 sem categoria
fixada), D1 (referências a "plan.md" por nome de arquivo), D2 (frase do
template sobre tasks na §5), D3 ("sintaticamente válida") e D6 (histórico de
processo na AMB-020 e AMB-021).

**Custo:** 2 arquivos (spec.md, DECISIONS.md), correção pontual.

---

## D-009 — Lacunas de validação descobertas ao projetar a implementação; AMB-020 e AMB-021 · `2026-10-18`

**Gatilho:** a redação do plan.md. Ao desenhar o modelo de dados e o
adaptador de entrada, seis situações apareceram para as quais a spec 1.0,
já declarada completa, não determinava o resultado. Nenhuma foi encontrada
ao escrever a spec: todas surgiram ao tentar representar o contrato da §4
em estruturas concretas, quando cada campo exigiu uma resposta para
"ausente", "de outro tipo" e "presente". O plan.md não introduziu regra
nenhuma; as decisões foram trazidas para a spec antes de o plan ser escrito.

**O que mudou na spec (versão 1.0 → 1.1):**
- §6: novas AMB-020 (abrangência de um defeito de estrutura: lote ou
  despesa — identificador ausente ou de tipo incompatível, período ou
  relação de despesas de tipo incompatível, elemento da relação que não é
  despesa) e AMB-021 (causas múltiplas de erro de item). §1: de "dezenove"
  para "vinte e uma" ambiguidades.
- §4.1: novo parágrafo "Espaços nas extremidades", com a definição única —
  todo caractere de espaço em branco do padrão Unicode, incluindo tabulação e
  espaços não separáveis —, referenciada pela RN-008, pela RN-010 e pela
  RN-012. Lote inválido: acrescentados período e relação de despesas com tipo
  incompatível. Despesa inválida: acrescentados identificador ausente e
  elemento que não é despesa; "uma informação tem tipo incompatível" passa a
  "uma informação obrigatória tem tipo incompatível"; todas as causas são
  reportadas. Acrescentado que informação não obrigatória com tipo
  incompatível é tratada como ausente. Tabela: identificador comparado e
  apresentado após a remoção de espaços.
- §4.2: identificador apresentado normalizado e, em erro sem identificador,
  substituído pela posição na relação, contada a partir de um; de "Causa do
  erro" para "Causas do erro", todas, na ordem da §4.1. Exemplo de d-009: de
  "causa" para "causas".
- RN-008: remissão ao alcance de "espaços" declarado na §4.1.
- RN-010: as mesmas inclusões da §4.1 nos itens 1 e 2; tipo incompatível só
  produz erro em informação obrigatória. Origem: acrescentadas AMB-015,
  AMB-020 e AMB-021. Aceite: dez casos acrescentados.
- RN-011: "para cima" declarado como "afastando-se do zero", com o mesmo
  critério para valores negativos. Aceite: −45,005 → −45,01, erro de item.
- RN-012: identificador apresentado na forma comparada; despesa sem
  identificador ou com identificador de tipo incompatível não constitui
  repetição. Origem: acrescentada a AMB-020. Aceite: três casos
  acrescentados.
- AMB-015, Decisão: o critério passa a alcançar o tipo do dado — campo sem
  regra com tipo incompatível é tratado como ausente; o fornecedor, por ser
  chave da RN-009, permanece obrigatório, e o seu tipo incompatível é erro de
  item. Consequências: dois casos acrescentados. Regra afetada: de "nenhuma
  RN" para "RN-010".
- §9: critérios de validação atualizados; novo critério sobre espaços e
  identificador normalizado.
- RN-010, item 1, e §4.1, Lote inválido: entrada sintaticamente válida cuja
  estrutura não é a de um lote passa a ser entrada que não pode ser lida —
  causa única de rejeição, sem período ausente nem despesas não informadas.
  Aceite acrescentado. Esclarecimento, pelo critério de abrangência da
  AMB-020: sem estrutura de lote não há onde procurar período nem despesas.

**Classificação:** AMB nova quando a decisão exige justificativa própria,
por não estar coberta por decisão existente; esclarecimento quando a regra
existente já alcança o caso e faltava declarar o alcance de um termo.
- AMB-020 reúne identificador ausente e tipo incompatível na estrutura,
  resolvidos pelo mesmo critério (abrangência do dano).
- AMB-021 é própria porque a AMB-018 trata de motivos de recusa, e erro não
  é recusa (RN-007).
- Esclarecimentos: o arredondamento de negativos (RN-011 já declarava um
  único critério), o alcance de "espaços" (termo já usado em três regras) e
  o tipo incompatível em campo opcional (critério já estabelecido pela
  AMB-015 para a ausência).

**Por quê:** cada uma das seis situações admitia leituras com resultados
verificáveis diferentes — lote rejeitado ou despesa em erro, uma causa ou
várias, −45,01 ou −45,00 na saída, fornecedor de tabulação aceito ou em
erro, descrição numérica aceita ou em erro. Decidi-las no código seria
introduzir regra de negócio fora da spec.

**O que isso invalidou:** nenhum critério de aceite. O resultado do lote de
exemplo é o mesmo. Muda a representação da causa de erro, de uma para uma
lista: d-009 passa a ter "causas: valor não positivo".

**Tasks afetadas:** nenhuma — tasks.md ainda não foi escrito.

**Custo:** 2 arquivos (spec.md, DECISIONS.md). Achado de projeto, não de
especificação: a spec foi declarada completa na D-008, e esta entrada é a
evidência de que o contrato só se mostrou incompleto quando foi
representado.

---

## D-008 — Fechamento: §1, §2 e §9 escritas; vazamentos e andaime do template removidos · `2026-10-18`

**Gatilho:** o fechamento da spec — redação das seções que permaneciam no
modelo — e uma varredura por menções a implementação ou formato.

**O que mudou na spec:**
- Cabeçalho: status de "rascunho" para "completa"; última alteração de
  `<data>` para 2026-10-18. Removidos os blocos "Regra de ouro" e "Teste de
  aceitação da própria spec", instruções do template.
- §1 e §2: de modelo vazio para o problema e o objetivo.
- §3, §6 e §8: removidos os comentários do template.
- §4.2: acrescentada a notação — colchetes para a lista de motivos e códigos
  de motivo como identificadores de negócio, sem prescrição de formato.
- §7: acrescentado o caso d-012 (despesa em dia não útil).
- §9: de modelo vazio para os critérios de aceite, derivados das RN-001 a
  RN-012, da §4 e da §7, incluindo o resultado esperado do lote de exemplo.
- §10: acrescentado "Dia útil e dia não útil".
- AMB-015, Texto do RH: de "o campo centro_custo no objeto colaborador" para
  "o centro de custo do colaborador".
- AMB-016, Texto do RH, O que não está claro, Decisão, Justificativa e Custo
  aceito: de "competencia", "inicio", "fim", "o par inicio/fim" e "o campo
  competencia" para "rótulo de competência", "data de início", "data de
  fim" e "o par de datas", preservando a distinção entre as três
  informações de que a decisão trata.
- AMB-018, Custo aceito: de "uma lista no lugar de um valor escalar" para
  "vários motivos no lugar de um só".
- RN-005, AMB-007, AMB-009, AMB-012, AMB-014, AMB-015 e AMB-016: de "arquivo
  de exemplo" para "lote de exemplo".

**Por quê:** a spec descreve o quê e o porquê, nunca a forma. Nomes de campo
da representação da entrada, "objeto", "valor escalar" e "arquivo"
descrevem a forma; comentários de template sinalizam documento não
finalizado e cujas instruções já foram cumpridas. O caso d-012 existia no
lote de exemplo, mas seu resultado dependia de uma ausência de regra que a
spec nunca declarava.

**O que isso invalidou:** nenhum critério de aceite.

**Tasks afetadas:** nenhuma — tasks.md ainda não foi escrito.

**Custo:** 1 arquivo, correção pontual.

---

## D-007 — Contrato de entrada e saída declarados na §4; validação estendida · `2026-10-18`

**Gatilho:** a redação da §4 resolveu a pendência "Contrato de entrada e
representação de erros" da §10, introduziu a AMB-019 (identificador
repetido) e fixou casos de validação que a RN-010 não cobria.

**O que mudou na spec:**
- §4: de modelo vazio para a declaração da entrada (informações do lote e de
  cada despesa, obrigatoriedade, lote inválido, despesa inválida) e da saída
  (informação por despesa com a regra que a exige, consolidado, lote
  rejeitado, exemplo).
- RN-010, Regra: erro de lote passa a incluir data de início ou fim ausente
  ou inválida, despesas não informadas e identificador repetido (RN-012), e a
  saída informa todas as causas detectáveis; de "arquivo malformado" para "a
  entrada não pode ser lida". Erro de item passa a incluir explicitamente a
  indicação de nota fiscal ausente e o texto obrigatório vazio após remoção
  de espaços. Acrescentados: lote sem despesas é válido; período de um dia é
  válido; informações não previstas são ignoradas. Origem: acrescentadas a
  AMB-019 e a §4.1. Depende de: de "§4 (contrato de entrada), pendente" para
  "RN-012; §4.1". Aceite: acrescentados seis casos.
- AMB-017: de "Em aberto: contrato de entrada da §4, registrado na §10" para
  "Contrato de entrada: declarado na §4.1". Decisão, item 1: de "ou arquivo
  malformado" para "ou a entrada não pode ser lida, entre outras causas
  declaradas na RN-010", para que quem lesse apenas a AMB-017 não
  implementasse um conjunto menor de causas de erro de lote.
- §6: removido o comentário de modelo que pedia ao menos oito ambiguidades e
  citava o nome do arquivo de exemplo.
- §10: removido o item "Contrato de entrada e representação de erros",
  resolvido pela §4.
- RN-009, Regra: de "ordem lexicográfica, caractere a caractere, sem
  extração de número" para "… pelos códigos dos caracteres, sem extração de
  número e sem normalização de caixa: letras maiúsculas precedem as
  minúsculas". Aceite: acrescentado "entre d-006 e D-006, mantém-se D-006".
- AMB-011, Decisão: de "a despesa de menor identificador" para "… — na
  ordem dos códigos dos caracteres, comparados um a um (RN-009) —".
  Justificativa: acrescentado o motivo da ordenação pelos códigos dos
  caracteres.
- RN-012, Aceite: de "d-006 e D-006 → distintos; esta regra não produz erro"
  para a mesma afirmação acrescida da assimetria — "ainda que categoria e
  fornecedor sejam normalizados (RN-008) — normaliza-se o que é termo de
  negócio, não o que é chave".

**Por quê:** a spec descreve o estado atual do sistema. A RN-010 dependia de
um contrato que não existia, e a §10 registrava como pendente o que a §4
agora declara. Quanto à ordenação dos identificadores: "ordem lexicográfica,
caractere a caractere" não dizia como os caracteres se ordenam entre si; a
ordenação pelos códigos dos caracteres é a única que produz resultado
determinístico independentemente de configuração regional ou de convenção
alfabética local, e uma ordenação que ignore a caixa deixaria "d-006" e
"D-006" empatados, sem critério de desempate — a independência de ordem
exigida pela RN-001 só se garante com um critério total.

**O que isso invalidou:** nenhum critério de aceite.

**Tasks afetadas:** nenhuma — tasks.md ainda não foi escrito.

**Custo:** 1 arquivo, correção pontual.

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
