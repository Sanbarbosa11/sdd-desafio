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
  motivo SEM_NOTA_FISCAL.
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
  SEM_NOTA_FISCAL
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
  nota fiscal exigida e ausente; reembolso 0,00, recusado, motivo
  SEM_NOTA_FISCAL (RN-004)
- d-003 (transporte urbano, 100,00, sem NF) → valor de referência 100,00;
  nota fiscal não exigida; reembolso 80,00, parcial
- d-013 (hospedagem, 690,00, sem NF) → valor de referência 690,00; nota
  fiscal exigida e ausente; resultado idêntico nas leituras (a) e (b),
  porque o limite de hospedagem é sempre R$ 250,00 (AMB-009); reembolso 0,00,
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
noites deve ser lançada como uma despesa por diária. A descrição não é
interpretada.
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
sendo que as duas diárias, lançadas separadamente, seriam reembolsadas
integralmente. A correção cabe ao processo de lançamento, não ao motor.
Aceito porque a alternativa é conceder limite adicional com base em
informação que o colaborador escreve e que o sistema não tem como conferir.
**Consequências verificáveis:**
- o limite de qualquer lançamento de hospedagem é R$ 250,00
- d-010 (hospedagem, 480,00, com NF, "Hotel Rio - 2 diarias") → reembolso
  250,00, parcial; excedente de 230,00 não reembolsado
- d-013 (hospedagem, 690,00, sem NF) → reembolso 0,00, recusado, motivo
  SEM_NOTA_FISCAL; a recusa precede a avaliação do limite, de modo que esta
  decisão não altera seu resultado
- hospedagem de 400,00 com NF descrita como "1 diaria" e a mesma hospedagem
  de 400,00 descrita como "2 diarias" produzem o mesmo resultado: 250,00,
  parcial
- hospedagem de 200,00 com NF → reembolso 200,00, integral

**Regra afetada:** RN-001

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
| Despesa acima de R$ 100,00 cujo teto fica abaixo de R$ 100,00, sem nota fiscal | d-004: 06/07, transporte urbano, 100,01, sem NF | Nota fiscal exigida e ausente: a exigência usa o valor lançado (100,01), não o teto de 80,00. Reembolso 0,00 (recusado), motivo SEM_NOTA_FISCAL | RN-003, RN-004 |
| Despesa muito acima do teto, sem nota fiscal | alimentação, 500,00, sem NF | Nota fiscal exigida e ausente, embora o teto da categoria seja 60,00. Reembolso 0,00 (recusado), motivo SEM_NOTA_FISCAL | RN-003, RN-004 |
| Hospedagem acima de R$ 100,00 sem nota fiscal | d-013: 22/07, hospedagem, 690,00, sem NF | Nota fiscal exigida e ausente. Reembolso 0,00 (recusado), motivo SEM_NOTA_FISCAL | RN-003, RN-004 |
| Hospedagem de várias noites lançada num único lançamento | d-010: 14/07, hospedagem, 480,00, com NF, "Hotel Rio - 2 diarias" | Nota fiscal exigida e presente; limite 250,00 (um lançamento, uma diária; a descrição não é interpretada); reembolso 250,00 (parcial); excedente de 230,00 não reembolsado | RN-001, RN-002, RN-003 |
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
- **Ampliação de limites em viagem (origem: AMB-007).** O item 6 da política
  não é implementado porque a entrada não registra o estado de viagem. Para
  implementá-lo seria necessário um campo explícito na entrada, informado
  pelo processo que autoriza a viagem — não pelo colaborador que lança a
  despesa. Enquanto isso não existir, colaboradores em viagem recebem menos
  do que a política lhes garante, e a regularização cabe ao financeiro.
- **Diárias e duplicatas (origem: AMB-009; afeta AMB-011).** Com cada diária
  lançada como despesa própria, duas noites no mesmo hotel, na mesma data e
  pelo mesmo valor tornam-se indistinguíveis de um lançamento duplicado — a
  mesma configuração do par d-006/d-007. A regra de duplicidade precisará
  estabelecer um critério capaz de separar os dois casos, ou declarar que não
  os separa. A decisão cabe à AMB-011.
