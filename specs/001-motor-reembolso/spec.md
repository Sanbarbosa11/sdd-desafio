# Spec — Motor de Cálculo de Reembolso

**Versão:** 1.0 · **Status:** rascunho · **Última alteração:** `<data>`

> **Regra de ouro deste arquivo:** ele descreve o QUÊ e o PORQUÊ. Nenhuma linha
> aqui pode citar linguagem, biblioteca, classe, função ou estrutura de pasta.
> Se apareceu solução, o lugar dela é o `plan.md`.
>
> **Teste de aceitação da própria spec:** uma pessoa que nunca viu o projeto
> consegue, lendo só este arquivo, verificar se o sistema está correto?

---

## 1. Problema

<Duas ou três frases. Quem sofre hoje, com o quê, e por quê isso custa caro.>

## 2. Objetivo

<Uma frase. O que passa a ser verdade quando isto existir.>

## 3. Fora de escopo

<Lista explícita. Este bloco vale ponto: ele impede o agente de inventar feature
e impede você de mover a trave depois.>

- Não determina se o colaborador está em viagem, e portanto não aplica a
  ampliação de 50% prevista no item 6 da política. Ver AMB-007.

## 4. Entrada e saída

**Entrada:** conforme `exemplos/despesas-exemplo.json`. Campos e significado:

| Campo | Tipo | Significado | Obrigatório |
|---|---|---|---|
| | | | |

**Saída:** definida por mim. Estrutura e significado de cada campo:

| Campo | Tipo | Significado |
|---|---|---|
| | | |

<Cole um exemplo de saída para uma entrada pequena. Vale mais que três parágrafos.>

## 5. Regras de negócio

Cada regra recebe um ID (`RN-001`, ...). As tasks vão referenciar esses IDs.

> **Limites nominais, sem ampliação por viagem:** o sistema não determina se o
> colaborador está em viagem (AMB-007). Todas as despesas são avaliadas com os
> limites nominais da política — alimentação R$ 60,00, transporte urbano
> R$ 80,00, hospedagem R$ 250,00 por lançamento (uma diária) —, e os
> critérios de aceite e os casos de borda deste documento valem para qualquer
> despesa, sem distinção de viagem.

### RN-001 — Limite de categoria aplicado por despesa individual

**Regra:** cada despesa de alimentação, transporte urbano ou hospedagem é
comparada, isoladamente, com o limite da sua categoria (alimentação R$ 60,00;
transporte urbano R$ 80,00; hospedagem R$ 250,00). Nenhuma outra despesa
altera o resultado da despesa avaliada por meio de agregação de valores: não
há soma de despesas da mesma categoria no mesmo dia. Não existe teto diário
agregado por categoria. Em hospedagem, o limite é por diária e cada
lançamento vale uma diária: R$ 250,00 por lançamento (AMB-009).
**Origem:** política do RH, itens 1, 2 e 3; decisões AMB-001, AMB-007 e AMB-009
**Depende de:** RN-002 (reembolso parcial, decidido na AMB-002).
**Aceite:**
- d-001 (03/07, alimentação, 72,50) → reembolso 60,00, parcial.
- d-002 (03/07, alimentação, 38,00) → reembolso 38,00, integral.
- O dia 03/07 totaliza 98,00 reembolsados em alimentação.
- Inverter a ordem de d-001 e d-002 na entrada não altera nenhum dos valores acima.
- Três despesas de alimentação de 59,00 no mesmo dia → 59,00 cada, 177,00 no total.
- d-010 (14/07, hospedagem, 480,00, com NF, "Hotel Rio - 2 diarias") →
  limite 250,00; reembolso 250,00, parcial; excedente 230,00. A descrição não
  altera o limite.

### RN-002 — Reembolso parcial: o limite é teto

**Regra:** uma despesa cujo valor é estritamente maior que o limite aplicável
tem reembolso igual ao limite aplicável; o excedente (valor menos limite) não
é reembolsado e a despesa é classificada como parcial. Uma despesa cujo valor
é menor ou igual ao limite aplicável tem reembolso igual ao seu valor e é
classificada como integral. O limite aplicável é o limite nominal da
categoria conforme a RN-001; não há ampliação por viagem (AMB-007).
**Origem:** política do RH, item 4; decisões AMB-002 e AMB-007
**Depende de:** RN-001 (limite da categoria).
**Aceite:**
- d-001 (03/07, alimentação, 72,50) → reembolso 60,00, parcial; excedente 12,50.
- Alimentação de 60,00 → reembolso 60,00, integral.
- Alimentação de 60,01 → reembolso 60,00, parcial; excedente 0,01.
- Transporte urbano de 90,00 → reembolso 80,00, parcial; excedente 10,00.

### RN-003 — Nota fiscal obrigatória acima de R$ 100,00

**Regra:** uma despesa cujo valor é estritamente maior que R$ 100,00 exige
nota fiscal. Uma despesa de valor menor ou igual a R$ 100,00 não exige nota
fiscal, tenha ela nota ou não. A comparação usa o valor lançado da despesa,
já normalizado conforme a AMB-014, antes de qualquer aplicação de limite; o
valor reembolsável não interfere na exigência. Uma despesa que exige nota fiscal e a
apresenta não sofre nenhum efeito desta regra e segue para o limite da sua
categoria. Uma despesa que exige nota fiscal e não a apresenta é recusada
conforme a RN-004.
**Origem:** política do RH, item 5; decisões AMB-004 e AMB-006
**Depende de:** RN-002 (reembolso parcial, decidido na AMB-002); RN-004
(recusa por ausência de nota fiscal exigida, decidida na AMB-005); AMB-014
(precisão monetária, que define a fronteira para valores como 100,004),
pendente.
**Aceite:**
- d-003 (06/07, transporte urbano, 100,00, sem NF) → nota fiscal não exigida;
  reembolso 80,00, parcial.
- d-004 (06/07, transporte urbano, 100,01, sem NF) → nota fiscal exigida e
  ausente; reembolso 0,00, recusado (RN-004).
- Transporte urbano de 100,01, com NF → nota fiscal exigida e presente;
  reembolso 80,00, parcial.
- Alimentação de 50,00, sem NF → nota fiscal não exigida; reembolso 50,00,
  integral.
- Alimentação de 500,00, sem NF → nota fiscal exigida e ausente, embora o
  teto da categoria seja 60,00; reembolso 0,00, recusado (RN-004).
- d-013 (22/07, hospedagem, 690,00, sem NF) → nota fiscal exigida e ausente;
  reembolso 0,00, recusado (RN-004).

### RN-004 — Recusa integral por ausência de nota fiscal exigida

**Regra:** uma despesa que exige nota fiscal conforme a RN-003 e não a
apresenta tem reembolso 0,00 e é classificada como recusada, com o motivo
SEM_NOTA_FISCAL na sua lista de motivos (RN-007). A recusa é integral e
independe do limite da categoria: o item não é avaliado contra o limite
(RN-001, RN-002), e um lançamento que seria integral caso tivesse nota é
recusado da mesma forma. O motivo SEM_NOTA_FISCAL é distinto de qualquer
outra causa de recusa, em particular de CATEGORIA_INELEGIVEL; quando mais de
uma causa se aplica, os motivos coexistem na lista.
**Origem:** política do RH, item 5; decisões AMB-005 e AMB-018
**Depende de:** RN-003 (exigência de nota fiscal); RN-007 (lista de motivos).
**Aceite:**
- d-004 (06/07, transporte urbano, 100,01, sem NF) → reembolso 0,00,
  recusado, motivos [SEM_NOTA_FISCAL].
- d-013 (22/07, hospedagem, 690,00, sem NF) → reembolso 0,00, recusado,
  motivos [SEM_NOTA_FISCAL].
- Alimentação de 500,00, sem NF → reembolso 0,00, recusado, motivos
  [SEM_NOTA_FISCAL], embora o teto da categoria seja 60,00.
- Hospedagem de 200,00, sem NF → reembolso 0,00, recusado, motivos
  [SEM_NOTA_FISCAL]; com NF, seria 200,00, integral.
- d-003 (06/07, transporte urbano, 100,00, sem NF) → nota fiscal não exigida;
  esta regra não se aplica; reembolso 80,00, parcial.
- Transporte urbano de 100,01, com NF → esta regra não se aplica; reembolso
  80,00, parcial.

### RN-005 — Período de competência e pertinência da despesa

**Regra:** o período de competência é o intervalo que vai da data de início à
data de fim informadas no período da entrada, com ambas as bordas inclusivas:
uma data igual à de início ou igual à de fim pertence ao período. O rótulo de
competência (o mês informado, como "2026-07") é informativo e não participa
da avaliação; divergência entre ele e o par de datas não impede o
processamento. Uma despesa pertence ao período quando a sua data está nesse
intervalo. A data da despesa é o único dado avaliado; a descrição não é
interpretada. Uma despesa fora do período é recusada conforme a RN-006.
**Origem:** política do RH, item 7; decisões AMB-008 e AMB-016
**Depende de:** RN-006 (recusa por despesa fora do período).
**Aceite:**
- Arquivo de exemplo → período de 2026-07-01 a 2026-07-31, ambas as datas
  incluídas.
- d-014 (31/07, igual à data de fim) → dentro do período.
- Despesa em 2026-07-01 (igual à data de início) → dentro do período.
- Despesa em 2026-08-01 → fora do período.
- d-008 (15/04, "Almoco de abril lancado com atraso") → fora do período; a
  descrição não altera o resultado.
- d-001 a d-007 e d-010 a d-013 (datas entre 03/07 e 22/07) → dentro do
  período. d-009 (11/07) é erro de item e não é avaliada por esta regra
  (RN-010).

### RN-006 — Recusa por despesa fora do período

**Regra:** uma despesa cuja data está fora do período conforme a RN-005 tem
reembolso 0,00 e é classificada como recusada, com o motivo
FORA_DA_COMPETENCIA na sua lista de motivos (RN-007). O item aparece na saída
com seus motivos, como qualquer outro. A recusa independe do limite da
categoria: o item não é avaliado contra o limite (RN-001, RN-002). Uma
despesa fora do período não participa de nenhuma agregação ou comparação com
outras despesas.
**Origem:** política do RH, item 7; decisões AMB-010 e AMB-018
**Depende de:** RN-005 (período e pertinência); RN-007 (lista de motivos).
**Aceite:**
- d-008 (15/04, alimentação, 41,00, com NF) → reembolso 0,00, recusado,
  motivos [FORA_DA_COMPETENCIA], embora seja válida em todos os outros aspectos.
- d-014 (31/07) → dentro do período; esta regra não se aplica.
- Despesa em 2026-08-01, alimentação, 30,00, com NF → reembolso 0,00,
  recusado, motivos [FORA_DA_COMPETENCIA].

### RN-007 — Lista de motivos de recusa

**Regra:** a saída de cada despesa traz a lista de todos os motivos de recusa
aplicáveis, e não um motivo único. Todas as regras de elegibilidade são
avaliadas para toda despesa sem erro de item (RN-010), sem interrupção na
primeira que se aplica, e cada uma que se aplica acrescenta seu motivo à
lista. Se a lista contém qualquer motivo, a despesa é recusada com reembolso
0,00 e não é avaliada contra o limite. Se a lista está vazia, a despesa segue
para o limite da categoria e é classificada como integral ou parcial conforme
a RN-002; o reembolso parcial não é motivo de recusa e não entra na lista. A
lista é ordenada na ordem das regras de elegibilidade declarada na §8, de
modo que a mesma entrada produza sempre a mesma saída. A classificação de
cada despesa assume um de quatro valores: integral, parcial, recusada ou
erro. Uma despesa com erro de item é classificada como erro e tem a lista de
motivos de recusa vazia: erro não é recusa (RN-010).
**Origem:** decisões AMB-017 e AMB-018
**Depende de:** §8 (ordem das regras de elegibilidade).
**Aceite:**
- Despesa de 150,00, sem NF, com data fora do período → reembolso 0,00,
  recusado, motivos [FORA_DA_COMPETENCIA, SEM_NOTA_FISCAL].
- d-008 → reembolso 0,00, recusado, motivos [FORA_DA_COMPETENCIA].
- d-004 → reembolso 0,00, recusado, motivos [SEM_NOTA_FISCAL].
- Despesa sem nenhuma violação (por exemplo, d-002) → lista de motivos vazia.
- d-001 (72,50, acima do limite) → reembolso 60,00, parcial; lista de motivos
  vazia.
- d-009 (−45,00) → classificação erro; reembolso 0,00; lista de motivos de
  recusa vazia.

### RN-008 — Normalização de categoria e fornecedor; elegibilidade da categoria

**Regra:** antes de qualquer comparação, a categoria e o fornecedor de cada
despesa são normalizados: letras maiúsculas são convertidas em minúsculas e
espaços nas extremidades são removidos. Nenhuma outra transformação é
aplicada — acentuação, separadores e sinônimos não são normalizados. Toda
regra que compara categorias ou fornecedores usa os valores normalizados.
As categorias elegíveis são
exatamente três: alimentacao, transporte_urbano e hospedagem. Uma despesa
cuja categoria normalizada seja qualquer outra é recusada, com o motivo
CATEGORIA_INELEGIVEL na sua lista de motivos (RN-007).
**Origem:** política do RH, item 9; decisão AMB-013
**Depende de:** RN-007 (lista de motivos).
**Aceite:**
- d-014 ("ALIMENTACAO", 61,00, com NF, 31/07) → categoria normalizada
  alimentacao; reembolso 60,00, parcial; lista de motivos vazia.
- d-005 ("coworking", 89,00, com NF) → reembolso 0,00, recusado, motivos
  [CATEGORIA_INELEGIVEL].
- "Alimentacao", "ALIMENTACAO" e " alimentacao " → a mesma categoria,
  alimentacao.
- "alimentação", com cedilha e til → não normalizada; recusada, motivos
  [CATEGORIA_INELEGIVEL].
- "transporte-urbano" → não normalizado para transporte_urbano; recusado,
  motivos [CATEGORIA_INELEGIVEL].
- Fornecedores "Bistro Central", "BISTRO CENTRAL" e " Bistro Central " → o
  mesmo fornecedor; "Bistrô Central" → fornecedor distinto.

### RN-009 — Duplicidade

**Regra:** duas ou mais despesas formam um grupo de duplicatas quando
coincidem em data, categoria normalizada (RN-008), fornecedor normalizado
(RN-008) e valor normalizado (AMB-014). A descrição e a presença de nota
fiscal não integram o critério. Só integram grupos as despesas que não receberam motivo de nenhuma
outra regra de elegibilidade e que não têm erro de item (RN-010): uma despesa
fora do período, de categoria inelegível, sem a nota fiscal exigida ou com
erro de item nunca é mantida nem recusada como duplicata. Dentro de um grupo,
a despesa de menor identificador é avaliada normalmente e as demais são
recusadas, com o motivo DUPLICATA na sua lista de motivos (RN-007). Os
identificadores são comparados em ordem lexicográfica, caractere a
caractere, sem extração de número; com identificadores de larguras
diferentes, "d-10" precede "d-9". O resultado não depende da ordem das
despesas na entrada.
**Origem:** política do RH, item 8; decisão AMB-011
**Depende de:** RN-007 (lista de motivos); RN-008 (categoria normalizada);
AMB-014 (valor normalizado), pendente.
**Aceite:**
- d-006 e d-007 (09/07, alimentação, Bistro Central, 54,90, com NF) → grupo de
  duplicatas; d-006 → reembolso 54,90, integral, lista de motivos vazia;
  d-007 → reembolso 0,00, recusado, motivos [DUPLICATA].
- Inverter a ordem de d-006 e d-007 na entrada → os mesmos dois resultados.
- Duas despesas iguais no critério, de valor até 100,00 (nota não exigida),
  uma com NF e outra sem → mesmo grupo; a de menor identificador é mantida,
  qualquer que seja a que tem a nota.
- Duas hospedagens iguais no critério, de 200,00, a de menor identificador
  sem NF e a outra com NF → a sem NF é recusada, motivos [SEM_NOTA_FISCAL], e
  não integra grupo; a com NF → reembolso 200,00, integral, sem DUPLICATA.
- Duas despesas iguais no critério, com descrições diferentes → grupo de
  duplicatas.
- Duas despesas iguais no critério, ambas fora do período → ambas recusadas,
  motivos [FORA_DA_COMPETENCIA]; nenhuma recebe DUPLICATA.
- Duas diárias no mesmo hotel, 240,00 cada, com NF, lançadas com as datas de
  cada noite (14/07 e 15/07) → não são duplicatas; reembolso 240,00, integral,
  cada uma. As mesmas duas lançadas ambas em 14/07 → grupo de duplicatas; a
  de menor identificador → 240,00, integral; a outra → motivos [DUPLICATA].
- Duas despesas iguais no critério, com identificadores "d-9" e "d-10" → "d-10"
  é a de menor identificador (ordem lexicográfica) e é mantida; "d-9" →
  motivos [DUPLICATA].
- Duas despesas iguais no critério, com fornecedores "Bistro Central" e
  "BISTRO CENTRAL" → grupo de duplicatas.

### RN-010 — Validação da entrada: erro de lote e erro de item

**Regra:** antes de qualquer regra de elegibilidade ou de valor, a entrada é
validada contra o contrato de entrada declarado na §4. Distinguem-se três
situações:

1. **Erro de lote** — a estrutura da entrada impede qualquer avaliação: data
   de início posterior à data de fim, período ausente, ou arquivo malformado.
   Nenhuma despesa é processada e o lote é rejeitado.
2. **Erro de item** — uma despesa não pode ser avaliada por campo obrigatório
   ausente, tipo incompatível, data inválida ou valor menor ou igual a zero.
   A despesa é classificada como erro, com reembolso 0,00, e não é avaliada
   por nenhuma regra de elegibilidade ou de valor; não integra grupo de
   duplicatas (RN-009). As demais despesas do lote são processadas
   normalmente.
3. **Recusa** — a despesa é bem formada e uma regra de elegibilidade a
   rejeita (RN-004, RN-006, RN-008, RN-009). Recebe a classificação recusada,
   com seus motivos (RN-007).

Os valores monetários aceitos são estritamente maiores que zero; um valor
igual ou inferior a zero constitui erro de item.
**Origem:** decisão AMB-017
**Depende de:** §4 (contrato de entrada), pendente — ver §10.
**Aceite:**
- d-009 (11/07, transporte urbano, −45,00, sem NF) → erro de item; reembolso
  0,00; não é avaliada por nenhuma regra de elegibilidade.
- d-005 (coworking, 89,00) → bem formada; recusada, motivos
  [CATEGORIA_INELEGIVEL]; não é erro.
- Entrada com data de início posterior à data de fim → lote rejeitado;
  nenhuma despesa processada.
- Despesa sem o campo de valor → erro de item; as demais despesas do lote são
  processadas.
- Despesa de valor 0,00 → erro de item.
- Um erro de item em uma despesa não altera o resultado de nenhuma outra.

---

## 6. Ambiguidades identificadas e decisões

> **Esta seção é o coração da spec e vale a maior parte dos 25 pontos do critério 1.**
> Uma ambiguidade que você resolveu no código sem registrar aqui conta como
> não resolvida.

### AMB-001 — Unidade de aplicação do limite por categoria

**Texto original do RH:** "Alimentação tem limite de R$ 60 por dia." / "Transporte urbano
tem limite de R$ 80 por dia." / "Hospedagem tem limite de R$ 250 por diária."
**O que não está claro:** a política não diz se o limite incide sobre cada
despesa individualmente ou sobre a soma das despesas da mesma categoria no
mesmo dia.
**Decisão:** o limite incide sobre CADA DESPESA individualmente, uniformemente
nas três categorias. Em hospedagem, o limite incide por diária e não por dia
de calendário; cada lançamento de hospedagem corresponde a uma diária
(AMB-009), e seu limite é R$ 250,00.
**Justificativa:** o limite é teto de razoabilidade por despesa, não orçamento
diário. Um colaborador que almoça e janta a trabalho tem duas necessidades
legítimas; zerar a segunda porque a primeira consumiu o teto puniria gasto
que a empresa reconhece como necessário. A avaliação por item reproduz a
conferência manual atual, é independente da ordem dos lançamentos, e permite
ao colaborador saber se uma despesa será paga no momento em que a faz.
**Custo aceito:** não existe teto diário agregado. Três almoços de R$ 59 no
mesmo dia somam R$ 177 reembolsados integralmente. Em transporte, d-003 e
d-004 poderiam somar até R$ 160 num dia contra um limite nominal de R$ 80.
Priorizo não punir despesa legítima sobre controle orçamentário diário.
**Consequências verificáveis** (com o reembolso parcial definido na AMB-002:
o excedente é cortado):
- d-001 (alimentação, 72,50) → reembolso 60,00, parcial
- d-002 (alimentação, 38,00) → reembolso 38,00, integral
- dia 03/07 totaliza 98,00
- d-006 e d-007 (54,90 cada) deixam de competir pelo limite; d-006 → 54,90,
  integral; d-007 → 0,00, recusado, motivos [DUPLICATA] (RN-009)
- a AMB-003 (qual item do dia sofre o corte) deixa de existir

**Em aberto:** registrado na §10.
**Regra afetada:** RN-001

### AMB-002 — Significado de "reembolso parcial"

**Texto original do RH:** "Despesas acima do limite são reembolsadas parcialmente." (item 4)
**O que não está claro:** "parcialmente" admite três leituras — (a) paga-se o
valor do limite e o excedente é cortado; (b) recusa-se o item inteiro; (c)
paga-se uma fração percentual do valor, sem que a política informe qual.
**Decisão:** adotada a leitura (a). O reembolso de uma despesa acima do limite
é o valor do limite aplicável; o excedente não é reembolsado e o item é
classificado como parcial. "Acima do limite" significa estritamente maior:
uma despesa de valor exatamente igual ao limite é reembolsada integralmente.
**Justificativa:** a leitura (c) é inoperável, pois exigiria inventar uma
porcentagem que a política não fornece. A leitura (b) contraria a palavra
"parcialmente" e puniria integralmente desvio mínimo — d-014 perderia R$ 61
por estourar R$ 1. A leitura (a) é a única compatível com a letra da política
e com a justificativa já adotada na AMB-001: o limite é teto de razoabilidade,
a empresa paga o que considera razoável e o colaborador arca com o excesso.
**Custo aceito:** o limite deixa de funcionar como barreira — não há desincentivo
a gastar acima dele, já que o colaborador sempre recebe o teto. Aceito porque
a alternativa pune desvio mínimo de forma desproporcional.
**Consequências verificáveis:**
- d-001 (72,50, limite 60) → 60,00, parcial
- d-014 (61,00, "ALIMENTACAO", limite 60) → 60,00, parcial; a categoria é
  normalizada para alimentacao (RN-008)
- d-003 (100,00, limite 80) → 80,00, parcial; a nota fiscal não é exigida (RN-003)
- uma despesa de exatamente 60,00 em alimentação → 60,00, integral
- a RN-001 e seus aceites permanecem válidos sem alteração

**Regra afetada:** RN-002

### AMB-003 — Distribuição do corte entre despesas do mesmo dia (dissolvida)

**Texto original do RH:** não há. A dúvida derivava da leitura agregada do
limite diário.
**O que não estava claro:** se o limite incidisse sobre a soma das despesas da
mesma categoria no mesmo dia, seria necessário decidir qual despesa sofre o
corte quando o conjunto estoura — ordem de aparição, maior valor, menor
valor ou rateio proporcional. d-001 e d-002 têm a mesma data sem hora, de
modo que nenhum critério cronológico resolveria o desempate.
**Decisão:** dissolvida pela AMB-001. Com o limite incidindo sobre cada despesa
individualmente, não há competição pelo mesmo teto e não existe corte a
distribuir.

Registrada para manter a rastreabilidade da numeração e documentar que a
dúvida foi identificada antes de ser eliminada.

### AMB-004 — Fronteira da obrigatoriedade de nota fiscal

**Texto original do RH:** "Nota fiscal é obrigatória acima de R$ 100." (item 5)
**O que não está claro:** "acima de R$ 100" admite duas leituras — (a)
estritamente maior, de modo que uma despesa de exatamente R$ 100,00 não
exige nota fiscal; (b) maior ou igual, de modo que R$ 100,00 já exige.
**Decisão:** adotada a leitura (a). A nota fiscal é obrigatória para despesas
de valor estritamente maior que R$ 100,00. Uma despesa de exatamente
R$ 100,00 não exige nota fiscal. A comparação usa o valor da despesa já
normalizado conforme a AMB-014 (precisão monetária).
**Justificativa:** "acima de" em português corrente exclui o valor citado, e a
AMB-002 já adotou esse mesmo sentido para "acima do limite" no item 4 da
política. Manter a mesma expressão com o mesmo significado nos dois itens
evita que a spec atribua sentidos diferentes à mesma palavra sem motivo
declarado. Uma política que pretendesse incluir o valor teria escrito "a
partir de R$ 100".
**Custo aceito:** R$ 100,00 passa a ser o maior valor reembolsável sem
comprovante fiscal, e um lançamento redondo nesse valor sem nota — como
d-003, uma corrida de táxi de exatamente R$ 100,00 — é aprovado sem
comprovação documental. Aceito porque a alternativa exigiria afirmar que o
RH escreveu "acima de" querendo dizer "a partir de", e a correção dessa
brecha cabe ao texto da política, não à interpretação do motor.
**Consequências verificáveis:**
- d-003 (transporte urbano, 100,00, sem NF) → nota fiscal dispensada; segue
  para o limite de 80,00 → reembolso 80,00, parcial
- d-004 (transporte urbano, 100,01, sem NF) → nota fiscal exigida e ausente;
  reembolso 0,00, recusado, motivos [SEM_NOTA_FISCAL] (RN-004)
- d-013 (hospedagem, 690,00, sem NF) → nota fiscal exigida e ausente;
  reembolso 0,00, recusado, motivos [SEM_NOTA_FISCAL] (RN-004)
- d-010 (hospedagem, 480,00, com NF) → nota fiscal exigida e presente; segue
  para o limite de 250,00 → reembolso 250,00, parcial
- uma despesa de 100,01 com nota fiscal → segue para o limite normalmente

**Regra afetada:** RN-003

### AMB-005 — Efeito da ausência de nota fiscal exigida

**Texto original do RH:** "Nota fiscal é obrigatória acima de R$ 100." (item 5)
A política estabelece quando a nota é exigida, mas não diz o que acontece
quando ela é exigida e não está presente.
**O que não está claro:** quatro leituras — (a) o item é recusado
integralmente; (b) o reembolso é limitado ao teto de dispensa de R$ 100,00;
(c) o item fica pendente de comprovação, com status próprio; (d) o item é
reembolsado normalmente e a saída registra um alerta.
**Decisão:** adotada a leitura (a). Uma despesa que exige nota fiscal e não a
apresenta tem reembolso zero e é classificada como recusada, com motivo
SEM_NOTA_FISCAL. A recusa é integral e independe do limite da categoria: o
item não é avaliado contra o limite, e um lançamento que estaria integral
caso tivesse nota é recusado da mesma forma. O motivo da recusa é registrado
de forma distinta de outras causas de recusa, em particular de
CATEGORIA_INELEGIVEL.
**Justificativa:** "obrigatória" estabelece condição, não recomendação. Sem o
comprovante, a empresa não tem como atestar que a transação ocorreu nem
quanto custou, de modo que não há base para pagar parcela alguma — o
pagamento parcial reembolsaria parte de uma despesa não comprovada. Esta é
também a consequência coerente com a AMB-006, que estabeleceu a natureza
binária da documentação: o comprovante existe ou não existe. A leitura (b)
produziria exatamente o resultado que a justificativa da AMB-006 usou para
rejeitar a leitura pelo valor reembolsável — um jantar de R$ 500 sem
comprovante renderia R$ 60 — esvaziando a regra em alimentação e em
transporte urbano. A leitura (d) trataria "obrigatória" como
"recomendada" e devolveria a decisão à conferência manual. A leitura (c)
tem mérito ao distinguir o que tem conserto do que não tem, mas exigiria um
estado adicional e um campo de valor potencial que a política não prevê, além
de abrir a questão nova de até quando a nota pode ser apresentada; o ganho
que ela traz é capturado aqui pelo motivo estruturado, sem criar estado novo.
**Custo aceito:** o salto na fronteira é máximo. d-003 (R$ 100,00, sem nota)
rende R$ 80,00 e d-004 (R$ 100,01, sem nota) rende R$ 0,00 — um centavo que
a empresa não pagaria em nenhum dos casos, já que o teto de transporte é
R$ 80,00, determina uma diferença de R$ 80,00 no reembolso. Aceito porque a
regra é documental e não comporta gradação: admitir pagamento parcial sem
comprovante seria admitir que a obrigatoriedade é negociável. O custo recai
sobretudo sobre fornecedores que habitualmente não emitem nota, conforme já
registrado na §10 — d-013 perde integralmente o reembolso.
**Consequências verificáveis:**
- d-004 (transporte urbano, 100,01, sem NF) → reembolso 0,00, recusado,
  motivos [SEM_NOTA_FISCAL]
- d-013 (hospedagem, 690,00, sem NF) → reembolso 0,00, recusado, motivos
  [SEM_NOTA_FISCAL]
- alimentação de 500,00 sem NF → reembolso 0,00, recusado, motivos
  [SEM_NOTA_FISCAL], embora o teto da categoria seja 60,00
- d-003 (transporte urbano, 100,00, sem NF) → nota não exigida; reembolso
  80,00, parcial — inalterado
- transporte urbano de 100,01 com NF → reembolso 80,00, parcial — inalterado

**Ordem de aplicação:** a verificação de nota fiscal precede a aplicação do
limite; registrado na §8.
**Em aberto:** registrado na §10.
**Regra afetada:** RN-003, RN-004

### AMB-006 — Valor de referência para a exigência de nota fiscal

**Texto original do RH:** item 5, "Nota fiscal é obrigatória acima de R$ 100." A política
informa a fronteira, mas não diz qual valor é comparado com ela.
**O que não está claro:** três leituras — (a) o valor original da despesa, já
normalizado; (b) o valor reembolsável após a aplicação do limite; (c) o valor
agregado de um grupo de despesas (dia, fornecedor ou ambos).
**Decisão:** adotada a leitura (a). A exigência de nota fiscal é avaliada sobre
o valor da despesa lançado, já normalizado conforme a AMB-014, antes de
qualquer aplicação de limite.
**Justificativa:** a nota fiscal comprova que a transação existiu e custou o
valor declarado — o objeto da comprovação é a despesa, não a parcela que a
empresa decide pagar. A leitura (b) esvaziaria a regra: como os limites são
sempre os nominais (AMB-007) — alimentação R$ 60 e transporte urbano R$ 80,
ambos abaixo de R$ 100 —, nenhuma despesa dessas categorias poderia exigir
nota, qualquer que fosse o valor lançado, e um jantar de R$ 500 sem
comprovante renderia R$ 60. O item 5 da política passaria a valer apenas
para hospedagem, a única categoria cujo limite supera R$ 100 — e nela a
leitura (b) produz o mesmo resultado que a (a), porque o limite de
hospedagem é sempre R$ 250,00 (AMB-009). A leitura (c) reintroduziria
agregação, que a AMB-001 rejeitou, abriria a questão nova de qual é o grupo,
e alteraria uma consequência já registrada na AMB-004.

Quanto à coerência com a AMB-002: lá se rejeitou punir desvio mínimo porque
a regra é de cálculo e opera sobre uma régua contínua — faz sentido pagar
proporcionalmente. Aqui a regra é documental, e documentação é binária: o
comprovante existe ou não existe, não há meio comprovante. Um limiar nítido
é próprio da natureza da regra, não uma inconsistência com a decisão anterior.

**Custo aceito:** a fronteira fica abrupta no valor lançado. d-004 (R$ 100,01)
difere de d-003 (R$ 100,00) por um centavo que a empresa não pagaria em
nenhum dos dois casos, já que o teto de transporte é R$ 80 — e ainda assim
a exigência de nota muda. A severidade dessa diferença foi fixada pela
AMB-005: a ausência de nota recusa o item integralmente, de modo que d-004
perde R$ 80,00 por conta do centavo. Registro aqui que o desconforto existe;
ele é aceito na AMB-005 como custo da natureza documental da regra.
**Consequências verificáveis:**
- d-004 (transporte urbano, 100,01, sem NF) → valor de referência 100,01;
  nota fiscal exigida e ausente; reembolso 0,00, recusado, motivos
  [SEM_NOTA_FISCAL] (RN-004)
- d-003 (transporte urbano, 100,00, sem NF) → valor de referência 100,00;
  nota fiscal não exigida; reembolso 80,00, parcial
- d-013 (hospedagem, 690,00, sem NF) → valor de referência 690,00; nota
  fiscal exigida e ausente; resultado idêntico nas leituras (a) e (b),
  porque o limite de hospedagem é sempre R$ 250,00 (AMB-009); reembolso 0,00,
  recusado, motivos [SEM_NOTA_FISCAL] (RN-004)
- alimentação de 500,00 sem NF → nota fiscal exigida e ausente (na leitura
  (b) não seria exigida, pois o teto é 60,00); reembolso 0,00, recusado,
  motivos [SEM_NOTA_FISCAL] (RN-004)

**Sem circularidade:** a exigência de nota passa a depender apenas do valor
normalizado, de modo que a cadeia valor → nota fiscal → limite → reembolso
é linear. A leitura (b) exigiria definir explicitamente qual "valor
reembolsável" é usado, sob pena de contradição lógica — um item cujo
reembolso cai abaixo de R$ 100 por efeito da ausência de nota deixaria de
exigir nota, e voltaria a exigi-la ao ser reembolsado integralmente.
**Regra afetada:** RN-003

### AMB-007 — Caracterização de "em viagem" e ampliação de limites

**Texto original do RH:** "Colaborador em viagem tem limites ampliados em 50%." (item 6)
**O que não está claro:** a política não define o que caracteriza estar em
viagem, não diz se o estado se aplica ao colaborador no período, ao dia ou à
despesa, e não informa quais limites são ampliados. A entrada não possui
nenhum campo que registre o estado de viagem — os únicos dados por despesa
são data, categoria, descrição, fornecedor, valor e presença de nota fiscal.
As leituras consideradas foram: (a) declarar a caracterização fora de escopo;
(b) estender a entrada com um campo explícito; (c) inferir viagem a partir de
lançamentos de hospedagem; (d) inferir a partir de termos na descrição ou no
fornecedor; (e) considerar todo o período em viagem se houver qualquer
hospedagem nele.
**Decisão:** adotada a leitura (a). A caracterização de estar em viagem está
fora do escopo deste sistema. O motor aplica sempre os limites nominais de
cada categoria e o item 6 da política não é implementado. A premissa global
declarada nas seções 5 e 7 deixa de ser provisória e passa a ser regra
permanente: todas as despesas são avaliadas com os limites nominais.
**Justificativa:** estar em viagem é um fato administrativo — alguém autoriza o
deslocamento — e a entrada não o registra. As leituras (c), (d) e (e)
deduzem a causa a partir de consequências e erram de formas previsíveis: a
hospedagem não captura deslocamento sem pernoite, como a corrida ao
aeroporto de d-003; o texto livre é sensível a grafia, produz falso positivo
em despesas locais que mencionem hotel, e é redigido pelo próprio
colaborador, que passaria a controlar o próprio limite; e considerar o
período inteiro ampliaria o limite de despesas sem relação alguma com a
viagem, como o almoço de plantão em sábado de d-012. As três também fariam o
reembolso de uma despesa depender da existência de outra no mesmo lote,
contrariando a justificativa da AMB-001, segundo a qual o colaborador sabe
se uma despesa será paga no momento em que a faz. A leitura (b) exigiria
estender um formato de entrada que o enunciado declara fixo, e produziria
viagem autodeclarada. Entre inferir um fato que o dado não contém e declarar
que o sistema não o determina, a segunda é a única posição que o motor
consegue sustentar diante de uma auditoria.
**Custo aceito:** o item 6 da política não é cumprido pelo motor, e o erro é
sempre na mesma direção — contra o colaborador em viagem, nunca contra a
empresa. No arquivo de exemplo, d-010 (hospedagem de 480,00 com nota)
recebe 250,00 em vez dos 375,00 que receberia se a diária fosse ampliada;
a diferença é de 125,00. Despesas de
alimentação e transporte de colaboradores efetivamente em viagem são
limitadas a 60,00 e 80,00 em vez de 90,00 e 120,00. A regularização desses
casos permanece com o financeiro, por conferência manual. Aceito porque a
alternativa é o motor conceder ampliação com base em indício que ele não
consegue comprovar, produzindo resultado que o auditor não tem como
verificar e que o colaborador pode influenciar.
**Consequências verificáveis:**
- todos os limites são os nominais: alimentação 60,00; transporte urbano
  80,00; hospedagem 250,00 por lançamento (uma diária)
- d-010 (hospedagem, 480,00, com NF) → limite 250,00, sem ampliação;
  reembolso 250,00, parcial (AMB-009)
- d-011 (alimentação, 15/07, no mesmo fornecedor de d-010) → limite 60,00;
  a proximidade com um lançamento de hospedagem não altera o limite
- d-003 (transporte urbano, 100,00, "Corrida aeroporto") → limite 80,00; a
  descrição não altera o limite
- d-013 (hospedagem, 690,00, sem NF) → recusado pela RN-004; a ausência de
  efeito sobre outras despesas é consequência desta decisão, não da recusa
- todos os aceites e casos de borda já registrados permanecem válidos sem
  alteração

**Em aberto:** registrado na §10.
**Regra afetada:** RN-001, RN-002; §3 (fora de escopo)

### AMB-008 — Dado que determina a pertinência ao período

**Texto original do RH:** "Despesas devem ser lançadas dentro do período de
competência." (item 7)
**O que não está claro:** a palavra "lançadas" sugere a data do lançamento, que
a entrada não possui — cada despesa traz apenas uma data, sem indicação de
quando foi registrada. As leituras consideradas foram: (a) usar a data da
despesa; (b) usar a data do lançamento, que não existe, deixando a regra sem
efeito ou fora de escopo; (c) aceitar qualquer despesa ocorrida até o fim do
período, recusando apenas datas futuras.
**Decisão:** adotada a leitura (a). A pertinência ao período é avaliada pela
data da despesa, comparada ao intervalo definido na AMB-016.
**Justificativa:** a data da despesa é o único dado de data que a entrada
fornece, e é estruturada e precisa. Lida como regra contábil, a frase do item
7 determina que cada despesa seja lançada na competência a que pertence — e
uma despesa pertence à competência em que ocorreu. A leitura (b) deixaria o
item 7 sem efeito algum: o motor reembolsaria despesas de qualquer data,
inclusive de anos anteriores. A leitura (c) faria uma despesa de abril ser
paga no fechamento de julho, dissolvendo a separação contábil por mês, que é
a finalidade do item 7.

Sobre a diferença em relação à AMB-007: ali a caracterização de viagem foi
declarada fora de escopo porque o dado não existe e não há substituto
confiável — toda inferência disponível era frágil ou manipulável. Aqui o dado
literal ("data do lançamento") também não existe, mas há substituto preciso e
estruturado: a data da despesa. Declarar a regra fora de escopo havendo dado
adequado seria descartar informação que a entrada fornece.

**Custo aceito:** uma despesa legítima apresentada no lote de uma competência
diferente da sua perde o reembolso. d-008 (alimentação de 41,00, com nota
fiscal, ocorrida em 15/04 e apresentada no lote de julho) não é reembolsada,
embora seja uma despesa válida em todos os outros aspectos. Como na AMB-009,
a correção cabe ao processo de lançamento: apresentar a despesa no lote da
competência correta.
**Consequências verificáveis:**
- d-008 (15/04, lote de julho) → fora do período; reembolso 0,00, recusado,
  motivos [FORA_DA_COMPETENCIA] (RN-006)
- as despesas entre 03/07 e 22/07 estão dentro do período, exceto d-009,
  que é erro de item e não é avaliada por esta regra (RN-010)
- a descrição de d-008 ("Almoco de abril lancado com atraso") não é
  interpretada, em coerência com a AMB-007 e a AMB-009

**Regra afetada:** RN-005

### AMB-009 — Número de diárias em um lançamento de hospedagem

**Texto original do RH:** "Hospedagem tem limite de R$ 250 por diária." (item 3)
**O que não está claro:** a política estabelece o limite por diária, mas a
entrada não informa quantas diárias um lançamento cobre. Cada despesa tem
uma única data, sem entrada nem saída, e nenhum campo de quantidade. O
número aparece apenas em texto livre na descrição, com redação variável —
"2 diarias" em d-010 e "3 noites" em d-013. As leituras consideradas foram:
(a) cada lançamento vale uma diária; (b) extrair o número da descrição;
(c) inferir o número a partir do valor, dividindo pelo limite; (d) estender
a entrada com um campo explícito.
**Decisão:** adotada a leitura (a). Cada lançamento de hospedagem é avaliado
como uma diária, e seu limite é sempre R$ 250,00. Uma estadia de várias
noites deve ser lançada como uma despesa por diária, e cada lançamento de
diária traz a data da noite a que se refere, de modo que diárias consecutivas
tenham datas distintas e não constituam duplicata (AMB-011). A descrição não
é interpretada.
**Justificativa:** o número de diárias não é dado estruturado, e as demais
leituras o produzem de formas que o motor não consegue sustentar. A leitura
(b) faria o limite depender de texto redigido pelo próprio colaborador —
escrever uma diária a mais na descrição vale R$ 250,00 de limite adicional,
sem que nada na despesa mude — e é o mesmo motivo pelo qual a AMB-007
rejeitou inferir viagem a partir de texto livre; o vocabulário é
comprovadamente aberto, já que o próprio arquivo de exemplo usa duas
palavras distintas para a mesma coisa. A leitura (c) esvaziaria o item 3 da
política: como o número de diárias seria sempre o suficiente para cobrir o
valor, nenhuma hospedagem jamais excederia o limite. A leitura (d) estenderia
um formato de entrada que o enunciado declara fixo, e no arquivo de exemplo
o resultado dependeria inteiramente do valor padrão adotado. A leitura (a) é
a única que produz o limite a partir de campos estruturados, é imune a
redação, e converte o "por diária" da política em uma regra de lançamento
verificável: uma diária, um lançamento.
**Custo aceito:** uma estadia lançada de forma consolidada é penalizada pela
forma do lançamento, não pelo valor. d-010 (R$ 480,00 por duas noites,
R$ 240,00 por noite, abaixo do limite) recebe R$ 250,00 e perde R$ 230,00 —
sendo que as duas diárias, lançadas separadamente, cada uma com a data da sua
noite, seriam reembolsadas integralmente. A correção cabe ao processo de lançamento, não ao motor.
Aceito porque a alternativa é conceder limite adicional com base em
informação que o colaborador escreve e que o sistema não tem como conferir.
**Consequências verificáveis:**
- o limite de qualquer lançamento de hospedagem é R$ 250,00
- d-010 (hospedagem, 480,00, com NF, "Hotel Rio - 2 diarias") → reembolso
  250,00, parcial; excedente de 230,00 não reembolsado
- d-013 (hospedagem, 690,00, sem NF) → reembolso 0,00, recusado, motivos
  [SEM_NOTA_FISCAL]; a recusa precede a avaliação do limite, de modo que esta
  decisão não altera seu resultado
- hospedagem de 400,00 com NF descrita como "1 diaria" e a mesma hospedagem
  de 400,00 descrita como "2 diarias" produzem o mesmo resultado: 250,00,
  parcial
- hospedagem de 200,00 com NF → reembolso 200,00, integral

**Regra afetada:** RN-001

### AMB-010 — Efeito de uma despesa fora do período

**Texto original do RH:** "Despesas devem ser lançadas dentro do período de
competência." (item 7) A política não estabelece a consequência de não
estarem.
**O que não está claro:** quatro leituras — (a) recusar com motivo próprio; (b)
excluir do processamento, de modo que o item não apareça na saída; (c) marcar
como pendente para a competência correta; (d) reembolsar normalmente e
registrar um alerta.
**Decisão:** adotada a leitura (a). Uma despesa cuja data esteja fora do
período tem reembolso 0,00 e é classificada como recusada, com motivo
FORA_DA_COMPETENCIA. O item aparece na saída com seu motivo, como qualquer
outro.
**Justificativa:** é o mesmo padrão adotado na AMB-005 — o item permanece
visível e o colaborador sabe por que não foi reembolsado e o que fazer, que é
apresentá-lo no lote da competência correta. A leitura (b) retiraria o item
da saída, e o enunciado exige que o motor justifique cada decisão; um item
ausente não distingue "excluído" de "esquecido". As leituras (c) e (d) foram
rejeitadas na AMB-005 pelos mesmos motivos que valem aqui: a primeira cria um
estado adicional e abre a questão de até quando a reapresentação é possível; a
segunda trata uma obrigação da política como recomendação.
**Custo aceito:** a saída passa a conter itens com reembolso zero que não
foram avaliados contra as demais regras de valor. Aceito porque a
visibilidade do item e a explicitação do motivo valem mais que a brevidade da
saída.
**Consequências verificáveis:**
- d-008 (alimentação, 41,00, com NF, 15/04) → reembolso 0,00, recusado,
  motivos [FORA_DA_COMPETENCIA]
- d-014 (31/07) → dentro do período; esta regra não se aplica
- uma despesa fora do período não participa de nenhuma agregação ou
  comparação com outras despesas

**Regra afetada:** RN-006

### AMB-011 — Duplicidade: critério e tratamento

**Texto original do RH:** "Duplicatas devem ser tratadas." (item 8) A política
não define o que caracteriza uma duplicata nem o que significa tratá-la.
**O que não está claro:** duas questões. (i) qual conjunto de campos precisa
coincidir para que duas despesas sejam consideradas a mesma; (ii) o que o
sistema faz com um grupo de duplicatas.
**Decisão:** duas ou mais despesas constituem um grupo de duplicatas quando
coincidem em data, categoria normalizada (AMB-013), fornecedor normalizado
(AMB-013) e valor normalizado (AMB-014). A descrição e a presença de nota
fiscal não integram o critério. Dentro de um grupo, a despesa de menor
identificador é avaliada normalmente e as demais são recusadas, com o motivo
DUPLICATA em sua lista de motivos. Despesas já recusadas por outra regra de
elegibilidade não integram grupos de duplicatas.
**Justificativa:** incluir a descrição no critério faria uma diferença de
acentuação ou de redação desfazer a detecção, e a spec já estabeleceu que a
descrição não é interpretada; exigir apenas o identificador repetido tornaria
a regra inócua, já que identificadores distintos são atribuídos a cada
lançamento. Quanto ao tratamento, recusar todas as despesas do grupo puniria
também o lançamento legítimo, e reembolsar todas registrando um alerta
trataria "devem ser tratadas" como recomendação — objeção já levantada na
AMB-005. Manter a de menor identificador, e não a primeira da entrada,
preserva o critério de aceite da RN-001, segundo o qual inverter a ordem das
despesas na entrada não altera nenhum resultado.
**Custo aceito:** duas despesas legítimas de mesmo valor, no mesmo fornecedor e
na mesma data — dois almoços de equipe no mesmo restaurante, por exemplo —
são tratadas como duplicata, e a segunda é recusada. A duplicidade é
inferência, não fato observado: o sistema não tem como distinguir a cópia do
lançamento genuíno. A correção cabe ao colaborador, que pode diferenciar os
lançamentos pelo valor efetivo de cada um.
**Consequências verificáveis:**
- d-006 e d-007 (ambos 09/07, alimentação, Bistro Central, 54,90) formam
  grupo de duplicatas; d-006 → reembolso 54,90, integral, lista de motivos
  vazia; d-007 → reembolso 0,00, recusado, motivos [DUPLICATA]
- inverter a ordem de d-006 e d-007 na entrada não altera nenhum dos dois
  resultados
- duas despesas iguais em todos os campos do critério, de valor até
  R$ 100,00, uma com nota fiscal e outra sem, formam o mesmo grupo; a de
  menor identificador é mantida, independentemente de qual delas possui a
  nota
- duas despesas iguais no critério, mas com descrições diferentes, formam
  grupo de duplicatas
- uma despesa fora do período (RN-006) não integra grupo de duplicatas, nem
  como mantida nem como recusada

**Regra afetada:** RN-009; AMB-009 (data de cada diária)

### AMB-012 — Tratamento de lançamentos de valor negativo (dissolvida)

**Texto original do RH:** não há; a política não menciona créditos, estornos
ou devoluções.
**O que não estava claro:** o arquivo de exemplo contém um lançamento de valor
negativo descrito como estorno (d-009, −45,00), e era necessário decidir se
ele seria ignorado, abatido do total do período, abatido do limite do dia, ou
tratado de outra forma.
**Decisão:** dissolvida pela AMB-017. O critério estrutural ali adotado
estabelece que valor menor ou igual a zero constitui erro de item, de modo que
d-009 recebe classificação erro, reembolso 0,00, e não é avaliado por nenhuma
regra de elegibilidade ou de valor. Não resta decisão de negócio a tomar sobre
lançamentos negativos: eles não são dado válido segundo o contrato de
entrada.

Registrada para manter a rastreabilidade da numeração e documentar que a
questão foi identificada antes de ser eliminada.
**Regra afetada:** nenhuma (dissolvida).

### AMB-013 — Identidade e elegibilidade da categoria

**Texto original do RH:** "Categorias fora da política não são reembolsáveis."
(item 9) A política nomeia três categorias — alimentação, transporte urbano e
hospedagem — ao estabelecer limites, e não apresenta lista de categorias
inelegíveis.
**O que não está claro:** duas questões. (i) se a comparação da categoria
distingue maiúsculas de minúsculas, já que d-014 traz "ALIMENTACAO"; (ii) se
as três categorias nomeadas esgotam as elegíveis, ou se são apenas aquelas
que possuem limite, podendo haver outras elegíveis sem teto.
**Decisão:** a categoria é normalizada antes da comparação, convertendo-se
maiúsculas em minúsculas e removendo-se espaços nas extremidades. Nenhuma
outra transformação é aplicada: acentuação, separadores e sinônimos não são
normalizados. As categorias elegíveis são exatamente três — alimentação,
transporte urbano e hospedagem. Qualquer outra categoria é inelegível, e a
despesa é recusada com o motivo CATEGORIA_INELEGIVEL em sua lista de motivos.
**Justificativa:** a categoria é campo estruturado com vocabulário fechado, e
uniformizar a caixa não constitui interpretação de texto livre — distingue-se,
nesse ponto, do que a AMB-007 e a AMB-009 recusaram. Tratar "ALIMENTACAO"
como categoria distinta faria d-014 perder os R$ 61,00 integrais por uma
diferença de grafia, o mesmo tipo de desproporção que a AMB-002 rejeitou ao
decidir o reembolso parcial. O alcance da normalização é mantido estreito
justamente para que a fronteira permaneça verificável: cada transformação
adicional aproximaria a regra da inferência. Quanto à segunda questão, admitir
categorias elegíveis fora das três esvaziaria o item 9 e eliminaria todo
controle de valor sobre elas, já que a política não estabelece limite para
categoria que não nomeia — um lançamento de R$ 5.000,00 em categoria
arbitrária seria reembolsado integralmente.

**Extensão ao fornecedor:** a mesma normalização, com o mesmo alcance — caixa
e espaços nas extremidades, nada mais —, aplica-se ao fornecedor, usado no
critério de duplicidade (AMB-011). "Bistro Central" e "BISTRO CENTRAL" são o
mesmo fornecedor, e deixar a comparação exata faria uma diferença de grafia
desfazer a detecção, que é o que a exclusão da descrição do critério
pretendia evitar. O alcance permanece estreito pelo mesmo motivo declarado
acima: ampliá-lo aproximaria a regra da inferência.

**Custo aceito:** despesas de trabalho legítimas cuja categoria a política não
nomeia nunca são reembolsadas pelo motor. d-005 (espaço de coworking, R$ 89,00,
com nota fiscal) é recusado, embora seja despesa plausível de trabalho. A
inclusão de novas categorias cabe ao RH, por alteração da política.
**Consequências verificáveis:**
- d-014 ("ALIMENTACAO", 61,00, com NF, 31/07) → categoria normalizada para
  alimentação; dentro do período; reembolso 60,00, parcial; lista de motivos
  vazia
- d-005 ("coworking", 89,00, com NF) → reembolso 0,00, recusado, motivos
  [CATEGORIA_INELEGIVEL]
- "Alimentacao", "ALIMENTACAO" e " alimentacao " são a mesma categoria
- "alimentação", com cedilha e til, não é normalizada para "alimentacao" e é
  categoria inelegível
- "transporte-urbano" não é normalizado para "transporte_urbano" e é
  categoria inelegível
- "Bistro Central", "BISTRO CENTRAL" e " Bistro Central " são o mesmo
  fornecedor; "Bistrô Central" não é

**Regra afetada:** RN-008; RN-009 (fornecedor normalizado); RN-004 (o motivo CATEGORIA_INELEGIVEL, já citado
ali, passa a existir)

### AMB-016 — Representação do período de competência

**Texto original do RH:** "Despesas devem ser lançadas dentro do período de
competência." (item 7) A política não diz como o período é representado. A
entrada traz três campos: competencia ("2026-07"), inicio ("2026-07-01") e
fim ("2026-07-31").
**O que não está claro:** duas questões independentes. (i) qual campo define
o período quando há mais de um — competencia, o par inicio/fim, ou os três
precisam concordar; (ii) se as datas de borda pertencem ao período.
**Decisão:** o período é o intervalo definido por inicio e fim, com ambas as
bordas inclusivas. Uma despesa cuja data seja igual a inicio ou igual a fim
está dentro do período. O campo competencia é tratado como rótulo
informativo e não participa da avaliação; divergência entre ele e o par
inicio/fim não impede o processamento.
**Justificativa:** o par inicio/fim é o dado mais expressivo — a competencia é
derivável dele, mas o inverso não: um fechamento que vá de 26/06 a 25/07 não
cabe em nenhum rótulo de mês. Exigir concordância entre os três campos
faria da divergência um erro de lote (RN-010), e um engano de digitação no
cabeçalho bloquearia o reembolso de todas as despesas do lote, custo alto
para proteger contra algo que a entrada de referência não apresenta. Quanto às
bordas, inicio e fim são datas sem hora, e "fim: 31/07" em linguagem corrente
inclui o dia 31; adotar intervalo semiaberto faria o último dia de todo mês
não pertencer a competência alguma, já que também não pertenceria à
competência seguinte — o que não é convenção de intervalo, é perda de dia.
**Custo aceito:** o campo competencia deixa de ter efeito sobre o cálculo. Um
lote cujo rótulo diga "2026-07" mas cujo par de datas cubra agosto será
processado como agosto, sem alerta. Aceito porque o par de datas é a
representação precisa, e tratar a divergência como erro de lote (RN-010)
bloquearia o lote inteiro por uma inconsistência que não impede a avaliação.
**Consequências verificáveis:**
- o período do arquivo de exemplo é [2026-07-01, 2026-07-31], ambas inclusivas
- d-014 (31/07) está dentro do período; seu resultado segue para as demais
  regras
- uma despesa em 2026-07-01 está dentro do período
- uma despesa em 2026-08-01 está fora do período

**Regra afetada:** RN-005

### AMB-017 — Distinção entre recusa e erro de entrada

**Texto original do RH:** não há. A política trata de despesas bem formadas e
não estabelece o que fazer diante de dado que não possa ser avaliado.
**O que não está claro:** casos como d-005 (categoria não prevista) e d-009
(valor negativo) não são cobertos pela política, e não havia critério
declarado que determinasse se constituem decisão de negócio ou dado
inválido. As leituras consideradas foram: (a) critério estrutural — erro é o
dado que não pode ser avaliado, recusa é o dado bem formado que uma regra
rejeita; (b) critério pela política — recusa quando há regra que rejeite,
erro quando nenhuma regra cobre o caso; (c) tratar tudo como recusa com
motivo, exceto o que impeça o processamento do lote.
**Decisão:** adotado o critério estrutural (a). Distinguem-se três situações:

1. Erro de lote — a estrutura da entrada impede qualquer avaliação: data de
   início posterior à data de fim, período ausente, ou arquivo malformado.
   Nenhuma despesa é processada e o lote é rejeitado.
2. Erro de item — uma despesa não pode ser avaliada por campo obrigatório
   ausente, tipo incompatível, data inválida ou valor menor ou igual a zero.
   A despesa recebe a classificação erro, com reembolso 0,00, e não é
   avaliada por nenhuma regra de elegibilidade ou de valor. As demais
   despesas do lote são processadas normalmente.
3. Recusa — a despesa é bem formada e uma regra de elegibilidade a rejeita.
   Recebe a classificação recusada, com seus motivos.

Os valores monetários aceitos são estritamente maiores que zero; um valor
igual ou inferior a zero constitui erro de item.

**Justificativa:** o critério estrutural mantém a fronteira estável — depende
do contrato da entrada, não do conjunto de regras vigentes. O critério (b) a
deslocaria a cada regra acrescentada: um caso hoje classificado como erro
passaria a recusa assim que uma regra o cobrisse, sem que nada no dado
tivesse mudado. O critério (c) faria a lista de motivos misturar decisões de
negócio com problemas de formato, e o colaborador deixaria de distinguir
"não foi reembolsado" de "foi lançado incorretamente", que exigem ações
diferentes. Quanto ao estado adicional: a AMB-005 recusou criar um quarto
estado ao rejeitar "pendente", mas aquele seria um estado de negócio que a
política não previa; "erro" é de outra natureza — não é uma decisão sobre o
reembolso, é a constatação de que não há decisão possível. Manter essa
constatação sob a mesma classificação de uma recusa fundamentada confundiria
as duas.
**Custo aceito:** a decisão exige que o contrato de entrada seja declarado na
§4 — campos obrigatórios, tipos e faixas admitidas — e torna o sistema mais
rígido quanto à forma do dado. Um lote inteiro é rejeitado por uma
inconsistência no cabeçalho do período, e uma despesa isolada é descartada
por um campo ausente. Aceito porque avaliar dado que não se pode interpretar
produziria resultado que ninguém consegue justificar.
**Consequências verificáveis:**
- d-009 (transporte urbano, −45,00) → erro de item; reembolso 0,00; não é
  avaliado por nenhuma regra de elegibilidade
- d-005 (coworking, 89,00) → bem formado; recusado, motivos
  [CATEGORIA_INELEGIVEL]; não é erro
- uma entrada com data de início posterior à data de fim → lote rejeitado,
  nenhuma despesa processada
- uma despesa sem o campo de valor → erro de item; as demais despesas do
  lote são processadas
- um erro de item em uma despesa não impede o processamento das demais

**Em aberto:** contrato de entrada da §4, registrado na §10.
**Regra afetada:** RN-010; RN-007 (quarto valor de classificação); RN-009
(despesa com erro não integra grupo)

### AMB-018 — Motivos múltiplos de recusa

**Texto original do RH:** não há. A dúvida surgiu das decisões AMB-005 e
AMB-010, que criaram dois motivos de recusa independentes.
**O que não está claro:** uma despesa pode violar mais de uma regra
simultaneamente — por exemplo, uma despesa de abril, de R$ 150,00, sem nota
fiscal, viola competência e documentação. A spec não declarava se a saída
reporta um único motivo ou todos.
**Decisão:** a saída de cada despesa traz a lista de todos os motivos
aplicáveis, e não um motivo único. Todas as regras de elegibilidade são
avaliadas e seus motivos coletados; o reembolso é zero se qualquer motivo de
recusa estiver presente. A lista é ordenada de forma determinística, na
ordem declarada na §8, de modo que a mesma entrada produza sempre a mesma
saída.
**Justificativa:** as causas de recusa são independentes entre si, e eleger
uma delas exigiria uma ordem de precedência arbitrária que esconderia as
demais. Reportar todas permite ao colaborador corrigir tudo de uma vez, em
lugar de descobrir um problema por submissão. A decisão também simplifica a
§8: a ordem das regras passa a tratar apenas do que afeta o valor
reembolsado, não do que afeta o rótulo.
**Custo aceito:** a representação da saída fica mais complexa, com uma lista
no lugar de um valor escalar, e é necessário declarar a ordenação da lista
para que o resultado seja determinístico.
**Consequências verificáveis:**
- uma despesa de 150,00, sem nota fiscal, com data fora do período →
  reembolso 0,00, recusado, motivos [FORA_DA_COMPETENCIA, SEM_NOTA_FISCAL]
- d-008 → reembolso 0,00, recusado, motivos [FORA_DA_COMPETENCIA]
- d-004 → reembolso 0,00, recusado, motivos [SEM_NOTA_FISCAL]
- uma despesa sem nenhuma violação → lista de motivos vazia

**Regra afetada:** RN-004, RN-006, RN-007; §8

<A política tem no mínimo oito. Se você achou menos, releia
`exemplos/despesas-exemplo.json` — cada item daquele arquivo existe por um motivo.>

---

## 7. Casos de borda

> **Limites nominais, sem ampliação por viagem:** o sistema não determina se o
> colaborador está em viagem (AMB-007). Todas as despesas são avaliadas com os
> limites nominais da política — alimentação R$ 60,00, transporte urbano
> R$ 80,00, hospedagem R$ 250,00 por lançamento (uma diária) —, e os
> critérios de aceite e os casos de borda deste documento valem para qualquer
> despesa, sem distinção de viagem.

| Caso | Entrada | Comportamento esperado | Regra |
|---|---|---|---|
| Duas despesas de alimentação no mesmo dia; esta acima do limite | d-001: 03/07, alimentação, 72,50 | Reembolso 60,00 (parcial); excedente de 12,50 não reembolsado | RN-001, RN-002 |
| Duas despesas de alimentação no mesmo dia; esta abaixo do limite, a outra acima | d-002: 03/07, alimentação, 38,00 | Reembolso 38,00 (integral); d-001 no mesmo dia não reduz o valor | RN-001 |
| Despesa ligeiramente acima do limite | alimentação, 61,00 | Reembolso 60,00 (parcial); excedente de 1,00 não reembolsado | RN-002 |
| Despesa de valor exatamente igual ao limite | alimentação, 60,00 | Reembolso 60,00 (integral) | RN-002 |
| Despesa de exatamente R$ 100,00 sem nota fiscal | d-003: 06/07, transporte urbano, 100,00, sem NF | Nota fiscal não exigida; reembolso 80,00 (parcial); excedente de 20,00 não reembolsado | RN-002, RN-003 |
| Despesa logo acima de R$ 100,00 com nota fiscal | transporte urbano, 100,01, com NF | Nota fiscal exigida e presente; reembolso 80,00 (parcial); excedente de 20,01 não reembolsado | RN-002, RN-003 |
| Despesa acima de R$ 100,00 cujo teto fica abaixo de R$ 100,00, sem nota fiscal | d-004: 06/07, transporte urbano, 100,01, sem NF | Nota fiscal exigida e ausente: a exigência usa o valor lançado (100,01), não o teto de 80,00. Reembolso 0,00 (recusado), motivos [SEM_NOTA_FISCAL] | RN-003, RN-004 |
| Despesa muito acima do teto, sem nota fiscal | alimentação, 500,00, sem NF | Nota fiscal exigida e ausente, embora o teto da categoria seja 60,00. Reembolso 0,00 (recusado), motivos [SEM_NOTA_FISCAL] | RN-003, RN-004 |
| Hospedagem acima de R$ 100,00 sem nota fiscal | d-013: 22/07, hospedagem, 690,00, sem NF | Nota fiscal exigida e ausente. Reembolso 0,00 (recusado), motivos [SEM_NOTA_FISCAL] | RN-003, RN-004 |
| Hospedagem de várias noites lançada num único lançamento | d-010: 14/07, hospedagem, 480,00, com NF, "Hotel Rio - 2 diarias" | Nota fiscal exigida e presente; limite 250,00 (um lançamento, uma diária; a descrição não é interpretada); reembolso 250,00 (parcial); excedente de 230,00 não reembolsado | RN-001, RN-002, RN-003 |
| Despesa que seria integral com nota, sem nota fiscal exigida | hospedagem, 200,00, sem NF | Reembolso 0,00 (recusado), motivos [SEM_NOTA_FISCAL]; a recusa não depende do limite (com NF seria 200,00, integral) | RN-004 |
| Despesa de outra competência, válida em todos os outros aspectos | d-008: 15/04, alimentação, 41,00, com NF, "Almoco de abril lancado com atraso" | Fora do período (a data da despesa decide; a descrição não é interpretada). Reembolso 0,00 (recusado), motivos [FORA_DA_COMPETENCIA]; não é avaliada contra o limite | RN-005, RN-006 |
| Despesa que viola duas regras de elegibilidade | 150,00, sem NF, data fora do período | Reembolso 0,00 (recusado), motivos [FORA_DA_COMPETENCIA, SEM_NOTA_FISCAL], nessa ordem (§8) | RN-004, RN-006, RN-007 |
| Despesa na data de fim do período, com categoria em maiúsculas | d-014: 31/07, "ALIMENTACAO", 61,00, com NF | Dentro do período (bordas inclusivas); categoria normalizada para alimentacao; reembolso 60,00 (parcial); excedente de 1,00 não reembolsado; lista de motivos vazia | RN-001, RN-002, RN-005, RN-008 |
| Par de despesas idênticas exceto pelo identificador; esta com o menor | d-006: 09/07, alimentação, Bistro Central, 54,90, com NF | Grupo de duplicatas com d-007; mantida por ter o menor identificador; reembolso 54,90 (integral); lista de motivos vazia | RN-009 |
| Par de despesas idênticas exceto pelo identificador; esta com o maior | d-007: 09/07, alimentação, Bistro Central, 54,90, com NF | Grupo de duplicatas com d-006; reembolso 0,00 (recusado), motivos [DUPLICATA]; o resultado não muda se a ordem na entrada for invertida | RN-009 |
| Valor negativo (estorno) | d-009: 11/07, transporte urbano, −45,00, sem NF, "Estorno de corrida cancelada" | Erro de item (valor menor ou igual a zero); classificação erro; reembolso 0,00; não é avaliada por nenhuma regra de elegibilidade ou de valor; não abate nenhuma outra despesa | RN-010 |
| Categoria fora das três elegíveis | d-005: 07/07, "coworking", 89,00, com NF | Reembolso 0,00 (recusado), motivos [CATEGORIA_INELEGIVEL]; não é erro de entrada | RN-008 |

## 8. Ordem de aplicação das regras

<Quando duas regras incidem sobre a mesma despesa, qual vale primeiro? A ordem
muda o resultado. Declarar isso separa spec boa de spec média.>

A avaliação tem três etapas:

0. **Validação** (RN-010). Um erro de lote rejeita a entrada inteira, e
   nenhuma despesa é processada. Uma despesa com erro de item é classificada
   como erro, com reembolso 0,00, e não segue para as etapas 1 e 2.
1. **Elegibilidade.** Para cada despesa sem erro de item, todas as regras de
   elegibilidade são avaliadas, sem interrupção na primeira que se aplica, e
   cada uma que se aplica acrescenta seu motivo à lista de motivos (RN-007),
   nesta ordem:
   1. Pertinência ao período (RN-005, RN-006) → FORA_DA_COMPETENCIA.
   2. Elegibilidade da categoria (RN-008) → CATEGORIA_INELEGIVEL.
   3. Exigência de nota fiscal (RN-003, RN-004) → SEM_NOTA_FISCAL.
   4. Duplicidade (RN-009) → DUPLICATA, avaliada apenas entre as despesas
      que não receberam motivo nos passos 1.1 a 1.3.
2. **Valor.** Apenas para despesa com lista de motivos vazia: limite da
   categoria (RN-001) e reembolso parcial (RN-002). Uma despesa com qualquer
   motivo de recusa tem reembolso 0,00 e não é avaliada contra o limite.

A validação precede tudo porque uma despesa que não pode ser avaliada não
tem como passar por nenhuma regra (origem: AMB-017). A ordem entre as etapas
1 e 2 afeta o valor: a elegibilidade precede o limite (origem: AMB-005), de
modo que uma despesa recusada não é avaliada contra o limite. A duplicidade
vem por último na elegibilidade porque é a única regra que compara despesas
entre si, e só deve considerar as que passaram nas demais (origem: AMB-011);
movê-la para antes mudaria quais despesas formam grupo e, portanto, qual é
mantida. Fora isso, a ordem dentro da etapa de elegibilidade não afeta o
valor — basta um motivo para o reembolso ser zero —; ela define a ordenação
da lista de motivos, para que a mesma entrada produza sempre a mesma saída
(origem: AMB-018).

## 9. Critérios de aceite

O sistema está pronto quando:

- [ ] <critério verificável, sem ler código>
- [ ] <...>

## 10. O que fica em aberto

- **Ausência de teto diário agregado (origem: AMB-001).** Com avaliação por
  despesa, a política não limita o gasto diário agregado. Se o RH pretendia
  esse controle, seria necessário um teto diário adicional que a política
  atual não estabelece.
- **Disponibilidade de nota fiscal por tipo de fornecedor (origem: AMB-006).**
  A política trata a nota fiscal como universalmente disponível. Na prática,
  fornecedores como hospedagem por aplicativo (d-013, Airbnb) e transporte
  informal podem não emitir nota. A spec não diferencia fornecedores porque a
  política não prevê essa distinção; acomodá-la exigiria regra específica
  sobre formas alternativas de comprovação, que caberia ao RH definir.
- **Ampliação de limites em viagem (origem: AMB-007).** O item 6 da política
  não é implementado porque a entrada não registra o estado de viagem. Para
  implementá-lo seria necessário um campo explícito na entrada, informado
  pelo processo que autoriza a viagem — não pelo colaborador que lança a
  despesa. Enquanto isso não existir, colaboradores em viagem recebem menos
  do que a política lhes garante, e a regularização cabe ao financeiro.
- **Contrato de entrada e representação de erros (origem: AMB-017).** A §4
  precisa declarar os campos obrigatórios, seus tipos e faixas admitidas, sem
  o que a RN-010 não é verificável. Precisa também definir como a saída
  representa uma despesa com erro de item — em particular, se e como informa
  qual campo falhou — e o que a saída contém quando o lote é rejeitado por
  erro de lote. Pendente até a redação da §4.
