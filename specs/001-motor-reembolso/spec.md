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

- Não faz `<...>`
- Não faz `<...>`

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

> **Premissa:** Salvo indicação em contrário, os critérios de aceite e os casos de borda
> deste documento assumem despesas fora de viagem, com os limites nominais da
> política. A caracterização de viagem e a ampliação de limites são tratadas
> na AMB-007; quando ela for decidida, os casos afetados serão revistos e os
> que dependerem do estado de viagem passarão a declará-lo explicitamente.

### RN-001 — Limite de categoria aplicado por despesa individual

**Regra:** cada despesa de alimentação, transporte urbano ou hospedagem é
comparada, isoladamente, com o limite da sua categoria (alimentação R$ 60,00;
transporte urbano R$ 80,00; hospedagem R$ 250,00 por diária). Nenhuma outra
despesa altera o resultado da despesa avaliada por meio de agregação de
valores: não há soma de despesas da mesma categoria no mesmo dia. A eventual
ampliação de limite por viagem é tratada na AMB-007 e opera sobre o valor do
limite, não sobre a soma das despesas. Não existe teto diário
agregado por categoria. Em hospedagem, o limite é por diária: o limite de um
lançamento de hospedagem é R$ 250,00 multiplicado pelo número de diárias que
ele cobre, número definido pela AMB-009 (pendente).
**Origem:** política do RH, itens 1, 2 e 3; decisão AMB-001
**Depende de:** RN-002 (reembolso parcial, decidido na AMB-002); AMB-007
(ampliação de limite em viagem) e AMB-009 (número de diárias em lançamento de
hospedagem), pendentes.
**Aceite:**
- d-001 (03/07, alimentação, 72,50) → reembolso 60,00, parcial.
- d-002 (03/07, alimentação, 38,00) → reembolso 38,00, integral.
- O dia 03/07 totaliza 98,00 reembolsados em alimentação.
- Inverter a ordem de d-001 e d-002 na entrada não altera nenhum dos valores acima.
- Três despesas de alimentação de 59,00 no mesmo dia → 59,00 cada, 177,00 no total.

### RN-002 — Reembolso parcial: o limite é teto

**Regra:** uma despesa cujo valor é estritamente maior que o limite aplicável
tem reembolso igual ao limite aplicável; o excedente (valor menos limite) não
é reembolsado e a despesa é classificada como parcial. Uma despesa cujo valor
é menor ou igual ao limite aplicável tem reembolso igual ao seu valor e é
classificada como integral. O limite aplicável é o da categoria conforme a
RN-001, com a eventual ampliação por viagem da AMB-007.
**Origem:** política do RH, item 4; decisão AMB-002
**Depende de:** AMB-007 (ampliação de limite em viagem) e AMB-009 (número de
diárias em lançamento de hospedagem), ambas pendentes.
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
categoria. O efeito sobre uma despesa que exige nota fiscal e não a
apresenta é definido pela AMB-005.
**Origem:** política do RH, item 5; decisões AMB-004 e AMB-006
**Depende de:** RN-002 (reembolso parcial, decidido na AMB-002); AMB-005
(efeito da ausência de nota fiscal exigida), AMB-007 (ampliação de limite em
viagem) e AMB-014 (precisão monetária, que define a fronteira para valores
como 100,004), pendentes.
**Aceite:**
- d-003 (06/07, transporte urbano, 100,00, sem NF) → nota fiscal não exigida;
  reembolso 80,00, parcial.
- d-004 (06/07, transporte urbano, 100,01, sem NF) → nota fiscal exigida e
  ausente.
- Transporte urbano de 100,01, com NF → nota fiscal exigida e presente;
  reembolso 80,00, parcial.
- Alimentação de 50,00, sem NF → nota fiscal não exigida; reembolso 50,00,
  integral.
- Alimentação de 500,00, sem NF → nota fiscal exigida e ausente, embora o
  teto da categoria seja 60,00.
- d-013 (22/07, hospedagem, 690,00, sem NF) → nota fiscal exigida e ausente,
  qualquer que seja o número de diárias.

### RN-004 — ...

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
de calendário — um lançamento pode cobrir múltiplas diárias numa única data;
quantas é objeto da AMB-009, e o limite aplicável a um lançamento de
hospedagem fica condicionado àquela decisão.
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
**Consequências verificáveis** (assumindo AMB-002 = corta o excedente):
- d-001 (alimentação, 72,50) → reembolso 60,00, parcial
- d-002 (alimentação, 38,00) → reembolso 38,00, integral
- dia 03/07 totaliza 98,00
- d-006 e d-007 (54,90 cada) deixam de competir pelo limite; o tratamento
  passa a depender apenas da regra de duplicata (AMB-011)
- a ambiguidade #3 (qual item do dia sofre o corte) deixa de existir

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
- d-014 (61,00, limite 60) → 60,00, parcial (condicionado à AMB-013)
- d-003 (100,00, limite 80) → 80,00, parcial (condicionado à AMB-004/005)
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
  resultado definido pela AMB-005
- d-013 (hospedagem, 690,00, sem NF) → nota fiscal exigida e ausente;
  resultado definido pela AMB-005
- d-010 (hospedagem, 480,00, com NF) → nota fiscal exigida e presente; segue
  para o limite
- uma despesa de 100,01 com nota fiscal → segue para o limite normalmente

**Regra afetada:** RN-003

### AMB-005 — ...

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
empresa decide pagar. A leitura (b) esvaziaria a regra: como o limite de
alimentação é no máximo R$ 90 e o de transporte fora de viagem é R$ 80,
nenhuma despesa dessas categorias poderia exigir nota, e um jantar de R$ 500
sem comprovante renderia R$ 60. Produziria ainda o efeito invertido de
exigir mais documentação de quem viaja, já que o limite ampliado passaria a
ultrapassar R$ 100. A leitura (c) reintroduziria agregação, que a AMB-001
rejeitou, abriria a questão nova de qual é o grupo, e alteraria uma
consequência já registrada na AMB-004.

Quanto à coerência com a AMB-002: lá se rejeitou punir desvio mínimo porque
a regra é de cálculo e opera sobre uma régua contínua — faz sentido pagar
proporcionalmente. Aqui a regra é documental, e documentação é binária: o
comprovante existe ou não existe, não há meio comprovante. Um limiar nítido
é próprio da natureza da regra, não uma inconsistência com a decisão anterior.

**Custo aceito:** a fronteira fica abrupta no valor lançado. d-004 (R$ 100,01)
difere de d-003 (R$ 100,00) por um centavo que a empresa não pagaria em
nenhum dos dois casos, já que o teto de transporte é R$ 80 — e ainda assim
a exigência de nota muda. A severidade dessa diferença depende da AMB-005:
se a ausência de nota recusar o item inteiro, d-004 perde R$ 80,00 por esse
centavo; se a ausência limitar o reembolso ao teto de dispensa, a diferença
some. Registro aqui que o desconforto existe e que seu tratamento cabe à
AMB-005, não a esta decisão.
**Consequências verificáveis:**
- d-004 (transporte urbano, 100,01, sem NF) → valor de referência 100,01;
  nota fiscal exigida e ausente; efeito definido pela AMB-005
- d-003 (transporte urbano, 100,00, sem NF) → valor de referência 100,00;
  nota fiscal não exigida; reembolso 80,00, parcial
- d-013 (hospedagem, 690,00, sem NF) → valor de referência 690,00; nota
  fiscal exigida e ausente; resultado idêntico nas leituras (a) e (b),
  porque o limite de hospedagem nunca é inferior a R$ 250
- alimentação de 500,00 sem NF → nota fiscal exigida e ausente (na leitura
  (b) não seria exigida, pois o teto é 60,00)

**Sem circularidade:** a exigência de nota passa a depender apenas do valor
normalizado, de modo que a cadeia valor → nota fiscal → limite → reembolso
é linear. A leitura (b) exigiria definir explicitamente qual "valor
reembolsável" é usado, sob pena de contradição lógica — um item cujo
reembolso cai abaixo de R$ 100 por efeito da ausência de nota deixaria de
exigir nota, e voltaria a exigi-la ao ser reembolsado integralmente.
**Regra afetada:** RN-003

<A política tem no mínimo oito. Se você achou menos, releia
`exemplos/despesas-exemplo.json` — cada item daquele arquivo existe por um motivo.>

---

## 7. Casos de borda

> **Premissa:** Salvo indicação em contrário, os critérios de aceite e os casos de borda
> deste documento assumem despesas fora de viagem, com os limites nominais da
> política. A caracterização de viagem e a ampliação de limites são tratadas
> na AMB-007; quando ela for decidida, os casos afetados serão revistos e os
> que dependerem do estado de viagem passarão a declará-lo explicitamente.

| Caso | Entrada | Comportamento esperado | Regra |
|---|---|---|---|
| Duas despesas de alimentação no mesmo dia; esta acima do limite | d-001: 03/07, alimentação, 72,50 | Reembolso 60,00 (parcial); excedente de 12,50 não reembolsado | RN-001, RN-002 |
| Duas despesas de alimentação no mesmo dia; esta abaixo do limite, a outra acima | d-002: 03/07, alimentação, 38,00 | Reembolso 38,00 (integral); d-001 no mesmo dia não reduz o valor | RN-001 |
| Despesa ligeiramente acima do limite | alimentação, 61,00 | Reembolso 60,00 (parcial); excedente de 1,00 não reembolsado | RN-002 |
| Despesa de valor exatamente igual ao limite | alimentação, 60,00 | Reembolso 60,00 (integral) | RN-002 |
| Despesa de exatamente R$ 100,00 sem nota fiscal | d-003: 06/07, transporte urbano, 100,00, sem NF | Nota fiscal não exigida; reembolso 80,00 (parcial); excedente de 20,00 não reembolsado | RN-002, RN-003 |
| Despesa logo acima de R$ 100,00 com nota fiscal | transporte urbano, 100,01, com NF | Nota fiscal exigida e presente; reembolso 80,00 (parcial); excedente de 20,01 não reembolsado | RN-002, RN-003 |
| Despesa acima de R$ 100,00 cujo teto fica abaixo de R$ 100,00, sem nota fiscal | d-004: 06/07, transporte urbano, 100,01, sem NF | Nota fiscal exigida e ausente: a exigência usa o valor lançado (100,01), não o teto de 80,00 | RN-003 |
| Despesa muito acima do teto, sem nota fiscal | alimentação, 500,00, sem NF | Nota fiscal exigida e ausente, embora o teto da categoria seja 60,00 | RN-003 |
| Hospedagem acima de R$ 100,00 sem nota fiscal | d-013: 22/07, hospedagem, 690,00, sem NF | Nota fiscal exigida e ausente, qualquer que seja o número de diárias | RN-003 |

## 8. Ordem de aplicação das regras

<Quando duas regras incidem sobre a mesma despesa, qual vale primeiro? A ordem
muda o resultado. Declarar isso separa spec boa de spec média.>

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
