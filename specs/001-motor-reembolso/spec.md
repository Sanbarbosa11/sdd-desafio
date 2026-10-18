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
categoria. Uma despesa que exige nota fiscal e não a apresenta é recusada
conforme a RN-004.
**Origem:** política do RH, item 5; decisões AMB-004 e AMB-006
**Depende de:** RN-002 (reembolso parcial, decidido na AMB-002); RN-004
(recusa por ausência de nota fiscal exigida, decidida na AMB-005); AMB-007
(ampliação de limite em viagem) e AMB-014 (precisão monetária, que define a
fronteira para valores como 100,004), pendentes.
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
- d-013 (22/07, hospedagem, 690,00, sem NF) → nota fiscal exigida e ausente,
  qualquer que seja o número de diárias; reembolso 0,00, recusado (RN-004).

### RN-004 — Recusa integral por ausência de nota fiscal exigida

**Regra:** uma despesa que exige nota fiscal conforme a RN-003 e não a
apresenta tem reembolso 0,00 e é classificada como recusada, com motivo
SEM_NOTA_FISCAL. A recusa é integral e independe do limite da categoria: o
item não é avaliado contra o limite (RN-001, RN-002), e um lançamento que
seria integral caso tivesse nota é recusado da mesma forma. O motivo
SEM_NOTA_FISCAL é distinto de qualquer outra causa de recusa, em particular
de CATEGORIA_INELEGIVEL.
**Origem:** política do RH, item 5; decisão AMB-005
**Depende de:** RN-003 (exigência de nota fiscal).
**Aceite:**
- d-004 (06/07, transporte urbano, 100,01, sem NF) → reembolso 0,00,
  recusado, motivo SEM_NOTA_FISCAL.
- d-013 (22/07, hospedagem, 690,00, sem NF) → reembolso 0,00, recusado,
  motivo SEM_NOTA_FISCAL, qualquer que seja o número de diárias.
- Alimentação de 500,00, sem NF → reembolso 0,00, recusado, motivo
  SEM_NOTA_FISCAL, embora o teto da categoria seja 60,00.
- Hospedagem de 200,00, sem NF → reembolso 0,00, recusado, motivo
  SEM_NOTA_FISCAL; com NF, seria 200,00, integral.
- d-003 (06/07, transporte urbano, 100,00, sem NF) → nota fiscal não exigida;
  esta regra não se aplica; reembolso 80,00, parcial.
- Transporte urbano de 100,01, com NF → esta regra não se aplica; reembolso
  80,00, parcial.

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
**Consequências verificáveis** (com o reembolso parcial definido na AMB-002:
o excedente é cortado):
- d-001 (alimentação, 72,50) → reembolso 60,00, parcial
- d-002 (alimentação, 38,00) → reembolso 38,00, integral
- dia 03/07 totaliza 98,00
- d-006 e d-007 (54,90 cada) deixam de competir pelo limite; o tratamento
  passa a depender apenas da regra de duplicata (AMB-011)
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
- d-014 (61,00, limite 60) → 60,00, parcial (condicionado à AMB-013)
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
  reembolso 0,00, recusado, motivo SEM_NOTA_FISCAL (RN-004)
- d-013 (hospedagem, 690,00, sem NF) → nota fiscal exigida e ausente;
  reembolso 0,00, recusado, motivo SEM_NOTA_FISCAL (RN-004)
- d-010 (hospedagem, 480,00, com NF) → nota fiscal exigida e presente; segue
  para o limite
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
transporte fora de viagem. A leitura (d) trataria "obrigatória" como
"recomendada" e devolveria a decisão à conferência manual. A leitura (c)
tem mérito ao distinguir o que tem conserto do que não tem, mas exigiria um
quarto estado e um campo de valor potencial que a política não prevê, além
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
  motivo SEM_NOTA_FISCAL
- d-013 (hospedagem, 690,00, sem NF) → reembolso 0,00, recusado, motivo
  SEM_NOTA_FISCAL, qualquer que seja o número de diárias
- alimentação de 500,00 sem NF → reembolso 0,00, recusado, motivo
  SEM_NOTA_FISCAL, embora o teto da categoria seja 60,00
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
a exigência de nota muda. A severidade dessa diferença foi fixada pela
AMB-005: a ausência de nota recusa o item integralmente, de modo que d-004
perde R$ 80,00 por conta do centavo. Registro aqui que o desconforto existe;
ele é aceito na AMB-005 como custo da natureza documental da regra.
**Consequências verificáveis:**
- d-004 (transporte urbano, 100,01, sem NF) → valor de referência 100,01;
  nota fiscal exigida e ausente; reembolso 0,00, recusado, motivo
  SEM_NOTA_FISCAL (RN-004)
- d-003 (transporte urbano, 100,00, sem NF) → valor de referência 100,00;
  nota fiscal não exigida; reembolso 80,00, parcial
- d-013 (hospedagem, 690,00, sem NF) → valor de referência 690,00; nota
  fiscal exigida e ausente; resultado idêntico nas leituras (a) e (b),
  porque o limite de hospedagem nunca é inferior a R$ 250; reembolso 0,00,
  recusado, motivo SEM_NOTA_FISCAL (RN-004)
- alimentação de 500,00 sem NF → nota fiscal exigida e ausente (na leitura
  (b) não seria exigida, pois o teto é 60,00); reembolso 0,00, recusado,
  motivo SEM_NOTA_FISCAL (RN-004)

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
| Despesa acima de R$ 100,00 cujo teto fica abaixo de R$ 100,00, sem nota fiscal | d-004: 06/07, transporte urbano, 100,01, sem NF | Nota fiscal exigida e ausente: a exigência usa o valor lançado (100,01), não o teto de 80,00. Reembolso 0,00 (recusado), motivo SEM_NOTA_FISCAL | RN-003, RN-004 |
| Despesa muito acima do teto, sem nota fiscal | alimentação, 500,00, sem NF | Nota fiscal exigida e ausente, embora o teto da categoria seja 60,00. Reembolso 0,00 (recusado), motivo SEM_NOTA_FISCAL | RN-003, RN-004 |
| Hospedagem acima de R$ 100,00 sem nota fiscal | d-013: 22/07, hospedagem, 690,00, sem NF | Nota fiscal exigida e ausente, qualquer que seja o número de diárias. Reembolso 0,00 (recusado), motivo SEM_NOTA_FISCAL | RN-003, RN-004 |
| Despesa que seria integral com nota, sem nota fiscal exigida | hospedagem, 200,00, sem NF | Reembolso 0,00 (recusado), motivo SEM_NOTA_FISCAL; a recusa não depende do limite (com NF seria 200,00, integral) | RN-004 |

## 8. Ordem de aplicação das regras

<Quando duas regras incidem sobre a mesma despesa, qual vale primeiro? A ordem
muda o resultado. Declarar isso separa spec boa de spec média.>

Ordem parcial, estendida à medida que as ambiguidades pendentes forem
decididas:

1. Exigência de nota fiscal (RN-003). Se a nota é exigida e ausente, o item é
   recusado (RN-004) e não segue para o passo 2.
2. Limite da categoria (RN-001) e reembolso parcial (RN-002).

A verificação de nota fiscal precede a aplicação do limite (origem:
AMB-005). Nas regras decididas até aqui, o resultado é o mesmo em qualquer
ordem, porque a recusa zera o item qualquer que seja o limite; a ordem fica
declarada para que um item recusado por ausência de nota não seja avaliado
contra o limite.

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
- **Hospedagem recusada como indício de viagem (origem: AMB-005; afeta
  AMB-007).** Se a caracterização de viagem vier a ser inferida a partir de
  despesas de hospedagem, será preciso decidir se uma hospedagem recusada por
  ausência de nota fiscal (RN-004) — como d-013 — ainda caracteriza viagem.
  A decisão cabe à AMB-007.
